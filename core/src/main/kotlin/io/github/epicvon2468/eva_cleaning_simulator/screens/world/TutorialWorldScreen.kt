package io.github.epicvon2468.eva_cleaning_simulator.screens.world

import io.github.epicvon2468.eva_cleaning_simulator.Main
import io.github.epicvon2468.eva_cleaning_simulator.assets.Textures
import io.github.epicvon2468.gdx_helpers.actors.SpriteActor

class TutorialWorldScreen(main: Main) : WorldScreen(main) {

	override fun postInit() {
		stage.addActor(SpriteActor(Textures.notFoundTexture).debug())
		stage.addActor(SpriteActor(Textures["alien.png"]).debug())
	}
}