package io.github.epicvon2468.eva_cleaning_simulator.screens.world

import io.github.epicvon2468.eva_cleaning_simulator.Main
import io.github.epicvon2468.eva_cleaning_simulator.actors.SpriteActor
import io.github.epicvon2468.eva_cleaning_simulator.assets.Textures

class TutorialWorldScreen(main: Main) : WorldScreen(main) {

	override fun postInit() {
		stage.addActor(SpriteActor(Textures.notFoundTexture).debug())
	}
}