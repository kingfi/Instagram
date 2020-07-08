package com.example.instagram

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.parse.ParseUser

class SignUpActivity : AppCompatActivity() {

    private var editUserSignUp: EditText? = null
    private var editPasswordSignUp: EditText? = null
    private var editEmailAddress: EditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        editUserSignUp = findViewById(R.id.editUserSignUp)
        editPasswordSignUp = findViewById(R.id.editPasswordSignUp)
        editEmailAddress = findViewById(R.id.editEmailAddress)
        var buttonSignUp = findViewById<Button>(R.id.buttonSignUp)

        buttonSignUp.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                // Create the ParseUser
                val user = ParseUser()

                // with statement allows access properties of User without needing to repeat
                // i.e. user.username =
                //user.setPassword
                with(user) {
                    username = editUserSignUp?.text.toString()
                    setPassword(editPasswordSignUp?.text.toString())
                    email = editEmailAddress?.text.toString()
                    signUpInBackground { e ->
                        if (e == null) {
                            // Sign up Successful
                            Toast.makeText(applicationContext, "Sign Up Successful!", Toast.LENGTH_SHORT).show()
                            val i = Intent(applicationContext, LoginActivity::class.java)
                            startActivity(i)

                        } else {
                            // Sign up didn't succeed. Look at the ParseException
                            // to figure out what went wrong
                            Toast.makeText(applicationContext, "Sign Up Failed!", Toast.LENGTH_SHORT).show()

                        }
                    }
                }

            }
        })


    }
}