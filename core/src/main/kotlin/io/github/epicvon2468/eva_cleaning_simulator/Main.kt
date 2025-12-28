package io.github.epicvon2468.eva_cleaning_simulator

import io.github.epicvon2468.eva_cleaning_simulator.screen.StageScreen

import ktx.app.KtxGame
import ktx.app.KtxScreen
import ktx.async.KtxAsync

class Main : KtxGame<KtxScreen>() {

	override fun create() {
		KtxAsync.initiate()

		addScreen(StageScreen())
		setScreen<StageScreen>()
	}
}