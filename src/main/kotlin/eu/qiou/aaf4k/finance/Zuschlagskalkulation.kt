package eu.qiou.aaf4k.finance

import eu.qiou.aaf4k.util.roundUpTo

class Zuschlagskalkulation(
    directMaterialCost: Map<Kostenstelle, Double>,
    indirectMaterialCost: Map<Kostenstelle, Double>,
    directLaborCost: Map<Kostenstelle, Double>,
    indirectLaborCost: Map<Kostenstelle, Double>,
    specialLaborCost: Double = 0.0,
    managementCostOverhead: Double = 0.0,
    distributionCostOverhead: Double = 0.0,
    val decimalPrecision: Int = 4
) {

    private val materialCostAllocationRate: Map<Kostenstelle, Double> =
        indirectMaterialCost.map { it.key to it.value / (directMaterialCost[it.key] ?: error("")) }.toMap()
    private val laborCostAllocationRate: Map<Kostenstelle, Double> =
        indirectLaborCost.map { it.key to it.value / (directLaborCost[it.key] ?: error("")) }.toMap()
    private val herstellkosten = directLaborCost.values.reduce { acc, d -> acc + d } +
            indirectLaborCost.values.reduce { acc, d -> acc + d } + directMaterialCost.values.reduce { acc, d -> acc + d } +
            indirectMaterialCost.values.reduce { acc, d -> acc + d } + specialLaborCost
    private val managementCostAllocationRate: Double = (managementCostOverhead / herstellkosten)
    private val distributionCostAllocationRate: Double = (distributionCostOverhead / herstellkosten)

    fun forProduct(
        directMaterialUnitCost: Map<Kostenstelle, Double>, directLaborUnitCost: Map<Kostenstelle, Double>,
        specialLaborUnitCost: Double = 0.0
    ): Kostentraegerrechnung {
        val directMaterialUnitCostRounded = directMaterialUnitCost.mapValues { it.value.roundUpTo(decimalPrecision) }
        val indirectMaterialUnitCost = directMaterialUnitCostRounded.mapValues {
            (it.value * (materialCostAllocationRate[it.key] ?: error(""))).roundUpTo(decimalPrecision)
        }
        val directLaborUnitCostRounded = (directLaborUnitCost).mapValues { it.value.roundUpTo(decimalPrecision) }
        val specialLaborUnitCostRounded = (specialLaborUnitCost).roundUpTo(decimalPrecision)
        val indirectLaborUnitCost = directLaborUnitCostRounded.mapValues {
            (it.value * (laborCostAllocationRate[it.key] ?: error(""))).roundUpTo(decimalPrecision)
        }
        val laborUnitCost = (directLaborUnitCostRounded.values.reduce { acc, d -> acc + d } +
                indirectLaborUnitCost.values.reduce { acc, d -> acc + d } + specialLaborUnitCostRounded).roundUpTo(
            decimalPrecision
        )
        val materialUnitCost = (directMaterialUnitCostRounded.values.reduce { acc, d -> acc + d } +
                indirectMaterialUnitCost.values.reduce { acc, d -> acc + d }).roundUpTo(decimalPrecision)
        val herstellCost = (laborUnitCost + materialUnitCost).roundUpTo(decimalPrecision)
        val indirectManagementUnitCost = (herstellCost * managementCostAllocationRate).roundUpTo(decimalPrecision)
        val indirectDistributionUnitCost = (herstellCost * distributionCostAllocationRate).roundUpTo(decimalPrecision)
        val managementAndDistributionCost =
            (indirectManagementUnitCost + indirectDistributionUnitCost).roundUpTo(decimalPrecision)
        val selbstCost = herstellCost + managementAndDistributionCost

        return Kostentraegerrechnung(
            directMaterialUnitCost = directMaterialUnitCostRounded,
            indirectMaterialUnitCost = indirectMaterialUnitCost,
            directLaborUnitCost = directLaborUnitCostRounded,
            indirectLaborUnitCost = indirectLaborUnitCost,
            laborUnitCost = laborUnitCost,
            specialLaborUnitCost = specialLaborUnitCostRounded,
            indirectManagementUnitCost = indirectManagementUnitCost,
            indirectDistributionUnitCost = indirectDistributionUnitCost,
            herstellkosten = herstellCost,
            selbstkosten = selbstCost,
            materialUnitCost = materialUnitCost,
            managementAndDistributionCost = managementAndDistributionCost
        )
    }
}