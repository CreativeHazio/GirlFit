package com.creativehazio.auth.presentation.emailverification

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.creativehazio.designsystem.components.GirlFitPrimaryButton
import com.creativehazio.designsystem.theme.Spacing
import girlfit.feature.auth.generated.resources.Res
import girlfit.feature.auth.generated.resources.back_to_login
import girlfit.feature.auth.generated.resources.check_spam
import girlfit.feature.auth.generated.resources.verify_email_body
import girlfit.feature.auth.generated.resources.verify_email_title
import org.jetbrains.compose.resources.stringResource

@Composable
fun EmailVerificationScreenRoot(
    modifier: Modifier = Modifier,
    email: String,
    emailVerificationViewModel: EmailVerificationViewModel,
    onNavigateToLogin: () -> Unit,
) {

    val snackbarHostState = remember { SnackbarHostState() }

    val uiState = emailVerificationViewModel.uiState.collectAsStateWithLifecycle().value
    val event = emailVerificationViewModel::onEvent

    LaunchedEffect(emailVerificationViewModel.effect) {
        emailVerificationViewModel.effect.collect {
            when (it) {
                EmailVerificationEffect.NavigateToLogin -> onNavigateToLogin()
                is EmailVerificationEffect.ShowError -> {
                    snackbarHostState.showSnackbar(
                        message = it.message.asStringSuspend(),
                        duration = SnackbarDuration.Long
                    )
                }

                is EmailVerificationEffect.ShowSuccess -> {
                    snackbarHostState.showSnackbar(
                        message = it.message.asStringSuspend(),
                        duration = SnackbarDuration.Long
                    )
                }
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) {
        EmailVerificationScreen(
            modifier = modifier,
            email = email,
            uiState = uiState,
            event = event
        )

    }

}

@Composable
internal fun EmailVerificationScreen(
    modifier: Modifier = Modifier,
    email: String,
    uiState: EmailVerificationState,
    event: (EmailVerificationEvent) -> Unit,
) {


    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(Spacing.Large)
            .imePadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            text = stringResource(Res.string.verify_email_title),
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(Spacing.Medium))

        Text(
            text = stringResource(Res.string.verify_email_body, email),
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(Spacing.ExtraLarge))

        GirlFitPrimaryButton(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(Res.string.back_to_login),
            onClick = {
                event(EmailVerificationEvent.OnBackToLoginClicked)
            }
        )

        Spacer(modifier = Modifier.height(Spacing.Medium))

        Text(
            text = stringResource(Res.string.check_spam),
            style = MaterialTheme.typography.bodyMedium
        )
        TextButton(
            enabled = uiState.isResendEnabled && !uiState.isLoading,
            onClick = { event(EmailVerificationEvent.ResendEmailVerification) }
        ) {
            Text(
                text = uiState.timerText.asString(),
                color = if (uiState.isResendEnabled) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.onSurfaceVariant
                }
            )
        }

    }


}

@Preview(showBackground = true)
@Composable
fun EmailVerificationScreenPreview() {
    EmailVerificationScreen(
        email = "davideze123@gmail.com",
        uiState = EmailVerificationState(),
        event = {}
    )
}