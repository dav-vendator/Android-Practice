package vendator.dav.com.androidxtension

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
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
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {
    private val PICK_IMAGE: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        button.setOnClickListener{
            ed.text.let{
                if (!TextUtils.isEmpty(it)){
                    loadImage(TextUtils.stringOrSpannedString(it))
                }else{
                    when(initPermissions()){
                       true ->  {
                           val intent = Intent()
                           intent.type = "image/*"
                           intent.action = Intent.ACTION_GET_CONTENT
                           startActivityForResult(Intent.createChooser(intent,
                                   "Select Picture"), PICK_IMAGE)
                       }
                       false -> {
                           Snackbar.make(findViewById(android.R.id.content),
                           "Empty url field",Snackbar.LENGTH_LONG).show()
                       }
                    }

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



    private fun initPermissions() : Boolean{
        var result = false
        Dexter.withActivity(this)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(object: PermissionListener {
                    override fun onPermissionGranted(response: PermissionGrantedResponse?) {
                       result = true
                    }

                    override fun onPermissionRationaleShouldBeShown(permission: PermissionRequest?, token: PermissionToken?) {
                         token!!.continuePermissionRequest()
                         Snackbar.make(findViewById<View>(android.R.id.content),
                                   "Permission is needed to read image",
                                    Snackbar.LENGTH_LONG
                                 ).show()
                    }

                    override fun onPermissionDenied(response: PermissionDeniedResponse?) {
                        Snackbar.make(findViewById<View>(android.R.id.content),
                                "Permission is needed to read image",
                                Snackbar.LENGTH_LONG
                        ).show()
                    }
                }).check()
        return result
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK){
            if (data != null){
              val inputStream = applicationContext.contentResolver.openInputStream(data.getData());
              val bitmap = BitmapFactory.decodeStream(inputStream)
              background.setImageBitmap(bitmap)
              createPaletteAsync(bitmap)

            }
        }
    }
}
