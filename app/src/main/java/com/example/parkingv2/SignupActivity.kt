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

class SignupActivity : AppCompatActivity() {
    private var inputEmail: EditText? = null
    private var inputPassword //hit option + enter if you on mac , for windows hit ctrl + enter
            : EditText? = null
    private var btnSignIn: Button? = null
    private var btnSignUp: Button? = null
    private var btnResetPassword: Button? = null
    private var progressBar: ProgressBar? = null
    private var auth: FirebaseAuth? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance()
        btnSignIn = findViewById(R.id.sign_in_button) as Button
        btnSignUp = findViewById(R.id.sign_up_button) as Button
        inputEmail = findViewById(R.id.email) as EditText
        inputPassword = findViewById(R.id.password) as EditText
        progressBar = findViewById(R.id.progressBar) as ProgressBar
        btnResetPassword = findViewById(R.id.btn_reset_password) as Button
        btnResetPassword!!.setOnClickListener {
            startActivity(
                Intent(
                    this@SignupActivity,
                    ResetPasswordActivity::class.java
                )
            )
        }
        btnSignIn!!.setOnClickListener { finish() }
        btnSignUp!!.setOnClickListener(View.OnClickListener {
            val email = inputEmail!!.text.toString().trim { it <= ' ' }
            val password = inputPassword!!.text.toString().trim { it <= ' ' }
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
            if (password.length < 6) {
                Toast.makeText(
                    applicationContext,
                    "Password too short, enter minimum 6 characters!",
                    Toast.LENGTH_SHORT
                ).show()
                return@OnClickListener
            }
            progressBar!!.visibility = View.VISIBLE
            //create user
            auth!!.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(
                    this@SignupActivity
                ) { task ->
                    Toast.makeText(
                        this@SignupActivity,
                        "createUserWithEmail:onComplete:" + task.isSuccessful,
                        Toast.LENGTH_SHORT
                    ).show()
                    progressBar!!.visibility = View.GONE
                    // If sign in fails, display a message to the user. If sign in succeeds
                    // the auth state listener will be notified and logic to handle the
                    // signed in user can be handled in the listener.
                    if (!task.isSuccessful) {
                        Toast.makeText(
                            this@SignupActivity,
                            "Authentication failed." + task.exception,
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        startActivity(
                            Intent(
                                this@SignupActivity,
                                MainActivity::class.java
                            )
                        )
                        finish()
                    }
                }
        })
    }

    override fun onResume() {
        super.onResume()
        progressBar!!.visibility = View.GONE
    }
}
