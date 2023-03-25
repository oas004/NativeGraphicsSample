package com.example.nativegraphicssample

import android.graphics.Bitmap
import android.graphics.Canvas
import android.view.View
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.quickbird.snapshot.Diffing
import com.quickbird.snapshot.Snapshotting
import com.quickbird.snapshot.bitmap
import com.quickbird.snapshot.fileSnapshotting
import com.quickbird.snapshot.intMean
import com.quickbird.snapshot.snapshot
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.robolectric.annotation.GraphicsMode

// This is a sample to show how you can use com.quickbird.snapshot.Snapshotting to verify the bitmap generation
class SnappshotSample : BaseComposeRobolectricTest() {


    @get:Rule
    val composeTestRule = createComposeRule()

    @OptIn(ExperimentalCoroutinesApi::class)
    @GraphicsMode(GraphicsMode.Mode.NATIVE)
    @Test
    fun cardTest() = runTest {
        lateinit var view: View

        composeTestRule.setContent {
            view = LocalView.current
            SomeTestComposable()
        }

        composeTestRule.awaitIdle()

        val snappshotting = Snapshotting(
            diffing = Diffing.bitmap(colorDiffing = Diffing.intMean),
            snapshot = { _: SemanticsNodeInteraction ->
                Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888).apply {
                    view.draw(Canvas(this))
                }
            }
        ).fileSnapshotting

        snappshotting.snapshot(composeTestRule.onRoot(), record = false, directoryName = "snapshots")

    }

}

@Composable
fun SomeTestComposable() {
    Column(Modifier.size(100.dp).background(Color.White), horizontalAlignment = Alignment.CenterHorizontally) {
        Box(Modifier.size(20.dp).clip(CircleShape).background(Color.Gray))
        Text(modifier = Modifier.fillMaxSize(),text = "Some Grey Circle", textAlign = TextAlign.Center)
    }
}
