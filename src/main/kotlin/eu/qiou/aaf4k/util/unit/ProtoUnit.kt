package eu.qiou.aaf4k.util.unit

import eu.qiou.aaf4k.reportings.GlobalConfiguration
import eu.qiou.aaf4k.util.io.JSONable
import java.util.*


abstract class ProtoUnit(
    open val scalar: UnitScalar = UnitScalar.UNIT,
    var locale: Locale = GlobalConfiguration.DEFAULT_LOCALE
) : JSONable {

    companion object {
        private var codePrepared = false
        private lateinit var reportingCode: Map<String, UnitScalar>

        val parseUnitType: (String) -> UnitScalar = { x ->
            if (!codePrepared) {
                reportingCode = UnitScalar.values().map {
                    it.token to it
                }.toMap()

                codePrepared = true
            }
            reportingCode[x] ?: error("Undefined reporting code $x")
        }
    }


    abstract fun format(locale: Locale = this.locale): (Number) -> String

    open fun convertTo(unit: ProtoUnit): (Double) -> Double {
        return { n -> n * (scalar.scalar / unit.scalar.scalar) }
    }
}