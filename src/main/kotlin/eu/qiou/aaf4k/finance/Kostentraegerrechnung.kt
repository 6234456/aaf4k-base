package eu.qiou.aaf4k.finance

data class Kostentraegerrechnung(
    val directMaterialUnitCost: Map<Kostenstelle, Double>,
    val indirectMaterialUnitCost: Map<Kostenstelle, Double>,
    val directLaborUnitCost: Map<Kostenstelle, Double>,
    val indirectLaborUnitCost: Map<Kostenstelle, Double>,
    val specialLaborUnitCost: Double = 0.0,
    val indirectManagementUnitCost: Double,
    val indirectDistributionUnitCost: Double,
    val specialDistributionUnitCost: Double = 0.0,
    val materialUnitCost: Double,
    val laborUnitCost: Double,
    val herstellkosten: Double = materialUnitCost + laborUnitCost,
    val managementAndDistributionCost: Double = indirectManagementUnitCost + indirectDistributionUnitCost + specialDistributionUnitCost,
    val selbstkosten: Double = herstellkosten + managementAndDistributionCost
)