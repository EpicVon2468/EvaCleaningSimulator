package io.github.epicvon2468.eva_cleaning_simulator.screens.world

import com.badlogic.gdx.utils.viewport.FitViewport
import com.badlogic.gdx.utils.viewport.Viewport

import io.github.epicvon2468.eva_cleaning_simulator.Main
import io.github.epicvon2468.eva_cleaning_simulator.screens.StageScreen

abstract class WorldScreen(
	main: Main,
	val worldWidth: Float = 640f,
	val worldHeight: Float = 480f,
	needsInit: Boolean = true
) : StageScreen(main, needsInit) {

	override fun stageViewportDefault(): Viewport = FitViewport(worldWidth, worldHeight)
}