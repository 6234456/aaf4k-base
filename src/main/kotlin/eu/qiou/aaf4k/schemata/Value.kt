package eu.qiou.aaf4k.schemata

abstract class Value(val id: Int, var value: Double, val desc: String = "", val source: Source?) {
    // set the indent level by display
    var indentLevel: Int = 0

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
    protected fun toSchema(): List<Schema> {
        return when (this) {
            is Expression -> this.left.toSchema() + this.right.toSchema()
            else -> listOf()
        } + listOf(Schema("${this.desc}${if (this.source == null) "" else "\n${this.source}"}",
            "${this.value}", indentLevel))
    }

    fun update(dict: Map<Int, Double>): Value {
        return if (this is Expression) {
            Expression(operator, left.update(dict), right.update(dict))
        } else {
            if (dict.containsKey(id)) {
                when (this) {
                    is Variable -> Variable(id, desc, dict[id] ?: error(""), source)
                    is Constant -> Constant(id, desc, dict[id] ?: error(""), source)
                    is Expression -> throw Exception("")
                    else -> throw Exception("Error")
                }
            } else {
                this
            }
        }
    }

}