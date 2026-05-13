package com.creativehazio.auth.presentation.login

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.creativehazio.common.resulthandler.UiText
import com.creativehazio.designsystem.components.CustomTextField
import com.creativehazio.designsystem.components.PrimaryButton
import com.creativehazio.designsystem.components.SecondaryButton
import com.creativehazio.designsystem.theme.Spacing
import girlfit.feature.auth.generated.resources.Res
import girlfit.feature.auth.generated.resources.app_name
import girlfit.feature.auth.generated.resources.continue_with_apple
import girlfit.feature.auth.generated.resources.continue_with_google
import girlfit.feature.auth.generated.resources.email
import girlfit.feature.auth.generated.resources.log_in_action
import girlfit.feature.auth.generated.resources.login_subtitle
import girlfit.feature.auth.generated.resources.no_account_prompt
import girlfit.feature.auth.generated.resources.password
import girlfit.feature.auth.generated.resources.sign_up_action
import org.jetbrains.compose.resources.stringResource

@Composable
fun LoginScreenRoot(
    paddingValues: PaddingValues = PaddingValues.Zero,
    loginViewModel: LoginViewModel,
    onNavigateToHome: () -> Unit,
    onNavigateToSignUp: () -> Unit,
    onNavigateToEmailVerification: () -> Unit,
) {
    val snackbarHostState = remember { SnackbarHostState() }

    val uiState = loginViewModel.uiState.collectAsStateWithLifecycle().value
    val event = loginViewModel::onEvent

    LaunchedEffect(loginViewModel.effect) {
        loginViewModel.effect.collect {
            when (it) {
                LoginEffect.NavigateToHome -> onNavigateToHome()
                LoginEffect.NavigateToSignUp -> onNavigateToSignUp()
                is LoginEffect.ShowError -> {
                    snackbarHostState.showSnackbar(
                        message = it.error.message().asStringSuspend(),
                        duration = SnackbarDuration.Long
                    )
                }

                is LoginEffect.ShowSuccess -> {
                    snackbarHostState.showSnackbar(
                        message = it.message,
                        duration = SnackbarDuration.Long
                    )
                }

                LoginEffect.NavigateToEmailVerification -> onNavigateToEmailVerification()
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { innerPadding ->
        LoginScreen(
            modifier = Modifier.padding(innerPadding),
            isLoading = uiState.isLoading,
            email = uiState.email,
            emailError = uiState.emailError,
            password = uiState.password,
            passwordError = uiState.passwordError,
            event = event,
        )
    }
}

@Composable
internal fun LoginScreen(
    modifier: Modifier = Modifier,
    isLoading: Boolean,
    email: String,
    password: String,
    event: (LoginEvent) -> Unit,
    emailError: UiText?,
    passwordError: UiText?,
) {

    Column(
        modifier = modifier.padding(Spacing.Medium)
            .imePadding(),
        verticalArrangement = Arrangement.spacedBy(Spacing.Medium)
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(Res.string.app_name),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleLarge
        )

        Column {
            Text(text = "Log In", style = MaterialTheme.typography.titleLarge)
            Text(
                text = stringResource(Res.string.login_subtitle),
                style = MaterialTheme.typography.bodyMedium
            )
        }

        CustomTextField(
            value = email,
            onValueChange = {
                event(LoginEvent.OnEmailChanged(it))
            },
            labelText = stringResource(Res.string.email),
            isError = emailError != null,
            errorText = emailError?.asString() ?: "",
            singleLine = true
        )

        CustomTextField(
            value = password,
            onValueChange = {
                event(LoginEvent.OnPasswordChanged(it))
            },
            labelText = stringResource(Res.string.password),
            visualTransformation = PasswordVisualTransformation(),
            isError = passwordError != null,
            errorText = passwordError?.asString() ?: "",
            singleLine = true
        )

        PrimaryButton(
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading,
            isLoading = isLoading,
            text = stringResource(Res.string.log_in_action),
            onClick = {
                event(LoginEvent.OnLoginClicked)
            }
        )

        Text(modifier = Modifier.fillMaxWidth(), text = "or", textAlign = TextAlign.Center)

        SecondaryButton(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(Res.string.continue_with_google),
            onClick = {}
        )

        SecondaryButton(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(Res.string.continue_with_apple),
            onClick = {}
        )

        Text(
            text = buildAnnotatedString {
                append(stringResource(Res.string.no_account_prompt))
                withStyle(
                    style = SpanStyle(
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                ) {
                    append(stringResource(Res.string.sign_up_action))
                }
            },
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = modifier
                .fillMaxWidth()
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) {
                    event(LoginEvent.OnSignUpClicked)
                }
                .padding(8.dp)
        )
    }

}

@Preview(showBackground = true)
@Composable
internal fun LoginScreenPreview() {
    LoginScreen(
        isLoading = true,
        email = "",
        password = "",
        event = {},
        emailError = null,
        passwordError = null,
    )
}