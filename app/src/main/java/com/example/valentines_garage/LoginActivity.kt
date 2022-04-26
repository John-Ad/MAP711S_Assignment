package com.example.valentines_garage

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class LoginActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        initButtons()
    }

    private fun initButtons() {
        //----   SET LOGIN BUTTON ONCLICK   ----
        val loginButton: Button = findViewById<Button>(R.id.btn_login)
        loginButton.setOnClickListener {
            login()
        }
    }

    /*
        LOGIN FUNCTION

        Desc: gets username and password values, validates them, and calls
              the login function in the Connection class. It will then start
              a new intent sending user to main page if login was successful
     */
    private fun login() {
        val username: String = findViewById<EditText>(R.id.edt_login_username).text.toString()
        val password: String = findViewById<EditText>(R.id.edt_login_password).text.toString()

        if (validate(username, password)) {
            if (Connection.login(username, password)) {
                Toast.makeText(this, "Successful login!", Toast.LENGTH_SHORT).show()

                val intent: Intent = Intent(this, JobsActivity::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Failed to login!", Toast.LENGTH_SHORT).show()
            }
            return
        }

        Toast.makeText(this, "username or password empty", Toast.LENGTH_SHORT).show()
    }

    private fun validate(username: String, password: String): Boolean {
        return username != "" && password != ""
    }
}