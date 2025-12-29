package io.github.epicvon2468.eva_cleaning_simulator.assets

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator

import ktx.freetype.generateFont

object Fonts {

	val jetBrainsMono: BitmapFont by lazy {
		FreeTypeFontGenerator(Resources.getFont("JetBrainsMono-Light.ttf")).generateFont {
			mono = true
			borderStraight = true
			kerning = false
			size = 32
			minFilter = Texture.TextureFilter.Linear
			magFilter = Texture.TextureFilter.Linear
		}
	}
}