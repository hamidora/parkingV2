package com.example.parkingv2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_before_login.*

class beforeLogin : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_before_login)
    }

    fun cadreLogin(view: View) {
        var ToPhone = Intent(this,LoginPhone::class.java)
        startActivity(ToPhone)
    }
    fun chefPark(view: View) {
        var ToLogin = Intent(this,LoginActivity::class.java)
        startActivity(ToLogin)
    }
}
