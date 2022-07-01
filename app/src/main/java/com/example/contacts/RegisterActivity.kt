package com.example.contacts

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_register.*
import java.text.SimpleDateFormat
import java.util.*

class RegisterActivity : AppCompatActivity() {

    private val OPEN_GALLERY = 1
    var uriPhoto : Uri? = null
    private val db = FirebaseFirestore.getInstance()    // Firestore 인스턴스 선언

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
    }
/* override 함수 */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode == Activity.RESULT_OK) {
            if(requestCode == OPEN_GALLERY) {
                uriPhoto = data?.data
                registerProfile.setImageURI(uriPhoto)
                registerBtnSave.setOnClickListener {
                    saveNum()
                    imageUpload(uriPhoto!!)
                }
            } else {
                Log.d("ActivityResult", "something wrong")
            }
        }
    }
/* 일반 함수 */
    // 파이어스토어에 이미지 업로드 하는 함수
    private fun imageUpload(uri : Uri) {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        var storage : FirebaseStorage? = FirebaseStorage.getInstance()
        // 파일 이름 생성
        var fileName = "IMAGE_${timeStamp}.png"
        var imagesRef = storage!!.reference.child(fileName)
        // 이미지 파일 업로드
        imagesRef.putFile(uri!!).addOnSuccessListener {
            Toast.makeText(this, "업로드 성공", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener{
            println(it)
            Toast.makeText(this, "업로드 실패", Toast.LENGTH_SHORT).show()
        }
    }
    // 번호 저장 함수
    private fun saveNum() {
        val data = hashMapOf(
            "name" to registerEtName.text.toString(),
            "number" to registerEtNumber.text.toString()
        )
        db.collection("contacts") // 작업할 컬렉션
            .add(data)
            .addOnSuccessListener {
                // 성공할경우
                Toast.makeText(this, "데이터가 추가되었습니다.", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { exception ->
                // 실패할경우
                Log.d("jeongmin", "데이터 쓰기 실패 : $exception")
            }
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }
    /* onClick 함수 */
    fun onClickSave(view: View) {
//        val data = hashMapOf(
//            "name" to registerEtName.text.toString(),
//            "number" to registerEtNumber.text.toString()
//        )
//        db.collection("contacts") // 작업할 컬렉션
//            .add(data)
//            .addOnSuccessListener {
//                // 성공할경우
//                Toast.makeText(this, "데이터가 추가되었습니다.", Toast.LENGTH_SHORT).show()
//            }
//            .addOnFailureListener { exception ->
//                // 실패할경우
//                Log.d("jeongmin", "데이터 쓰기 실패 : $exception")
//            }
//        val intent = Intent(this, MainActivity::class.java)
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//        startActivity(intent)
    }
    fun onClickCancel(view: View) {}
    fun onClickPick(view: View) {
        val intent : Intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, OPEN_GALLERY)
    }
}