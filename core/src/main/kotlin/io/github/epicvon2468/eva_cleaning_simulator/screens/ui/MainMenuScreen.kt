package io.github.epicvon2468.eva_cleaning_simulator.screens.ui

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input

import io.github.epicvon2468.eva_cleaning_simulator.Main
import io.github.epicvon2468.eva_cleaning_simulator.assets.I18n

class MainMenuScreen(main: Main) : MenuScreen(main) {

	override fun postInit() {
		Gdx.graphics.setTitle(I18n.translate("screen.main.text.title"))
		//stage.addActor(TextActor())
		table.add(I18n.translate("screen.main.text.hello_world"))
	}

	override fun render(delta: Float) {
		if (Gdx.input.isKeyJustPressed(Input.Keys.TAB)) main.setScreen<LoadingMenuScreen>()
		super.render(delta)
	}
}