package com.example.kikicouriers.models

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class TestMain {

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


}
