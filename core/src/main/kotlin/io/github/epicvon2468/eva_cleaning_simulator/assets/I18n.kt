package io.github.epicvon2468.eva_cleaning_simulator.assets

import com.badlogic.gdx.utils.I18NBundle

data object I18n {

	@JvmField
	val bundle: I18NBundle = Resources.getTranslation()

	fun translate(key: String, default: String = key): String = bundle[key].let { if (it == "???$key???") default else it }
}