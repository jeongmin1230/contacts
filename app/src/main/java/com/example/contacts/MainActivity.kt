package com.example.contacts

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.setPadding
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.list_layout.*

class MainActivity : AppCompatActivity() {

    private val TAG : String = "jeongmin"

    private val db = FirebaseFirestore.getInstance()    // Firestore 인스턴스 선언
    private val itemList = arrayListOf<ListLayout>()    // 리스트 아이템 배열
    private val adapter = ListAdapter(itemList)         // 리사이클러 뷰 어댑터

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        checkPermission()
        load()

        rvList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rvList.adapter = adapter

    }

/* --------------------기능함수-------------------- */

    // 엡이 시작되자마자 FireStore database 의 데이터를 불러오기 위한 함수
    private fun load() {
        db.collection("contacts") // 작업할 컬렉션
            .get() // 문서 가져오기
            .addOnSuccessListener { result ->
                // 성공할 경우
                itemList.clear()
                for(document in result) { // 가져온 문서들은 result 에 들어감
                    val item = ListLayout(document["name"] as String, document["number"] as String)
                    itemList.add(item)
                }
                adapter.notifyDataSetChanged() // 리사이클러 뷰 갱신
            }
            .addOnFailureListener { exception ->
                // 실패할 경우
                Log.d(TAG, "Error getting documents : $exception")
            }
    }
    // permission 체크 하는 함수
    private fun checkPermission() {
        val status = ContextCompat.checkSelfPermission(this, "android.permission.READ_CONTACTS")
        if(status == PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "permission granted")
        } else {
            requestPermission()
            Log.d(TAG, "permission denied")
        }
    }
    // permission 요청하는 함수
    private fun requestPermission() {
        ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_CONTACTS), 99)
    }


/* --------------------onClick 함수-------------------- */

    fun onClickRegister(view: View) {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }

    fun onClickCall(view: View) {

        Log.d(TAG, "onClick Call ImageButton")

    }
}