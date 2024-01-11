package com.spongycode.songquest.screen.auth.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.spongycode.songquest.R

@Composable
fun CustomTextField(
    modifier: Modifier = Modifier,
    text: String = "",
    labelText: String = "",
    placeHolderText: String = "",
    shape: Shape,
    singleLine: Boolean = true,
    keyboardType: KeyboardType = KeyboardType.Text,
    isPasswordToggleDisplayed: Boolean = keyboardType == KeyboardType.Password,
    isPasswordVisible: Boolean = false,
    onPasswordToggleClick: (Boolean) -> Unit = {},
    onValueChange: (String) -> Unit,
    focusRequester: FocusRequester = FocusRequester()
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .then(modifier)
    ) {
        OutlinedTextField(
            value = text,
            onValueChange = {
                onValueChange(it)
            },
            label = { Text(text = labelText) },
            placeholder = {
                Text(
                    color = MaterialTheme.colorScheme.inversePrimary,
                    text = placeHolderText
                )
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = keyboardType
            ),

            visualTransformation = if (!isPasswordVisible && isPasswordToggleDisplayed) {
                PasswordVisualTransformation()
            } else {
                VisualTransformation.None
            },
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
                .focusRequester(focusRequester = focusRequester),
            shape = shape,
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                unfocusedIndicatorColor = Color.LightGray,
                focusedContainerColor = MaterialTheme.colorScheme.background,
                unfocusedContainerColor = MaterialTheme.colorScheme.background,
                cursorColor = MaterialTheme.colorScheme.primary
            ),
            singleLine = singleLine,
            trailingIcon = if (isPasswordToggleDisplayed) {
                val icon: @Composable () -> Unit = {
                    Icon(
                        painter = if (isPasswordVisible) painterResource(id = R.drawable.baseline_visibility_24)
                        else painterResource(id = R.drawable.baseline_visibility_off_24),
                        contentDescription = null,
                        modifier = Modifier
                            .size(30.dp)
                            .clickable { onPasswordToggleClick(!isPasswordVisible) },
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
                icon
            } else null,
            textStyle = TextStyle(
                fontWeight = FontWeight.W500,
                fontSize = 18.sp
            )
        )
    }
}