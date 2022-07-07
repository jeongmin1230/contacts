package com.example.contacts

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.telephony.PhoneNumberFormattingTextWatcher
import android.text.Editable
import android.util.Log
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.activity_edit.*
import kotlinx.android.synthetic.main.activity_register.*

class Edit : AppCompatActivity() {

    private val OPEN_GALLERY = 1
    var uriPhoto : Uri? = null
    private val db = FirebaseFirestore.getInstance()    // Firestore 인스턴스 선언

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)

        basicLoad()

        editEtNumber.addTextChangedListener(PhoneNumberFormattingTextWatcher())
    }
/* override 함수 */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode == Activity.RESULT_OK) {
            if(requestCode == OPEN_GALLERY) {
                uriPhoto = data?.data
                editProfile.setImageURI(uriPhoto)
                editBtnSave.setOnClickListener {
                    editNum()
                    imageUpload(uriPhoto!!)
                }
            } else {
                Log.d("jeongmin", "something wrong")
            }
        }
    }
/* 기능 함수 */
    // 기본정보
    private fun basicLoad() {
        val name = intent.getStringExtra("name").toString() // 이름 받아오기
        val number = intent.getStringExtra("number").toString() // 번호 받아오기

        Log.i("jeongmin", "Edit activity 안 : ${intent.getStringExtra("name")}")
        Log.i("jeongmin", "Edit activity 안 : ${intent.getStringExtra("number")}")

        loadImage()

        editEtName.text = Editable.Factory.getInstance().newEditable(name)
        editEtNumber.text = Editable.Factory.getInstance().newEditable(number)
    }

    // 이미지 로드 함수
    private fun loadImage() {
        val storage : FirebaseStorage = FirebaseStorage.getInstance("gs://contacts-857e9.appspot.com")
        val storageReference = storage.reference
        val pathReference = storageReference.child("/profile_${intent.getStringExtra("number")}_${intent.getStringExtra("name")}.png")

        Log.d("jeongmin", "Edit 안 : /profile_${intent.getStringExtra("number")}_${intent.getStringExtra("name")}.png")

        pathReference.downloadUrl.addOnSuccessListener { uri ->
            if(uri == null) {
                detailIv.setImageResource(R.drawable.basic)
            } else {
                Glide.with(editProfile.context)
                    .load(uri)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .centerCrop()
                    .into(editProfile)
            }
        }
    }
    // 번호 수정 함수
    private fun editNum() { // 이미지 설정 안하면 저장이 안돼요
        val data = hashMapOf(
            "name" to editEtName.text.toString(),
            "number" to editEtNumber.text.toString()
        )
        db.collection("contacts").document(editEtNumber.text.toString() + "_" + editEtName.text.toString()) // 작업할 컬렉션
            .set(data)
            .addOnSuccessListener {
                // 성공할경우
                Toast.makeText(this, "데이터가 수정되었습니다.", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { exception ->
                // 실패할경우
                Log.d("jeongmin", "데이터 쓰기 실패 : $exception")
            }
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("number", editEtNumber.text.toString())
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
    }

    // 파이어스토어에 이미지 수정한거 업로드 하는 함수
    private fun imageUpload(uri : Uri) {
        var editEtNumberText = editEtNumber.text.toString()
        var editEtNameText = editEtName.text.toString()
        var storage : FirebaseStorage? = FirebaseStorage.getInstance()
        // 파일 이름 생성
        var fileName = "profile_${editEtNumberText}_${editEtNameText}.png"
        var imagesRef = storage!!.reference.child(fileName)
        // 이미지 파일 업로드
        imagesRef.putFile(uri!!).addOnSuccessListener {
            Toast.makeText(this, "업로드 성공", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }.addOnFailureListener{
            println(it)
            Toast.makeText(this, "업로드 실패", Toast.LENGTH_SHORT).show()
        }
    }
/* onClick 함수 */
    fun onClickPick(view: View) {
        val intent : Intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, OPEN_GALLERY)
    }
/* onClick 함수 */
    fun onClickCancel(view: View) {}
    fun onClickSave(view: View) {
        editNum()
    }
}