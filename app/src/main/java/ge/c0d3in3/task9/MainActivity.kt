package ge.c0d3in3.task9

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import java.util.regex.Pattern

class MainActivity : AppCompatActivity() {

    private lateinit var emailEt: EditText
    private lateinit var passwordEt: EditText
    private lateinit var confirmPasswordEt: EditText
    private lateinit var firebaseAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        emailEt = findViewById(R.id.emailEt)
        passwordEt = findViewById(R.id.passwordEt)
        confirmPasswordEt = findViewById(R.id.confirmPasswordEt)
        firebaseAuth = FirebaseAuth.getInstance()

        findViewById<Button>(R.id.submitBtn).setOnClickListener {
            val email = emailEt.text.toString()
            val password = passwordEt.text.toString()
            val confirmPassword = confirmPasswordEt.text.toString()
            submit(email, password, confirmPassword)
        }
    }

    private fun submit(
        email: String,
        password: String,
        confirmPassword: String
    ) {
        if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) return toast("fields are empty")
        if(password != confirmPassword) return toast("passwords aren't same")
        if(!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) return toast("email isn't valid")
        if(!isValidPasswordFormat(password)) return toast("password isn't valid")
        register(email, password)
    }

    private fun register(email: String, password: String){
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
            it.addOnSuccessListener {
                toast("you have signed up!")
            }
            it.addOnFailureListener { e ->
                toast(e.message ?: "there was failure while sign in")
            }
        }
    }

    private fun toast(text:String) {
        Toast.makeText(
            baseContext,
            text,
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun isValidPasswordFormat(password: String): Boolean {
        val passwordREGEX = Pattern.compile("^" +
                "(?=.*[0-9])" +         //at least 1 digit
                "(?=.*[@#$%^&+=])" +    //at least 1 special character
                ".{8,}" +               //at least 8 characters
                "$");
        return passwordREGEX.matcher(password).matches()
    }
}