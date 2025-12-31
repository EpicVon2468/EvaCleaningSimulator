package io.github.epicvon2468.eva_cleaning_simulator.assets

import com.badlogic.gdx.assets.loaders.I18NBundleLoader
import com.badlogic.gdx.utils.I18NBundle

import ktx.assets.Asset
import ktx.assets.getValue
import ktx.assets.load

import java.util.Locale

data object I18n {

	// libGDX tries to read it as UTF-8, but IntelliJ won't let me reformat it as such no matter what.
	private val _bundle: Asset<I18NBundle> = Resources.load(
		"resources/i18n/i18n",
		I18NBundleLoader.I18NBundleParameter(Locale.getDefault(), "ISO-8859-1")
	)
	val bundle: I18NBundle by _bundle

	fun translate(key: String, default: String = key): String = bundle[key].let { if (it == "???$key???") default else it }
}