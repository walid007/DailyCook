package com.upfunstudio.dailycook

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_add.*
import java.text.SimpleDateFormat
import java.util.*

class AddActivity : AppCompatActivity() {
    private var mAuth: FirebaseAuth? = null
    private var progress: ProgressDialog? = null
    private var mDataRed: DatabaseReference? = null
    private var mStoreRef: StorageReference? = null
    private val READ_IMAGE = 1
    private var photo = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)




        mAuth = FirebaseAuth.getInstance()
        mStoreRef = FirebaseStorage.getInstance().reference
        progress = ProgressDialog(this)


        buReturn.setOnClickListener {
            finish()

        }



        imagerec.setOnClickListener {

            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(Intent.createChooser(intent, "chose gallery"), READ_IMAGE)


        }


        buAdd.setOnClickListener {

            mDataRed = FirebaseDatabase.getInstance().reference.child("recipes")



            try {

                val titleAd = titleAdd.text.toString()
                val desAd = desAdd.text.toString()
                val types = spinnerType.selectedItem.toString()


                // use regular expression latter
                //val mPattern = Pattern.compile("^([1-9][0-9]{0,2})?(\\.[0-9]?)?$")


                if (TextUtils.isEmpty(titleAd)) {

                    titleAdd.error = "Please enter the title"

                } else if (TextUtils.isEmpty(desAd)) {
                    desAdd.error = "Please enter the description of the meal"
                } else {
                    val map = HashMap<String, Any>()
                    map.put("title", titleAdd)
                    map.put("des", desAdd)
                    map.put("photo", photo)
                    map.put("type", types)
                    map.put("userID", mAuth!!.currentUser!!.uid)

                    progress!!.setCanceledOnTouchOutside(false)
                    progress!!.setMessage("Saving...")
                    progress!!.show()

                    mDataRed!!.push().setValue(map).addOnCompleteListener { task ->

                        if (task.isSuccessful) {
                            progress!!.dismiss()
                            finish()


                        } else {
                            Toast.makeText(applicationContext, "Something went wrong!", Toast.LENGTH_SHORT).show()

                        }

                    }


                }

            } catch (ex: Exception) {
            }

        }

/*
This is an android app
we built it by using
Kotlin as handling and
Firebase backend like
Auth,Storage and Realtime database
 */
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (requestCode == READ_IMAGE && resultCode == Activity.RESULT_OK) {
            val uri = data!!.data


            val userID = mAuth!!.currentUser!!.uid
            val currentDate = SimpleDateFormat("yyy-mmm-dd").format(Date())

            progress!!.setCanceledOnTouchOutside(false)
            progress!!.setMessage("Saving...")
            progress!!.show()


            mStoreRef!!.child("images/").child(userID + currentDate).putFile(uri)
                    .addOnCompleteListener {

                        task ->
                        if (task.isSuccessful) {
                            Toast.makeText(applicationContext, "Added photo Successfully",
                                    Toast.LENGTH_SHORT).show()
                            progress!!.dismiss()
                            //imagerec.setImageResource(R.drawable.ic_add_a_photo_added_24dp)
                            photo = task.result.downloadUrl.toString()

                        } else {
                            Toast.makeText(applicationContext, "Something went wrong!", Toast.LENGTH_SHORT).show()

                        }

                    }


        }



        super.onActivityResult(requestCode, resultCode, data)
    }
}
