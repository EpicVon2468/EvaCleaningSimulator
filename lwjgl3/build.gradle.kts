@file:OptIn(ExperimentalKotlinGradlePluginApi::class)
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi

import io.github.fourlastor.construo.Target

import org.gradle.internal.os.OperatingSystem as OS

import java.net.URI

buildscript {
	repositories {
		gradlePluginPortal()
	}
}

plugins {
	id("application")
	alias(libs.plugins.kotlin.jvm)
	alias(libs.plugins.construo)
}

sourceSets.main.get().resources.srcDirs += rootProject.file("assets")
application.mainClass = "net.fabricmc.loader.impl.launch.knot.KnotClient"
kotlin {
	jvmToolchain(25)
	kotlinDaemonJvmArgs = listOf("-XX:+UseCompactObjectHeaders", "--enable-native-access=ALL-UNNAMED")
}

repositories {
	maven {
		url = URI("https://repo.spongepowered.org/maven/")
	}
	maven {
		url = URI("https://maven.fabricmc.net/")
	}
}

dependencies {
	val gdxControllersVersion: String by project
	val gdxVersion: String by project
	implementation("com.badlogicgames.gdx-controllers:gdx-controllers-desktop:$gdxControllersVersion")
	implementation("com.badlogicgames.gdx:gdx-backend-lwjgl3:$gdxVersion")
	implementation("com.badlogicgames.gdx:gdx-box2d-platform:$gdxVersion:natives-desktop")
	implementation("com.badlogicgames.gdx:gdx-freetype-platform:$gdxVersion:natives-desktop")
	implementation("com.badlogicgames.gdx:gdx-lwjgl3-angle:$gdxVersion")
	implementation("com.badlogicgames.gdx:gdx-platform:$gdxVersion:natives-desktop")
	implementation(project(":core"))

	implementation("net.fabricmc:fabric-loader:0.18.3")
	implementation("net.fabricmc:access-widener:2.1.0")
	implementation("net.fabricmc:tiny-mappings-parser:0.2.2.14")
	implementation("com.google.guava:guava:33.5.0-jre")
	implementation("com.google.code.gson:gson:2.13.2")
	implementation("net.fabricmc:sponge-mixin:0.17.0+mixin.0.8.7") {
		// CVE-2022-25647
		exclude(group = "com.google.code.gson", module = "gson")
		// CVE-2023-2976, CVE-2020-8908, CVE-2018-10237
		exclude(group = "com.google.guava", module = "guava")
	}
	implementation("org.ow2.asm:asm:9.9.1")
	implementation("org.ow2.asm:asm-analysis:9.9.1")
	implementation("org.ow2.asm:asm-commons:9.9.1")
	implementation("org.ow2.asm:asm-tree:9.9.1")
	implementation("org.ow2.asm:asm-util:9.9.1")
}

tasks.run.get().apply {
	workingDir = rootProject.file("assets")
	// You can uncomment the next line if your IDE claims a build failure even when the app closed properly.
	//setIgnoreExitValue(true)
	jvmArgs("--enable-native-access=ALL-UNNAMED")

	// You can't update this within the JVM...
	// (You can update the environment variable, but it doesn't update to respect that you did as such).
	fun isNVIDIA() = File("/proc/driver").list().let {
		it.size > 0 && it.any { path: String -> "NVIDIA" in path.uppercase() }
	}
	val nvidiaThreadedOptimisationsFix: String by project
	if (OS.current() == OS.LINUX && nvidiaThreadedOptimisationsFix == "true" && isNVIDIA()) {
		val nvidiaThreadedOptimisationsFixWarning: String by project
		if (nvidiaThreadedOptimisationsFixWarning == "true") {
			fun separator(isStart: Boolean) {
				if (isStart) print("\u001B[91m")
				repeat(57) { print("---") }
				println('\b')
				if (!isStart) println("\u001B[0m")
			}
			separator(true)
			println("WARNING: Applying Linux NVIDIA threaded optimisations fix!")
			println("WARNING: To be able to run with the standalone jar, you will need to disable (set to 0) the '__GL_THREADED_OPTIMIZATIONS' environment variable beforehand!")
			println("WARNING: If you think this is a mistake or if this has been since fixed, either open an issue or set 'nvidiaThreadedOptimisationsWarning' to 'false' in gradle.properties!")
			separator(false)
		}
		environment("__GL_THREADED_OPTIMIZATIONS", 0)
	}

	systemProperty("sun.misc.unsafe.memory.access", "allow")

	if ("mac" in System.getProperty("os.name").lowercase()) jvmArgs("-XstartOnFirstThread")
}

val appName: String by project
val projectVersion: String by project

tasks.jar.get().apply {
	// sets the name of the .jar file this produces to the name of the game or app, with the version after.
	archiveFileName.set("${appName}-${projectVersion}.jar")
	// the duplicatesStrategy matters starting in Gradle 7.0; this setting works.
	duplicatesStrategy = DuplicatesStrategy.EXCLUDE
	dependsOn(configurations.runtimeClasspath)
	// FIXME: What does this do?  Does it need porting over?  If so, how to do so?
//	from {
//		configurations.runtimeClasspath.collect { if (it.isDirectory()) it else zipTree(it) }
//	}
	// these "exclude" lines remove some unnecessary duplicate files in the output JAR.
	exclude("META-INF/INDEX.LIST", "META-INF/*.SF", "META-INF/*.DSA", "META-INF/*.RSA")
	dependencies {
		exclude("META-INF/INDEX.LIST", "META-INF/maven/**")
	}
	manifest {
		attributes(
			"Main-Class" to application.mainClass,
			"Enable-Native-Access" to "ALL-UNNAMED"
		)
	}
	// this last step may help on some OSes that need extra instruction to make runnable JARs.
	doLast {
		file(archiveFile).setExecutable(/*executable =*/ true, /*ownerOnly =*/ false)
	}
}

