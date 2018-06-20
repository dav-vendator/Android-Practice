package io.vendator.dav.sharedelementtransitions.activities

import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import io.vendator.dav.sharedelementtransitions.R
import io.vendator.dav.sharedelementtransitions.fragments.ImageDescriptionFragment
import io.vendator.dav.sharedelementtransitions.fragments.ImageFragment

class FragmentActivity : AppCompatActivity(),ImageFragment.OnFragmentInteractionListener {
    val TAG = FragmentActivity::class.java.simpleName

    override fun onFragmentInteraction(view: View) {
        val transitionNameOne =  view.findViewById<ImageView>(R.id.imageView3)
                                                .transitionName
        val transitionNameTwo = view.findViewById<TextView>(R.id.textView3)
                                                .transitionName
        supportFragmentManager
                .beginTransaction()
                .addSharedElement(view.findViewById(R.id.imageView3),transitionNameOne)
                .addSharedElement(view.findViewById(R.id.textView3),transitionNameTwo)
                .setReorderingAllowed(true)
                .addToBackStack(ImageDescriptionFragment::class.java.simpleName)
                .replace(R.id.fragment_stage,ImageDescriptionFragment.newInstance(transitionNameOne,transitionNameTwo))
                .commit()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fragment)

        supportFragmentManager
                .beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.fragment_stage,ImageFragment())
                .commit()
    }
}
