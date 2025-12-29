package io.github.epicvon2468.eva_cleaning_simulator.utils

import com.badlogic.gdx.utils.Disposable

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