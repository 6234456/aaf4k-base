package eu.qiou.aaf4k.schemata

import eu.qiou.aaf4k.util.io.ExcelUtil

data class Schema(val desc:String, val value:Any, val indentLevel: Int = 0,
                  val format: ExcelUtil.DataFormat = ExcelUtil.DataFormat.DEFAULT, val formula:String?=null)