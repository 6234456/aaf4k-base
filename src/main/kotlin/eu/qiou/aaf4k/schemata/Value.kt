package eu.qiou.aaf4k.schemata

abstract class Value(val id: Int, var value: Double, val desc: String, val source: Source?) {

    operator fun plus(value: Value): Expression {
        return Expression(Add, this, value)
    }

    operator fun times(value: Value): Expression {
        return Expression(Multiply, this, value)
    }

    fun then(operator: (Expression) -> Expression): Expression {
        return operator(Expression(this))
    }

    fun operate(operator: UnaryOperator): Expression {
        return Expression(operator.calculate(this))
    }
}