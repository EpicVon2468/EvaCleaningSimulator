package io.github.epicvon2468.eva_cleaning_simulator.utils

import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.utils.Disposable

import ktx.actors.centerPosition

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

@OptIn(ExperimentalContracts::class)
inline fun <T : Disposable?, R> T.use(block: (T) -> R): R {
	contract {
		callsInPlace(block, InvocationKind.EXACTLY_ONCE)
	}
	try {
		return block(this)
	} finally {
		this?.dispose()
	}
}

fun Actor.centrePosition(
	width: Float = this.stage.width,
	height: Float = this.stage.height,
	normalise: Boolean = false
) = this.centerPosition(width, height, normalise)