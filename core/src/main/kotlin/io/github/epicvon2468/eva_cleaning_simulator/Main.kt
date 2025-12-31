package io.github.epicvon2468.eva_cleaning_simulator

import com.badlogic.gdx.Application
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.utils.I18NBundle
import com.badlogic.gdx.utils.Logger

import io.github.epicvon2468.eva_cleaning_simulator.assets.I18n
import io.github.epicvon2468.eva_cleaning_simulator.assets.Resources
import io.github.epicvon2468.eva_cleaning_simulator.screens.MainMenuScreen

import ktx.app.KtxGame
import ktx.app.KtxScreen
import ktx.async.KtxAsync

class Main : KtxGame<KtxScreen>() {

	@Suppress("GDXKotlinLogLevel")
	override fun create() {
		Gdx.app.logLevel = Application.LOG_DEBUG
		Resources.logger.level = Logger.DEBUG
		I18NBundle.setExceptionOnMissingKey(false)
		KtxAsync.initiate()
		Gdx.graphics.setTitle(I18n.translate("screen.main.text.title"))

		addScreen(MainMenuScreen())
		setScreen<MainMenuScreen>()
	}
}