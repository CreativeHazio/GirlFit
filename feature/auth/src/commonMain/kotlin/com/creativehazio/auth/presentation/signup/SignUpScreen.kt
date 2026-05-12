package com.creativehazio.auth.presentation.signup

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import com.creativehazio.designsystem.components.CustomTextField
import com.creativehazio.designsystem.theme.Spacing
import kotlinx.coroutines.flow.Flow


@Composable
fun SignUpScreenRoot(
    paddingValues: PaddingValues = PaddingValues.Zero,
    signUpViewModel: SignUpViewModel,
    onNavigateToHome: () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }

    val uiState = signUpViewModel.uiState.collectAsStateWithLifecycle().value
    val event = signUpViewModel::onEvent

    LaunchedEffect(signUpViewModel.effect) {
        signUpViewModel.effect.collect {
            when(it) {
                SignUpEffect.NavigateToHome -> onNavigateToHome()
                is SignUpEffect.ShowError -> {
                    snackbarHostState.showSnackbar(
                        message = "",
                        duration = SnackbarDuration.Short
                    )
                }
                is SignUpEffect.ShowSuccess -> {
                    snackbarHostState.showSnackbar(
                        message = it.message,
                        duration = SnackbarDuration.Short
                    )
                }
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { innerPadding ->
        SignUpScreen(
            modifier = Modifier.padding(innerPadding),
            name = uiState.name,
            email = uiState.email,
            password = uiState.password,
            event = event,
        )
    }
}

@Composable
internal fun SignUpScreen(
    modifier: Modifier,
    name: String,
    email: String,
    password: String,
    event: (SignUpEvent) -> Unit,
) {

    Column(
        modifier = modifier.padding(Spacing.Medium)
            .imePadding()
    ) {
        CustomTextField(
            value = name,
            onValueChange = {
                event(SignUpEvent.OnNameChanged(it))
            },
            labelText = "Name",
            isError = true,
            errorText = "Name too short",
            singleLine = true
        )

        CustomTextField(
            value = email,
            onValueChange = {
                event(SignUpEvent.OnEmailChanged(it))
            },
            labelText = "Email",
            singleLine = true
        )

        CustomTextField(
            value = password,
            onValueChange = {
                event(SignUpEvent.OnPasswordChanged(it))
            },
            labelText = "Password",
            visualTransformation = PasswordVisualTransformation(),
            singleLine = true
        )
    }

}