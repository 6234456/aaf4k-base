package eu.qiou.aaf4k.schemata

class Constant(val desc: String, val value: Double) : Value {
    override fun value(): Double {
        return value
    }
}