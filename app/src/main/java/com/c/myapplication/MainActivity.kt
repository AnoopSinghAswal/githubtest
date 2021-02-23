package com.c.myapplication

import android.os.Bundle
import android.view.MenuItem
import androidx.databinding.DataBindingUtil
import com.c.myapplication.databinding.ActivityLoginSignUpBinding


class MainActivity : BaseActivity() {
    private var binding: ActivityLoginSignUpBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login_sign_up)



    }

    override fun onPause() {
        super.onPause()


    }



    override fun onRestart() {
        super.onRestart()

    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.home -> onBackPressed()

        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {

    }
}
