package eu.qiou.aaf4k.schemata

import eu.qiou.aaf4k.util.io.ExcelUtil

class Variable(id: Int, desc: String, value: Double, source: Source? = null, format: ExcelUtil.DataFormat = ExcelUtil.DataFormat.NUMBER) :
    Value(id, desc = desc, value = value, source = source, format = format)