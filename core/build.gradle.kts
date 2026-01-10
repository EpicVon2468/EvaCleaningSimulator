@file:OptIn(ExperimentalKotlinGradlePluginApi::class)
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi

plugins {
	alias(libs.plugins.kotlin.jvm)
}

tasks.compileJava.get().options.encoding = "UTF-8"
tasks.compileTestJava.get().options.encoding = "UTF-8"

repositories {
	mavenCentral()
}

dependencies {
	val kotlinxCoroutinesVersion: String by project
	api(libs.ashley)
	api(libs.libGDX.controllers.core)
	api(libs.libGDX.box2D)
	api(libs.libGDX.freetype)
	api(libs.libGDX)
	api(libs.visUI)
	api(libs.libKTX.actors)
	api(libs.libKTX.app)
	api(libs.libKTX.ashley)
	api(libs.libKTX.assets)
	api(libs.libKTX.assets.async)
	api(libs.libKTX.async)
	api(libs.libKTX.box2D)
	api(libs.libKTX.collections)
	api(libs.libKTX.freetype)
	api(libs.libKTX.freetype.async)
	api(libs.libKTX.graphics)
	api(libs.libKTX.i18n)
	api(libs.libKTX.json)
	api(libs.libKTX.log)
	api(libs.libKTX.maths)
	api(libs.libKTX.preferences)
	api(libs.libKTX.reflect)
	api(libs.libKTX.scene2D)
	api(libs.libKTX.style)
	api(libs.libKTX.tiled)
	api(libs.libKTX.vis)
	api(libs.libKTX.vis.style)
	api("org.jetbrains.kotlinx:kotlinx-coroutines-core:$kotlinxCoroutinesVersion")
	compileOnly(libs.jetBrains.annotations)
	implementation(project(":gdx_helpers"))
}

kotlin {
	jvmToolchain(25)
	kotlinDaemonJvmArgs = listOf("-XX:+UseCompactObjectHeaders", "--enable-native-access=ALL-UNNAMED")
}