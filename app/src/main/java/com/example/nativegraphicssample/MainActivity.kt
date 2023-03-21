package com.example.nativegraphicssample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.nativegraphicssample.ui.theme.NativeGraphicsSampleTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NativeGraphicsSampleTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
                    Greeting("Android")
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    NativeGraphicsSampleTheme {
        Greeting("Android")
    }
}

@Composable
fun SomeCard() {
    MaterialTheme {
        Box(Modifier.size(300.dp).background(MaterialTheme.colors.background)) {
            Card(
                modifier = Modifier.size(200.dp).align(Alignment.Center),
                backgroundColor = MaterialTheme.colors.surface,
                elevation = 8.dp
            ) {
                Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Title",
                        style = MaterialTheme.typography.h1
                    )

                    Text(
                        text = "Body",
                        style = MaterialTheme.typography.body1
                    )


                    Button(onClick = {}) {

                        Text(
                            text = "Body",
                            style = MaterialTheme.typography.button
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun SomeCardPreveiw() {
    SomeCard()
}