package com.c.myapplication

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.GradientDrawable
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.provider.Settings
import android.text.Layout.JUSTIFICATION_MODE_INTER_WORD
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.*
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ProcessLifecycleOwner
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject

import retrofit2.Call
import retrofit2.Response
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
open class BaseActivity : AppCompatActivity(), LifecycleObserver {
    private var timer: CountDownTimer?= null
    private var connManager: ConnectivityManager? = null
    private var calculatedTime: Long? = null
    private var childJsonArray: JsonArray? = null
    private var progressDialog: Dialog? = null
    private var exit: Boolean = false
    private var isTextVisible: Boolean = false



    companion object{
        var isAppBg = false

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
        overridePendingTransition(0, 0)

    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return super.onTouchEvent(event)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onAppBackgrounded() {
        //App in background
        isAppBg = true
        Log.e("isAppBg",isAppBg.toString())

        timer = object : CountDownTimer(60 * 10 * 1000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                Log.e("prinrtttt", (millisUntilFinished / 1000).toString())
            }

            override fun onFinish() {
               // startActivity(Intent(this@BaseActivity, SplashActivity::class.java))
              //  finish()
            }
        }
        timer!!.start()

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onAppForegrounded() {
        isAppBg = false
        Log.e("isAppBg",isAppBg.toString())
        if (timer!=null) timer!!.cancel()


        // App in foreground
    }

    fun gotoMainActivity(storyFrom: String?) {
     //   val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("storyFrom",storyFrom)
        startActivity(intent)
        finish()
    }




    fun showToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }


    fun loadFragmentWithoutBundleWithoutTransition(fragment: Fragment, container: Int) {
        supportFragmentManager.beginTransaction().replace(container, fragment).addToBackStack(null).commit()
    }


    fun loadFragmentWithBundleWithoutTransition(fragment: Fragment, container: Int, bundle: Bundle) {
        fragment.arguments = bundle
        supportFragmentManager.beginTransaction().replace(container, fragment).addToBackStack(null).commit()
    }


    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            val v = currentFocus
            if (v is AppCompatEditText || v is EditText) {
                val outRect = Rect()
                v.getGlobalVisibleRect(outRect)
                if (!outRect.contains(event.rawX.toInt(), event.rawY.toInt())) {
                    Log.d("focus", "touchevent")
                    v.clearFocus()
                    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(v.windowToken, 0)
                }
            }
        }
        return super.dispatchTouchEvent(event)
    }



    fun animation(animationType: Int): Animation {
        val animation = AnimationUtils.loadAnimation(this, animationType)
        return animation
    }

    fun isTablet(): Boolean {
        return (this.resources.configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE
    }

   /* fun getToken():String{
        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    return@OnCompleteListener
                }

                // Get new Instance ID token
                token  = task.result?.token!!
                Log.e("token", token)


                // Log and toast

            })
        return token!!
    }*/



    fun chnageBoaderRadius(textTV: AppCompatEditText, dimension: Float): GradientDrawable {
        val gradientDrawable: GradientDrawable
        gradientDrawable = textTV.background.mutate() as GradientDrawable
        gradientDrawable.cornerRadius = dimension
        gradientDrawable.invalidateSelf()
        return gradientDrawable
    }

    fun transitionScreen() {

    }


    fun isValidEmail(email: String): Boolean {
        val emailPattern: String =
            "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"
        val pattern = Pattern.compile(emailPattern)
        val matcher = pattern.matcher(email)
        return matcher.matches()
    }





    fun isValidPassword(password: String): Boolean {
        val passwordPattern: String = "((?=.*\\d)(?=.*[A-Z])(?=.*\\W).{8,30})"
        val pattern = Pattern.compile(passwordPattern)
        val matcher = pattern.matcher(password)
        return matcher.matches()
    }

    fun isValidName(name: String): Boolean {
        val namePattern: String = "^[\\p{L} .'-]+$"
        val pattern = Pattern.compile(namePattern)
        val matcher = pattern.matcher(name)
        return matcher.matches()
    }



    fun animation(): Animation {
        val animation = AnimationUtils.loadAnimation(this, android.R.anim.slide_out_right)
        return animation

    }




    @SuppressLint("HardwareIds")
    fun getDeviceId(): String {
        return Settings.Secure.getString(this.contentResolver, Settings.Secure.ANDROID_ID)
    }







    fun startProgressDialog() {
        if (progressDialog != null && !progressDialog!!.isShowing) {
            try {
                progressDialog!!.show()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun stopProgressDialog() {
        if (progressDialog != null && progressDialog!!.isShowing) {
            try {
                progressDialog!!.dismiss()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    @SuppressLint("SimpleDateFormat")
    fun calculateTime(source: String, target: String, finalTime: Date, currentTime: Date): Long {
        val sdf = SimpleDateFormat(source)
        val sdf2 = SimpleDateFormat(target)
        try {
            val finalTime = SimpleDateFormat(target).format(sdf.parse(finalTime.toString()))
            val currentTime = SimpleDateFormat(target).format(sdf.parse(currentTime.toString()))
            calculatedTime = sdf2.parse(finalTime.toString()).time - sdf2.parse(currentTime).time
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return calculatedTime!!
    }








}