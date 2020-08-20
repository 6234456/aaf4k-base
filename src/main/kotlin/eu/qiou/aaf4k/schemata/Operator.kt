package eu.qiou.aaf4k.schemata

import kotlin.math.round

interface Operator {
    fun calculate(left: Double, right: Double): Double
    fun calculate(left: Value, right: Value): Double = round(calculate(left.value, right.value) * 100) / 100.0
}