package com.example.androidsnsproject

import android.Manifest
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.example.androidsnsproject.databinding.ActivityPostBinding
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import com.google.firebase.storage.ktx.storage
import java.text.SimpleDateFormat
import java.util.*

class PostActivity : AppCompatActivity() {
    lateinit var storage: FirebaseStorage
    lateinit var binding: ActivityPostBinding

    var PICK_IMAGE_FROM_ALBUM = 0
    var photoUri : Uri? = null
    var auth : FirebaseAuth? = null
    var firestore : FirebaseFirestore? = null

    lateinit var storagePermission: ActivityResultLauncher<String>
    lateinit var gallaryLauncher: ActivityResultLauncher<String>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Firebase.auth.currentUser ?: finish()

        storage = Firebase.storage
        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        storagePermission = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted->
            if(isGranted) {
                setViews()
            } else {
                Toast.makeText(baseContext,"외부저장소 권한 승인",Toast.LENGTH_LONG).show()
                finish()
            }
        }

        gallaryLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri->
            binding.postPhoto.setImageURI(uri)
            photoUri = uri
        }
        storagePermission.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)

    }

    fun setViews() {
        binding.imageButton.setOnClickListener {
            gallaryLauncher.launch("image/*")
        }

        binding.postUploadButton.setOnClickListener {
            contentUpload(photoUri!!)
        }
    }

    fun contentUpload(photoUri: Uri){

        var timestamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        var imageFileName = "IMAGE_" + timestamp + "_.jpg"

        var storageRef = storage?.reference?.child("images")?.child(imageFileName)

        storageRef?.putFile(photoUri)?.continueWithTask{ task: Task<UploadTask.TaskSnapshot> ->
            return@continueWithTask storageRef.downloadUrl
        }?.addOnSuccessListener { uri ->
            var Data = Data()

            Data.timestamp = System.currentTimeMillis()
            Data.uid = auth?.currentUser?.uid
            Data.userId = auth?.currentUser?.email
            Data.posttext = binding.postContent.text.toString()
            Data.imageUrl = uri.toString()

            firestore?.collection("images")?.document()?.set(Data)

            setResult(RESULT_OK)
            finish()

        }
    }
}