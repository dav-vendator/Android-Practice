package io.vendator.dav.sharedelementtransitions.adapters

import android.support.v7.widget.RecyclerView
import android.view.OrientationEventListener
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import io.vendator.dav.sharedelementtransitions.R
import io.vendator.dav.sharedelementtransitions.utilities.onItemClick

class ListAdapter(arrayList: ArrayList<String>, listener: onItemClick) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    val _arrayList : ArrayList<String>
    val _listener : onItemClick
    init{
        _arrayList = arrayList
        _listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
       val view = ViewGroup.inflate(parent.context, R.layout.imagegrid_element,null)
       //TODO : Add code for shared transition here
       return ListViewHolder(view)
    }

    override fun getItemCount(): Int  = _arrayList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        with(holder as ListViewHolder){
            //TODO : holder.image = AsyncRequest
            text.text = _arrayList[position]
            image.transitionName = "image_$position"
            text.transitionName = "text_$position"
            _view.setOnClickListener {
                _listener.onClick(it)
            }
        }
    }



    class ListViewHolder(view  : View) : RecyclerView.ViewHolder(view){
        val _view : View
        val image : ImageView
        val text : TextView
        init{
            _view = view
            image = view.findViewById(R.id.imageView3)
            text = view.findViewById(R.id.textView3)
        }
    }

}