package br.com.pontofacil.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.pontofacil.util.SessionManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LoginViewModel(private val sessionManager: SessionManager) : ViewModel() {

    private val _loginState = MutableStateFlow<LoginStatus>(LoginStatus.Idle)
    val loginState: StateFlow<LoginStatus> = _loginState

    fun login(email: String, pass: String, rememberMe: Boolean) {
        if (email.isBlank() || pass.isBlank()) {
            _loginState.value = LoginStatus.Error("E-mail e senha são obrigatórios")
            return
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _loginState.value = LoginStatus.Error("E-mail inválido")
            return
        }

        viewModelScope.launch {
            _loginState.value = LoginStatus.Loading
            
            // Simulação de chamada de rede/autenticação
            delay(1500) 

            // Lógica de validação simples (pode ser expandida para Firebase/API)
            if (pass.length >= 6) {
                if (rememberMe) {
                    sessionManager.saveUserEmail(email)
                }
                sessionManager.setLoggedIn(true)
                _loginState.value = LoginStatus.Success
            } else {
                _loginState.value = LoginStatus.Error("A senha deve ter pelo menos 6 caracteres")
            }
        }
    }

    sealed class LoginStatus {
        object Idle : LoginStatus()
        object Loading : LoginStatus()
        object Success : LoginStatus()
        data class Error(val message: String) : LoginStatus()
    }
}
