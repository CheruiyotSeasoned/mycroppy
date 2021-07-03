package com.obsuen.mycroppy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
    private lateinit var  auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        var log = findViewById<Button>(R.id.onloginbutton)
        log.setOnClickListener(){
            signin()
        }
        auth = FirebaseAuth.getInstance()
    }
    private fun signin(){
        val email = findViewById<EditText>(R.id.etEmail)
        val password = findViewById<EditText>(R.id.etPassword)
        val etemail = email.text.toString()
        val etpass = password.text.toString()
        auth.signInWithEmailAndPassword(etemail,etpass).addOnCompleteListener { task ->
            if(task.isSuccessful){
                val intent= Intent(this,DashboardActivity::class.java)
                startActivity(intent)
                finish()
            }
        }.addOnFailureListener { exception ->
            Toast.makeText(applicationContext,exception.localizedMessage, Toast.LENGTH_LONG).show()
        }
    }


}