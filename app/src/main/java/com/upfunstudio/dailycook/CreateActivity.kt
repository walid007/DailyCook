package com.upfunstudio.dailycook

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_create.*

class CreateActivity : AppCompatActivity() {

    private var mAuth: FirebaseAuth? = null
    private var progress: ProgressDialog? = null
    private var mDataRed: DatabaseReference? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create)
        mAuth = FirebaseAuth.getInstance()
        progress = ProgressDialog(this)



        buSignUp.setOnClickListener {
            val username = usernameSignUp.text.toString()
            val email = emailSignUp.text.toString()
            val pass = passwordSignup.text.toString()

            mDataRed = FirebaseDatabase.getInstance().reference.child("Users")


            try {

                // use regular expression latter
                //val mPattern = Pattern.compile("^([1-9][0-9]{0,2})?(\\.[0-9]?)?$")


                if (TextUtils.isEmpty(username)) {

                    usernameSignUp.error = "Please enter the Username"

                } else if (TextUtils.isEmpty(email)) {
                    emailSignUp.error = "Please enter the Email"


                } else if (TextUtils.isEmpty(pass)) {

                    passwordSignup.error = "Please enter Password"

                } else {
                    progress!!.setMessage("Create...")
                    progress!!.setCanceledOnTouchOutside(false)
                    progress!!.show()


                    mAuth!!.createUserWithEmailAndPassword(email, pass)
                            .addOnCompleteListener {

                                task ->
                                if (task.isSuccessful) {


                                    mDataRed!!.child(mAuth!!.currentUser!!.uid)
                                            .child("username")
                                            .setValue(username).addOnCompleteListener {

                                        task ->
                                        if (task.isSuccessful) {


                                            progress!!.dismiss()
                                            startActivity(Intent(applicationContext
                                                    , MainActivity::class.java))

                                        } else {
                                            Toast.makeText(applicationContext, "Something went wrong!", Toast.LENGTH_SHORT).show()

                                        }

                                    }


                                } else {
                                    progress!!.hide()
                                    Toast.makeText(applicationContext, "Something went wrong!", Toast.LENGTH_SHORT).show()
                                }

                            }


                }
            } catch (ex: Exception) {
            }


        }


    }
}
