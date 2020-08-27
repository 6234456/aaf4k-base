package eu.qiou.aaf4k.schemata

class Variable(id: Int, desc: String, value: Double, source: Source? = null) :
    Value(id, desc = desc, value = value, source = source)