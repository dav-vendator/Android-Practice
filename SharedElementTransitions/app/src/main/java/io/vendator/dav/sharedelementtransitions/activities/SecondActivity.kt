package io.vendator.dav.sharedelementtransitions.activities

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import kotlinx.android.synthetic.main.activity_second.*
import android.view.View
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import io.vendator.dav.sharedelementtransitions.R
import io.vendator.dav.sharedelementtransitions.utilities.CircleTransform
import java.lang.Exception


class SecondActivity : AppCompatActivity() {
    val TAG = SecondActivity::class.java.simpleName
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)
        window.enterTransition = null
        postponeEnterTransition()
        Picasso.get()
               .load("https://images.unsplash.com/photo-1475738384599-8cf3db232ffa?ixlib=rb-0.3.5&ixid=eyJhcHBfaWQiOjEyMDd9&s=1c8aa50c5dd4cdf4b25b49ccca05e36a&auto=format&fit=crop&w=1015&q=80")
                .transform(CircleTransform())
                . into(imageView2,object:Callback{
                    override fun onSuccess() {
                        startPostponedEnterTransition()
                    }

                    override fun onError(e: Exception?) {
                        startPostponedEnterTransition()
                    }

                })
    }

    override fun onEnterAnimationComplete() {
        super.onEnterAnimationComplete()

        val duration = 400L
        val scaleWidth = ObjectAnimator.ofFloat(button,"scaleX",0F,1F)
        val scaleHeight = ObjectAnimator.ofFloat(button,"scaleY",0F,1F)
        val anim = AnimatorSet()
        if(button.scaleX == 0F) {
            Handler().postDelayed({
                anim.play(scaleHeight)
                        .with(scaleWidth)
                anim.duration = duration
                anim.start()
            }, 300)
        }
    }


    fun onLaunchFragment(view : View){
        startActivity(Intent(this@SecondActivity, FragmentActivity::class.java))
    }

    override fun onBackPressed() {
        val duration = 400L
        val scaleWidth = ObjectAnimator.ofFloat(button,"scaleX",1F,0F)
        val scaleHeight = ObjectAnimator.ofFloat(button,"scaleY",1F,0F)
        val anim = AnimatorSet()
        if(button.scaleX == 1F) {
            anim.play(scaleHeight)
                    .with(scaleWidth)
            anim.duration = duration
            anim.start()
        }
        Handler().postDelayed({
            super.onBackPressed()
        },500)
    }
}
