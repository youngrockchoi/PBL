/**
 * LoginActivity:
 */

package com.example.androidsnsproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.androidsnsproject.databinding.ActivityLoginBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {
    lateinit var binding: ActivityLoginBinding // binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // btnLogin: Login 수행
        binding.btnLogin.setOnClickListener {
            // editEmail: 이메일 입력 텍스트 창
            val userEmail = binding.editEmail.text.toString()
            // editPwd: 비밀번호 입력 텍스트 창
            val password = binding.editPwd.text.toString()
            doLogin(userEmail, password)
        }

        // btnGoSignup: 회원가입 화면으로 넘어감
        binding.btnGoSignup.setOnClickListener {
            startActivity(
                Intent(this, SignupActivity::class.java)
            )
        }
    }

    private fun doLogin(userEmail: String, password: String) {
        Firebase.auth.signInWithEmailAndPassword(userEmail, password)
            .addOnCompleteListener(this) { // it: Task<AuthResult!>
                if (it.isSuccessful) {
                    startActivity(
                        Intent(this, MainActivity::class.java)
                    )
                    finish()
                } else {
                    Log.w("LoginActivity", "signInWithEmail", it.exception)
                    Toast.makeText(this, "Authentication failed.", Toast.LENGTH_SHORT).show()
                }
            }
    }
}