package com.spongycode.songquest.ui.screen.gameplay.leaderboard.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.spongycode.songquest.util.CategoryConvertor
import com.spongycode.songquest.util.Fonts

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomDropDownMenu(
    selectedCategory: String = "",
    onChangeCategory: (String) -> Unit = {}
) {
    var expanded by remember { mutableStateOf(false) }
    val categories = CategoryConvertor.giveAllCategories()

    val configuration = LocalConfiguration.current
    val width = (configuration.screenWidthDp - 70)

    Box(
        modifier = Modifier
            .fillMaxWidth(),
        contentAlignment = Alignment.TopCenter
    ) {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = {
                expanded = !expanded
            }
        ) {

            OutlinedTextField(
                readOnly = true,
                value = CategoryConvertor.codeToDisplayText(selectedCategory),
                onValueChange = {},
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                textStyle = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontFamily = Fonts.poppinsFamily,
                    fontSize = 18.sp
                ),
                modifier = Modifier
                    .menuAnchor(MenuAnchorType.PrimaryNotEditable, true)
                    .width(width.dp)
            )
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.background)
                    .exposedDropdownSize()
                    .padding(horizontal = 10.dp)
            ) {
                categories.forEach { item ->
                    val displayText = CategoryConvertor.codeToDisplayText(item)
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = displayText,
                                fontFamily = Fonts.poppinsFamily,
                                fontSize = 15.sp
                            )
                        },
                        onClick = {
                            onChangeCategory(item)
                            expanded = false
                        },
                        contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewCustomDropDownMenu() {
    CustomDropDownMenu()
}