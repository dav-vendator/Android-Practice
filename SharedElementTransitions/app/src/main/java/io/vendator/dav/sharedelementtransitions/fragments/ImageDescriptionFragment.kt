package io.vendator.dav.sharedelementtransitions.fragments

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.graphics.Palette
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import io.vendator.dav.sharedelementtransitions.R
import io.vendator.dav.sharedelementtransitions.utilities.ImageUrls
import java.lang.Exception
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.fragment_image_description.*


private const val ARG_PARAM = "param"
private const val ARG_PARAM2 = "param2"



class ImageDescriptionFragment : Fragment() {

    private var param: String? = null
    private var param2 : String? = null

    private var listener: OnFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.also {
            param = it.getString(ARG_PARAM)
            param2 = it.getString(ARG_PARAM2)
       }
        postponeEnterTransition()
        sharedElementEnterTransition = TransitionInflater.from(context).inflateTransition(android.R.transition.move)
        sharedElementReturnTransition = TransitionInflater.from(context).inflateTransition(android.R.transition.move)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

       val view = inflater.inflate(R.layout.fragment_image_description, container, false)
       val imgView = view.findViewById<ImageView>(R.id.imageView4)
       imgView.transitionName = param
        view.findViewById<TextView>(R.id.textView4).text= "Image-${param2!!.split("_")[1]}"

       view.findViewById<TextView>(R.id.textView4).transitionName = param2
       Picasso.get()
               .load(ImageUrls.urls[imgView.transitionName.split("_")[1].toInt()%ImageUrls.size])
               .into(object : Target{
                   override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
                       startPostponedEnterTransition()
                   }

                   override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {
                       startPostponedEnterTransition()
                   }

                   override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
                      createPaletteAsync(bitmap!!)
                      imgView.setImageBitmap(bitmap)

                      startPostponedEnterTransition()

                   }

               })
       return view
    }

    fun createPaletteAsync(bitmap: Bitmap) {
        Palette.from(bitmap).generate { palette ->
            (activity as AppCompatActivity).supportActionBar!!.setBackgroundDrawable(ColorDrawable(palette.getDominantColor(Color.BLACK)))
            activity!!.window.statusBarColor = palette.getDarkVibrantColor(Color.BLACK)
        }
    }



    fun onButtonPressed(uri: Uri) {
        listener?.onFragmentInteraction(uri)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
//        if (context is OnFragmentInteractionListener) {
//            listener = context
//        } else {
//            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
//        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }


    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onFragmentInteraction(uri: Uri)
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String,param2 : String) =
                ImageDescriptionFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM, param1)
                        putString(ARG_PARAM2,param2)

                    }
                }
    }
}
