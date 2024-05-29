package com.example.kikicouriers.models

import java.math.BigDecimal
import java.math.RoundingMode
import java.text.DecimalFormat

// Argument specs for runnable file, used for separating and validating input arguments
const val NUM_OF_LEADING_ARGS = 2
const val NUM_OF_ARGS_PER_PACKAGE = 4
const val NUM_OF_TRAILING_ARGS = 3

data class Offer(
    val code: String,
    val discount: Double,
    val minDistance: Int,
    val maxDistance: Int,
    val minWeight: Int,
    val maxWeight: Int
)

data class Package(
    val id: String,
    val weight: Int,
    val distance: Int,
    val offerCode: String?,
    val offerDiscount: Int = 0,
    val deliveryCost: Int = 0,
    var timeTaken: Double = 0.0,
    var deliveredAt: Double = 0.0
)


// If there are more valid offer codes, add here to the list
val offers = listOf(
    Offer("OFR001", 0.10, 0, 200, 70, 200),
    Offer("OFR002", 0.07, 50, 150, 100, 250),
    Offer("OFR003", 0.05, 50, 250, 10, 150)
)

fun main(args: Array<String>) {

    if (args.size < 9) {
        println(
            "Usage: [base_delivery_cost] [no_of_packges]\n" +
                    "[pkg_id1] [pkg_weight1_in_kg] [distance1_in_km] [offer_code1] [no_of_vehicles] [max_speed] [max_carriable_weight]"
        )
        return
    }
    // Extract arguments
    val baseDeliveryCost = args[0].toIntOrNull()
    val noOfPackages = args[1].toIntOrNull()
    val noOfVehicles = args[args.size - 3].toIntOrNull()
    val maxSpeed = args[args.size - 2].toIntOrNull()
    val maxAllowedWeight = args[args.size - 1].toIntOrNull()

    // Do basic validations and return if cannot proceed further
    if (baseDeliveryCost == null) {
        println("Base delivery cost cannot be null")
        return
    }
    if (noOfPackages == null || noOfPackages == 0) {
        println("Please input details of at least one package")
        return
    }
    if (noOfVehicles == null || maxSpeed == null || maxAllowedWeight == null) {
        println("At least one vehicle info is required for delivery")
        return
    }
    if (args.size != (NUM_OF_LEADING_ARGS + (NUM_OF_ARGS_PER_PACKAGE * noOfPackages) + NUM_OF_TRAILING_ARGS)) {
        println("Provided data doesn't match number of packages")
        return
    }
    // Split data into an array of packages
    val packages = args.copyOfRange(2, args.size - 3).asList().chunked(4).map {
        Package(it[0], it[1].toInt(), it[2].toInt(), it[3])
    }

    val costEstimatedPackages = estimateCosts(packages, baseDeliveryCost)

    val timeCalculatedPackages =
        calculateDeliveryTime(costEstimatedPackages, noOfVehicles, maxSpeed, maxAllowedWeight)

    timeCalculatedPackages.forEach { pkg ->
        val decimalFormat = DecimalFormat("#.00")
        println("${pkg.id} ${pkg.offerDiscount} ${pkg.deliveryCost} ${decimalFormat.format(pkg.deliveredAt)}")
//        println("${pkg.id} ${pkg.offerDiscount} ${pkg.deliveryCost} ${decimalFormat.format(pkg.deliveredAt)}")
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

/**
 * Estimate the cost of delivering each package in a list of packages.
 * Returns list of package with discount and final cost estimate values applied
 */
fun estimateCosts(packages: List<Package>, baseDeliveryCost: Int): List<Package> {
    return packages.map { pkg ->
        val costs = getDiscountAndDeliveryCost(
            baseDeliveryCost,
            pkg.weight,
            pkg.distance,
            pkg.offerCode,
            offers
        )
        pkg.copy(offerDiscount = costs.first, deliveryCost = costs.second)
    }
}

/**
 * Returns a set of packages sorted by their total weights, in a descending order
 */
fun createDeliverySets(packages: List<Package>, maxSum: Int): List<List<Package>> {
    val sortedPackages = packages.sortedByDescending { it.weight }
    val sets = mutableListOf<MutableList<Package>>()

    sortedPackages.forEach { pkg ->
        val set = sets.find { it.sumOf { it.weight } + pkg.weight <= maxSum }
        if (set != null) {
            set.add(pkg)
        } else {
            sets.add(mutableListOf(pkg))
        }
    }
    return sets.sortedByDescending { it.sumOf { it.weight } }
}

/**
 * Compute the deliveries to maximize efficiency based on the given vehicle constraints.
 * Returns a package with time taken and delivered times applied
 *
 * Logic:
 * while packages set is non empty{
 *      pick the vehicle with lowest current time
 *      pick the heaviest set from packages
 *      Calculate time for each items in a delivery set - timeTaken, delivered at (Current time of vehicle + timeTaken)
 *      Update current time of vehicle (it + Time taken for farthest package x 2)
 *      Removed the delivered item from top of list
 * }
 */
fun calculateDeliveryTime(
    packages: List<Package>,
    numOfVehicles: Int,
    maxSpeed: Int,
    maxAllowedWeight: Int
): List<Package> {

    val packageSetsToBeDelivered = createDeliverySets(packages, maxAllowedWeight).toMutableList()

    val vehicleTimer = DoubleArray(numOfVehicles) { 0.0 }
    var packageSetIndex = 0

    while (packageSetIndex < packageSetsToBeDelivered.size) {
        for (vehicle in 0 until numOfVehicles) {
            val firstAvailableVehicleTimer = vehicleTimer.minOrNull()!!     // numOfVehicles will be >0
            val firstAvailableVehicleIndex =
                vehicleTimer.indexOfFirst { it == firstAvailableVehicleTimer }

            packageSetsToBeDelivered.first().forEach() {
                it.timeTaken = BigDecimal(it.distance.toDouble() / maxSpeed).setScale(2, RoundingMode.FLOOR).toDouble()
                it.deliveredAt = BigDecimal(firstAvailableVehicleTimer + it.timeTaken).setScale(2, RoundingMode.HALF_UP).toDouble()
                println("Delivered ${it.id} at ${it.deliveredAt}")
            }
            val farthestPackage = packageSetsToBeDelivered.first().maxBy { it.timeTaken }
            vehicleTimer[firstAvailableVehicleIndex] =
                vehicleTimer[firstAvailableVehicleIndex] + 2 * farthestPackage.timeTaken
            packageSetsToBeDelivered.removeAt(0)
        }
        packageSetIndex++
    }
    return packages
}