package com.example.nativegraphicssample

import android.graphics.Bitmap
import android.graphics.Canvas
import android.view.View
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.test.junit4.createComposeRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestName
import org.robolectric.annotation.GraphicsMode
import java.io.File

class CardScreenshotTest : BaseComposeRobolectricTest() {


    @get:Rule
    val composeTestRule = createComposeRule()

    @get:Rule
    val testName = TestName()

    @OptIn(ExperimentalCoroutinesApi::class)
    @GraphicsMode(GraphicsMode.Mode.NATIVE)
    @Test
    fun cardTest() = runTest {
        lateinit var view: View

        composeTestRule.setContent {
            view = LocalView.current
            SomeCard()
        }

        composeTestRule.awaitIdle()

        val image = Bitmap.createBitmap(
            /* width = */ view.width,
            /* height = */ view.height,
            /* config = */ Bitmap.Config.ARGB_8888
        ).apply {
            view.draw(Canvas(this))
        }
        val path = System.getProperty("user.dir")

        File("$path/src/test/snapshots/images", "${testName.methodName}-screenshot.png")
            .writeBitmap(image, Bitmap.CompressFormat.PNG, 100)

        println("Saved file at $path")
    }

}

