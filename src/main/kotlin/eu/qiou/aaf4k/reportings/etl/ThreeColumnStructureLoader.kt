package eu.qiou.aaf4k.reportings.etl

import eu.qiou.aaf4k.reportings.base.Account
import eu.qiou.aaf4k.reportings.base.CollectionAccount
import eu.qiou.aaf4k.reportings.base.ProtoAccount
import eu.qiou.aaf4k.util.io.ExcelUtil
import org.apache.poi.ss.usermodel.Row

class ThreeColumnStructureLoader(
    val path: String,
    val sheetIndex: Int = 0,
    val sheetName: String? = null,
    val keyCol: Int = 1,
    val secondaryKeyCol: Int = 2,
    val sep: String = " "
) : StructureLoader {
    override fun load(): List<ProtoAccount> {
        val res: MutableList<ProtoAccount> = mutableListOf()
        var tmpAggregateAccount: CollectionAccount? = null

        val f: (Row) -> Unit = {

            it.getCell(keyCol - 1)?.stringCellValue?.trim()?.let {
                if (!it.isEmpty()) {
                    val t1 = it.split(sep)

                    tmpAggregateAccount?.let { res.add(it) }
                    tmpAggregateAccount =
                        CollectionAccount(id = t1.first().toLong(), name = t1.drop(1).joinToString(" "))
                }
            }

            it.getCell(secondaryKeyCol - 1)?.numericCellValue?.let { x ->
                if (x != 0.0) {
                    tmpAggregateAccount?.add(
                        Account(
                            id = x.toLong(),
                            name = it.getCell(secondaryKeyCol)?.stringCellValue?.trim() ?: "",
                            value = 0L
                        )
                    )
                }
            }
        }

        ExcelUtil.loopThroughRows(path, sheetIndex, sheetName, f)

        res.add(tmpAggregateAccount!!)

        return res
    }
}