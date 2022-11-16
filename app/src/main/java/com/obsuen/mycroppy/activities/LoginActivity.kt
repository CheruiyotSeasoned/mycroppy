package com.obsuen.mycroppy.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.obsuen.mycroppy.R
import com.obsuen.mycroppy.RegisterActivity
import com.obsuen.mycroppy.helpers.PrefManager

class LoginActivity : AppCompatActivity() {
    private lateinit var  auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.newlogin)
        var log = findViewById<Button>(R.id.onloginbutton)
        var register = findViewById<TextView>(R.id.register)
        register.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
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
                val intent= Intent(this, NewDashBoard::class.java)
                saveLoginDetails(etemail,etpass)
                startActivity(intent)
                finish()
            }
        }.addOnFailureListener { exception ->
            Toast.makeText(applicationContext,exception.localizedMessage, Toast.LENGTH_LONG).show()
        }
    }

    private fun saveLoginDetails(etemail: String, etpass: String) {
         PrefManager(this)
             .saveLoginDetails(etemail, etpass);

    }


}