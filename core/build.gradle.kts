@file:OptIn(ExperimentalKotlinGradlePluginApi::class)
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi

plugins {
	kotlin("jvm") version "2.3.0"
}

tasks.compileJava.get().options.encoding = "UTF-8"
tasks.compileTestJava.get().options.encoding = "UTF-8"

repositories {
	mavenCentral()
}

dependencies {
	val ashleyVersion: String by project
	val gdxControllersVersion: String by project
	val gdxVersion: String by project
	val visUiVersion: String by project
	val ktxVersion: String by project
	val kotlinVersion: String by project
	val kotlinxCoroutinesVersion: String by project
	api("com.badlogicgames.ashley:ashley:$ashleyVersion")
	api("com.badlogicgames.gdx-controllers:gdx-controllers-core:$gdxControllersVersion")
	api("com.badlogicgames.gdx:gdx-box2d:$gdxVersion")
	api("com.badlogicgames.gdx:gdx-freetype:$gdxVersion")
	api("com.badlogicgames.gdx:gdx:$gdxVersion")
	api("com.kotcrab.vis:vis-ui:$visUiVersion")
	api("io.github.libktx:ktx-actors:$ktxVersion")
	api("io.github.libktx:ktx-app:$ktxVersion")
	api("io.github.libktx:ktx-ashley:$ktxVersion")
	api("io.github.libktx:ktx-assets-async:$ktxVersion")
	api("io.github.libktx:ktx-assets:$ktxVersion")
	api("io.github.libktx:ktx-async:$ktxVersion")
	api("io.github.libktx:ktx-box2d:$ktxVersion")
	api("io.github.libktx:ktx-collections:$ktxVersion")
	api("io.github.libktx:ktx-freetype-async:$ktxVersion")
	api("io.github.libktx:ktx-freetype:$ktxVersion")
	api("io.github.libktx:ktx-graphics:$ktxVersion")
	api("io.github.libktx:ktx-i18n:$ktxVersion")
	api("io.github.libktx:ktx-json:$ktxVersion")
	api("io.github.libktx:ktx-log:$ktxVersion")
	api("io.github.libktx:ktx-math:$ktxVersion")
	api("io.github.libktx:ktx-preferences:$ktxVersion")
	api("io.github.libktx:ktx-reflect:$ktxVersion")
	api("io.github.libktx:ktx-scene2d:$ktxVersion")
	api("io.github.libktx:ktx-style:$ktxVersion")
	api("io.github.libktx:ktx-tiled:$ktxVersion")
	api("io.github.libktx:ktx-vis-style:$ktxVersion")
	api("io.github.libktx:ktx-vis:$ktxVersion")
	api("org.jetbrains.kotlin:kotlin-stdlib:$kotlinVersion")
	api("org.jetbrains.kotlinx:kotlinx-coroutines-core:$kotlinxCoroutinesVersion")
	compileOnly("org.jetbrains:annotations:26.0.2-1")
	implementation(project(":gdx_helpers"))
}

kotlin {
	jvmToolchain(25)
	kotlinDaemonJvmArgs = listOf("-XX:+UseCompactObjectHeaders", "--enable-native-access=ALL-UNNAMED")
}