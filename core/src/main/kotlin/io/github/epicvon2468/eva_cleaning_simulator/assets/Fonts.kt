package io.github.epicvon2468.eva_cleaning_simulator.assets

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont

import ktx.assets.Asset
import ktx.assets.getValue
import ktx.freetype.loadFreeTypeFont

data object Fonts {

	private val _jetBrainsMono: Asset<BitmapFont> = Resources.loadFreeTypeFont("resources/fonts/JetBrainsMono-Light.ttf") {
		mono = true
		borderStraight = true
		kerning = false
		size = 32
		minFilter = Texture.TextureFilter.Linear
		magFilter = Texture.TextureFilter.Linear
	}
	val jetBrainsMono: BitmapFont by _jetBrainsMono

	@JvmField
	@Suppress("GDXKotlinStaticResource")
	val preResourceLoad: BitmapFont = BitmapFont()
}