package eu.qiou.aaf4k.schemata

object Add : Operator {
    override fun calculate(left: Double, right: Double): Double {
        return left + right
    }

    override fun sign(): String = "+"
}