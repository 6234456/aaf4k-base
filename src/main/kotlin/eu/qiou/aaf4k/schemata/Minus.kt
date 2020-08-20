package eu.qiou.aaf4k.schemata

object Minus : Operator {
    override fun calculate(left: Double, right: Double): Double {
        return left - right
    }
}