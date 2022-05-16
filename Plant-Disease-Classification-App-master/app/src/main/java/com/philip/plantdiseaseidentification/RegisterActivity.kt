package com.philip.plantdiseaseidentification

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.activity_register.editTextTextEmailAddress
import kotlinx.android.synthetic.main.activity_register.editTextTextPassword

class RegisterActivity : AppCompatActivity() {
    private lateinit var databaseRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        databaseRef = FirebaseDatabase.getInstance().getReference("Farmers")

        tv_login.setOnClickListener{
            startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
        }

        button_register.setOnClickListener{
            when{
                TextUtils.isEmpty(firstName.text.toString().trim{it <= ' '}) ->{
                    Toast.makeText(
                        this@RegisterActivity,
                        "Please enter your first name",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                TextUtils.isEmpty(lastName.text.toString().trim{it <= ' '}) ->{
                    Toast.makeText(
                        this@RegisterActivity,
                        "Please enter your last name",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                TextUtils.isEmpty(location.text.toString().trim{it <= ' '}) ->{
                    Toast.makeText(
                        this@RegisterActivity,
                        "Please enter your location",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                TextUtils.isEmpty(editTextTextEmailAddress.text.toString().trim{it <= ' '}) ->{
                    Toast.makeText(
                        this@RegisterActivity,
                        "Please enter your email address",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                TextUtils.isEmpty(editTextTextPassword.text.toString().trim{it <= ' '}) ->{
                Toast.makeText(
                    this@RegisterActivity,
                    "Please enter your password",
                    Toast.LENGTH_SHORT
                ).show()
            }
            else ->{
                val firstName: String = firstName.text.toString().trim{it <= ' '}
                val lastName: String = lastName.text.toString().trim{it <= ' '}
                val location: String = location.text.toString().trim{it <= ' '}
                val email: String = editTextTextEmailAddress.text.toString().trim{it <= ' '}
                val password: String = editTextTextPassword.text.toString().trim{it <= ' '}

                //get firebase user
                val user = FirebaseAuth.getInstance().currentUser

                val farmerData = FarmerModel(user?.uid, firstName, lastName, location, email)
                if (user != null) {
                    databaseRef.child(user.uid).setValue(farmerData).addOnCompleteListener { Toast.makeText(
                        this, "This farmer data has been uploaded to the cloud database.",
                        Toast.LENGTH_LONG).show()
                    }.addOnFailureListener{err -> Toast.makeText(
                        this, "Error ${err.message}", Toast.LENGTH_LONG).show()
                    }
                }

                FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password).addOnCompleteListener(
                    OnCompleteListener<AuthResult> { task ->
                        if (task.isSuccessful){
                            val firebaseUser: FirebaseUser = task.result!!.user!!

                            Toast.makeText(
                                this@RegisterActivity,
                                "You were registered succesfully!",
                                Toast.LENGTH_SHORT
                            ).show()

                            val intent =
                                Intent(this@RegisterActivity, MainActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            intent.putExtra("user_id", firebaseUser.uid)
                            intent.putExtra("email_id", email)
                            startActivity(intent)
                            finish()
                        } else{
                            Toast.makeText(
                                this@RegisterActivity,
                                task.exception!!.message.toString(),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                )
            }
            }
        }
    }
}