package com.example.contacts

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.activity_edit.*

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
/* onClick 함수 */
    // 전화
    fun onClickDetailCall(view: View) {
        val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel:${intent.getStringExtra("number")}"))
        startActivity(intent)
    }
    // 문자
    fun onClickDetailMessage(view: View) {
        val intent = Intent(Intent.ACTION_SENDTO, Uri.parse("sms:${intent.getStringExtra("number")}"))
        startActivity(intent)
    }
    // 수정
    fun onClickDetailEdit(view: View) {
        val name = intent.getStringExtra("name") // 이름 받아오기
        val number = intent.getStringExtra("number") // 번호 받아오기

        val intent = Intent(this, Edit::class.java)
            intent.putExtra("name", name)
            intent.putExtra("number", number)

        Log.i("jeongmin", "Detail activity 안 : $name")
        Log.i("jeongmin", "Detail activity 안 : $number")
        startActivity(intent)
    }
}