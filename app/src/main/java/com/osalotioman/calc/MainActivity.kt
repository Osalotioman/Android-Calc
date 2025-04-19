package com.osalotioman.calc

//package com.example.test1

import androidx.activity.ComponentActivity
//import androidx.compose.material3.* //Text
// Use the material in CodeAssist
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

            val textFieldState = remember { mutableStateOf(TextFieldValue("0")) }

            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                SimpleButton(
                    onClick = { textFieldState.value = TextFieldValue("0") },
                    buttonText = "Clear"
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
                                if(item == "="){
                                    textFieldState.value = TextFieldValue(
                                        eval(textFieldState.value.toString())
                                    )
                                }else if(textFieldState.value.text == "0"){
                                    textFieldState.value = TextFieldValue(
                                        item
                                    )
                                }else{
                                    textFieldState.value = TextFieldValue(
                                        textFieldState.value.text + item
                                    )
                                }
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

fun eval(expression: String): String {
    return object {
        var pos = -1
        var ch = 0

        fun nextChar() {
            ch = if (++pos < expression.length) expression[pos].code else -1
        }

        fun eat(charToEat: Int): Boolean {
            while (ch == ' '.code) nextChar()
            if (ch == charToEat) {
                nextChar()
                return true
            }
            return false
        }

        fun parse(): Double {
            nextChar()
            val x = parseExpression()
            if (pos < expression.length) throw RuntimeException("Unexpected: ${expression[pos]}")
            return x
        }

        fun parseExpression(): Double {
            var x = parseTerm()
            while (true) {
                x = when {
                    eat('+'.code) -> x + parseTerm()
                    eat('-'.code) -> x - parseTerm()
                    else -> return x
                }
            }
        }

        fun parseTerm(): Double {
            var x = parseFactor()
            while (true) {
                x = when {
                    eat('*'.code) -> x * parseFactor()
                    eat('/'.code) -> x / parseFactor()
                    else -> return x
                }
            }
        }

        fun parseFactor(): Double {
            if (eat('+'.code)) return parseFactor() // unary plus
            if (eat('-'.code)) return -parseFactor() // unary minus

            var x: Double
            val startPos = pos
            if (eat('('.code)) {
                x = parseExpression()
                if (!eat(')'.code)) throw RuntimeException("Missing )")
            } else if (ch in '0'.code..'9'.code || ch == '.'.code) {
                while (ch in '0'.code..'9'.code || ch == '.'.code) nextChar()
                x = expression.substring(startPos, pos).toDouble()
            } else {
                throw RuntimeException("Unexpected: '${ch.toChar()}'")
            }

            return x
        }
    }.parse().toString()
}