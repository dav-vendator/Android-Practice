package io.vendator.dav.sharedelementtransitions.activities

import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import io.vendator.dav.sharedelementtransitions.R
import io.vendator.dav.sharedelementtransitions.fragments.ImageFragment

class FragmentActivity : AppCompatActivity(),ImageFragment.OnFragmentInteractionListener {
    val TAG = FragmentActivity::class.java.simpleName

    override fun onFragmentInteraction(uri: Uri) {
        Log.e(TAG,"Fragment Clicked")
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
