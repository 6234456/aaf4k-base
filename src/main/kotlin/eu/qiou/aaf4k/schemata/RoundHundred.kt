package eu.qiou.aaf4k.schemata

object RoundHundred : UnaryOperator {
    override fun calculate(value: Value): Value {
        return Variable(
            value.id,
            "rounded to hundred: " + value.desc,
            (value.value / 100.0).toInt() * 100.0,
            value.source
        )
    }
}