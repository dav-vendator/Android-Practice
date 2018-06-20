
package io.vendator.dav.sharedelementtransitions.fragments

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerdView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup


import io.vendator.dav.sharedelementtransitions.R
import io.vendator.dav.sharedelementtransitions.adapters.ListAdapter
import io.vendator.dav.sharedelementtransitions.decorations.ItemDecoration
import io.vendator.dav.sharedelementtransitions.utilities.onItemClick
import java.util.*
import kotlin.collections.ArrayList


class ImageFragment : Fragment(), onItemClick {
    private var listener: OnFragmentInteractionListener? = null
    private lateinit var inflatedView : View
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        inflatedView = inflater.inflate(R.layout.fragment_image, container, false)
        recyclerView = inflatedView.findViewById(R.id.recycler)
        recyclerView.addItemDecoration(ItemDecoration())
        recyclerView.adapter = ListAdapter(ArrayList(Collections.nCopies(10,"TextView")),this)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        return inflatedView
    }


    fun onGridImagePressed(view: View) {
        listener?.onFragmentInteraction(view)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    override fun onClick(view: View) {
       this.listener!!.onFragmentInteraction(view)
    }

    interface OnFragmentInteractionListener {
        fun onFragmentInteraction(view: View)
    }

}
