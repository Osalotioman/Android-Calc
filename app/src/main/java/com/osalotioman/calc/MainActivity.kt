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

class MainActivity : ComponentActivity() {

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
	
		// This is an extension function of Activity that sets the @Composable function that's
		// passed to it as the root view of the activity. This is meant to replace the .xml file
		// that we would typically set using the setContent(R.id.xml_file) method. The setContent
		// block defines the activity's layout.
		setContent {
			// Column is a composable that places its children in a vertical sequence. You
			// can think of it similar to a LinearLayout with the vertical orientation. 
			// In addition we also pass a few modifiers to it.
			val context = LocalContext.current
			fun showToast(message: String) {
        		Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
   		 }
			// You can think of Modifiers as implementations of the decorators pattern that are used to
			// modify the composable that its applied to. In the example below, we configure the
			// Column to occupy the entire available height & width using Modifier.fillMaxSize().
			Column(
				modifier = Modifier.fillMaxSize(),
				verticalArrangement = Arrangement.Center,
				horizontalAlignment = Alignment.CenterHorizontally,
				content = {
					SimpleButton(
    					onClick = { showToast("Telepathy") },
    					buttonText = "Calculator"
					)
					
					RightAlignedTextBox(
       				 value = TextFieldValue(""),
      				  onValueChange = {}
				    )
					ButtonGrid()
				}
			)
		}
    }
}

// We represent a Composable function by annotating it with the @Composable annotation. Composable
// functions can only be called from within the scope of other composable functions.
@Composable
fun SimpleText(
	displayText: String,
	style: TextStyle = TextStyle.Default
) {
    // We should think of composable functions to be similar to lego blocks - each composable
    // function is in turn built up of smaller composable functions. Here, the Text() function is
    // pre-defined by the Compose UI library; you call that function to declare a text element
    // in your app.
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
    modifier: Modifier = Modifier
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
                            onClick = { /* Handle click event */ },
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
    value: TextFieldValue = TextFieldValue(""),  // The initial value of the TextField
    onValueChange: (TextFieldValue) -> Unit = {}
) {
    Box(modifier = modifier.fillMaxWidth(0.8f)) {
        // The TextField with the right-aligned text using padding and alignment
        TextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .align(Alignment.CenterEnd)  // Align the TextField to the right within the Box
                .padding(16.dp),  // Optional padding around the text field
            textStyle = TextStyle(
                color = Color.Black,
                fontWeight = FontWeight.Normal
            ),
            placeholder = { Text("0") },  // Placeholder text
            singleLine = true,
			enabled = false
        )
    }
}

