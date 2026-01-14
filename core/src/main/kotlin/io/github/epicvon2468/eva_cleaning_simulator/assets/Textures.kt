package io.github.epicvon2468.eva_cleaning_simulator.assets

import com.badlogic.gdx.graphics.Texture

import io.github.epicvon2468.gdx_helpers.disposables.DoNotManuallyDispose

import ktx.assets.Asset
import ktx.assets.getValue
import ktx.assets.load

data object Textures {

	const val TEXTURES_DIR: String = "resources/textures"

	private val _notFoundTexture: Asset<Texture> = preload("not_found.png")
	@DoNotManuallyDispose(DoNotManuallyDispose.Reason.MANAGED_RESOURCE)
	val notFoundTexture: Texture by _notFoundTexture

	init {
		preload("other", "alien.png")
	}

	fun preload(vararg path: String): Asset<Texture> = "$TEXTURES_DIR${path.joinToString(separator = "/", prefix = "/")}".let(Resources::load)

	operator fun get(vararg path: String): Texture = "$TEXTURES_DIR${path.joinToString(separator = "/", prefix = "/")}".let { path: String ->
		if (path !in Resources) notFoundTexture else Resources[path]
	}
}