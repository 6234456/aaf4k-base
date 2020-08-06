package eu.qiou.aaf4k.schemata

object RoundDownwards : UnaryOperator {
    override fun calculate(value: Value): Variable {
        return Variable(value.id, "round downwards: ${value.desc}", value.value.toInt() * 1.0, value.source)
    }
}