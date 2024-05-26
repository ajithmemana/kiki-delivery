package com.example.kikicouriers.models
// App.kt
data class Offer(
    val code: String,
    val discount: Double,
    val minDistance: Int,
    val maxDistance: Int,
    val minWeight: Int,
    val maxWeight: Int
)

fun main(args: Array<String>) {
    if (args.size < 6) {
        println("Usage: [base_delivery_cost] [no_of_packges]\n" +
                "[pkg_id1] [pkg_weight1_in_kg] [distance1_in_km] [offer_code1]")
        return
    }

    val baseDeliveryCost = args[0].toIntOrNull()
    val noOfPackages = args[1].toIntOrNull()
    val pkgId1 = args[2].toIntOrNull()
    val pkgWeight1InKg = args[3].toIntOrNull()
    val distance1InKm = args[4].toIntOrNull()
    val offerCode1 = args[5]

    if (baseDeliveryCost == null || pkgWeight1InKg == null || distance1InKm == null) {
        println("Please provide valid values for base delivery cost, package weight, and distance.")
        return
    }

    // If there are more valid offer codes, add here to the list
    val offers = listOf(
        Offer("OFR001", 0.10, 0, 200, 70, 200),
        Offer("OFR002", 0.07, 50, 150, 100, 250),
        Offer("OFR003", 0.05, 50, 250, 10, 150)
    )

    val deliveryCost = getDeliveryCost(baseDeliveryCost, pkgWeight1InKg, distance1InKm, offerCode1, offers)
    println("The total delivery cost is: $deliveryCost")
}

fun getDeliveryCost(baseCost: Int, weight: Int, distance: Int, offerCode: String?, offers: List<Offer>): Int {
    val totalCost = baseCost + (weight * 10) + (distance * 5)
    val discount = offerCode?.let { code ->
        offers.find { it.code == code && distance in it.minDistance..it.maxDistance && weight in it.minWeight..it.maxWeight }?.discount
    } ?: 0.0
    return (totalCost * (1 - discount)).toInt()
}
