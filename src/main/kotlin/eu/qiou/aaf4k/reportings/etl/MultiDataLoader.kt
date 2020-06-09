package eu.qiou.aaf4k.reportings.etl

import eu.qiou.aaf4k.util.time.TimeParameters

interface MultiDataLoader {
    fun loadMultiple(): Map<TimeParameters, Map<Long, Double>>

    fun whitelist(l: Map<TimeParameters, Map<Long, Double>>) = l.values.fold(setOf<Long>()) { acc, map ->
        acc + map.keys
    }


}