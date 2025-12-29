package io.github.epicvon2468.eva_cleaning_simulator.actors

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.GlyphLayout
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.utils.viewport.Viewport

import io.github.epicvon2468.eva_cleaning_simulator.assets.Fonts

data class TextActor(@JvmField val text: String = "Hello, world!") : Actor() {

	val viewport: Viewport get() = stage!!.viewport
	@JvmField val layout: GlyphLayout = GlyphLayout(Fonts.jetBrainsMono, text)

	override fun draw(batch: Batch, parentAlpha: Float) {
		Fonts.jetBrainsMono.draw(batch, text, (viewport.worldWidth - layout.width) / 2f, (viewport.worldHeight + layout.height) / 2f)
	}
}