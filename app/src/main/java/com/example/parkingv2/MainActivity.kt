package com.example.parkingv2

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuth.AuthStateListener
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    private var btnChangePassword: Button? = null
    private var btnRemoveUser: Button? = null
    private var changePassword: Button? = null
    private var remove: Button? = null
    private var signOut: Button? = null
    private var email: TextView? = null
    private var oldEmail: EditText? = null
    private var password: EditText? = null
    private var newPassword: EditText? = null
    private var progressBar: ProgressBar? = null
    private var auth: FirebaseAuth? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //get firebase auth instance
        auth = FirebaseAuth.getInstance()
        email = this.findViewById(R.id.useremail)

        //get current user
        val user = FirebaseAuth.getInstance().currentUser
        setDataToView(user)
        authListener = AuthStateListener { firebaseAuth ->
            val user = firebaseAuth.currentUser
            if (user == null) {
                // user auth state is changed - user is null
                // launch login activity
                startActivity(Intent(this@MainActivity, LoginActivity::class.java))
                finish()
            }
        }
        btnChangePassword = findViewById<Button>(R.id.change_password_button)
        btnRemoveUser = findViewById<Button>(R.id.remove_user_button)
        changePassword = findViewById<Button>(R.id.changePass)
        remove = findViewById<Button>(R.id.remove)
        signOut = findViewById<Button>(R.id.sign_out)
        oldEmail = findViewById<EditText>(R.id.old_email)
        password = findViewById<EditText>(R.id.password)
        newPassword = findViewById<EditText>(R.id.newPassword)
        oldEmail!!.visibility = View.GONE
        password!!.visibility = View.GONE
        newPassword!!.visibility = View.GONE
        changePassword!!.visibility = View.GONE
        remove!!.visibility = View.GONE
        progressBar = findViewById<ProgressBar>(R.id.progressBar)
        if (progressBar != null) {
            progressBar!!.visibility = View.GONE
        }
        btnChangePassword!!.setOnClickListener {
            oldEmail!!.visibility = View.GONE
            password!!.visibility = View.GONE
            newPassword!!.visibility = View.VISIBLE
            changePassword!!.visibility = View.VISIBLE
            remove!!.visibility = View.GONE
        }
        changePassword!!.setOnClickListener {
            progressBar!!.visibility = View.VISIBLE
            if (user != null && newPassword!!.text.toString().trim { it <= ' ' } != "") {
                if (newPassword!!.text.toString().trim { it <= ' ' }.length < 6) {
                    newPassword!!.error = "Password too short, enter minimum 6 characters"
                    progressBar!!.visibility = View.GONE
                } else {
                    user.updatePassword(newPassword!!.text.toString().trim { it <= ' ' })
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Toast.makeText(
                                    this@MainActivity,
                                    "Password is updated, sign in with new password!",
                                    Toast.LENGTH_SHORT
                                ).show()
                                signOut()
                                progressBar!!.visibility = View.GONE
                            } else {
                                Toast.makeText(
                                    this@MainActivity,
                                    "Failed to update password!",
                                    Toast.LENGTH_SHORT
                                ).show()
                                progressBar!!.visibility = View.GONE
                            }
                        }
                }
            } else if (newPassword!!.text.toString().trim { it <= ' ' } == "") {
                newPassword!!.error = "Enter password"
                progressBar!!.visibility = View.GONE
            }
        }
        btnRemoveUser!!.setOnClickListener {
            progressBar!!.visibility = View.VISIBLE
            user?.delete()?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(
                        this@MainActivity,
                        "Your profile is deleted:( Create a account now!",
                        Toast.LENGTH_SHORT
                    ).show()
                    startActivity(
                        Intent(
                            this@MainActivity,
                            SignupActivity::class.java
                        )
                    )
                    finish()
                    progressBar!!.visibility = View.GONE
                } else {
                    Toast.makeText(
                        this@MainActivity,
                        "Failed to delete your account!",
                        Toast.LENGTH_SHORT
                    ).show()
                    progressBar!!.visibility = View.GONE
                }
            }
        }
        signOut!!.setOnClickListener { signOut() }
    }

    @SuppressLint("SetTextI18n")
    private fun setDataToView(user: FirebaseUser?) {
        email!!.text = "User Email: " + user!!.email
    }

    // this listener will be called when there is change in firebase user session
    var authListener: AuthStateListener? =
        AuthStateListener { firebaseAuth ->
            val user = firebaseAuth.currentUser
            if (user == null) {
                // user auth state is changed - user is null
                // launch login activity
                startActivity(Intent(this@MainActivity, LoginActivity::class.java))
                finish()
            } else {
                setDataToView(user)
            }
        }

    //sign out method
    fun signOut() {
        auth!!.signOut()


// this listener will be called when there is change in firebase user session
        val authListener =
            AuthStateListener { firebaseAuth ->
                val user = firebaseAuth.currentUser
                if (user == null) {
                    // user auth state is changed - user is null
                    // launch login activity
                    startActivity(Intent(this@MainActivity, LoginActivity::class.java))
                    finish()
                }
            }
    }

    override fun onResume() {
        super.onResume()
        progressBar!!.visibility = View.GONE
    }

    override fun onStart() {
        super.onStart()
        auth!!.addAuthStateListener(authListener!!)
    }

    override fun onStop() {
        super.onStop()
        if (authListener != null) {
            auth!!.removeAuthStateListener(authListener!!)
        }
    }
}
