package com.osalotioman.calc

import androidx.activity.ComponentActivity
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.activity.compose.setContent
import android.os.Bundle

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.*
import androidx.compose.ui.unit.dp

import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.font.FontWeight

import android.widget.Toast
import androidx.compose.ui.platform.LocalContext

import androidx.compose.runtime.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val context = LocalContext.current
            fun showToast(message: String) {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            }

            val textFieldState = remember { mutableStateOf(TextFieldValue("54")) }

            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                SimpleButton(
                    onClick = { textFieldState.value = TextFieldValue("7968+2626") },
                    buttonText = "Calculator"
                )

                RightAlignedTextBox(
                    value = textFieldState.value,
                    onValueChange = { textFieldState.value = it }
                )

                ButtonGrid(
                    textFieldState = textFieldState
                )
            }
        }
    }
}

@Composable
fun SimpleText(
    displayText: String,
    style: TextStyle = TextStyle.Default
) {
    Text(
        text = displayText,
        style = style
    )
}

@Composable
fun SimpleButton(
    onClick: () -> Unit,
    buttonText: String,
    modifier: Modifier = Modifier,
    colors: ButtonColors = ButtonDefaults.buttonColors()
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        colors = colors
    ) {
        Text(text = buttonText)
    }
}

@Composable
fun ButtonGrid(
    modifier: Modifier = Modifier,
    textFieldState: MutableState<TextFieldValue>
) {
    val items = listOf(
        "1", "2", "3", "-",
        "4", "5", "6", "÷",
        "7", "8", "9", "×",
        "0", ".", "+", "="
    )

    LazyColumn(modifier = modifier) {
        items.chunked(4).forEach { rowItems ->
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    rowItems.forEach { item ->
                        Button(
                            onClick = {
                                textFieldState.value = TextFieldValue(
                                    textFieldState.value.text + item
                                )
                            },
                            modifier = Modifier
                                .padding(4.dp)
                                .weight(1f)
                        ) {
                            Text(text = item)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun RightAlignedTextBox(
    modifier: Modifier = Modifier,
    value: TextFieldValue = TextFieldValue(""),
    onValueChange: (TextFieldValue) -> Unit = {}
) {
    Box(modifier = modifier.fillMaxWidth(0.8f)) {
        TextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(16.dp),
            textStyle = TextStyle(
                color = Color.Black,
                fontWeight = FontWeight.Normal
            ),
            placeholder = { Text("0") },
            singleLine = true,
            enabled = false
        )
    }
}