package io.github.epicvon2468.eva_cleaning_simulator.assets

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.files.FileHandle
import com.badlogic.gdx.utils.I18NBundle

import java.util.Locale

object Resources {

	enum class Type(val folder: String) {
		FONT("fonts"),
		TEXTURE("textures"),
		TRANSLATION("i18n")
	}

	fun getResource(path: String, type: Type): FileHandle = Gdx.files.internal("resources/${type.folder}/$path")
	fun getFont(path: String): FileHandle = getResource(path, Type.FONT)
	fun getTexture(path: String): FileHandle = getResource(path, Type.TEXTURE)
	private val i18nFile: FileHandle = getResource("i18n", Type.TRANSLATION) // Not a real file, and yet it is what I18NBundle needs.
	fun getTranslation(locale: Locale): I18NBundle = I18NBundle.createBundle(i18nFile, locale)
}