package eu.qiou.aaf4k.schemata

class Expression : Value {
    var value: Double = 0.0

    constructor(value: Value) {
        this.value = value.value()
    }

    constructor(operator: Operator, left: Value, right: Value) {
        this.value = operator.calculate(left, right)
    }

    constructor(vararg expression: Expression) {
        this.value = expression.fold(0.0) { acc, x ->
            acc + x.value
        }
    }

    override fun value(): Double {
        return value
    }
}

