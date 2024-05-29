package com.example.kikicouriers.models

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class TestMain {

    // Offer codes
    @Test
    fun testCase1() {
        val baseCost = 100
        val weight = 5
        val distance = 5
        val offerCode = "OFR001"

        val result = getDiscountAndDeliveryCost(baseCost, weight, distance, offerCode, offers)
        assertEquals(0 to 175, result)
    }

    @Test
    fun testCase2() {
        val baseCost = 100
        val weight = 10
        val distance = 100
        val offerCode: String? = "OFR003"

        val result = getDiscountAndDeliveryCost(baseCost, weight, distance, offerCode, offers)
        assertEquals(35 to 665, result)
    }


    @Test
    fun testInvalidOfferCode() {
        val baseCost = 100
        val weight = 150
        val distance = 100
        val offerCode = "INVALID"

        val result = getDiscountAndDeliveryCost(baseCost, weight, distance, offerCode, offers)
        assertEquals(0 to 2100, result)
    }

    // Delivery time

    @Test
    fun testCreateDeliverySets() {
        val packages = listOf(
            Package("PKG1", 50, 150, "OFR002"),
            Package("PKG2", 75, 200, null),
            Package("PKG3", 175, 100, "OFR001"),
            Package("PKG4", 110, 50, null),
            Package("PKG5", 155, 50, "OFR001")
        )
        val maxSum = 200
        val expected = listOf(
            listOf(packages[3], packages[1]),
            listOf(packages[2]),
            listOf(packages[4]),
            listOf(packages[0])
        )
        val result = createDeliverySets(packages, maxSum)
        assertEquals(expected, result)
    }

    @Test
    fun testCalculateDeliveryTime() {
        val packages = listOf(
            Package("PKG1", 50, 30, "OFR002"),
            Package("PKG2", 75, 125, "OFR008"),
            Package("PKG3", 175, 100, "OFR003"),
            Package("PKG4", 110, 60, "OFR002"),
            Package("PKG5", 155, 95, "NA"),
        )
        val numOfVehicles = 2
        val maxSpeed = 70
        val maxAllowedWeight = 200

        val expected = listOf(
            packages[0].copy(deliveredAt = 3.98, timeTaken = 0.42),
            packages[1].copy(deliveredAt = 1.78, timeTaken = 1.78),
            packages[2].copy(deliveredAt = 1.42, timeTaken = 1.42),
            packages[3].copy(deliveredAt = 0.85, timeTaken = 0.85),
            packages[4].copy(deliveredAt = 4.19, timeTaken = 1.35),
        )
        val result = calculateDeliveryTime(packages, numOfVehicles, maxSpeed, maxAllowedWeight)
        assertEquals(expected, result)
    }

}
