

package com.upfunstudio.dailycook

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_profil.*
import java.text.SimpleDateFormat
import java.util.*

class ProfilActivity : AppCompatActivity() {
    private var mAuth: FirebaseAuth? = null
    private var progress: ProgressDialog? = null
    private var mDataRed: DatabaseReference? = null
    private var mStoreRef: StorageReference? = null
    private val READ_IMAGE = 1
    private var photo = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profil)




        mAuth = FirebaseAuth.getInstance()
        mStoreRef = FirebaseStorage.getInstance().reference
        progress = ProgressDialog(this)
        mDataRed = FirebaseDatabase.getInstance().reference.child("Users")
        // to work offline
        mDataRed!!.keepSynced(true)

        mDataRed!!.child(mAuth!!.currentUser!!.uid)
                .addValueEventListener(object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError?) {

                    }

                    override fun onDataChange(dataSnapshot: DataSnapshot?) {

                        val username = dataSnapshot!!.child("username").value.toString()
                        val image = dataSnapshot!!.child("photoProfile").value.toString()

                        usernameDefault.text = username
                        Picasso.with(applicationContext).load(image)
                                .into(imageProfile)


                    }


                })


        buReturn.setOnClickListener {
            finish()

        }




        imageProfile.setOnClickListener {

            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(Intent.createChooser(intent, "chose gallery"), READ_IMAGE)


        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (requestCode == READ_IMAGE && resultCode == Activity.RESULT_OK) {
            val uri = data!!.data


            val userID = mAuth!!.currentUser!!.uid
            val currentDate = SimpleDateFormat("yyy-mmm-dd").format(Date())

            progress!!.setCanceledOnTouchOutside(false)
            progress!!.setMessage("Saving...")
            progress!!.show()


            mStoreRef!!.child("imagesProfile/").child(userID + currentDate).putFile(uri)
                    .addOnCompleteListener {

                        task ->
                        if (task.isSuccessful) {
                            Toast.makeText(applicationContext, "Added photo Successfully",
                                    Toast.LENGTH_SHORT).show()

                            photo = task.result.downloadUrl.toString()
                            mDataRed!!.child(mAuth!!.currentUser!!.uid)
                                    .child("photoProfile")
                                    .setValue(photo).addOnCompleteListener {

                                task ->
                                if (task.isSuccessful) {

                                    //imageProfile.setImageURI(uri)
                                    progress!!.dismiss()

                                } else {
                                    Toast.makeText(applicationContext, "Something went wrong!", Toast.LENGTH_SHORT).show()

                                }


                            }
                        }
                    }


        }



        super.onActivityResult(requestCode, resultCode, data)
    }

}
