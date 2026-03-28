package com.dam.proydrp.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import com.dam.proydrp.R
import com.dam.proydrp.ui.common.LocalDimensions
import com.dam.proydrp.ui.theme.ProydrpTheme

@Composable
fun CustomAlertDialog(
    title: Int,
    body: Int,
    primaryButtonText: Int,
    secondaryButtonText: Int,
    icon: Int,
    mode: Boolean = false,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit = {}
) {

    val dimensions = LocalDimensions.current

    AlertDialog(
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(icon),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(dimensions.extraBig)
                )
                Spacer(modifier = Modifier.width(dimensions.standard))
                Text(
                    text = stringResource(title),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        },

        onDismissRequest = onDismiss,
        text = { Text(text = stringResource(body), fontSize = 17.sp, color = MaterialTheme.colorScheme.onPrimary) },
        properties = DialogProperties(
            dismissOnBackPress = false,
            dismissOnClickOutside = false
        ),

        containerColor = MaterialTheme.colorScheme.background,
        tonalElevation = dimensions.medium,
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.tertiary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ),

                modifier = Modifier.padding(
                    start = dimensions.small,
                )
            ) {
                Text(
                    text =  stringResource(primaryButtonText),
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
        },

        dismissButton = if (mode) {
            {
                OutlinedButton(
                    onClick = onDismiss,
                    border = BorderStroke(dimensions.tiny, MaterialTheme.colorScheme.tertiary),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    ),
                ) {
                    Text(
                        text = stringResource(secondaryButtonText),
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        } else null
    )
}

@Preview(showBackground = true)
@Composable
fun CustomAlertDialogOKCancelPreview() {
    ProydrpTheme {
        Surface(
            color = MaterialTheme.colorScheme.surface,
        ) {
            CustomAlertDialog(
                title = R.string.login_title,
                body = R.string.logout_warning,
                primaryButtonText = R.string.confirm,
                secondaryButtonText = R.string.cancel,
                icon = R.drawable.error_icon,
                mode = true,
                onConfirm = {},
                onDismiss = {},
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CustomAlertDialogOKPreview() {
    ProydrpTheme {
        Surface(
            color = MaterialTheme.colorScheme.surface,
        ) {
            CustomAlertDialog(
                title = R.string.login_title,
                body = R.string.logout_warning,
                primaryButtonText = R.string.confirm,
                secondaryButtonText = R.string.cancel,
                icon = R.drawable.error_icon,
                mode = false,
                onConfirm = {},
                onDismiss = {},
            )
        }
    }
}




