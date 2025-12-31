package io.github.epicvon2468.eva_cleaning_simulator.assets

import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.scenes.scene2d.ui.Skin

import ktx.style.label
import ktx.style.skin

data object Skins {

	val primary: Skin by lazy {
		skin {
			add("default", Fonts.jetBrainsMono, BitmapFont::class.java)
			label {
				font = Fonts.jetBrainsMono
			}
		}
	}

	@JvmField
	@Suppress("GDXKotlinStaticResource") // Nothing to dispose
	val preResourceLoad: Skin = skin {
		add("default", Fonts.preResourceLoad, BitmapFont::class.java)
		label {
			font = Fonts.preResourceLoad
		}
	}
}