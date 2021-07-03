package com.obsuen.mycroppy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth

class RegisterActivity : AppCompatActivity() {
    private lateinit var  auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        val register = findViewById<Button>(R.id.onregisterbutton)
        register.setOnClickListener{
            signup()
        }
        auth = FirebaseAuth.getInstance()

    }
    private fun signup(){
        val email = findViewById<EditText>(R.id.etregisterEmail)
        val pass = findViewById<EditText>(R.id.etNewPassword)
        val nemail = email.text.toString()
        val mypass = pass.text.toString()

        auth.createUserWithEmailAndPassword(nemail,mypass).addOnCompleteListener { task ->
            if(task.isSuccessful){
                Toast.makeText(applicationContext,"Registration complete",Toast.LENGTH_SHORT).show()
            }

        }
    }
}