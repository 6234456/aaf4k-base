package eu.qiou.aaf4k.schemata

interface UnaryOperator {
    fun calculate(value: Value): Value
}