package io.github.epicvon2468.eva_cleaning_simulator.assets

import com.badlogic.gdx.utils.I18NBundle

import java.util.Locale

object I18n {

	var language: Locale = Locale.getDefault()
		set(value) {
			field = value
			bundle = Resources.getTranslation(value)
		}
	// TODO: Cache
	var bundle: I18NBundle = Resources.getTranslation(language)
		private set

	fun translate(key: String, default: String = key): String = bundle[key].let { if (it == "???$key???") default else it }
}