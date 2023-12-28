package se.hasanen.uppgifty7compose

import android.content.res.Resources
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import se.hasanen.uppgifty7compose.ui.theme.Uppgifty7ComposeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Uppgifty7ComposeTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting()
                }
            }
        }
    }

    @Composable
    fun Greeting(modifier: Modifier = Modifier) {
        var item by remember { mutableStateOf("") }
        var result by remember { mutableStateOf("result") }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {

                Spacer(modifier = Modifier.height(40.dp))
                Row {
                    Button(onClick = {
                        runTextRecognition(R.drawable.pizza) { recognizedText ->
                            item = "Pizza"
                            result = recognizedText
                        }
                    }) {
                        Text("Pizza", fontSize = 20.sp)
                    }
                    Spacer(modifier = Modifier.width(10.dp))

                    Button(onClick = {
                        runTextRecognition(R.drawable.pasta) { recognizedText ->
                            item = "Pasta"
                            result = recognizedText
                        }
                    }) {
                        Text("Pasta", fontSize = 20.sp)
                    }
                    Spacer(modifier = Modifier.width(10.dp))

                    Button(onClick = {
                        runTextRecognition(R.drawable.burger) { recognizedText ->
                            item = "Burger"
                            result = recognizedText
                        }
                    }) {
                        Text("Burger", fontSize = 20.sp)
                    }
                }

                Spacer(modifier = Modifier.height(40.dp))

                Text("$item ingredients:", fontSize = 20.sp)

                Spacer(modifier = Modifier.height(40.dp))
                Text(result, fontSize = 17.sp, modifier = Modifier.padding(all = 15.dp).align(Alignment.Start))
            }
        }

    private fun runTextRecognition(item: Int, callback: (String) -> Unit) {

        val selectedImage = BitmapFactory.decodeResource(resources, item)

        val image = InputImage.fromBitmap(selectedImage, 0)
        val textRecognizerOptions = TextRecognizerOptions.Builder().build()
        val recognizer = TextRecognition.getClient(textRecognizerOptions)

        recognizer.process(image)
            .addOnSuccessListener { texts ->
                callback.invoke(processTextRecognitionResult(texts))
            }
            .addOnFailureListener { e ->
                e.printStackTrace()
            }
    }

    private fun processTextRecognitionResult(texts: Text): String {
        val stringBuilder = StringBuilder()
        val blocks: List<Text.TextBlock> = texts.getTextBlocks()
        if (blocks.size == 0) {
            return "No text found"
        }
        for (i in blocks.indices) {
            val lines: List<Text.Line> = blocks[i].getLines()
            for (j in lines.indices) {
                val elements: List<Text.Element> = lines[j].getElements()
                for (k in elements.indices) {
                    //Log.i("MLKITDEBUG", elements[k].text + " " + elements[k].confidence.toString())
                    stringBuilder.appendLine(elements[k].text)

                }
            }
        }
        return stringBuilder.toString().trim()
    }

    @Preview(showBackground = true)
    @Composable
    fun GreetingPreview() {
        Uppgifty7ComposeTheme {
            Greeting(modifier = Modifier)
        }
    }
}