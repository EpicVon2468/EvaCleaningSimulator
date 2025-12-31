package io.github.epicvon2468.eva_cleaning_simulator.assets

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.Texture

import ktx.freetype.registerFreeTypeFontLoaders

// https://libgdx.com/wiki/managing-your-assets
data object Resources : AssetManager() {

	init {
		registerFreeTypeFontLoaders()
		loadAll()
	}

	@Suppress("UnusedExpression")
	fun loadAll() {
		I18n
		Fonts
	}

	fun getTexture(path: String): Texture = this[path]
}