// Builds a JAR that only includes the files needed to run on macOS, not Windows or Linux.
// The file size for a Mac-only JAR is about 7MB smaller than a cross-platform JAR.
tasks.register<Jar>("jarMac") {
	dependsOn("jar")
	group = "build"
	archiveFileName.set("${appName}-${projectVersion}-mac.jar")
	exclude(
		"windows/x86/**", "windows/x64/**",
		"linux/arm32/**", "linux/arm64/**", "linux/x64/**",
		"**/*.dll", "**/*.so",
		"META-INF/INDEX.LIST", "META-INF/*.SF", "META-INF/*.DSA", "META-INF/*.RSA"
	)
	dependencies {
		exclude(
			"windows/x86/**", "windows/x64/**",
			"linux/arm32/**", "linux/arm64/**", "linux/x64/**",
			"META-INF/INDEX.LIST", "META-INF/maven/**"
		)
	}
}

// Builds a JAR that only includes the files needed to run on Linux, not Windows or macOS.
// The file size for a Linux-only JAR is about 5MB smaller than a cross-platform JAR.
tasks.register<Jar>("jarLinux") {
	dependsOn("jar")
	group = "build"
	archiveFileName.set("${appName}-${projectVersion}-linux.jar")
	exclude(
		"windows/x86/**", "windows/x64/**",
		"macos/arm64/**", "macos/x64/**",
		"**/*.dll", "**/*.dylib",
		"META-INF/INDEX.LIST", "META-INF/*.SF", "META-INF/*.DSA", "META-INF/*.RSA"
	)
	dependencies {
		exclude(
			"windows/x86/**", "windows/x64/**",
			"macos/arm64/**", "macos/x64/**",
			"META-INF/INDEX.LIST", "META-INF/maven/**"
		)
	}
}

// Builds a JAR that only includes the files needed to run on Windows, not Linux or macOS.
// The file size for a Windows-only JAR is about 6MB smaller than a cross-platform JAR.
tasks.register<Jar>("jarWin") {
	dependsOn("jar")
	group = "build"
	archiveFileName.set("${appName}-${projectVersion}-win.jar")
	exclude(
		"macos/arm64/**", "macos/x64/**",
		"linux/arm32/**", "linux/arm64/**", "linux/x64/**",
		"**/*.dylib", "**/*.so",
		"META-INF/INDEX.LIST", "META-INF/*.SF", "META-INF/*.DSA", "META-INF/*.RSA"
	)
	dependencies {
		exclude(
			"macos/arm64/**", "macos/x64/**",
			"linux/arm32/**", "linux/arm64/**", "linux/x64/**",
			"META-INF/INDEX.LIST", "META-INF/maven/**"
		)
	}
}

construo {
	// name of the executable
	name.set(appName)
	// human-readable name, used for example in the `.app` name for macOS
	humanName.set(appName)

	targets.apply {
		register<Target.Linux>("linuxX64") {
			architecture.set(Target.Architecture.X86_64)
			jdkUrl.set("https://cache-redirector.jetbrains.com/intellij-jbr/jbr-25-linux-x64-b176.4.tar.gz")
			// Linux does not currently have a way to set the icon on the executable
		}
		register<Target.MacOs>("macM1") {
			architecture.set(Target.Architecture.AARCH64)
			jdkUrl.set("https://cache-redirector.jetbrains.com/intellij-jbr/jbr-25-osx-aarch64-b176.4.tar.gz")
			// macOS needs an identifier
			identifier.set("io.github.epicvon2468.eva_cleaning_simulator.$appName")
			// Optional: icon for macOS, as an ICNS file
			macIcon.set(project.file("icons/logo.icns"))
		}
		register<Target.MacOs>("macX64") {
			architecture.set(Target.Architecture.X86_64)
			jdkUrl.set("https://cache-redirector.jetbrains.com/intellij-jbr/jbr-25-osx-x64-b176.4.tar.gz")
			// macOS needs an identifier
			identifier.set("io.github.epicvon2468.eva_cleaning_simulator.$appName")
			// Optional: icon for macOS, as an ICNS file
			macIcon.set(project.file("icons/logo.icns"))
		}
		register<Target.Windows>("winX64") {
			architecture.set(Target.Architecture.X86_64)
			// Optional: icon for Windows, as a PNG
			icon.set(project.file("icons/logo.png"))
			jdkUrl.set("https://cache-redirector.jetbrains.com/intellij-jbr/jbr-25-windows-x64-b176.4.zip")
			// Uncomment the next line to show a console when the game runs, to print messages.
			//useConsole.set(true)
		}
	}
}

// Equivalent to the jar task; here for compatibility with gdx-setup.
tasks.register("dist") {
	dependsOn("jar")
}

distributions {
	main {
		contents {
			into("libs") {
				project.configurations.runtimeClasspath.get().files.filter { file: File ->
					file.name != project.tasks.jar.get().outputs.files.singleFile.name
				}.forEach { file: File ->
					exclude(file.name)
				}
			}
		}
	}
}

tasks.startScripts.get().dependsOn(":lwjgl3:jar")
tasks.startScripts.get().classpath = project.tasks.jar.get().outputs.files