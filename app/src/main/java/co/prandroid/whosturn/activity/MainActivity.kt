package co.prandroid.whosturn.activity

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.design.widget.Snackbar
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import co.prandroid.whosturn.R
import co.prandroid.whosturn.util.Utils
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    override fun onClick(p0: View?) {
       /* if (v.id == R.id.btnSignIn) {
            singIn(etEmail!!.getText().toString().trim(), etPassword!!.getText().toString().trim())
        } else if (v.id == R.id.btnCreateAccount) {
            Log.e("TAG","createAccount")
            createNewAccount(etEmail!!.getText().toString().trim(), etPassword!!.getText().toString().trim())
        } else if (v.id == R.id.btnSignOut) {
            signOut()
        }*/
    }


    private var mAuth: FirebaseAuth? = null

    private var firebaseAuthListener: FirebaseAuth.AuthStateListener? = null

    var mProgressDialog: ProgressDialog? = null

    var textViewUE:TextView?=null

    //var useremail:String= null!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.nav_header_main)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)
        mAuth = FirebaseAuth.getInstance()

       // useremail=intent.getStringExtra(Utils.USER_EMAIL)
        updateUI(mAuth)
    }

    fun  updateUI(mAuth: FirebaseAuth?) {
        val navHeaderView = nav_view.getHeaderView(0)
        val textViewUserName = navHeaderView.findViewById<TextView>(R.id.textViewUserName)
        val textViewUserEmail = navHeaderView.findViewById<TextView>(R.id.textViewUserEmail)
        textViewUserName.text="Hello Turner"
        textViewUserEmail.text=intent.getStringExtra(Utils.USER_EMAIL).toString()
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }


    fun singOutSucessfully() {
          //  val firebaseUser = this.mAuth!!.currentUser!!
            Toast.makeText(this@MainActivity, "Logout Sucessuflly", Toast.LENGTH_SHORT).show()
            var intent= Intent(this, LoginActivity::class.java)
            startActivity(intent)
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_settings -> return true
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_home -> {
                // Handle the camera action
            }
            R.id.nav_rate -> {

            }
            R.id.nav_support -> {

            }
            R.id.nav_logout ->{
                logOut()
            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    fun logOut(){
        val alert = AlertDialog.Builder(this)

        // Builder
        with (alert) {
            setTitle("Are you sure want to logout?")
            setPositiveButton("OK") {
                dialog, whichButton ->
                dialog.dismiss()
                mAuth!!.signOut()
                singOutSucessfully()
            }

            setNegativeButton("NO") {
                dialog, whichButton ->
                dialog.dismiss()
            }
        }

        // Dialog
        val dialog = alert.create()
        dialog.show()
    }
}
