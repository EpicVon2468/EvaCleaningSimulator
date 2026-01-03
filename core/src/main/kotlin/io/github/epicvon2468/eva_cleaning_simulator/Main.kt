package io.github.epicvon2468.eva_cleaning_simulator

import com.badlogic.gdx.Application
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.utils.I18NBundle

import io.github.epicvon2468.eva_cleaning_simulator.assets.Fonts
import io.github.epicvon2468.eva_cleaning_simulator.assets.Resources
import io.github.epicvon2468.eva_cleaning_simulator.screens.ui.LoadingMenuScreen
import io.github.epicvon2468.eva_cleaning_simulator.screens.ui.MainMenuScreen

import ktx.app.KtxGame
import ktx.app.KtxScreen
import ktx.async.KtxAsync

// multiplayer at some point?
class Main : KtxGame<KtxScreen>() {

	@Suppress("GDXKotlinLogLevel")
	override fun create() {
		Gdx.app.logLevel = Application.LOG_DEBUG
		I18NBundle.setExceptionOnMissingKey(false)
		KtxAsync.initiate()

		addScreens()
		setScreen<LoadingMenuScreen>()
	}

	private fun addScreens() {
		addScreen(MainMenuScreen(this))
		addScreen(LoadingMenuScreen(this))
	}

	override fun dispose() {
		super.dispose()
		Fonts.dispose()
		Resources.dispose()
	}
}