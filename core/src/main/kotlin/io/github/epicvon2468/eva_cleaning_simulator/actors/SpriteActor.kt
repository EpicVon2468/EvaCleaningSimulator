package io.github.epicvon2468.eva_cleaning_simulator.actors

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.scenes.scene2d.Actor

import io.github.epicvon2468.eva_cleaning_simulator.assets.Textures

interface SpriteRenderable {

	val sprite: Sprite

	fun draw(batch: Batch, parentAlpha: Float) = this.sprite.draw(batch, parentAlpha)
}

open class SpriteActor(
	override val sprite: Sprite = Sprite(Textures.notFoundTexture)
) : Actor(), SpriteRenderable {

	constructor(texture: Texture) : this(Sprite(texture))

	override fun draw(batch: Batch, parentAlpha: Float) = super<SpriteRenderable>.draw(batch, parentAlpha)
}