@file:Suppress("InconsistentCommentForJavaParameter")
package io.github.epicvon2468.eva_cleaning_simulator.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.utils.viewport.Viewport

import io.github.epicvon2468.eva_cleaning_simulator.Main
import io.github.epicvon2468.eva_cleaning_simulator.assets.Skins

import ktx.app.KtxScreen

// https://libgdx.com/wiki/graphics/2d/scene2d/scene2d-ui
// https://libgdx.com/wiki/graphics/2d/scene2d/table
abstract class StageScreen(@JvmField val main: Main, @JvmField var needsInit: Boolean = true) : KtxScreen {

	open val stage: Stage by lazy {
		Stage(stageViewportDefault()).apply {
			Gdx.input.inputProcessor = this
		}
	}
	open val table: Table by lazy {
		Table(tableSkinDefault()).apply {
			setFillParent(true)
			this@StageScreen.stage.addActor(this)
			clip = true
			debug = true
		}
	}

	abstract fun stageViewportDefault(): Viewport
	open fun tableSkinDefault(): Skin = Skins.primary

	override fun show() {
		if (needsInit) {
			preInit()
			stage
			table
			postInit()
			needsInit = false
		}
	}

	open fun preInit() = Unit

	open fun postInit() = Unit

	override fun resize(width: Int, height: Int) = stage.viewport.update(width, height, /*centreCamera =*/ true)

	override fun render(delta: Float) {
		stage.act(delta)
		stage.draw()
	}

	override fun dispose() {
		stage.dispose()
	}
}