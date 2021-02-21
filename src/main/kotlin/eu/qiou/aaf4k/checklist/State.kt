package eu.qiou.aaf4k.checklist

import java.lang.Exception

enum class State(val code: Int) {
    PENDING(0),
    NOT_RELEVANT(500),
    FAILED(400),
    PROCESSING(300),
    FULFILLED(200);

    companion object {
        fun of(code: Int): State {
            return State.values().firstOrNull { it.code == code } ?: throw Exception("Unknown Code: $code")
        }
    }
}

operator fun State.unaryPlus(): Boolean = this == State.FULFILLED
operator fun State.not(): Boolean = !(+this)