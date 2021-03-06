package eu.qiou.aaf4k.schemata

import eu.qiou.aaf4k.util.io.ExcelUtil

abstract class Value(
    val id: Int, var value: Double, val desc: String = "", val source: Source?,
    val format: ExcelUtil.DataFormat = ExcelUtil.DataFormat.NUMBER, var indentLevel: Int = 0
) {

    operator fun plus(value: Value): Expression {
        return Expression(Add, this, value, this.indentLevel)
    }

    operator fun minus(value: Value): Expression {
        return Expression(Minus, this, value, this.indentLevel)
    }

    operator fun times(value: Value): Expression {
        return Expression(Multiply, this, value, this.indentLevel)
    }

    fun byId(id: Int):Value? {
        return when(this){
            is Expression -> this.left.byId(id) ?: (this.right.byId(id))
            else -> if (this.id == id) this else null
        }
    }

    override fun toString(): String {
        return when (this) {
            is Expression -> String.format("%s%n%60s", "${this.left}\n${this.right}", this.format.stringFormat(this.value)) //"${this.left}\n${this.right}\n\t\t\t\t${ this.format.stringFormat(this.value) }\n"
            else ->  String.format("%-50s%s%n%s", this.desc, this.format.stringFormat(this.value), this.source ?: "") //"${this.desc}\t\t\t\t${this.format.stringFormat(this.value)}\n${this.source ?: ""}"
        }
    }
    protected fun toSchema(): List<Schema> {
        return when (this) {
            is Expression -> this.left.toSchema() + this.right.toSchema()
            else -> listOf()
        } + listOf(Schema("${this.desc}${if (this.source == null) "" else "\n${this.source}"}", value = this.value,
            indentLevel = indentLevel, format = this.format,
            formula = if (this is Expression) this.formula() else null
            ))
    }

    fun width():Int{
        return when(this){
            is Expression ->  this.right.width() + this.left.width() + 1
            else -> 1
        }
    }

    fun update(dict: Map<Int, Double>): Value {
        return if (this is Expression) {
            Expression(operator, left.update(dict), right.update(dict), this.indentLevel)
        } else {
            if (dict.containsKey(id)) {
                when (this) {
                    is Variable -> Variable(id, desc, dict[id] ?: error(""), source, format, indentLevel)
                    is Constant -> Constant(id, desc, dict[id] ?: error(""), source, format, indentLevel)
                    is Expression -> throw Exception("")
                    else -> throw Exception("Error")
                }
            } else {
                this
            }
        }
    }
}