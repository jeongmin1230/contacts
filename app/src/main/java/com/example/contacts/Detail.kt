package com.example.contacts

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_detail.*

class Detail : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        loadDetail()
    }
/* 일반 함수 */
    private fun loadDetail() {
        val name = intent.getStringExtra("name") // 이름 받아오기
        val number = intent.getStringExtra("number") // 번호 받아오기

        loadImage()

        detailName.text = name
        detailNumber.text = number
    }
    private fun loadImage() {
        val storage : FirebaseStorage = FirebaseStorage.getInstance("gs://contacts-857e9.appspot.com")
        val storageReference = storage.reference
        val pathReference = storageReference.child("/profile_${intent.getStringExtra("number")}_${intent.getStringExtra("name")}.png")

        Log.d("jeongmin", "Detail 안 : /profile_${intent.getStringExtra("number")}_${intent.getStringExtra("name")}.png")

        pathReference.downloadUrl.addOnSuccessListener { uri ->
            if(uri == null) {
                detailIv.setImageResource(R.drawable.basic)
            } else {
                Glide.with(detailIv.context)
                    .load(uri)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .centerCrop()
                    .into(detailIv)
            }
            }
        }
    }