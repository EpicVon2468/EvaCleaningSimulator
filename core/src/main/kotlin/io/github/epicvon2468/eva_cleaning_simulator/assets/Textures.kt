package io.github.epicvon2468.eva_cleaning_simulator.assets

import com.badlogic.gdx.graphics.Texture

import io.github.epicvon2468.eva_cleaning_simulator.utils.DoNotManuallyDispose

import ktx.assets.Asset
import ktx.assets.getValue
import ktx.assets.load

data object Textures {

	private val _notFoundTexture: Asset<Texture> = Resources.load("resources/textures/not_found.png")
	@DoNotManuallyDispose(DoNotManuallyDispose.Reason.MANAGED_RESOURCE)
	val notFoundTexture: Texture by _notFoundTexture

	operator fun get(path: String): Texture = if (path !in Resources) notFoundTexture else Resources[path]
}