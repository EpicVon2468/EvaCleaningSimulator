@file:Suppress("InconsistentCommentForJavaParameter")
package io.github.epicvon2468.eva_cleaning_simulator.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.ScreenViewport

import ktx.app.KtxScreen

open class StageScreen(@JvmField var needsInit: Boolean = true) : KtxScreen {

	val stage: Stage by lazy { Stage(ScreenViewport()) }

	override fun show() {
		if (needsInit) {
			preInit()
			stage
			Gdx.input.inputProcessor = stage
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