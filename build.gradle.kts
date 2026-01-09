import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.net.URI

buildscript {
	repositories {
		mavenCentral()
		gradlePluginPortal()
		mavenLocal()
		google()
		//maven { url = java.net.URI("https://central.sonatype.com/repository/maven-snapshots/") }
	}
	dependencies {
		val kotlinVersion: String by project
//		classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
	}
}

plugins {
	kotlin("jvm") version "2.3.0"
}

repositories {
	mavenCentral()
}

allprojects {
//	plugins.getAt("idea").apply(this)
//
//	// This allows you to "Build and run using IntelliJ IDEA", an option in IDEA's Settings.
//	idea {
//		module {
//			outputDir = file('build/classes/java/main')
//			testOutputDir = file('build/classes/java/test')
//		}
//	}
}

configure(subprojects) {
//	plugins {
//		id("java-library") apply true
//		id("kotlin") apply true
//	}
//	java.sourceCompatibility = 25

	// From https://lyze.dev/2021/04/29/libGDX-Internal-Assets-List/
	// The article can be helpful when using assets.txt in your project.
	tasks.register("generateAssetList") {
		inputs.dir("${project.rootDir}/assets/")
		// projectFolder/assets
		val assetsFolder = File("${project.rootDir}/assets/")
		// projectFolder/assets/assets.txt
		val assetsFile = File(assetsFolder, "assets.txt")
		// delete that file in case we've already created it
		assetsFile.delete()

		// iterate through all files inside that folder
		// convert it to a relative path
		// and append it to the file assets.txt
		for (entry in assetsFolder.listFiles().sorted()) {
			assetsFile.writeText(entry.path.substringAfter(assetsFolder.path))
		}
//		fileTree(assetsFolder).collect { assetsFolder.relativePath(it) }.sort().forEach {
//			assetsFile.append("$it\n")
//		}
	}
	tasks.findByPath("processResources")?.dependsOn("generateAssetList")

//	compileJava {
//		options.setIncremental(true)
//	}
//	compileKotlin.compilerOptions.jvmTarget.set(JvmTarget.JVM_25)
//	compileTestKotlin.compilerOptions.jvmTarget.set(JvmTarget.JVM_25)
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