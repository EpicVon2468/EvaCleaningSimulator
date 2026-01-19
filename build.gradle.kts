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

	// NOTE: This task has been partially rewritten to work with the Kotlin Gradle DSL
	// From https://lyze.dev/2021/04/29/libGDX-Internal-Assets-List/
	// The article can be helpful when using assets.txt in your project.
	tasks.register("generateAssetList") {
		// projectFolder/assets
		val assetsFolder: File = project.rootDir.resolve("assets")
		inputs.dir(assetsFolder)
		// projectFolder/assets/assets.txt
		val assetsFile: File = assetsFolder.resolve("assets.txt")
		// delete that file in case we've already created it
		assetsFile.delete()

		// iterate through all files inside that folder
		// convert it to a relative path
		// and append it to the file assets.txt
		fun appendRecursive(folder: File) {
			for (entry: File in folder.listFiles().sorted()) {
				if (entry.isDirectory) {
					appendRecursive(entry)
					continue
				}
				assetsFile.appendText(entry.path.substringAfter(assetsFolder.path).drop(1) + '\n')
			}
		}
		appendRecursive(assetsFolder)
	}
	tasks.processResources.get().dependsOn("generateAssetList")

	tasks.compileJava.get().apply {
		options.isIncremental = true
	}
//	tasks.compileKotlin.get().compilerOptions.jvmTarget.set(JvmTarget.JVM_25)
//	tasks.compileTestKotlin.get().compilerOptions.jvmTarget.set(JvmTarget.JVM_25)
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