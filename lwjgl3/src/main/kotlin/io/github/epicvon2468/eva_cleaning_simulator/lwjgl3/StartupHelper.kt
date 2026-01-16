/*
 * Copyright 2020 damios
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at:
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
//Note, the above licence and copyright applies to this file only.
package io.github.epicvon2468.eva_cleaning_simulator.lwjgl3

import com.badlogic.gdx.Version
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3NativesLoader

import org.lwjgl.system.JNI
import org.lwjgl.system.linux.UNISTD
import org.lwjgl.system.macosx.LibC
import org.lwjgl.system.macosx.ObjCRuntime

import java.io.File
import java.lang.management.ManagementFactory

/**
 * Adds some utilities to ensure that the JVM was started with the
 * `-XstartOnFirstThread` argument, which is required on macOS for LWJGL 3
 * to function. Also helps on Windows when users have names with characters from
 * outside the Latin alphabet, a common cause of startup crashes.
 *
 * [Based on this java-gaming.org post by kappa](https://jvm-gaming.org/t/starting-jvm-on-mac-with-xstartonfirstthread-programmatically/57547)
 * @author damios
 */
object StartupHelper {

	private const val JVM_RESTARTED_ARG = "jvmIsRestarted"

	// Don't switch out the `.not()` postfix for a `!` prefix.  Can (and has) cause(d) confusion since the `!` is easy to miss.
	/**
	 * Must only be called on Linux. Check OS first!
	 * @return true if NVIDIA drivers are in use on Linux, false otherwise
	 */
	fun isLinuxNvidia(): Boolean = File("/proc/driver").list { _, path: String -> "NVIDIA" in path.uppercase() }.isNullOrEmpty().not()

	/**
	 * Starts a new JVM if the application was started on macOS without the
	 * `-XstartOnFirstThread` argument. This also includes some code for
	 * Windows, for the case where the user's home directory includes certain
	 * non-Latin-alphabet characters (without this code, most LWJGL3 apps fail
	 * immediately for those users). Returns whether a new JVM was started and
	 * thus no code should be executed.
	 *
	 * **Usage:**
	 *
	 * ```
	 * fun main() {
	 *   if (StartupHelper.startNewJvmIfRequired(true)) return // This handles macOS support and helps on Windows.
	 *   // after this is the actual main method code
	 * }
	 * ```
	 *
	 * @param redirectOutput
	 * whether the output of the new JVM should be rerouted to the
	 * old JVM, so it can be accessed in the same place; keeps the
	 * old JVM running if enabled
	 * @return whether a new JVM was started and thus no code should be executed
	 * in this one
	 */
	@JvmOverloads
	fun startNewJvmIfRequired(redirectOutput: Boolean = true): Boolean {
		val osName: String = System.getProperty("os.name").lowercase()
		if ("mac" in osName) return startNewJvm0(isMac = true, redirectOutput)
		if ("windows" in osName) {
			// Here, we are trying to work around an issue with how LWJGL3 loads its extracted .dll files.
			// By default, LWJGL3 extracts to the directory specified by "java.io.tmpdir", which is usually the user's home.
			// If the user's name has non-ASCII (or some non-alphanumeric) characters in it, that would fail.
			// By extracting to the relevant "ProgramData" folder, which is usually "C:\ProgramData", we avoid this.
			// We also temporarily change the "user.name" property to one without any chars that would be invalid.
			// We revert our changes immediately after loading LWJGL3 natives.
			val programData: String = System.getenv("ProgramData") ?: "C:\\Temp"
			val prevTmpDir: String = System.getProperty("java.io.tmpdir", programData)
			val prevUser: String = System.getProperty("user.name", "libGDX_User")
			System.setProperty("java.io.tmpdir", "$programData\\libGDX-temp")
			System.setProperty(
				"user.name",
				"User_${prevUser.hashCode()}_GDX${Version.VERSION}".replace('.', '_')
			)
			Lwjgl3NativesLoader.load()
			System.setProperty("java.io.tmpdir", prevTmpDir)
			System.setProperty("user.name", prevUser)
			return false
		}
		return startNewJvm0(isMac = false, redirectOutput)
	}

