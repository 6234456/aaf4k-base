package eu.qiou.aaf4k.schemata

import eu.qiou.aaf4k.util.io.ExcelUtil


class Expression(val operator: Operator, val left: Value, val right: Value) :
    Value(-1, operator.calculate(left, right), "", null){

    fun toXl(path:String){
        ExcelUtil.createWorksheetIfNotExists(path, callback = {
            this.toSchema().forEachIndexed { index, pair ->
                it.createRow(index).let {
                    x ->
                        x.createCell(0).setCellValue(pair.desc)
                        x.createCell(2 + pair.indentLevel).setCellValue(pair.value)
                }
            }
        })
    }

    fun toVariable(id: Int, desc: String = this.desc, source: Source? = this.source):Variable{
        return Variable(id, desc, this.value, source)
    }
}
