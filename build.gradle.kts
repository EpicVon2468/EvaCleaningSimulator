@file:OptIn(ExperimentalKotlinGradlePluginApi::class)
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi

import java.net.URI

buildscript {
	repositories {
		mavenCentral()
		gradlePluginPortal()
		mavenLocal()
		google()
		//maven { url = java.net.URI("https://central.sonatype.com/repository/maven-snapshots/") }
	}
}

// TODO: Replace all dependencies with version catalogues `libs.versions.toml`.
plugins {
	alias(libs.plugins.kotlin.jvm)
}

kotlin {
	jvmToolchain(25)
	kotlinDaemonJvmArgs = listOf("-XX:+UseCompactObjectHeaders", "--enable-native-access=ALL-UNNAMED")
}

repositories {
	mavenCentral()
}

allprojects {
	apply(plugin = "idea")
	// This allows you to "Build and run using IntelliJ IDEA", an option in IDEA's Settings.
//	idea {
//		module {
//			outputDir = file("build/classes/java/main")
//			testOutputDir = file("build/classes/java/test")
//		}
//	}
}

configure(subprojects) {
	apply(plugin = "java-library")
	java.sourceCompatibility = JavaVersion.VERSION_25

	tasks.compileJava.get().apply {
		options.isIncremental = true
	}
//	tasks.compileKotlin.get().compilerOptions.jvmTarget.set(JvmTarget.JVM_25)
//	tasks.compileTestKotlin.get().compilerOptions.jvmTarget.set(JvmTarget.JVM_25)

	if (project.name == "gdx_helpers") return@configure

	val classPathListing: File = rootProject.file("build/classPathListing-${project.name}.txt")

	// NOTE: This task has been partially rewritten to work with the Kotlin Gradle DSL
	// From https://lyze.dev/2021/04/29/libGDX-Internal-Assets-List/
	// The article can be helpful when using assets.txt in your project.
	tasks.register("generateAssetList") {
		// projectFolder/assets
		val assetsFolder: File = rootProject.file("assets")
		inputs.dir(assetsFolder)
		// projectFolder/assets/assets.txt
		val assetsFile: File = assetsFolder.resolve("assets.txt")
		// delete that file in case we've already created it
		assetsFile.delete()

		// iterate through all files inside that folder
		// convert it to a relative path
		// and append it to the file assets.txt
		fun appendRecursive(rootFolder: File, folder: File = rootFolder, outputFile: File) {
			for (entry: File in folder.listFiles().sorted()) {
				if (entry.isDirectory) {
					appendRecursive(
						rootFolder = rootFolder,
						folder = entry,
						outputFile = outputFile
					)
					continue
				}
				outputFile.appendText(entry.path.substringAfter(rootFolder.path).drop(1) + '\n')
			}
		}
		appendRecursive(rootFolder = assetsFolder, outputFile = assetsFile)

		// This stuff is for optimising the extraction of the classpath to /tmp/* for FabricLoader to use.
		classPathListing.delete()
		val classPath: File = project.file("src/main/kotlin")
		inputs.dir(classPath)
		appendRecursive(rootFolder = classPath, outputFile = classPathListing)
	}
	tasks.processResources.get().dependsOn("generateAssetList")

	tasks.processResources.get().apply {
		from(rootProject.files("assets")).into(project.file("src/main/resources"))
	}
}

subprojects {
	val projectVersion: String by project
	version = projectVersion
	//ext.appName = "EvaCleaningSimulator"
	repositories {
		mavenCentral()
		// You may want to remove the following line if you have errors downloading dependencies.
		mavenLocal()
		maven { url = URI("https://central.sonatype.com/repository/maven-snapshots/") }
		maven { url = URI("https://jitpack.io") }
	}
}