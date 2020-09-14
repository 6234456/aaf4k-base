package eu.qiou.aaf4k.schemata

import eu.qiou.aaf4k.util.io.ExcelUtil
import eu.qiou.aaf4k.util.template.Template


class Expression(val operator: Operator, val left: Value, val right: Value, indentLevel: Int = 0) :
    Value(-1, operator.calculate(left, right), "", null, ExcelUtil.DataFormat.NUMBER, indentLevel) {

    fun toXl(path:String){
        ExcelUtil.createWorksheetIfNotExists(path, callback = {
            this.toSchema().forEachIndexed { index, pair ->
                it.createRow(index).let {
                    x ->
                        x.createCell(0).setCellValue(pair.desc)
                        x.createCell(2 + pair.indentLevel).apply {
                            if (pair.formula != null)
                                ExcelUtil.Update(this).dataFormat(pair.format.format).formula(
                                    Template.R1C1(this, pair.formula)
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
            is Expression -> "R[-${1 + right.width()}]C"
            else -> "R[-2]C"
        } + "[${this.left.indentLevel - this.indentLevel}]${operator.sign()}R[-1]C[${this.right.indentLevel - this.indentLevel}]"
    }
}
