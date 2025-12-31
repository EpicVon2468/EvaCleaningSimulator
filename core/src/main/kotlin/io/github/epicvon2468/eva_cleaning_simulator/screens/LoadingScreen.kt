package io.github.epicvon2468.eva_cleaning_simulator.screens

import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.utils.Logger

import io.github.epicvon2468.eva_cleaning_simulator.Main
import io.github.epicvon2468.eva_cleaning_simulator.assets.Resources
import io.github.epicvon2468.eva_cleaning_simulator.assets.Skins

class LoadingScreen(main: Main) : StageScreen(main) {

	var progress: Float = 0f
		private set

	override fun getTableSkin(): Skin = Skins.preResourceLoad

	@Suppress("GDXKotlinLogLevel")
	override fun postInit() {
		// Can't use '…' since it doesn't render (shows replacement char)
		table.add("Loading...")
		println("Pre-Resources call")
		// Call initialiser + set log level
		Resources.logger.level = Logger.DEBUG
	}

	override fun render(delta: Float) {
		if (Resources.update()) //main.setScreen<MainMenuScreen>()
		progress = Resources.progress * 100
		(table.getChild(0) as Label).setText("$progress%")
		super.render(delta)
	}
}