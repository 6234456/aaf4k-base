package eu.qiou.aaf4k.schemata

interface Operator {
    fun calculate(left: Value, right: Value): Double
}