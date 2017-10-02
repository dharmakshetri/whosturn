package co.prandroid.whosturn.activity

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.*
import co.prandroid.whosturn.BaseActivity
import co.prandroid.whosturn.R
import co.prandroid.whosturn.util.Utils
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

/**
 * Created by dharmakshetri on 9/26/17.
 */
class LoginActivity : BaseActivity(),  View.OnClickListener {
    val TAG = "FIREBASE AUTH"
    // private Button btnLogin;
    private var etEmail: EditText? = null
    var etPassword: EditText? = null
    private var tvStatus: TextView? = null
    var tvDetails: TextView? = null

    private var mAuth: FirebaseAuth? = null

    private var firebaseAuthListener: FirebaseAuth.AuthStateListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)
        //Views

        etEmail = findViewById<EditText>(R.id.et_email)
        etPassword = findViewById<EditText>(R.id.et_password)


        //Buttons
        findViewById<Button>(R.id.btnSignIn).setOnClickListener(this)
        findViewById<Button>(R.id.btnCreateAccount).setOnClickListener(this)


        mAuth = FirebaseAuth.getInstance()

        firebaseAuthListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            val firebaseUser = firebaseAuth.currentUser
            if (firebaseUser != null) {
                Log.e("TAG", " onAuthStateChange: singed_in" + firebaseUser.uid)
                signInSucessfully()
            } else {
                Log.e("TAG", " onAuthStateChange: singed_out")

            }
            //updateUI(firebaseUser)
        }
    }
    override fun onStart() {
        super.onStart()
        mAuth!!.addAuthStateListener(firebaseAuthListener!!)
    }

    override fun onStop() {
        super.onStop()
        if (mAuth != null) {
            mAuth!!.addAuthStateListener(firebaseAuthListener!!)
        }
    }

    override fun onClick(v: View) {
        if (v.id == R.id.btnSignIn) {
            singIn(etEmail!!.getText().toString().trim(), etPassword!!.getText().toString().trim())
        } else if (v.id == R.id.btnCreateAccount) {
            Log.e("TAG","createAccount")
            createNewAccount(etEmail!!.getText().toString().trim(), etPassword!!.getText().toString().trim())
        }
    }


    //update UIs
    fun updateUI(user: FirebaseUser?) {

        hideProgressDialog()
        if (user != null) {

            tvStatus!!.text= getString(R.string.emailpassword_status_fmt, user.email)
            tvDetails!!.text=getString(R.string.firebase_status_fmt, user.uid)

            findViewById<LinearLayout>(R.id.email_password_buttons).visibility = View.GONE
            findViewById<LinearLayout>(R.id.email_password_fields).visibility = View.GONE

        } else {
            tvStatus!!.text="Sign Out"
            tvDetails!!.text=(null)

            findViewById<LinearLayout>(R.id.email_password_buttons).visibility = View.VISIBLE
            findViewById<LinearLayout>(R.id.email_password_fields).visibility = View.VISIBLE

        }
    }



    // for signin

    fun singIn(email: String, password: String) {
        if (!validateForm()) {
            return
        }

        showProgressDialog()
        mAuth!!.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (!task.isSuccessful) {
                        Toast.makeText(this@LoginActivity, "User Login Failed", Toast.LENGTH_SHORT).show()
                    }else{
                        signInSucessfully()
                    }
                    hideProgressDialog()
                }

    }

    fun signInSucessfully() {
        val firebaseUser = this.mAuth!!.currentUser!!
        Toast.makeText(this@LoginActivity, "Sign In Sucessuflly", Toast.LENGTH_SHORT).show()
        var intent=Intent(this, MainActivity::class.java)
        intent.putExtra(Utils.USER_TOKEN,firebaseUser.uid)
        intent.putExtra(Utils.USER_EMAIL,firebaseUser.email)
        startActivity(intent)
    }

    // create new account
    fun createNewAccount(email: String, password: String) {
        Log.e("TAG","createAccount"+"email: ${email} and password: ${password}" )

        if (!validateForm()) {
            return
        }
        showProgressDialog()

        this.mAuth!!.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task: Task<AuthResult> ->
            if (task.isSuccessful) {
                //Registration OK
                signInSucessfully()

            } else {
                //Registration error
                Log.e("TAG"," ss user: ")
                Toast.makeText(this@LoginActivity, "Something error on username and password Or You are already registered.", Toast.LENGTH_SHORT).show()
            }
            hideProgressDialog()
        }
    }

    //validation forms
    fun validateForm(): Boolean {
        var valid = true

        val email = etEmail!!.getText().toString().trim()
        val password = etPassword!!.getText().toString().trim()


        if (TextUtils.isEmpty(email)) {
            etEmail!!.error= ("Required, Email field")
            valid = false
        } else {
            etEmail!!.error=(null)
        }

        if (TextUtils.isEmpty(password)) {
            etPassword!!.error=("Requried, Password Field")
            valid = false
        } else {
            etPassword!!.error=(null)
        }
        Log.e("TAG","valid"+valid )
        return valid
    }


}