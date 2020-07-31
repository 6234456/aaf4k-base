package eu.qiou.aaf4k.schemata

interface Value {
    fun value(): Double

    operator fun plus(value: Value): Expression {
        return Expression(Add, this, value)
    }

    operator fun times(value: Value): Expression {
        return Expression(Multiply, this, value)
    }
}