package eu.qiou.aaf4k.schemata

object Add : Operator {
    override fun calculate(left: Value, right: Value): Double {
        return left.value + right.value
    }

    override fun equals(other: Any?): Boolean {
        return this === other
    }

    override fun hashCode(): Int {
        return System.identityHashCode(this)
    }
}