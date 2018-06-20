package io.vendator.dav.sharedelementtransitions.fragments

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

import io.vendator.dav.sharedelementtransitions.R

private const val ARG_PARAM = "param"
private const val ARG_PARAM2 = "param2"



class ImageDescriptionFragment : Fragment() {

    private var param: String? = null
    private var param2 : String? = null

    private var listener: OnFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param = it.getString(ARG_PARAM)
            param2 = it.getString(ARG_PARAM2)
       }
        //postponeEnterTransition();
        setSharedElementEnterTransition(TransitionInflater.from(getContext()).inflateTransition(android.R.transition.move))


    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

       val view = inflater.inflate(R.layout.fragment_image_description, container, false)
       view.findViewById<ImageView>(R.id.imageView4).transitionName = param
       view.findViewById<TextView>(R.id.textView4).transitionName = param2
       return view
    }

    // TODO: Rename method, update argument and hook method into UI event
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
