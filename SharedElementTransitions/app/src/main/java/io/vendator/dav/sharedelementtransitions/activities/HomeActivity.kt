package io.vendator.dav.sharedelementtransitions.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.support.v4.app.ActivityOptionsCompat
import android.content.Intent
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import io.vendator.dav.sharedelementtransitions.R
import kotlinx.android.synthetic.main.activity_home.*


class HomeActivity : AppCompatActivity() {
    private val TAG = this::class.java.simpleName
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        window.exitTransition = null
        Picasso.get()
               .load("https://images.unsplash.com/photo-1475738384599-8cf3db232ffa?ixlib=rb-0.3.5&ixid=eyJhcHBfaWQiOjEyMDd9&s=1c8aa50c5dd4cdf4b25b49ccca05e36a&auto=format&fit=crop&w=1015&q=80")
                .into(imageView)
    }

    fun startAnimating(view : View){
          Log.e(TAG,"Clicked")
        val imageView = findViewById<ImageView>(R.id.imageView)
        val textView = findViewById<TextView>(R.id.textView2)
        val first  = android.support.v4.util.Pair<View,String>(imageView,getString(R.string.transition_name))
        val second = android.support.v4.util.Pair<View,String>(textView,getString(R.string.transition_text))
        val intent = Intent(this@HomeActivity, SecondActivity::class.java)
        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this@HomeActivity,
                first,second)
        startActivity(intent, options.toBundle())
    }
}
