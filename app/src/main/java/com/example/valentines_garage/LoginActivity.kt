package com.example.valentines_garage

import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import com.google.gson.JsonObject
import com.valentines.connection.APIClient
import com.valentines.connection.APIInterface
import com.valentines.connection.Connection
import com.valentines.connection.State
import com.valentines.connection.models.PostResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        initButtons()
        setLoading(false)
    }

    private fun initButtons() {
        //----   SET LOGIN BUTTON ONCLICK   ----
        val loginButton: Button = findViewById<Button>(R.id.btn_login)
        loginButton.setOnClickListener {
            getUserInput()
        }

        val gotoJobsAdminButton: Button = findViewById<Button>(R.id.btn_goto_jobs)
        gotoJobsAdminButton.setOnClickListener {
            gotoJobsPageAdmin()
        }

        val gotoJobsEmpButton: Button = findViewById<Button>(R.id.btn_goto_jobs_emp)
        gotoJobsEmpButton.setOnClickListener {
            gotoJobsPageEmployee()
        }
    }

    /*
        LOGIN FUNCTION

        Desc: gets username and password values, validates them, and calls
              the login function in the Connection class. It will then start
              a new intent sending user to main page if login was successful
     */
    private fun getUserInput() {
        val username: String = findViewById<EditText>(R.id.edt_login_username).text.toString()
        val password: String = findViewById<EditText>(R.id.edt_login_password).text.toString()

        if (validate(username, password)) {

            var jsonObject = JsonObject()
            jsonObject.addProperty("username", username)
            jsonObject.addProperty("password", password)

            login(jsonObject, username)

            return
        }

        Toast.makeText(this, "username or password empty", Toast.LENGTH_SHORT).show()
    }

    //----   LOGIN   ----
    private fun login(data: JsonObject, username: String) {
        val apiInterface: APIInterface = APIClient.getInstance().create(APIInterface::class.java)
        val call = apiInterface.login(data)

        call.enqueue(object : Callback<PostResponse> {
            override fun onResponse(
                call: Call<PostResponse>,
                response: Response<PostResponse>
            ) {
                var res: PostResponse? = response.body()

                if (res != null) {

                    try {
                        // parse user type
                        val userType: Int = res.getStatus().toInt()

                        // set state
                        val state = State.getInstance()
                        state.setUserType(userType)
                        state.setUsername(username)

                        // start main activity
                        gotoJobsPage()

                    } catch (ex: Exception) {
                        showToast(res.getStatus())
                    }

                } else {
                    showToast("Login failed, try again later")
                }

                setLoading(false)
            }

            override fun onFailure(call: Call<PostResponse>, t: Throwable) {
                setLoading(false)
                showToast("Login failed, try again later" + t.message)
                call.cancel()
            }
        })
    }

    /*
        GO TO JOBS PAGE

        temp function to help dev skip to jobs page for admin
    */
    private fun gotoJobsPageAdmin() {
        State.getInstance().setUserType(1)
        gotoJobsPage()
    }

    private fun gotoJobsPageEmployee() {
        State.getInstance().setUserType(2)
        gotoJobsPage()
    }

    private fun gotoJobsPage() {
        val intent: Intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    //----   VALIDATE USER INPUT   ----
    private fun validate(username: String, password: String): Boolean {
        return username != "" && password != ""
    }

    //----   SET LOADING   ----
    private fun setLoading(loading: Boolean) {
        val prgBar: ProgressBar = findViewById<ProgressBar>(R.id.prg_login)
        prgBar.visibility = if (loading) View.VISIBLE else View.GONE
    }

    //----   SHOW TOAST   ----
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}