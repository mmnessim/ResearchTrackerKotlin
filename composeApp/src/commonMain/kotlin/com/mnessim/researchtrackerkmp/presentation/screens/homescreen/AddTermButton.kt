package com.mnessim.researchtrackerkmp.presentation.screens.homescreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.unit.dp

@Composable
fun AddTermButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    tag: String = "AddTermButton"
) {
    IconButton(
        onClick = onClick,
        modifier = modifier
            .background(MaterialTheme.colorScheme.surfaceTint, shape = CircleShape)
            .semantics { testTag = tag },
        shape = CircleShape
    ) {
        Icon(
            tint = MaterialTheme.colorScheme.inverseOnSurface,
            imageVector = Icons.Default.Add,
            contentDescription = "Add new term"
        )
    }
}