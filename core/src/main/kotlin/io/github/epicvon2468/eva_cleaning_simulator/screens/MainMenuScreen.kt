package io.github.epicvon2468.eva_cleaning_simulator.screens

import io.github.epicvon2468.eva_cleaning_simulator.actors.TextActor

class MainMenuScreen : StageScreen() {

	override fun postInit() {
		stage.addActor(TextActor())
	}
}