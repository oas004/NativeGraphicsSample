package com.example.nativegraphicssample

import android.graphics.Bitmap
import android.graphics.Canvas
import android.view.View
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.layout.boundsInParent
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import androidx.core.graphics.applyCanvas
import java.io.File
import kotlin.math.roundToInt


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

fun File.writeBitmap(bitmap: Bitmap, format: Bitmap.CompressFormat, quality: Int) {
    outputStream().use { out ->
        bitmap.compress(format, quality, out)
        out.flush()
    }
}

