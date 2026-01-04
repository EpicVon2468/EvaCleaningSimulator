package io.github.epicvon2468.eva_cleaning_simulator.assets

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.utils.Disposable

import io.github.epicvon2468.eva_cleaning_simulator.utils.DoNotManuallyDispose

import ktx.assets.Asset
import ktx.assets.getValue
import ktx.freetype.loadFreeTypeFont

data object Fonts : Disposable {

	private val _jetBrainsMono: Asset<BitmapFont> = Resources.loadFreeTypeFont("resources/fonts/JetBrainsMono-Light.ttf") {
		mono = true
		borderStraight = true
		kerning = false
		size = 32
		minFilter = Texture.TextureFilter.Linear
		magFilter = Texture.TextureFilter.Linear
	}
	@DoNotManuallyDispose(DoNotManuallyDispose.Reason.MANAGED_RESOURCE)
	val jetBrainsMono: BitmapFont by _jetBrainsMono

	@JvmField
	@Suppress("GDXKotlinStaticResource")
	val preResourceLoad: BitmapFont = BitmapFont().apply {
		regions.forEach { textureRegion: TextureRegion ->
			textureRegion.texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear)
		}
		data.scale(2.0f)
	}

	override fun dispose() {
		// don't dispose JetBrainsMono-Light since it's owned by Resources
		preResourceLoad.dispose()
	}
}