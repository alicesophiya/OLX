package com.sofi.olxapplication.ui.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.sofi.olxapplication.BaseAcitivity
import com.sofi.olxapplication.MainActivity
import com.sofi.olxapplication.R
import com.sofi.olxapplication.utilities.Constants
import com.sofi.olxapplication.utilities.SharedPref
import kotlinx.android.synthetic.main.activity_login.*
import java.util.*


class LoginActivity : BaseAcitivity(), View.OnClickListener {
    private var callbackManager: CallbackManager? = null
    private lateinit var auth: FirebaseAuth
    private val RC_SIGN_IN = 100
    private val TAG = LoginActivity::class.java.simpleName
    private var googleSignInOptions: GoogleSignInOptions? = null
    private var googleSignInClient: GoogleSignInClient? = null
    private val EMAIL = "email"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        FacebookSdk.sdkInitialize(getApplicationContext())
        auth = FirebaseAuth.getInstance()
        callbackManager = CallbackManager.Factory.create()
        login_button.setReadPermissions(Arrays.asList(EMAIL))

        clickListeners()
        configureGoogleSignin()
        registerFbCallBack()
    }

    private fun clickListeners() {
        ll_fb.setOnClickListener(this)
        ll_google.setOnClickListener(this)
    }

    override fun onClick(v: View?) {

        when (v?.id) {
            R.id.ll_fb -> {
                login_button.performClick()
            }

            R.id.ll_google -> {
                googleSignIn()

            }
        }
    }

    private fun googleSignIn() {

        val signInIntent = googleSignInClient?.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    private fun configureGoogleSignin() {

        googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions!!)
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                firebaseAuthWithGoogle(account!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e)
                // ...
            }
        } else {
            callbackManager?.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun firebaseAuthWithGoogle(account: GoogleSignInAccount) {
        val credentials = GoogleAuthProvider.getCredential(account.idToken, null)
        auth.signInWithCredential(credentials).addOnCompleteListener {
            if (it.isSuccessful) {
                if(account.email!=null)
                    SharedPref(this).setString(Constants.USER_EMAIL,account.email!!)
                if(account.id!=null)
                    SharedPref(this).setString(Constants.USER_ID,account.id!!)
                if(account.displayName!=null)
                    SharedPref(this).setString(Constants.USER_NAME,account.displayName!!)
                 if(account.photoUrl!=null)
                    SharedPref(this).setString(Constants.USER_PHOTO,account.photoUrl.toString()!!)
                startActivity(Intent(this,MainActivity::class.java))
                finish()

            } else {
                Toast.makeText(this, "Google signin Failed :", Toast.LENGTH_LONG).show()
            }
        }
    }


    private fun registerFbCallBack() {
        LoginManager.getInstance().registerCallback(callbackManager,
            object : FacebookCallback<LoginResult?> {
                override fun onSuccess(result: LoginResult?) {
                    handleFacebookAccess(result?.accessToken)

                }

                override fun onCancel() {

                }

                override fun onError(error: FacebookException?) {

                }

            })
    }

    private fun handleFacebookAccess(accessToken: AccessToken?) {
        val credential = FacebookAuthProvider.getCredential(accessToken.toString())
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")
                    val account = auth.currentUser
                    SharedPref(this).setString(Constants.USER_ID,account?.uid!!)
                    if(account.email!=null)
                        SharedPref(this).setString(Constants.USER_EMAIL,account.email!!)
                    if(account.displayName!=null)
                        SharedPref(this).setString(Constants.USER_NAME,account.displayName!!)
                    if(account.photoUrl!=null)
                        SharedPref(this).setString(Constants.USER_PHOTO,account.photoUrl.toString()!!)
                    startActivity(Intent(this,MainActivity::class.java))
                    finish()

                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    Toast.makeText(
                        baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT
                    ).show()

                }

            }
    }
}


