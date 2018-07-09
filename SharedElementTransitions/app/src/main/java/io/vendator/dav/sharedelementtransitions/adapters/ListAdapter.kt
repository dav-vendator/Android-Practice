package io.vendator.dav.sharedelementtransitions.adapters

import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.OrientationEventListener
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import io.vendator.dav.sharedelementtransitions.R
import io.vendator.dav.sharedelementtransitions.utilities.ImageUrls
import io.vendator.dav.sharedelementtransitions.utilities.onItemClick
import java.lang.Exception

class ListAdapter(arrayList: ArrayList<String>, listener: onItemClick) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    private val _arrayList : ArrayList<String> = arrayList
    private val _listener : onItemClick = listener

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
            Picasso.get()
                    .load(ImageUrls.urls[position%ImageUrls.size])
                    .into(image,object :  Callback{
                        override fun onSuccess() {
                            Log.e(this@ListAdapter::class.java.simpleName,"Loading success")
                        }

                        override fun onError(e: Exception?) {
                            Log.e(this@ListAdapter::class.java.simpleName,"Loading error $e")
                        }
                    })
            image.transitionName = "image_$position"
            text.transitionName = "text_$position"
            text.text = "Image-$position"
            _view.setOnClickListener {
                _listener.onClick(it)
            }
        }
    }



    class ListViewHolder(view  : View) : RecyclerView.ViewHolder(view){
        val _view : View = view
        val image : ImageView = view.findViewById(R.id.imageView3)
        val text : TextView = view.findViewById(R.id.textView3)
    }

}