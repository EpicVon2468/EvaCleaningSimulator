package io.github.epicvon2468.eva_cleaning_simulator.assets

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator

import ktx.freetype.generateFont

object Fonts {

	val jetbrainsMono: BitmapFont by lazy {
		FreeTypeFontGenerator(Gdx.files.internal("JetBrainsMono-Light.ttf")).generateFont {
			mono = true
			borderStraight = true
			minFilter = Texture.TextureFilter.Linear
			magFilter = Texture.TextureFilter.Linear
		}
	}
}