package com.example.nativegraphicssample

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.layout.boundsInParent
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.captureToImage
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.core.graphics.applyCanvas
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestName
import org.robolectric.Shadows.shadowOf
import org.robolectric.annotation.GraphicsMode
import org.robolectric.shadows.ShadowDisplay
import org.robolectric.shadows.ShadowDisplayManager
import java.io.File
import java.io.FileOutputStream
import kotlin.math.roundToInt

class ComposeSnapshotTest : BaseComposeRobolectricTest() {


    @get:Rule
    val composeTestRule = createComposeRule()

    @get:Rule
    val testName = TestName()

    @Before
    fun setup() {
        val display = ShadowDisplay.getDefaultDisplay()
        ShadowDisplayManager.changeDisplay(display.displayId, "+round")
        shadowOf(display).apply {
            setXdpi(320f)
            setYdpi(320f)
            setHeight(454)
            setWidth(454)
        }
    }

    @GraphicsMode(GraphicsMode.Mode.NATIVE)
    @Test
    fun testApproach2() {
        composeTestRule.apply {
            setContent {
                Snapshot(testName.methodName).SnapShotWrapper(
                    content = {
                        MaterialTheme {
                            Box(Modifier.background(MaterialTheme.colors.background)) {
                                Greeting("Hello!")
                            }
                        }
                    }
                )
            }
        }
    }
}

class Snapshot(private val testName: String) {

    @Composable
    fun SnapShotWrapper(
        content: @Composable () -> Unit
    ) {
        val view = LocalView.current

        var capturingViewBounds by remember { mutableStateOf<Rect?>(null) }

        Box(
            modifier = Modifier.fillMaxSize().onGloballyPositioned {
                capturingViewBounds = it.boundsInParent()
            }
        ) { content() }

        capturingViewBounds?.let {
            val bounds = it
            val image = Bitmap.createBitmap(
                bounds.width.roundToInt(), bounds.height.roundToInt(),
                Bitmap.Config.ARGB_8888
            ).applyCanvas {
                translate(-bounds.left, -bounds.top)
                view.draw(this)
            }
            val path = System.getProperty("user.dir")

            File("$path/src/test/snapshots/images", "${testName}-screenshot.png")
                .writeBitmap(image, Bitmap.CompressFormat.PNG, 100)

            println("Saved file at $path")
        }

    }
}

private fun File.writeBitmap(bitmap: Bitmap, format: Bitmap.CompressFormat, quality: Int) {
    outputStream().use { out ->
        bitmap.compress(format, quality, out)
        out.flush()
    }
}

fun assertScreenshotMatchesGolden(
    goldenName: String,
    node: SemanticsNodeInteraction
) {
    val bitmap = node.captureToImage().asAndroidBitmap()

    // Save screenshot to file for debugging
    saveScreenshot(goldenName + System.currentTimeMillis().toString(), bitmap)
    val golden = InstrumentationRegistry.getInstrumentation()
        .context.resources.assets.open("$goldenName.png").use { BitmapFactory.decodeStream(it) }

    golden.compare(bitmap)
}

private fun saveScreenshot(filename: String, bmp: Bitmap) {
    val path = InstrumentationRegistry.getInstrumentation().targetContext.filesDir.canonicalPath
    FileOutputStream("$path/$filename.png").use { out ->
        bmp.compress(Bitmap.CompressFormat.PNG, 100, out)
    }
    println("Saved screenshot to $path/$filename.png")
}

private fun Bitmap.compare(other: Bitmap) {
    if (this.width != other.width || this.height != other.height) {
        throw AssertionError("Size of screenshot does not match golden file (check device density)")
    }
    // Compare row by row to save memory on device
    val row1 = IntArray(width)
    val row2 = IntArray(width)
    for (column in 0 until height) {
        // Read one row per bitmap and compare
        this.getRow(row1, column)
        other.getRow(row2, column)
        if (!row1.contentEquals(row2)) {
            throw AssertionError("Sizes match but bitmap content has differences")
        }
    }
}

private fun Bitmap.getRow(pixels: IntArray, column: Int) {
    this.getPixels(pixels, 0, width, 0, column, width, 1)
}
