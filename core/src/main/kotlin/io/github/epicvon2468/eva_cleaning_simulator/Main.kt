package io.github.epicvon2468.eva_cleaning_simulator

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.utils.I18NBundle

import io.github.epicvon2468.eva_cleaning_simulator.assets.I18n
import io.github.epicvon2468.eva_cleaning_simulator.screens.MainMenuScreen

import ktx.app.KtxGame
import ktx.app.KtxScreen
import ktx.async.KtxAsync

class Main : KtxGame<KtxScreen>() {

	override fun create() {
		I18NBundle.setExceptionOnMissingKey(false)
		KtxAsync.initiate()
		Gdx.graphics.setTitle(I18n.translate("screen.main.text.title"))

		addScreen(MainMenuScreen())
		setScreen<MainMenuScreen>()
	}
}