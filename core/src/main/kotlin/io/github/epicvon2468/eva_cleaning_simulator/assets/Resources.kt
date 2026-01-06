package io.github.epicvon2468.eva_cleaning_simulator.assets

import com.badlogic.gdx.assets.AssetManager

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
		Textures
	}
}