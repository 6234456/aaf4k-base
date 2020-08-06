package eu.qiou.aaf4k.schemata

class Variable(id: Int, desc: String, value: Double, source: Source? = null) :
    Value(id, desc = desc, value = value, source = source) {

    operator fun unaryPlus(): Expression {
        return Expression(this)
    }

    operator fun unaryMinus(): Expression {
        return Expression(Variable(id, desc, value * -1, source))
    }
}