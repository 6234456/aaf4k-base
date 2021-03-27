package eu.qiou.aaf4k.finance

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
    val costs: Array<Double>
) {
    init {
        // "The matrix is not square!"
        assert(matrix.isEmpty() || matrix.size == matrix.first().size)
        assert(costCenters.size == matrix.size)
        assert(costCenters.size == costs.size)
        assert(costCenters.size == costCenters.map { it.id }.toSet().size)

    }

    private val mapPos2CostCenter = costCenters.mapIndexed { index, kostenstelle -> index to kostenstelle }.toMap()
    private val mapCostCenter2Pos = costCenters.mapIndexed { index, kostenstelle -> kostenstelle to index }.toMap()

    fun stufenleiterVerfahren(): Array<Pair<Kostenstelle, Array<Double>>> {
        var costTemp = costs
        // sort based on innerbetrieblicher Leistungsverrechnung, desc
        return (costCenters.zip(matrix).filter { it.first.isAuxiliary }.map {
            it.first to
                    it.second.reduceIndexed { index, acc, d -> acc + if (mapPos2CostCenter[index]!!.isAuxiliary) d else 0.0 } / it.second.reduce { acc, d -> acc + d } * costs[mapCostCenter2Pos[it.first]!!]
        }.sortedByDescending { it.second }.map { it.first } + costCenters.filter { !it.isAuxiliary }).map {
            val index = mapCostCenter2Pos[it]!!
            val contribution = matrix[index]
            val cost = costTemp[index]

            // excluding the contribution to itself
            val total =
                if (it.isAuxiliary) cost / contribution.mapIndexed { i, d -> if (i >= index) d else 0.0 }.reduce { acc, d -> acc + d } else cost
            val distributed = if (it.isAuxiliary) contribution.mapIndexed { i, d -> if (i >= index) d * total else 0.0 }
            else contribution.mapIndexed { i, d -> if (i == index) cost else 0.0 }

            // cascading to the next level
            costTemp = costTemp.zip(distributed)
                .foldIndexed(listOf<Double>()) { i, acc, pair -> acc + listOf(if (i > index) pair.first + pair.second else 0.0) }
                .toTypedArray()


            it to distributed.toTypedArray()
        }.toTypedArray()
    }
}