	private const val MAC_ERR_MSG = "There was a problem evaluating whether the JVM was started with the -XstartOnFirstThread argument."
	private const val LINUX_ERR_MSG = "There was a problem evaluating whether the JVM was restarted with __GL_THREADED_OPTIMIZATIONS disabled."
	private const val MAC_ERR_MSG_2 = "A Java installation could not be found. If you are distributing this app with a bundled JRE, be sure to set the -XstartOnFirstThread argument manually!"
	private const val LINUX_ERR_MSG_2 = "A Java installation could not be found. If you are distributing this app with a bundled JRE, be sure to set the environment variable __GL_THREADED_OPTIMIZATIONS=0"

	fun startNewJvm0(isMac: Boolean, redirectOutput: Boolean): Boolean {
		val processID: Long = if (isMac) LibC.getpid() else UNISTD.getpid().toLong()
		if (!isMac) {
			// No need to restart non-NVIDIA Linux
			if (!isLinuxNvidia()) return false
			// check whether __GL_THREADED_OPTIMIZATIONS is already disabled
			if (System.getenv("__GL_THREADED_OPTIMIZATIONS") == "0") return false
		} else {
			// There is no need for -XstartOnFirstThread on Graal native image
			if (System.getProperty("org.graalvm.nativeimage.imagecode", "").isNotEmpty()) return false

			// Checks if we are already on the main thread, such as from running via Construo.
			val objcMsgSend: Long = ObjCRuntime.getLibrary().getFunctionAddress("objc_msgSend")
			val nsThread: Long = ObjCRuntime.objc_getClass("NSThread")
			val currentThread: Long = JNI.invokePPP(nsThread, ObjCRuntime.sel_getUid("currentThread"), objcMsgSend)
			val isMainThread: Boolean = JNI.invokePPZ(currentThread, ObjCRuntime.sel_getUid("isMainThread"), objcMsgSend)
			if (isMainThread) return false

			if (System.getenv("JAVA_STARTED_ON_FIRST_THREAD_$processID") == "1") return false
		}

		// check whether the JVM was previously restarted
		// avoids looping, but most certainly leads to a crash
		if (System.getProperty(JVM_RESTARTED_ARG) == "true") {
			System.err.println(/*x =*/ if (isMac) MAC_ERR_MSG else LINUX_ERR_MSG)
			return false
		}

		// Restart the JVM with updated (env || jvmArgs)
		val jvmArgs: MutableList<String> = mutableListOf()
		// The following line is used assuming you target Java 8, the minimum for LWJGL3.
		val javaExecPath = "${System.getProperty("java.home")}/bin/java"
		// If targeting Java 9 or higher, you could use the following instead of the above line:
		//val javaExecPath = ProcessHandle.current().info().command().orElseThrow()
		if (!File(javaExecPath).exists()) {
			System.err.println(/*x =*/ if (isMac) MAC_ERR_MSG_2 else LINUX_ERR_MSG_2)
			return false
		}

		jvmArgs += javaExecPath
		if (isMac) jvmArgs += "-XstartOnFirstThread"
		jvmArgs += "-D$JVM_RESTARTED_ARG=true"
		jvmArgs += ManagementFactory.getRuntimeMXBean().inputArguments
		jvmArgs += "-cp"
		jvmArgs += System.getProperty("java.class.path")
		jvmArgs += System.getenv("JAVA_MAIN_CLASS_$processID") ?: run {
			val trace = Thread.currentThread().stackTrace
			if (trace.isNotEmpty()) trace[trace.lastIndex].className
			else {
				System.err.println("The main class could not be determined through stacktrace.")
				return false
			}
		}

		try {
			val processBuilder = ProcessBuilder(jvmArgs)
			if (!isMac) processBuilder.env("__GL_THREADED_OPTIMIZATIONS", "0")

			if (!redirectOutput) processBuilder.start()
			else processBuilder.inheritIO().start().waitFor()
		} catch (e: Exception) {
			System.err.println("There was a problem restarting the JVM")
			e.printStackTrace()
		}

		return true
	}

	private fun ProcessBuilder.env(name: String, value: String) = this.environment().set(name, value)
}