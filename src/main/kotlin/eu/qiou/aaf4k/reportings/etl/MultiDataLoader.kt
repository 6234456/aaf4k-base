package eu.qiou.aaf4k.reportings.etl

import eu.qiou.aaf4k.util.time.TimeParameters

interface MultiDataLoader {
    fun loadMultiple(): Map<TimeParameters, Map<Long, Double>>
}