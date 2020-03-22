package com.example.parkingv2

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
    private var inputEmail: EditText? = null
    private var inputPassword: EditText? = null
    private var auth: FirebaseAuth? = null
    private var progressBar: ProgressBar? = null
    private var btnSignup: Button? = null
    private var btnLogin: Button? = null
    private var btnReset: Button? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance()
        if (auth!!.currentUser != null) {
            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
            finish()
        }
        setContentView(R.layout.activity_login)
        inputEmail = findViewById(R.id.email) as EditText
        inputPassword = findViewById(R.id.password) as EditText
        progressBar = findViewById(R.id.progressBar) as ProgressBar
        btnSignup = findViewById(R.id.btn_signup) as Button
        btnLogin = findViewById(R.id.btn_login) as Button
        btnReset = findViewById(R.id.btn_reset_password) as Button

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance()
        btnSignup!!.setOnClickListener {
            startActivity(
                Intent(
                    this@LoginActivity,
                    SignupActivity::class.java
                )
            )
        }
        btnReset!!.setOnClickListener {
            startActivity(
                Intent(
                    this@LoginActivity,
                    ResetPasswordActivity::class.java
                )
            )
        }
        btnLogin!!.setOnClickListener(View.OnClickListener {
            val email = inputEmail!!.text.toString()
            val password = inputPassword!!.text.toString()
            if (TextUtils.isEmpty(email)) {
                Toast.makeText(
                    applicationContext,
                    "Enter email address!",
                    Toast.LENGTH_SHORT
                ).show()
                return@OnClickListener
            }
            if (TextUtils.isEmpty(password)) {
                Toast.makeText(applicationContext, "Enter password!", Toast.LENGTH_SHORT)
                    .show()
                return@OnClickListener
            }
            progressBar!!.visibility = View.VISIBLE

            //authenticate user
            auth!!.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(
                    this@LoginActivity
                ) { task ->
                    // If sign in fails, display a message to the user. If sign in succeeds
                    // the auth state listener will be notified and logic to handle the
                    // signed in user can be handled in the listener.
                    progressBar!!.visibility = View.GONE
                    if (!task.isSuccessful) {
                        // there was an error
                        if (password.length < 6) {
                            inputPassword!!.error = getString(R.string.minimum_password)
                        } else {
                            Toast.makeText(
                                this@LoginActivity,
                                getString(R.string.auth_failed),
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    } else {
                        val intent =
                            Intent(this@LoginActivity, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                }
        })
    }
}