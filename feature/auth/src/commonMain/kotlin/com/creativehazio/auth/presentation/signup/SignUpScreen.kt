package com.creativehazio.auth.presentation.signup

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.creativehazio.common.resulthandler.UiText
import com.creativehazio.designsystem.components.GirlFitTextField
import com.creativehazio.designsystem.components.GirlFitPrimaryButton
import com.creativehazio.designsystem.components.GirlFitSecondaryButton
import com.creativehazio.designsystem.theme.Spacing
import girlfit.feature.auth.generated.resources.Res
import girlfit.feature.auth.generated.resources.app_name
import girlfit.feature.auth.generated.resources.apple_logo
import girlfit.feature.auth.generated.resources.continue_with_apple
import girlfit.feature.auth.generated.resources.continue_with_google
import girlfit.feature.auth.generated.resources.email
import girlfit.feature.auth.generated.resources.google_logo
import girlfit.feature.auth.generated.resources.has_account_prompt
import girlfit.feature.auth.generated.resources.log_in_action
import girlfit.feature.auth.generated.resources.name
import girlfit.feature.auth.generated.resources.or_divider
import girlfit.feature.auth.generated.resources.password
import girlfit.feature.auth.generated.resources.sign_up_action
import girlfit.feature.auth.generated.resources.sign_up_subtitle
import girlfit.feature.auth.generated.resources.sign_up_title
import org.jetbrains.compose.resources.stringResource


@Composable
fun SignUpScreenRoot(
    paddingValues: PaddingValues = PaddingValues.Zero,
    signUpViewModel: SignUpViewModel,
    onNavigateToEmailVerification: (String) -> Unit,
    onNavigateToLogin: () -> Unit,
) {
    val snackbarHostState = remember { SnackbarHostState() }

    val uiState = signUpViewModel.uiState.collectAsStateWithLifecycle().value
    val event = signUpViewModel::onEvent

    LaunchedEffect(signUpViewModel.effect) {
        signUpViewModel.effect.collect {
            when (it) {
                is SignUpEffect.NavigateToEmailVerification -> onNavigateToEmailVerification(it.email)
                is SignUpEffect.ShowError -> {
                    snackbarHostState.showSnackbar(
                        message = it.error.message().asStringSuspend(),
                        duration = SnackbarDuration.Short
                    )
                }

                SignUpEffect.NavigateToLogin -> onNavigateToLogin()
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { innerPadding ->
        SignUpScreen(
            modifier = Modifier.padding(innerPadding),
            name = uiState.name,
            nameError = uiState.nameError,
            email = uiState.email,
            emailError = uiState.emailError,
            password = uiState.password,
            passwordError = uiState.passwordError,
            event = event,
            isLoading = uiState.isLoading
        )
    }
}

@Composable
internal fun SignUpScreen(
    modifier: Modifier = Modifier,
    name: String,
    email: String,
    password: String,
    isLoading: Boolean,
    event: (SignUpEvent) -> Unit,
    nameError: UiText?,
    emailError: UiText?,
    passwordError: UiText?,
) {

    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    Column(
        modifier = modifier.padding(Spacing.Medium)
            .imePadding()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = {
                    keyboardController?.hide()
                }
            ),
        verticalArrangement = Arrangement.spacedBy(Spacing.Medium)
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(Res.string.app_name),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleLarge
        )

        Column {
            Text(
                text = stringResource(Res.string.sign_up_title),
                style = MaterialTheme.typography.titleLarge
            )
            Text(
                text = stringResource(Res.string.sign_up_subtitle),
                style = MaterialTheme.typography.bodyMedium
            )
        }

        GirlFitTextField(
            value = name,
            onValueChange = {
                event(SignUpEvent.OnNameChanged(it))
            },
            labelText = stringResource(Res.string.name),
            isError = nameError != null,
            errorText = nameError?.asString() ?: "",
            singleLine = true
        )

        GirlFitTextField(
            value = email,
            onValueChange = {
                event(SignUpEvent.OnEmailChanged(it))
            },
            labelText = stringResource(Res.string.email),
            isError = emailError != null,
            errorText = emailError?.asString() ?: "",
            singleLine = true
        )

        GirlFitTextField(
            value = password,
            onValueChange = {
                event(SignUpEvent.OnPasswordChanged(it))
            },
            labelText = stringResource(Res.string.password),
            visualTransformation = PasswordVisualTransformation(),
            isError = passwordError != null,
            errorText = passwordError?.asString() ?: "",
            singleLine = true
        )

        GirlFitPrimaryButton(
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading,
            isLoading = isLoading,
            text = stringResource(Res.string.sign_up_action),
            onClick = {
                focusManager.clearFocus()
                event(SignUpEvent.OnSignUpClicked)
            }
        )

        Text(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(Res.string.or_divider),
            textAlign = TextAlign.Center
        )

        GirlFitSecondaryButton(
            modifier = Modifier.fillMaxWidth(),
            leadingIcon = Res.drawable.google_logo,
            text = stringResource(Res.string.continue_with_google),
            onClick = {}
        )

        GirlFitSecondaryButton(
            modifier = Modifier.fillMaxWidth(),
            leadingIcon = Res.drawable.apple_logo,
            text = stringResource(Res.string.continue_with_apple),
            onClick = {}
        )

        Text(
            text = buildAnnotatedString {
                append(stringResource(Res.string.has_account_prompt))
                withStyle(
                    style = SpanStyle(
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                ) {
                    append(stringResource(Res.string.log_in_action))
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
                    event(SignUpEvent.OnLoginClicked)
                }
                .padding(8.dp)
        )
    }

}

@Preview(showBackground = true)
@Composable
internal fun SignUpScreenPreview() {
    SignUpScreen(
        name = "",
        email = "",
        password = "",
        event = {},
        nameError = null,
        emailError = null,
        passwordError = null,
        isLoading = false
    )
}