package io.github.epicvon2468.eva_cleaning_simulator.lwjgl3.fabric

import net.fabricmc.loader.impl.game.GameProvider
import net.fabricmc.loader.impl.game.patch.GameTransformer
import net.fabricmc.loader.impl.launch.FabricLauncher
import net.fabricmc.loader.impl.util.Arguments

import java.nio.file.Path
import java.io.File
import java.io.InputStream
import java.lang.invoke.MethodHandle
import java.lang.invoke.MethodHandles
import java.lang.invoke.MethodType
import java.nio.file.Files
import java.util.jar.JarEntry
import java.util.jar.JarFile

import kotlin.concurrent.thread
import kotlin.io.path.ExperimentalPathApi
import kotlin.io.path.createDirectories
import kotlin.io.path.deleteRecursively
import kotlin.io.path.exists
import kotlin.io.path.isDirectory
import kotlin.io.path.notExists
import kotlin.io.path.writeBytes

class Lwjgl3GameProvider : GameProvider {

	private lateinit var args: Arguments

	override fun getGameId(): String = "eva_cleaning_simulator"

	override fun getGameName(): String = "Evangelion Cleaning Simulator"

	override fun getRawGameVersion(): String = "0.0.0"

	override fun getNormalizedGameVersion(): String = "0.0.0"

	override fun getBuiltinMods(): Collection<GameProvider.BuiltinMod> = emptyList()

	override fun getEntrypoint(): String = "io.github.epicvon2468.eva_cleaning_simulator.lwjgl3.Lwjgl3Launcher"

	override fun getLaunchDirectory(): Path = File("").absoluteFile.resolve(".run").toPath()

	override fun requiresUrlClassLoader(): Boolean = false

	override fun getBuiltinTransforms(className: String): Set<GameProvider.BuiltinTransform> = emptySet()

	override fun isEnabled(): Boolean = true

	private val classPath: MutableSet<Path> = mutableSetOf()

	override fun locateGame(
		launcher: FabricLauncher,
		args: Array<String>
	): Boolean {
		this.args = Arguments().apply { parse(args) }
		launcher.classPath.filterTo(this.classPath) { path: Path ->
			return@filterTo if (!path.isDirectory()) extractClassPath(path).let { false }
			else path.resolve(LWJGL3LAUNCHER_CLASS).exists()
		}
		return this.classPath.isNotEmpty()
	}

	@OptIn(ExperimentalPathApi::class)
	private fun extractClassPath(jarPath: Path) {
		if (classPath.any { it.startsWith(System.getProperty("java.io.tmpdir")) }) return
		val jar = JarFile(jarPath.toFile())
		val classPathListing: JarEntry? = jar.getJarEntry("classPathListing.txt")
		if (classPathListing == null) {
			jar.close()
			return
		}
		val listing: String = jar.getInputStream(classPathListing).use(InputStream::readAllBytes).decodeToString()
		val tmp: Path = Files.createTempDirectory("EvaCleaningSimulator")
		for (entryName: String in listing.split('\n').dropLast(1)) {
			if (!entryName.startsWith("io/github/epicvon2468/eva_cleaning_simulator")) continue
			val classForm: String = entryName.dropLast(2) + "class"
			val entry: JarEntry = jar.getJarEntry(classForm) ?: error("No entry found in jar '$jarPath' for name '$classForm'!")
			val path: Path = tmp.resolve(entry.name)
			path.parent.let { parent: Path ->
				if (parent.notExists()) parent.createDirectories()
			}
			path.writeBytes(jar.getInputStream(entry).use(InputStream::readAllBytes))
		}
		println("tmp: $tmp")
		this.classPath.add(tmp)
		jar.close()
		Runtime.getRuntime().addShutdownHook(thread(start = false, name = "ExtractedGameClassPathCleaner") {
			tmp.deleteRecursively()
		})
	}

	override fun initialize(launcher: FabricLauncher) = this.entrypointTransformer.locateEntrypoints(launcher, this.classPath.toList())

	private val transformer = GameTransformer()
	override fun getEntrypointTransformer(): GameTransformer = this.transformer

	override fun unlockClassPath(launcher: FabricLauncher) = this.classPath.forEach(launcher::addToClassPath)

	override fun launch(loader: ClassLoader) {
		val `class`: Class<*> = loader.loadClass(this.entrypoint)
		val main: MethodHandle = MethodHandles.publicLookup().findStatic(
			/*refc =*/ `class`,
			/*name =*/ "main",
			/*type =*/ MethodType.fromMethodDescriptorString(
				/*descriptor =*/ "([Ljava/lang/String;)V",
				/*loader =*/ loader
			)
		)
		main.invokeExact(this.arguments.toArray())
	}

	override fun getArguments(): Arguments = args

	override fun getLaunchArguments(sanitize: Boolean): Array<String> = this.arguments.toArray()

	companion object {

		const val LWJGL3LAUNCHER_CLASS: String = "io/github/epicvon2468/eva_cleaning_simulator/lwjgl3/Lwjgl3Launcher.class"
	}
}