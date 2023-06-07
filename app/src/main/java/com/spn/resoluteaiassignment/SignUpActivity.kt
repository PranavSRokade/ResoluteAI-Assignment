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
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import com.google.firebase.auth.FirebaseAuth

class SignUpActivity : AppCompatActivity() {
    lateinit var alreadyHaveAccount: TextView
    lateinit var signUpEmail: EditText
    lateinit var signUpPassword: EditText
    lateinit var phoneNumber: EditText
    lateinit var register: Button
    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        supportActionBar?.title = "Register"

        alreadyHaveAccount = findViewById(R.id.login_existing_account)
        signUpEmail = findViewById(R.id.signup_email)
        signUpPassword = findViewById(R.id.signup_password)
        phoneNumber = findViewById(R.id.signup_phone)
        register = findViewById(R.id.register_button)
        auth = FirebaseAuth.getInstance()
        val showPassword: Drawable = resources.getDrawable(R.drawable.show_password, null)
        val hidePassword: Drawable = resources.getDrawable(R.drawable.hide_password, null)

        register.setOnClickListener {
            signUpUser()
        }

        signUpPassword.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                val touchableAreaEnd = signUpPassword.right - signUpPassword.compoundDrawablesRelative[2].bounds.width()

                // Check if the touch event occurred on the "Show Password" icon
                if (event.rawX >= touchableAreaEnd) {
                    togglePasswordVisibility(signUpPassword, showPassword, hidePassword)
                    return@setOnTouchListener true
                }
            }
            false
        }

        alreadyHaveAccount.setOnClickListener {
            startActivity(Intent(this@SignUpActivity, LoginActivity::class.java))
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
    
    fun signUpUser() {
        if (!Patterns.EMAIL_ADDRESS.matcher(signUpEmail.text.toString()).matches()) {
            Toast.makeText(
                applicationContext,
                "Please enter valid Email to continue",
                Toast.LENGTH_SHORT
            ).show()
        } else if (signUpEmail.text.toString().isEmpty()) {
            Toast.makeText(
                applicationContext,
                "Please enter your Email to continue",
                Toast.LENGTH_SHORT
            ).show()
        } else if (signUpPassword.text.toString().isEmpty()) {
            Toast.makeText(
                applicationContext,
                "Please enter your Password to continue",
                Toast.LENGTH_SHORT
            ).show()
        } else if (phoneNumber.text.toString().isEmpty()) {
            Toast.makeText(
                applicationContext,
                "Please enter your Phone Number to continue",
                Toast.LENGTH_SHORT
            ).show()
        }

        auth.createUserWithEmailAndPassword(
            signUpEmail.text.toString(),
            signUpPassword.text.toString()
        )
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(baseContext, "Registration Successful", Toast.LENGTH_SHORT)
                        .show()

                    auth.signInWithEmailAndPassword(
                        signUpEmail.text.toString(),
                        signUpPassword.text.toString()
                    )
                        .addOnCompleteListener(this) { task ->
                            if (task.isSuccessful) {
                                val user = auth.currentUser

                                startActivity(
                                    Intent(
                                        this@SignUpActivity,
                                        ScanQRCodeActivity::class.java
                                    )
                                )
                                finish()

                            } else {
                                Toast.makeText(
                                    baseContext, "Authentication failed. Try Again",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                } else {
                    Toast.makeText(
                        baseContext,
                        "Authentication failed. Try Again",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }
}