package io.github.epicvon2468.eva_cleaning_simulator.screens.world

import io.github.epicvon2468.eva_cleaning_simulator.Main
import io.github.epicvon2468.eva_cleaning_simulator.assets.Textures
import io.github.epicvon2468.gdx_helpers.actors.SpriteActor

class TutorialWorldScreen(main: Main) : WorldScreen(main) {

	override fun postInit() {
		val debug: SpriteActor = SpriteActor(Textures.notFoundTexture).debug()
		val alien: SpriteActor = SpriteActor(Textures["other", "alien.png"]).debug()
		stage.addActor(debug)
		stage.addActor(alien)
		table.add(debug.copy()).width(64f)
		table.row()
		table.add(alien.copy())
	}
}