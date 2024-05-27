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

// If there are more valid offer codes, add here to the list
val offers = listOf(
    Offer("OFR001", 0.10, 0, 200, 70, 200),
    Offer("OFR002", 0.07, 50, 150, 100, 250),
    Offer("OFR003", 0.05, 50, 250, 10, 150)
)

fun main(args: Array<String>) {

    if (args.size < 6) {
        println(
            "Usage: [base_delivery_cost] [no_of_packges]\n" +
                    "[pkg_id1] [pkg_weight1_in_kg] [distance1_in_km] [offer_code1]"
        )
        return
    }

    val baseDeliveryCost = args[0].toIntOrNull()
    val noOfPackages = args[1].toIntOrNull()

    if (baseDeliveryCost == null) {
        println("Base delivery cost cannot be null")
        return
    }
    if (noOfPackages == null || noOfPackages == 0) {
        println("Please input details of at least one package")
        return
    }
    if (args.size != (2 + (4 * noOfPackages))) {
        println("Provided data doesn't match number of packages")
        return
    }

    val packages = args.copyOfRange(2, args.size).asList().chunked(4)

    packages.forEach { it ->
        val calculatedCosts =
            getDiscountAndDeliveryCost(
                baseDeliveryCost,
                it[1].toInt(),
                it[2].toInt(),
                it[3],
                offers
            )
        println("${it[0]}  ${calculatedCosts.first} ${calculatedCosts.second}")
    }
}

/**
 * Calculates and returns the offer discount and delivery cost as a Pair
 */
fun getDiscountAndDeliveryCost(
    baseCost: Int,
    weight: Int,
    distance: Int,
    offerCode: String?,
    offers: List<Offer>
): Pair<Int, Int> {
    val totalCost = baseCost + (weight * 10) + (distance * 5)
    val discount = offerCode?.let { code ->
        offers.find { it.code == code && distance in it.minDistance..it.maxDistance && weight in it.minWeight..it.maxWeight }?.discount
    } ?: 0.0
    return (discount * totalCost).toInt() to (totalCost * (1 - discount)).toInt()
}

