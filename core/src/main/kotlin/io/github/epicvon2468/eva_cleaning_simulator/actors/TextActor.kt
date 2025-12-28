package io.github.epicvon2468.eva_cleaning_simulator.actors

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.scenes.scene2d.Actor

import io.github.epicvon2468.eva_cleaning_simulator.assets.Fonts

class TextActor : Actor() {

	override fun draw(batch: Batch, parentAlpha: Float) {
		Fonts.jetbrainsMono.draw(batch, "Hello, world!", 500.0f, 500.0f)
	}
}