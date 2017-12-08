package com.upfunstudio.dailycook

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.google.android.gms.ads.AdRequest
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.ads.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.recipe.view.*


class MainActivity : AppCompatActivity() {

    private var mAuth: FirebaseAuth? = null
    private var arrayList: ArrayList<User>? = null
    private var adapter: AdapterClass? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        mAuth = FirebaseAuth.getInstance()
        adapter = AdapterClass()
        arrayList = ArrayList()
        loadDataOnline()
        loadDataOffLine()


    }

    override fun onStart() {
        super.onStart()
        checkUser()

        // add the ad
        val adRequest = AdRequest.Builder().addTestDevice("testing").build()
        adView.loadAd(adRequest)


    }

    fun loadDataOffLine() {


        arrayList!!.add(User("", "", "", "", ""))
        arrayList!!.add(User("", "", "", "", ""))
        arrayList!!.add(User("", "", "", "", ""))
        arrayList!!.add(User("", "", "", "", ""))
        arrayList!!.add(User("", "", "", "", ""))
        arrayList!!.add(User("", "", "", "", ""))
        arrayList!!.add(User("", "", "", "", ""))
        arrayList!!.add(User("", "", "", "", ""))

        recycleView.adapter = adapter
        adapter!!.notifyDataSetChanged()

    }

    fun loadDataOnline() {

        val add = FirebaseDatabase.getInstance().reference.child("recipes")
        add.keepSynced(true)



        add.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError?) {


            }

            override fun onDataChange(p0: DataSnapshot?) {

                try {
                    arrayList!!.clear()
                    val items = p0!!.value as HashMap<String, Any>

                    for (key in items.keys) {

                        val values = items[key] as HashMap<String, Any>

                        arrayList!!.add(User(
                                values["des"] as String,
                                values["photo"] as String,
                                values["title"] as String,
                                values["type"] as String,
                                values["userID"] as String))


                    }

                    recycleView.adapter = adapter
                    adapter!!.notifyDataSetChanged()


                } catch (ex: Exception) {
                }

            }


        })


    }

    inner class AdapterClass : BaseAdapter() {

        override fun getCount(): Int {

            return arrayList!!.size
        }

        override fun getItem(position: Int): Any {
            return arrayList!![position]

        }

        override fun getItemId(position: Int): Long {

            return position.toLong()
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {


            val view = layoutInflater.inflate(R.layout.recipe, parent, false)
            val items = arrayList!![position]


            view.titleRec.text = items.title
            Picasso.with(applicationContext).load(items.photo)
                    .into(view.photoRecipe)

            // handle username
            val add = FirebaseDatabase.getInstance().reference.child("Users")
                    .child(items.userID)
            add.keepSynced(true)
            add.addValueEventListener(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError?) {


                }

                override fun onDataChange(p0: DataSnapshot?) {

                    try {


                        val items = p0!!.value as HashMap<String, Any>

                        for (key in items.keys) {

                            val values = items[key] as String

                            if (values == "username") {
                                view.username.append(values)
                            }


                        }


                    } catch (ex: Exception) {
                    }

                }


            })




            view.setOnClickListener {

                val intent = Intent(applicationContext, DesActivity::class.java)
                intent.putExtra("photo", items.photo)
                intent.putExtra("title", items!!.title)
                intent.putExtra("des", items!!.des)

                startActivity(intent)


            }

            return view

        }


    }

    fun checkUser() {

        if (mAuth!!.currentUser == null) {

            startActivity(Intent(this, StartActivity::class.java))
            finish()
        }


    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)

        // todo : handle search


        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {
            R.id.action_settings -> {
                startActivity(Intent(applicationContext, ProfilActivity::class.java))
                return true
            }
            R.id.logout -> {
                mAuth!!.signOut()
                checkUser()
                return true
            }
            R.id.add -> {
                val intent = Intent(applicationContext, AddActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                return true


            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    class User {

        var des: String? = null
        var photo: String? = null
        var title: String? = null
        var type: String? = null
        var userID: String? = null

        constructor(des: String,
                    photo: String,
                    title: String,
                    type: String,
                    userID: String
        ) {

            this.des = des
            this.photo = photo
            this.title = title
            this.type = type
            this.userID = userID


        }


    }
}
