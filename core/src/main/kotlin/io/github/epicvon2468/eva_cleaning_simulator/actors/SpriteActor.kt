package io.github.epicvon2468.eva_cleaning_simulator.actors

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.scenes.scene2d.Actor

import io.github.epicvon2468.eva_cleaning_simulator.assets.Textures

import org.jetbrains.annotations.ApiStatus

interface SpriteRenderable {

	@get:ApiStatus.Internal
	val sprite: Sprite

	fun draw(batch: Batch, parentAlpha: Float) = this.sprite.draw(batch, parentAlpha)
}

open class SpriteActor(
	@ApiStatus.Internal
	override val sprite: Sprite = Sprite(Textures.notFoundTexture)
) : Actor(), SpriteRenderable {

	constructor(texture: Texture) : this(Sprite(texture))

	init {
		this.setBounds(this.x, this.y, this.sprite.width, this.sprite.height)
		this.setScale(this.sprite.scaleX, this.sprite.scaleY)
		this.rotation = this.sprite.rotation
	}

	override fun positionChanged() {
		val x: Float = this.x
		val y: Float = this.y
		if (this.sprite.x != x || this.sprite.y != y) this.sprite.setPosition(x, y)
	}
	override fun sizeChanged() {
		val width: Float = this.width
		val height: Float = this.height
		if (this.sprite.width != width || this.sprite.height != height) this.sprite.setSize(width, height)
	}
	override fun scaleChanged() {
		val scaleX: Float = this.scaleX
		val scaleY: Float = this.scaleY
		if (this.sprite.scaleX != scaleX || this.sprite.scaleY != scaleY) this.sprite.setScale(scaleX, scaleY)
	}
	override fun rotationChanged() {
		val rotation: Float = this.rotation
		if (this.sprite.rotation != rotation) this.sprite.rotation = rotation
	}

	override fun draw(batch: Batch, parentAlpha: Float) = super<SpriteRenderable>.draw(batch, parentAlpha)
}