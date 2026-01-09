package io.github.epicvon2468.eva_cleaning_simulator.actors

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.Stage

import io.github.epicvon2468.eva_cleaning_simulator.assets.Textures
import io.github.epicvon2468.eva_cleaning_simulator.utils.centrePosition

interface SpriteRenderable {

	val sprite: Sprite

	fun draw(batch: Batch, parentAlpha: Float) = this.sprite.draw(batch, parentAlpha)
}

open class SpriteActor(
	override val sprite: Sprite = Sprite(Textures.notFoundTexture),
	x: Float = 0f,
	y: Float = 0f
) : Actor(), SpriteRenderable {

	override fun setStage(stage: Stage?) {
		super.setStage(stage)
		if (stage != null) this.centrePosition()
	}

	init {
		//this.setPosition((x - sprite.width), (y + sprite.height))
		this.setBounds(x, y, sprite.width, sprite.height)
	}

	override fun positionChanged() = this.sprite.setPosition(this.x, this.y)

	constructor(texture: Texture, x: Float = 0f, y: Float = 0f) : this(Sprite(texture), x, y)

	override fun draw(batch: Batch, parentAlpha: Float) = super<SpriteRenderable>.draw(batch, parentAlpha)
}