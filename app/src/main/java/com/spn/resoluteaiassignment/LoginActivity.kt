package com.spn.resoluteaiassignment

import android.content.Intent
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.text.Layout
import android.text.Spannable
import android.text.SpannableString
import android.text.style.AlignmentSpan
import android.util.Patterns
import android.view.MotionEvent
import android.widget.*
import androidx.core.content.res.ResourcesCompat
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
    lateinit var registerYourself: TextView
    lateinit var loginEmail: EditText
    lateinit var loginPassword: EditText
    lateinit var login: Button
    lateinit var auth: FirebaseAuth
    lateinit var showPassword: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        supportActionBar?.title = "Login"

        registerYourself = findViewById(R.id.registerYourself)
        loginEmail = findViewById(R.id.login_email)
        loginPassword = findViewById(R.id.login_password)
        login = findViewById(R.id.login_button)
        auth = FirebaseAuth.getInstance()
        val showPassword: Drawable = resources.getDrawable(R.drawable.show_password, null)
        val hidePassword: Drawable = resources.getDrawable(R.drawable.hide_password, null)


        if(auth.currentUser != null){
            startActivity(Intent(this@LoginActivity, ScanQRCodeActivity::class.java))
            finish()
        }

        loginPassword.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                val touchableAreaEnd = loginPassword.right - loginPassword.compoundDrawablesRelative[2].bounds.width()

                // Check if the touch event occurred on the "Show Password" icon
                if (event.rawX >= touchableAreaEnd) {
                    togglePasswordVisibility(loginPassword, showPassword, hidePassword)
                    return@setOnTouchListener true
                }
            }
            false
        }

        login.setOnClickListener {
            loginUser()
        }

        registerYourself.setOnClickListener{
            startActivity(Intent(this@LoginActivity, SignUpActivity::class.java))
            finish()
        }
    }



    fun togglePasswordVisibility(passwordEditText: EditText, showPasswordIcon: Drawable, hidePasswordIcon: Drawable) {
        if (passwordEditText.inputType == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
            // Hide password
            passwordEditText.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            passwordEditText.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, showPasswordIcon, null)
        } else {
            // Show password
            passwordEditText.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            passwordEditText.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, hidePasswordIcon, null)
        }

        passwordEditText.typeface = ResourcesCompat.getFont(this, R.font.proxima_nova_regular)
        passwordEditText.setSelection(passwordEditText.text.length)
    }

    private fun loginUser() {
        auth.signInWithEmailAndPassword(
            loginEmail.text.toString(),
            loginPassword.text.toString()
        )
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser

                    startActivity(Intent(this@LoginActivity, ScanQRCodeActivity::class.java))
                    finish()

                } else {
                    Toast.makeText(
                        baseContext, "Authentication failed. Try Again",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }
}