package com.app.happy_birthday.helper


import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import com.app.happy_birthday.R
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.GraphRequest
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import org.json.JSONException

abstract class SocialRegisterActivity : BaseActivity() {
    private var callbackManager: CallbackManager? = null
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    lateinit var socialRegisterCallBack : SocialRegisterInterface
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        callbackManager = CallbackManager.Factory.create()
    }


    abstract fun getParentView(): View?

    override fun getTopInsetView(): View? {
        return  getParentView()
    }

    fun onFacebookLogin(OnFacebookSuccess:(model:SocialRegisterData) -> Unit , OnError:(msg:String) -> Unit){
        LoginManager.getInstance().registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(result: LoginResult) {
                val request =
                    GraphRequest.newMeRequest(result.accessToken) { `object`, response ->
                        println("RESPONSE FB LOGIN :::: " + response.toString())
                        try {
                            val data = SocialRegisterData()
                            data.strSocialFirstName = `object`?.getString("first_name") ?: ""
                            data.strSocialLastName = `object`?.getString("last_name") ?: ""
                            if (`object`?.has("email") == true) {
                                data.strSocialEmail = `object`.getString("email") ?: ""
                            }
                            LoginManager.getInstance().logOut()
                            if (data.strSocialEmail.toString().isEmpty()) {
                                OnError(getString(R.string.email_not_found))
                            } else {
                                OnFacebookSuccess(data)
                            }
                        } catch (e: JSONException) {
                            LoginManager.getInstance().logOut()
                        }
                    }
                val parameters = Bundle()
                parameters.putString(
                    "fields",
                    "id,name,email,gender, first_name, last_name, picture"
                )
                request.parameters = parameters
                request.executeAsync()
            }

            override fun onCancel() {
            }

            override fun onError(error: FacebookException) {
                OnError(getString(R.string.error_fb))
            }
        })
        LoginManager.getInstance().logInWithReadPermissions(
            this,
            callbackManager!!,
            listOf("public_profile", "email")
        )
    }

    fun onGoogleLogin(OnGoogleSuccess:(model:SocialRegisterData) -> Unit , OnError:(msg:String) -> Unit){
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
        resultGoogleLoginLauncher.launch(mGoogleSignInClient.signInIntent)
        socialRegisterCallBack = object : SocialRegisterInterface{
            override fun googleLogin(Data: SocialRegisterData) {
                OnGoogleSuccess(Data)
            }

            override fun onError(msg: String) {
                OnError(msg)
            }
        }
    }

    private var resultGoogleLoginLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            handleSignInResult(task)
        }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            if (account != null) {
                val data = SocialRegisterData()
                data.strSocialEmail = account.email ?: ""
                data.strSocialFirstName  = account.givenName ?: ""
                data.strSocialLastName  = account.familyName ?: ""
                mGoogleSignInClient.signOut()
                socialRegisterCallBack.googleLogin(data)
            } else {
                socialRegisterCallBack.onError(getString(R.string.error_google))
            }
        } catch (e: ApiException) {
            socialRegisterCallBack.onError(getString(R.string.error_google))
        }
    }

    override fun onResume() {
        super.onResume()
    }

    interface SocialRegisterInterface {
        fun fbLogin(fbData: SocialRegisterData) {}
        fun googleLogin(Data: SocialRegisterData) {}
        fun onError(msg:String){}
        fun googleLogin(mGoogleSignInClient: GoogleSignInClient) {}
    }

    data class SocialRegisterData(
        var strSocialFirstName: String? = null,
        var strSocialLastName: String? = null,
        var strSocialEmail: String? = null,
        var strSocialRegisterType: String? = null,
        var strSocialImage: String? = null
    )
}

