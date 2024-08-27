package com.example.movieapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class RegisterActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        val emailEditText: EditText = findViewById(R.id.emailEditText)
        val passwordEditText: EditText = findViewById(R.id.passwordEditText)
        val nameEditText: EditText = findViewById(R.id.nameEditText)
        val registerButton: Button = findViewById(R.id.registerButton)

        registerButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()
            val name = nameEditText.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty() && name.isNotEmpty()) {
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            val user = auth.currentUser
                            user?.let {
                                val userId = it.uid
                                val userMap = hashMapOf(
                                    "userId" to userId,
                                    "name" to name,
                                    "email" to email
                                )
                                db.collection("users").document(userId).set(userMap)
                                    .addOnSuccessListener {
                                        Toast.makeText(this, "Kayıt başarılı", Toast.LENGTH_SHORT).show()
                                        startActivity(Intent(this, MainActivity::class.java))
                                        finish()
                                    }
                                    .addOnFailureListener { e ->
                                        Toast.makeText(this, "Kayıt başarısız: ${e.message}", Toast.LENGTH_SHORT).show()
                                    }
                            }
                        } else {
                            Toast.makeText(this, "Kayıt başarısız: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
            } else {
                Toast.makeText(this, "Lütfen tüm alanları doldurun", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
