package com.zaziapps.mtg_scanner.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import com.zaziapps.mtg_scanner.data.model.LanguageItem
import com.zaziapps.mtg_scanner.ui.themes.Gold
import com.zaziapps.mtg_scanner.ui.themes.Beige
import com.zaziapps.mtg_scanner.ui.themes.Brown
import com.zaziapps.mtg_scanner.ui.themes.LightBrown

/**
 * Language Selector component.
 * @param selectedLanguage LanguageItem | Selected Language object
 * @param languages List<LanguageItem> | List of available Languages object
 * @param onLanguageSelected (String) -> Unit | Lambda expression triggered when a language is selected
 * @param modifier Modifier | View modifier
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LanguageSelector(
    selectedLanguage: LanguageItem,
    languages: List<LanguageItem>,
    onLanguageSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    // Expanded component controller
    var isExpanded by remember { mutableStateOf(false) }

    // SELECTOR
    Box(modifier = modifier) {
        ExposedDropdownMenuBox(
            expanded = isExpanded,
            onExpandedChange = { isExpanded = !isExpanded },
        ) {
            TextField(
                value = selectedLanguage.displayName,
                onValueChange = {},
                readOnly = true,
                maxLines = 1,
                singleLine = true,
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded)
                },
                // Styles component
                colors = ExposedDropdownMenuDefaults.textFieldColors(
                    focusedTextColor = Gold,      // Gold text
                    unfocusedTextColor = Beige,    // Beige text
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = LightBrown,  // Inferior line
                    unfocusedIndicatorColor = Brown.copy(alpha = 0.5f),
                    focusedTrailingIconColor = Gold
                ),
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )

            // Expandable menu
            ExposedDropdownMenu(
                expanded = isExpanded,
                onDismissRequest = { isExpanded = false },
            ) {
                languages.forEach { language ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = language.displayName,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                color = if (language == selectedLanguage) Gold else Beige
                            )
                        },
                        onClick = {
                            onLanguageSelected(language.displayName)
                            isExpanded = false
                        }
                    )
                }
            }
        }
    }
}
