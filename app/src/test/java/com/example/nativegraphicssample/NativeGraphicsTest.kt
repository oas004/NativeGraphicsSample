package com.example.nativegraphicssample

import androidx.compose.ui.test.captureToImage
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.printToLog
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestName
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode
import org.robolectric.shadows.ShadowPixelCopy

class NativeGraphicsTest : BaseComposeRobolectricTest() {

    @get:Rule
    val composeTestRule = createComposeRule()

    @get:Rule
    val testName = TestName()


    @GraphicsMode(GraphicsMode.Mode.NATIVE)
    @Config(shadows = [ShadowPixelCopy::class], minSdk = 26)
    @Test
    fun testNativeGraphicsApproach1() {
        composeTestRule.apply {
            setContent {
                Greeting("Hello!")
            }
        }

        // This would be ideal, but it does not work for some reason.
        composeTestRule.onRoot().captureToImage()
        composeTestRule.onRoot().printToLog("SCREEN NODE TREE")
    }

}
