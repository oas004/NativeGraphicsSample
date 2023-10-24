package com.example.nativegraphicssample

import org.junit.Before
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.shadows.ShadowLog
import org.robolectric.shadows.ShadowPixelCopy

@Config(
    instrumentedPackages = [
        "androidx.loader.content"
    ],
    shadows = [ShadowPixelCopy::class], minSdk = 26
)
@RunWith(RobolectricTestRunner::class)
abstract class BaseComposeRobolectricTest: Robolectric() {

    /**
     * This is default to print the node tree.
     */
    @Before
    @Throws(Exception::class)
    open fun setupLogCat() {
        ShadowLog.stream = System.out // Redirect Logcat to console
    }

}
