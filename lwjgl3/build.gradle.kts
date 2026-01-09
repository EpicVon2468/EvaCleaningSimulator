@file:OptIn(ExperimentalKotlinGradlePluginApi::class)

import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi

import io.github.fourlastor.construo.Target

buildscript {
	repositories {
		gradlePluginPortal()
	}
	dependencies {
		classpath("io.github.fourlastor:construo:2.1.0")
//		if (enableGraalNative == 'true') {
//			classpath "org.graalvm.buildtools.native:org.graalvm.buildtools.native.gradle.plugin:0.9.28"
//		}
	}
}
plugins {
	id("application")
	kotlin("jvm") apply true
	id("io.github.fourlastor.construo") version "2.1.0" apply true
}

sourceSets.main.get().resources.srcDirs += rootProject.file("assets")
application.mainClass = "io.github.epicvon2468.eva_cleaning_simulator.lwjgl3.Lwjgl3Launcher"
kotlin {
	jvmToolchain(25)
	kotlinDaemonJvmArgs = listOf("-XX:+UseCompactObjectHeaders", "--enable-native-access=ALL-UNNAMED")
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

//	if (enableGraalNative == 'true') {
//		implementation "io.github.berstanio:gdx-svmhelper-backend-lwjgl3:$graalHelperVersion"
//		implementation "io.github.berstanio:gdx-svmhelper-extension-box2d:$graalHelperVersion"
//		implementation "io.github.berstanio:gdx-svmhelper-extension-freetype:$graalHelperVersion"
//	}
}

tasks.run.get().apply {
	workingDir = rootProject.file("assets")
	// You can uncomment the next line if your IDE claims a build failure even when the app closed properly.
	//setIgnoreExitValue(true)
	jvmArgs("--enable-native-access=ALL-UNNAMED")
	environment("__GL_THREADED_OPTIMIZATIONS", 0)

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
//	from {
//		configurations.runtimeClasspath.collect { if (it.isDirectory()) it else zipTree(it) }
//	}
	// these "exclude" lines remove some unnecessary duplicate files in the output JAR.
	exclude("META-INF/INDEX.LIST", "META-INF/*.SF", "META-INF/*.DSA", "META-INF/*.RSA")
	dependencies {
		exclude("META-INF/INDEX.LIST", "META-INF/maven/**")
	}
	// setting the manifest makes the JAR runnable.
	// enabling native access helps avoid a warning when Java 24 or later runs the JAR.
	manifest {
		attributes("Main-Class" to application.mainClass, "Enable-Native-Access" to "ALL-UNNAMED")
	}
	// this last step may help on some OSes that need extra instruction to make runnable JARs.
	doLast {
		file(archiveFile).setExecutable(true, false)
	}
}

// Builds a JAR that only includes the files needed to run on macOS, not Windows or Linux.
// The file size for a Mac-only JAR is about 7MB smaller than a cross-platform JAR.
tasks.register<Jar>("jarMac") {
	dependsOn("jar")
	group = "build"
	archiveFileName.set("${appName}-${projectVersion}-mac.jar")
	exclude("windows/x86/**", "windows/x64/**", "linux/arm32/**", "linux/arm64/**", "linux/x64/**", "**/*.dll", "**/*.so",
		"META-INF/INDEX.LIST", "META-INF/*.SF", "META-INF/*.DSA", "META-INF/*.RSA")
	dependencies {
		exclude("windows/x86/**", "windows/x64/**", "linux/arm32/**", "linux/arm64/**", "linux/x64/**",
			"META-INF/INDEX.LIST", "META-INF/maven/**")
	}
}

// Builds a JAR that only includes the files needed to run on Linux, not Windows or macOS.
// The file size for a Linux-only JAR is about 5MB smaller than a cross-platform JAR.
tasks.register<Jar>("jarLinux") {
	dependsOn("jar")
	group = "build"
	archiveFileName.set("${appName}-${projectVersion}-linux.jar")
	exclude("windows/x86/**", "windows/x64/**", "macos/arm64/**", "macos/x64/**", "**/*.dll", "**/*.dylib",
		"META-INF/INDEX.LIST", "META-INF/*.SF", "META-INF/*.DSA", "META-INF/*.RSA")
	dependencies {
		exclude("windows/x86/**", "windows/x64/**", "macos/arm64/**", "macos/x64/**",
			"META-INF/INDEX.LIST", "META-INF/maven/**")
	}
}

// Builds a JAR that only includes the files needed to run on Windows, not Linux or macOS.
// The file size for a Windows-only JAR is about 6MB smaller than a cross-platform JAR.
tasks.register<Jar>("jarWin") {
	dependsOn("jar")
	group = "build"
	archiveFileName.set("${appName}-${projectVersion}-win.jar")
	exclude("macos/arm64/**", "macos/x64/**", "linux/arm32/**", "linux/arm64/**", "linux/x64/**", "**/*.dylib", "**/*.so",
		"META-INF/INDEX.LIST", "META-INF/*.SF", "META-INF/*.DSA", "META-INF/*.RSA")
	dependencies {
		exclude("macos/arm64/**", "macos/x64/**", "linux/arm32/**", "linux/arm64/**", "linux/x64/**",
			"META-INF/INDEX.LIST", "META-INF/maven/**")
	}
}

construo {
	// name of the executable
	name.set(appName)
	// human-readable name, used for example in the `.app` name for macOS
	humanName.set(appName)

	targets.register<Target.Linux>("linuxX64") {
		architecture.set(Target.Architecture.X86_64)
		jdkUrl.set("https://cache-redirector.jetbrains.com/intellij-jbr/jbr-25-linux-x64-b176.4.tar.gz")
		// Linux does not currently have a way to set the icon on the executable
	}
	targets.register<Target.MacOs>("macM1") {
		architecture.set(Target.Architecture.AARCH64)
		jdkUrl.set("https://cache-redirector.jetbrains.com/intellij-jbr/jbr-25-osx-aarch64-b176.4.tar.gz")
		// macOS needs an identifier
		identifier.set("io.github.epicvon2468.eva_cleaning_simulator.$appName")
		// Optional: icon for macOS, as an ICNS file
		macIcon.set(project.file("icons/logo.icns"))
	}
	targets.register<Target.MacOs>("macX64") {
		architecture.set(Target.Architecture.X86_64)
		jdkUrl.set("https://cache-redirector.jetbrains.com/intellij-jbr/jbr-25-osx-x64-b176.4.tar.gz")
		// macOS needs an identifier
		identifier.set("io.github.epicvon2468.eva_cleaning_simulator.$appName")
		// Optional: icon for macOS, as an ICNS file
		macIcon.set(project.file("icons/logo.icns"))
	}
	targets.register<Target.Windows>("winX64") {
		architecture.set(Target.Architecture.X86_64)
		// Optional: icon for Windows, as a PNG
		icon.set(project.file("icons/logo.png"))
		jdkUrl.set("https://cache-redirector.jetbrains.com/intellij-jbr/jbr-25-windows-x64-b176.4.zip")
		// Uncomment the next line to show a console when the game runs, to print messages.
		//useConsole.set(true)
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

//if (enableGraalNative == 'true') {
//	apply from: file("nativeimage.gradle")
//}