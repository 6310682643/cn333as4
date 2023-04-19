package com.example.randompic

import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Website
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import coil.request.ImageRequest
import com.example.randompic.ui.theme.RandomPicTheme
import java.net.HttpURLConnection
import java.net.URL



enum class ImageType {
    plant, animal, food, nature
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RandomPicTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    ImageSelector()
                }
            }
        }
    }

    @Composable
    fun ImageSelector() {
        var width by remember { mutableStateOf("") }
        var height by remember { mutableStateOf("") }
        var selectType by remember { mutableStateOf(ImageType.plant) }
        var isWidthValid by remember { mutableStateOf(true) }
        var isHeightValid by remember { mutableStateOf(true) }
        var validNum by remember { mutableStateOf(false) }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = width,
                onValueChange = {
                    width = it
                    isWidthValid = checkNum(width)
                },
                label = { Text("Enter width") },
                isError = !isWidthValid,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            OutlinedTextField(
                value = height,
                onValueChange = {
                    height = it
                    isHeightValid = checkNum(height)
                },
                label = { Text("Enter height") },
                isError = !isHeightValid,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            Text(
                "Select image type",
                color = Color.Black,
                modifier = Modifier.padding(top = 16.dp)
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(top = 8.dp)
            ) {
                ImageType.values().forEach { imageType ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clickable { selectType = imageType }
                            .padding(4.dp)
                    ) {
                        RadioButton(
                            selected = selectType == imageType,
                            onClick = null,
                            modifier= Modifier.align(Alignment.CenterVertically)
                        )
                        Text(
                            text = imageType.name,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }
                }
            }

            Button(
                onClick = {
                    if (isWidthValid && isHeightValid) {
                        validNum = true
                    }
                },
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 16.dp)
            ) {
                Text(text = "Display Image")
            }

            if (validNum) {
                DisplayImage(width.toInt(), height.toInt(), selectType)
            }
        }
    }

    private fun checkNum(input: String): Boolean {
        return input.toIntOrNull() != null && input.toInt() > 0
    }

    private fun getImageUrl(width: Int, height: Int, imageType: ImageType): String {
        return "https://loremflickr.com/${width}/${height}/${imageType.name.toLowerCase().capitalize()}"
    }


    @Composable
    fun DisplayImage(width: Int, height: Int, type: ImageType) {
        val imageUrl = getImageUrl(width, height, type)
        val painter = rememberImagePainter(data = imageUrl)
        Image(
            painter = painter,
            contentDescription = "",
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        )
    }
}
