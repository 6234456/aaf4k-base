package eu.qiou.aaf4k.checklist

import eu.qiou.aaf4k.reportings.base.Identifiable

data class Item(override val id: Long, val desc: String, var state: State = State.PENDING) : Identifiable {
    override fun toString(): String {
        return "${if (+this) "* " else " "}${this.desc}"
    }
}

operator fun Item.not(): Boolean = this.state != State.FULFILLED
operator fun Item.unaryPlus(): Boolean = this.state == State.FULFILLED