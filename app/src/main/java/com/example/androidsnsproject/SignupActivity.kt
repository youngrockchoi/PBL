package com.example.androidsnsproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.androidsnsproject.databinding.ActivitySignupBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SignupActivity : AppCompatActivity() {
    lateinit var binding: ActivitySignupBinding // binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // btnSignup: 회원가입 수행
        binding.btnSignup.setOnClickListener {
            // editInputEmail: 이메일 입력 텍스트 창
            val userEmail = binding.editInputEmail.text.toString()
            // editInputPwd: 비밀번호 입력 텍스트 창
            val password = binding.editInputPwd.text.toString()

            // user의 정보를 따로 저장할 DB 필요
            // editInputName: 이름 입력 텍스트 창
            // editInputBirth: 생일 입력 텍스트 창

            // 각 입력에 대해 형식 검사 필요

            doSignIn(userEmail, password)
        }
    }

    // 회원가입 함수
    private fun doSignIn(userEmail: String, password: String) {
        // 사용자 생성
        Firebase.auth.createUserWithEmailAndPassword(userEmail, password)
            .addOnCompleteListener {
                if(it.isSuccessful) {
//                    println("sign-up success")
//                    println(Firebase.auth.currentUser?.uid)
                    startActivity(
                        Intent(this, MainActivity::class.java)
                    )
                    finish()
                } else {
//                    println("sign-up failed ${it.exception?.message}")
                    Log.w("SignupActivity", "signUpWithEmail", it.exception)
                    Toast.makeText(this, "Authentication failed.", Toast.LENGTH_SHORT).show()
                }
            }
    }
}