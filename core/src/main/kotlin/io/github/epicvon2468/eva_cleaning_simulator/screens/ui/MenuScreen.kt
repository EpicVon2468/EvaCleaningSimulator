package io.github.epicvon2468.eva_cleaning_simulator.screens.ui

import com.badlogic.gdx.utils.viewport.Viewport

import io.github.epicvon2468.eva_cleaning_simulator.Main
import io.github.epicvon2468.eva_cleaning_simulator.screens.StageScreen

import ktx.graphics.LetterboxingViewport

abstract class MenuScreen(main: Main, needsInit: Boolean = true) : StageScreen(main, needsInit) {

	override fun stageViewportDefault(): Viewport = LetterboxingViewport()
}