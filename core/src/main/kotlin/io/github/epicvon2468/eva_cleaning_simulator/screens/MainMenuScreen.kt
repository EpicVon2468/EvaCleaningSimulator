package io.github.epicvon2468.eva_cleaning_simulator.screens

import com.badlogic.gdx.scenes.scene2d.ui.Label

import io.github.epicvon2468.eva_cleaning_simulator.assets.Skins

class MainMenuScreen : StageScreen() {

	override fun postInit() {
		//stage.addActor(TextActor())
		table.add(Label("Hello, world!", Skins.primary))
	}
}