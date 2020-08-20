package eu.qiou.aaf4k.schemata

abstract class Value(val id: Int, var value: Double, val desc: String = "", val source: Source?) {

    operator fun plus(value: Value): Expression {
        return Expression(Add, this, value)
    }

    operator fun minus(value: Value): Expression {
        return Expression(Minus, this, value)
    }

    operator fun times(value: Value): Expression {
        return Expression(Multiply, this, value)
    }

    override fun toString(): String {
        return when (this) {
            is Expression -> "${this.left}${this.right}\n\t\t\t\t${this.value}\n"
            else -> "${this.desc}\t\t\t\t${this.value}\n${this.source ?: ""}"
        }
    }

}