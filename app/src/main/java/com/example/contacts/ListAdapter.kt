package com.example.contacts

import android.content.Intent
import android.net.Uri
import android.system.Os.bind
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.list_layout.view.*

class ListAdapter(val itemList: ArrayList<ListLayout>): RecyclerView.Adapter<ListAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_layout, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: ListAdapter.ViewHolder, position: Int) {
        holder.name.text = itemList[position].name

        val item = itemList[position]
        val listener = View.OnClickListener { it ->
            Toast.makeText(it.context, "Clicked -> NAME : ${item.name}, NUMBER : ${item.number}", Toast.LENGTH_SHORT).show()
            val intent = Intent(holder.itemView?.context, Detail::class.java)
            intent.putExtra("name", item.name) // 이름 전달
            intent.putExtra("number", item.number) // 번호 전달
            ContextCompat.startActivity(holder.itemView.context, intent, null)
        }
        holder.apply {
            bind(listener, item)
            itemView.tag=item
        }
//        holder.number.text = itemList[position].number
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private var view: View = itemView
        val name: TextView = itemView.findViewById(R.id.listName)
//        val number: TextView = itemView.findViewById(R.id.list_tv_number)

        fun bind(listener: View.OnClickListener, item: ListLayout) {
            val storage : FirebaseStorage = FirebaseStorage.getInstance("gs://contacts-857e9.appspot.com")
            val storageReference = storage.reference
            val pathReference = storageReference.child("/profile_${item.number}_${item.name}.png")

            pathReference.downloadUrl.addOnSuccessListener { uri ->
                if(uri == null) {
                    view.listIv.setImageResource(R.drawable.basic)
                } else {
                    Glide.with(view.listIv.context)
                        .load(uri)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .centerCrop()
                        .into(view.listIv)
                }
            }
            view.listName.text = item.name
            view.setOnClickListener(listener)
        }
    }
}