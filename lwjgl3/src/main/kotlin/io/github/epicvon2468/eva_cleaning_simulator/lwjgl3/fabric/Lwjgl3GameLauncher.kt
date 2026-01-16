package io.github.epicvon2468.eva_cleaning_simulator.lwjgl3.fabric

import io.github.epicvon2468.eva_cleaning_simulator.lwjgl3.StartupHelper

import net.fabricmc.loader.impl.launch.knot.KnotClient

// Intercept main to allow JVM restart if needed.
object Lwjgl3GameLauncher {

	@JvmStatic
	fun main(args: Array<String>) {
		if (StartupHelper.startNewJvmIfRequired()) return
		KnotClient.main(args)
	}
}