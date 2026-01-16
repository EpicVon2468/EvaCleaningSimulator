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
// Changes: Removed macOS utilities, changed function name, changed from class with companion object to object, removed information about macOS from javadoc.
// Note, the above licence and copyright applies to this file only.
package io.github.epicvon2468.eva_cleaning_simulator.lwjgl3

import com.badlogic.gdx.Version
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3NativesLoader

/**
 * Adds some utilities to help on Windows when users have names with characters from
 * outside the Latin alphabet, a common cause of startup crashes.
 *
 * [Based on this java-gaming.org post by kappa](https://jvm-gaming.org/t/starting-jvm-on-mac-with-xstartonfirstthread-programmatically/57547)
 * @author damios
 */
object StartupHelper {

	// TODO: Bring in the new version of StartupHelperKt, and figure out how to make it work with FabricLoader?  Or just intercept the Fabric 'main' with an entirely separate main that calls this first...
	fun help() {
		if ("windows" in System.getProperty("os.name").lowercase()) {
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
		}
	}
}