package eu.qiou.aaf4k.schemata

import eu.qiou.aaf4k.util.io.ExcelUtil
import eu.qiou.aaf4k.util.template.Template


class Expression(val operator: Operator, val left: Value, val right: Value) :
    Value(-1, operator.calculate(left, right), "", null, ExcelUtil.DataFormat.NUMBER){

    fun toXl(path:String){
        ExcelUtil.createWorksheetIfNotExists(path, callback = {
            this.toSchema().forEachIndexed { index, pair ->
                it.createRow(index).let {
                    x ->
                        x.createCell(0).setCellValue(pair.desc)
                        x.createCell(2 + pair.indentLevel).apply {
                            if (pair.formula != null)
                                ExcelUtil.Update(this).dataFormat(pair.format.format).formula(
                                    Template.parseFormula(this, pair.formula,
                                        "V", offsetColumn = pair.indentLevel * -1 )
                                )
                            else
                                ExcelUtil.Update(this).dataFormat(pair.format.format).value(pair.value)
                        }
                }
            }
        })
    }

    fun formula():String{
        return when(right){
            is Expression -> "[-${1+right.width()}]"
            else -> "[-2]"
        } + "${operator.sign()}[-1]"
    }
}
