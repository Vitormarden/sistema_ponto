package br.com.pontofacil.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import br.com.pontofacil.MainActivity
import br.com.pontofacil.databinding.ActivityLoginBinding
import br.com.pontofacil.ui.viewmodel.LoginViewModel
import br.com.pontofacil.util.SessionManager
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val sessionManager by lazy { SessionManager(this) }
    
    // Factory simples para injetar o SessionManager no ViewModel
    private val viewModel: LoginViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return LoginViewModel(sessionManager) as T
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Verifica se o usuário já está logado
        if (sessionManager.isLoggedIn()) {
            startMainActivity()
            return
        }

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupListeners()
        observeViewModel()
    }

    private fun setupListeners() {
        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()
            val rememberMe = binding.cbRememberMe.isChecked
            
            viewModel.login(email, password, rememberMe)
        }
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            viewModel.loginState.collectLatest { state ->
                when (state) {
                    is LoginViewModel.LoginStatus.Loading -> {
                        showLoading(true)
                    }
                    is LoginViewModel.LoginStatus.Success -> {
                        showLoading(false)
                        startMainActivity()
                    }
                    is LoginViewModel.LoginStatus.Error -> {
                        showLoading(false)
                        Toast.makeText(this@LoginActivity, state.message, Toast.LENGTH_SHORT).show()
                    }
                    else -> showLoading(false)
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.pbLoading.visibility = if (isLoading) View.VISIBLE else View.GONE
        binding.btnLogin.isEnabled = !isLoading
        binding.etEmail.isEnabled = !isLoading
        binding.etPassword.isEnabled = !isLoading
    }

    private fun startMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}
