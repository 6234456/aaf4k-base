package eu.qiou.aaf4k.reportings.plan

import kotlin.math.abs

class TrailAndError(
    private var trail: Double = 0.0, private val function: (Double) -> Double,
    private val tolerance: Double = 0.0001,
    private val maxLoop: Int = 50000, private val step: Double = 0.00001
) {

    // get input with the target value = 0 of monotonous function
    fun target(): Double {
        var cnt = 0
        var last = function(trail)
        var lastValue = trail
        var current = 0.0
        var lastStep = step
        var temp = 0.0

        while (true) {
            current = function(trail)

            if (abs(current) <= tolerance)
                return trail

            if (cnt++ == maxLoop)
                throw Exception("Maximal loop reached.")

            if (cnt == 1) {
                trail += step
            } else {
                if ((last > 0 && current < 0) || (last < 0 && current > 0)) {
                    temp = (lastValue + trail) / 2
                    lastValue = if (abs(last) > abs(current)) trail else lastValue
                    last = if (abs(last) > abs(current)) current else last
                    trail = temp
                } else if ((last > current && current > 0) || (last < current && current < 0)) {
                    lastStep *= 2
                    lastValue = trail
                    trail += lastStep
                    last = current
                } else {
                    lastStep = step
                    lastValue = trail
                    trail -= lastStep
                    last = current
                }
            }

        }
    }
}