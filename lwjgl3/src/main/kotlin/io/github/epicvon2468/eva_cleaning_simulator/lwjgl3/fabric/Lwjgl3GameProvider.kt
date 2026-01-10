package io.github.epicvon2468.eva_cleaning_simulator.lwjgl3.fabric

import net.fabricmc.loader.impl.game.GameProvider
import net.fabricmc.loader.impl.game.patch.GameTransformer
import net.fabricmc.loader.impl.launch.FabricLauncher
import net.fabricmc.loader.impl.util.Arguments

import java.lang.reflect.Method
import java.nio.file.Path
import java.io.File

import kotlin.io.path.exists
import kotlin.io.path.isDirectory

class Lwjgl3GameProvider : GameProvider {

	private lateinit var args: Arguments

	override fun getGameId(): String = "eva_cleaning_simulator"

	override fun getGameName(): String = "Evangelion Cleaning Simulator"

	override fun getRawGameVersion(): String = "0.0.0"

	override fun getNormalizedGameVersion(): String = "0.0.0"

	override fun getBuiltinMods(): Collection<GameProvider.BuiltinMod> = emptyList()

	override fun getEntrypoint(): String = "io.github.epicvon2468.eva_cleaning_simulator.lwjgl3.Lwjgl3Launcher"

	override fun getLaunchDirectory(): Path = File("").absoluteFile.parentFile.resolve(".run").toPath()

	override fun requiresUrlClassLoader(): Boolean = false

	override fun getBuiltinTransforms(className: String): Set<GameProvider.BuiltinTransform> = emptySet()

	override fun isEnabled(): Boolean = true

	private val classPath: MutableSet<Path> = mutableSetOf()

	override fun locateGame(
		launcher: FabricLauncher,
		args: Array<String>
	): Boolean {
		this.args = Arguments().apply { parse(args) }
		for (path: Path in launcher.classPath) {
			println("path: $path")
			if (!path.isDirectory()) {
				if ("core/build/libs/core-" in path.toString()) this.classPath.add(path)
				continue
			}
			println("got path: $path")
			if (
				path.resolve("io/github/epicvon2468/eva_cleaning_simulator/MainKt.class").exists()
				|| path.resolve("io/github/epicvon2468/eva_cleaning_simulator/lwjgl3/Lwjgl3Launcher.class").exists()
			) {
				this.classPath.add(path)
				println("exists")
			}
		}
		return true
	}

	override fun initialize(launcher: FabricLauncher) = this.entrypointTransformer.locateEntrypoints(launcher, this.classPath.toList())

	private val transformer = GameTransformer()
	override fun getEntrypointTransformer(): GameTransformer = this.transformer

	override fun unlockClassPath(launcher: FabricLauncher) {
		this.classPath.forEach(launcher::addToClassPath)
	}

	override fun launch(loader: ClassLoader) {
		val `class`: Class<*> = loader.loadClass(this.entrypoint)
		val main: Method = `class`.getMethod("main", Array<String>::class.java)
		main(null, this.arguments.toArray())
	}

	override fun getArguments(): Arguments = args

	override fun getLaunchArguments(sanitize: Boolean): Array<String> = this.arguments.toArray()
}