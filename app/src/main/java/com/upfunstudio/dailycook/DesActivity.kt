package com.upfunstudio.dailycook

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_des.*


class DesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_des)

        try {
            val photo = intent.extras.getString("photo")
            val title = intent.extras.getString("title")
            val des = intent.extras.getString("des")
            Picasso.with(applicationContext).load(photo)
                    .into(imageDesActivity)



            titleActivityDes.text = title
            desActivityDes.text = des


        } catch (ex: Exception) {
        }


        // todo : get the photo , title and get des from database
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {

        menuInflater.inflate(R.menu.menu_des, menu)


        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {
            R.id.share -> {


                // todo : share


                return true
            }
            R.id.like -> {
                // todo : add to fav
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}
