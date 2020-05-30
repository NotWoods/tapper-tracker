package com.tigerxdaphne.tappertracker.pages.edit

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class MinDateValidatorTest {

    @Test
    fun testValidation() {
        val validator = MinDateValidator(minDay = 100)
        assertTrue(validator.isValid(200))
        assertTrue(validator.isValid(100))
        assertFalse(validator.isValid(0))
    }
}
