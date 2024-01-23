package com.example.kotlinpractice.models

import android.content.Context
import android.os.Build.MODEL
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlinpractice.R
import android.util.Log
import com.nostra13.universalimageloader.core.ImageLoader

class MyAdapter<T>(
    private var items: List<T>,
    private val onItemClicked: (position: Int) -> Unit,
    private val onDataUpdated: () -> Unit
) : RecyclerView.Adapter<MyAdapter.MyViewHolder>() {

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): MyViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            //.inflate(R.layout.list_item_simple, viewGroup, false)
            .inflate(R.layout.list_item_card, viewGroup, false)
        return MyViewHolder(view, onItemClicked)
    }

    override fun onBindViewHolder(viewHolder: MyViewHolder, position: Int) {
        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.textView.text = items[position].toString()

        var friend = items[position].toString()

        if(friend.contains("pictureURL")) {
            Log.d("Adapter", friend)
            // Check if pictureURL is not null
            // Set visibility to visible and load image using your preferred image-loading library
            //            viewHolder.imageView.visibility = View.VISIBLE
            // Load image using Universal Image Loader (this is just an example, adjust it based on your library)
            //ImageLoader.getInstance().displayImage(friend.pictureURL, viewHolder.imageView)


        }
        // Set text


    }
    fun updateData(newItems: List<T>) {
        items = newItems
        notifyDataSetChanged()
        onDataUpdated.invoke()

    }

    class MyViewHolder2(itemView: View, private val onItemClicked: (position: Int) -> Unit) :
        RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val textView: TextView = itemView.findViewById(R.id.textview_list_item)

        init {
            itemView.setOnClickListener(this)

        }

        override fun onClick(view: View) {
            val position = bindingAdapterPosition
            onItemClicked(position)
        }
    }
    class MyViewHolder(
        itemView: View,
        private val onItemClicked: (position: Int) -> Unit
    ) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        val textView: TextView = itemView.findViewById(R.id.textview_list_item)
        val imageView: ImageView = itemView.findViewById(R.id.imageView)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(view: View) {
            val position = absoluteAdapterPosition
            onItemClicked(position)
        }

        fun bindData(item: Friend) {
            if (!item.pictureUrl.isNullOrBlank()) {
                ImageLoader.getInstance().displayImage(item.pictureUrl, imageView)
            } else {
                imageView.setImageResource(R.drawable.card_background)
            }
        }
    }


    /*class MyViewHolder(
        itemView: View,
        private val onItemClicked: (position: Int) -> Unit
    ) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        val textView: TextView = itemView.findViewById(R.id.textview_list_item)
        val imageView: ImageView = itemView.findViewById(R.id.imageView)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(view: View) {
            val position = bindingAdapterPosition
            onItemClicked(position)
        }


        fun bindData(item: Friend) {
            if (!item.pictureUrl.isNullOrBlank()) {
                ImageLoader.getInstance().displayImage(item.pictureUrl, imageView)
            } else {
                imageView.setImageResource(R.drawable.card_background)


            }
        }
    }*/

}