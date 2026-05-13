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
import androidx.compose.runtime.*
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarVisuals
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
import com.creativehazio.common.resulthandler.UiText
import com.creativehazio.designsystem.components.CustomTextField
import com.creativehazio.designsystem.components.PrimaryButton
import com.creativehazio.designsystem.components.SecondaryButton
import com.creativehazio.designsystem.theme.Spacing
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow


@Composable
fun SignUpScreenRoot(
    paddingValues: PaddingValues = PaddingValues.Zero,
    signUpViewModel: SignUpViewModel,
    onNavigateToEmailVerification: () -> Unit,
    onNavigateToLogin: () -> Unit,
) {
    val snackbarHostState = remember { SnackbarHostState() }

    val uiState = signUpViewModel.uiState.collectAsStateWithLifecycle().value
    val event = signUpViewModel::onEvent

    LaunchedEffect(signUpViewModel.effect) {
        signUpViewModel.effect.collect {
            when (it) {
                SignUpEffect.NavigateToEmailVerification -> onNavigateToEmailVerification()
                is SignUpEffect.ShowError -> {
                    snackbarHostState.showSnackbar(
                        message = it.error.message().asStringSuspend(),
                        duration = SnackbarDuration.Long
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

    Column(
        modifier = modifier.padding(Spacing.Medium)
            .imePadding(),
        verticalArrangement = Arrangement.spacedBy(Spacing.Medium)
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = "GirlFit",
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleLarge
        )

        Column {
            Text(text = "Sign Up", style = MaterialTheme.typography.titleLarge)
            Text(
                text = "Start your fitness journey today",
                style = MaterialTheme.typography.bodyMedium
            )
        }

        CustomTextField(
            value = name,
            onValueChange = {
                event(SignUpEvent.OnNameChanged(it))
            },
            labelText = "Name",
            isError = nameError != null,
            errorText = nameError?.asString() ?: "",
            singleLine = true
        )

        CustomTextField(
            value = email,
            onValueChange = {
                event(SignUpEvent.OnEmailChanged(it))
            },
            labelText = "Email",
            isError = emailError != null,
            errorText = emailError?.asString() ?: "",
            singleLine = true
        )

        CustomTextField(
            value = password,
            onValueChange = {
                event(SignUpEvent.OnPasswordChanged(it))
            },
            labelText = "Password",
            visualTransformation = PasswordVisualTransformation(),
            isError = passwordError != null,
            errorText = passwordError?.asString() ?: "",
            singleLine = true
        )

        PrimaryButton(
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading,
            isLoading = isLoading,
            text = "Sign Up",
            onClick = {
                event(SignUpEvent.OnSignUpClicked)
            }
        )

        Text(modifier = Modifier.fillMaxWidth(), text = "or", textAlign = TextAlign.Center)

        SecondaryButton(
            modifier = Modifier.fillMaxWidth(),
            text = "Continue with google",
            onClick = {}
        )

        SecondaryButton(
            modifier = Modifier.fillMaxWidth(),
            text = "Continue with apple",
            onClick = {}
        )

        Text(
            text = buildAnnotatedString {
                append("Already have an account? ")
                withStyle(
                    style = SpanStyle(
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                ) {
                    append("Log in")
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