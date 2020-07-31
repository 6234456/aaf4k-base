package eu.qiou.aaf4k.schemata

data class Variable(val desc: String, var value: Double) : Value {
    override fun value(): Double {
        return value
    }

    operator fun unaryPlus(): Expression {
        return Expression(this)
    }

    operator fun unaryMinus(): Expression {
        return Expression(this.copy(value = this.value * -1.0))
    }
}