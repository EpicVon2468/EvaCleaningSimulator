package io.github.epicvon2468.eva_cleaning_simulator.assets

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.assets.loaders.I18NBundleLoader
import com.badlogic.gdx.files.FileHandle
import com.badlogic.gdx.utils.I18NBundle

import ktx.freetype.registerFreeTypeFontLoaders

import java.util.Locale

// TODO: https://libgdx.com/wiki/managing-your-assets
// 	Add loading screen + make async
data object Resources : AssetManager() {

	init {
		registerFreeTypeFontLoaders()
		loadAll()
	}

	fun loadAll() {
		load("resources/i18n/i18n", I18NBundle::class.java, I18NBundleLoader.I18NBundleParameter(Locale.getDefault(), "ISO-8859-1"))
	}

	fun getFont(path: String): FileHandle = this[path]
	fun getTexture(path: String): FileHandle = this[path]
	fun getTranslation(): I18NBundle = finishLoadingAsset<I18NBundle>("resources/i18n/i18n")
}