package vendator.dav.com.androidxtension

import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.graphics.Palette
import android.text.TextUtils
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        button.setOnClickListener{
            ed.text.let{
                if (!TextUtils.isEmpty(it)){
                    loadImage(TextUtils.stringOrSpannedString(it))
                }else{
                   Snackbar.make(findViewById(android.R.id.content),
                           "Empty url field",Snackbar.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun loadImage(stringOrSpannedString: CharSequence?) {
        Glide.with(applicationContext)
                .load(Uri.parse(stringOrSpannedString.toString()))
                .into(object : SimpleTarget<Drawable>() {
                    override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                        createPaletteAsync((resource as BitmapDrawable).bitmap)
                        background.setImageDrawable(resource)
                    }
                })
    }


    fun createPaletteAsync(bitmap: Bitmap) {
        Palette.from(bitmap).generate { palette ->
            supportActionBar!!.setBackgroundDrawable(ColorDrawable(palette.getDominantColor(Color.BLACK)))
            window.statusBarColor = palette.getDarkVibrantColor(Color.BLACK)
        }
    }
}
