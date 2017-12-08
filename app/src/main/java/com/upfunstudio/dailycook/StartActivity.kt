package com.upfunstudio.dailycook

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_start.*

class StartActivity : AppCompatActivity() {

    private var mAuth: FirebaseAuth? = null
    private var progress: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)
        mAuth = FirebaseAuth.getInstance()
        progress = ProgressDialog(this)

        buCreate.setOnClickListener {

            startActivity(Intent(this, CreateActivity::class.java))
        }

        buLogin.setOnClickListener {


            try{

                val email = emailLogin.text.toString()
                val pass = passwordLogin.text.toString()


                // use regular expression latter
                //val mPattern = Pattern.compile("^([1-9][0-9]{0,2})?(\\.[0-9]?)?$")


                if (TextUtils.isEmpty(email)) {

                emailLogin.error="Please enter the email "


            }else if(TextUtils.isEmpty(pass)){
                passwordLogin.error="Please enter the  password!!!"
            }
            else {
                progress!!.setCanceledOnTouchOutside(false)
                progress!!.setMessage("Login...")
                progress!!.show()


                mAuth!!.signInWithEmailAndPassword(email, pass)
                        .addOnCompleteListener {

                            task ->
                            if (task.isSuccessful) {

                                progress!!.dismiss()
                                startActivity(Intent(this,MainActivity::class.java))



                            } else {
                                progress!!.hide()
                                Toast.makeText(applicationContext, "Something went wrong!", Toast.LENGTH_SHORT).show()

                            }

                        }


            }
            }catch (ex:Exception){}

        }


    }
}
