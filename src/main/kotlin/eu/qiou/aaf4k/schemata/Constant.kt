package eu.qiou.aaf4k.schemata

import eu.qiou.aaf4k.util.io.ExcelUtil

class Constant(id: Int, desc: String, value: Double, source: Source? = null, format: ExcelUtil.DataFormat = ExcelUtil.DataFormat.INT) :
    Value(id, desc = desc, value = value, source = source, format = format)