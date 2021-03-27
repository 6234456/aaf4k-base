package eu.qiou.aaf4k.finance

import eu.qiou.aaf4k.util.sortedWithOrderList

/**
 * the matrix of the contribution(Leistungsaustausch) between the auxiliary cost centers (Hilfskostenstellen) and from
 * auxiliary cost centers to the primary cost centers
 *
 * each row of the matrix contains the contributions to all the cost centers (incl. itself)
 *
 */
data class Leistungsmatrix(
    private val matrix: Array<Array<Double>>,
    val costCenters: Array<Kostenstelle>,
    val indirectCosts: Array<Double>
) {
    init {
        // "The matrix is not square!"
        val size = matrix.size
        assert(matrix.isEmpty() || matrix.all { it.size == size })
        assert(costCenters.size == size)
        assert(size == indirectCosts.size)
        assert(size == costCenters.map { it.id }.toSet().size)
    }

    private val n = matrix.size

    private val mapPos2CostCenter = costCenters.mapIndexed { index, kostenstelle -> index to kostenstelle }.toMap()
    private val mapCostCenter2Pos = costCenters.mapIndexed { index, kostenstelle -> kostenstelle to index }.toMap()

    fun stufenleiterVerfahren(): Array<Pair<Kostenstelle, Array<Double>>> {
        // recording the cascading of the indirectCosts
        var costTemp = indirectCosts
        var orderList: List<Int> = listOf()
        var matrixTemp = matrix

        // sort based on innerbetrieblicher Leistungsverrechnung, desc
        return (costCenters.zip(matrix).filter { it.first.isAuxiliary }.map {
            it.first to
                    it.second.reduceIndexed { index, acc, d ->
                        acc + if ((mapPos2CostCenter[index] ?: error("")).isAuxiliary) d else 0.0
                    } / it.second.reduce { acc, d -> acc + d } * indirectCosts[mapCostCenter2Pos[it.first]!!]
        }.sortedByDescending { it.second }.map { it.first } + costCenters.filter { !it.isAuxiliary }).apply {
            orderList =
                this.mapIndexed { index, kostenstelle -> (mapCostCenter2Pos[kostenstelle] ?: error("")) to index }
                    .sortedBy { it.first }.map { it.second }
            costTemp = costTemp.toList().sortedWithOrderList(orderList).toTypedArray()
            matrixTemp = matrix.toList().sortedWithOrderList(orderList).map {
                it.toList().sortedWithOrderList(orderList).toTypedArray()
            }.toTypedArray()
        }.mapIndexed {

            // idx  the index in the sorted array with hilfkostenstelle at the beginning
                idx, x ->
            val index = idx
            val contribution = matrixTemp[index]
            val cost = costTemp[idx]

            // excluding the contribution to itself
            val total =
                if (x.isAuxiliary) cost / contribution.mapIndexed { i, d -> if (i > idx) d else 0.0 }.reduce { acc, d -> acc + d } else cost
            val distributed = if (x.isAuxiliary) contribution.mapIndexed { i, d -> if (i > idx) d * total else 0.0 }
            else contribution.mapIndexed { i, d -> if (i == idx) cost else 0.0 }

            // cascading to the next level
            costTemp = costTemp.zip(distributed)
                .foldIndexed(listOf<Double>()) { i, acc, pair -> acc + listOf(if (i > idx) pair.first + pair.second else 0.0) }
                .toTypedArray()


            x to distributed.toTypedArray()
        }.toTypedArray()
    }

    fun gleichungsVerfahren(): Array<Pair<Kostenstelle, Array<Double>>> {
        // two type of situation
        // 1. the Haupt-Kostenstellen has explicit output Amount to certain Kostentraeger
        // 2. or by default with the output amount to be 1 and matrix reflects only innerbetriebliche Verrechnung

        val isDefaultSituation = matrix.any {
            it.reduce { acc, d -> acc + d } == 0.0
        }


        val contributions = matrix.mapIndexed { index, v ->
            val kostenstelle = mapPos2CostCenter[index]!!
            (1..n).map {
                val value = v.reduce { acc, d -> acc + d }
                if (it - 1 == index) {
                    if (isDefaultSituation) {
                        -1.0 * if (kostenstelle.isAuxiliary)
                            value
                        else
                            1.0
                    } else {
                        -1.0 * value
                    }
                } else
                    0.0
            }.toDoubleArray()
        }.toTypedArray()

        return Matrix(matrix.mapIndexed { index, doubles ->
            val kostenstelle = mapPos2CostCenter[index]!!
            doubles.apply {
                // if situation 1 , set the contribution of Hauptkostenstellen to itself to 0
                if (!isDefaultSituation && !kostenstelle.isAuxiliary) {
                    this[index] = 0.0
                }
            }.toDoubleArray()
        }.toTypedArray()).transpose().plus(
            Matrix(contributions)
        ).solve(
            Matrix(
                indirectCosts.map {
                    doubleArrayOf(it * -1.0)
                }.toTypedArray()
            )
        ).data.mapIndexed { index, doubles ->
            mapPos2CostCenter[index]!! to doubles.toTypedArray()
        }.toTypedArray()
    }
}