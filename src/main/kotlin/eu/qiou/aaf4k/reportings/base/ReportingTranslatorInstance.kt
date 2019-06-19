package eu.qiou.aaf4k.reportings.base

import java.io.InputStream

class ReportingTranslatorInstance(private val inputStream: InputStream) : ReportingTranslator {
    private val acc = accumulator()

    override fun translate(src: Map<Long, Double>): Map<Long, Double> {
        return acc(src)
    }

    private fun accumulator(): (Map<Long, Double>) -> Map<Long, Double> {
        return { x: Map<Long, Double> ->
            AccountingFrame.inflate(0, "", inputStream).structure.map {
                it.id to when(it){
                    is CollectionAccount -> it.subAccounts.fold(0.0){ y, e -> y + x.getOrDefault(e.id, 0.0) }
                    else ->  x.getOrDefault(it.id, 0.0)
                }
            }.toMap().filter { it.value != 0.0 }
        }
    }
}