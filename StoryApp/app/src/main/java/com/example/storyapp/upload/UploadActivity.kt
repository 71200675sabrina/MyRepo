package com.example.storyapp.upload

import android.content.Intent
import android.content.Intent.ACTION_GET_CONTENT
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.example.storyapp.api.ApiConfig
import com.example.storyapp.databinding.ActivityUploadBinding
import com.example.storyapp.model.UserModel
import com.example.storyapp.model.story.UploadStoryResponse
import com.example.storyapp.view.MainActivity
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class UploadActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUploadBinding
    private lateinit var currentPhotoPath: String
    private var getFile: File? = null
    private lateinit var user : UserModel

    companion object {
        private val REQUIRED_PERMISSIONS = arrayOf(android.Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                Toast.makeText(this,"Tidak mendapatka permission", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }
        binding.btnCamera.setOnClickListener { startTakePhoto() }
        binding.btnGallery.setOnClickListener { startGallery() }
        binding.btnUpload.setOnClickListener { uploadImage() }
    }

    private fun startGallery(){
        val intent = Intent()
        intent.action = ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGallery.launch(chooser)
    }

    private fun startTakePhoto(){
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.resolveActivity(packageManager)

        createCustomTempFile(application).also{
            val photoURI : Uri = FileProvider.getUriForFile(
                this@UploadActivity,
                "com.example.storyapp",
                it
            )
            currentPhotoPath = it.absolutePath
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            launcherIntentCamera.launch(intent)
        }
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){
        result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg = result.data?.data as Uri

            selectedImg.let { uri ->
                val myFile = uriToFile(uri, this@UploadActivity)
                getFile = myFile
                binding.ivPreview.setImageURI(uri)
            }
        }
    }

    private fun uploadImage(){
        if (getFile != null){
            val file = reduceFileImage(getFile as File)
            val description = binding.editdesc.text.toString().toRequestBody("text/plain".toMediaType())
            val requestImageFile = file.asRequestBody("image.jpeg".toMediaType())
            val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                "photo",
                file.name,
                requestImageFile
            )
            val token = "Bearer" + user.token
            val apiService = ApiConfig.getApiService()
            val uploadImageRequest = apiService.uploadImage(token,imageMultipart,description)
            uploadImageRequest.enqueue(object : Callback<UploadStoryResponse> {
                override fun onResponse(
                    call: Call<UploadStoryResponse>,
                    response: Response<UploadStoryResponse>
                ) {
                    if(response.isSuccessful){
                        val responseBody = response.body()
                        if (responseBody != null && !responseBody.error){
                            showLoading(false)
                            val builder = AlertDialog.Builder(this@UploadActivity)
                            builder.setTitle("Story berhasil di upload")
                            builder.setMessage("Story telah di update")
                            builder.setPositiveButton("Selesai"){_,_->
                                val intent = Intent (this@UploadActivity, MainActivity::class.java)
                                startActivity(intent)
                                finish()
                            }
                            builder.show()
                        }else{
                            showLoading(false)
                            Toast.makeText(this@UploadActivity, response.message(), Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        showLoading(false)
                        Toast.makeText(this@UploadActivity, response.message(), Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<UploadStoryResponse>, t: Throwable) {
                    showLoading(false)
                    Toast.makeText(this@UploadActivity, t.message, Toast.LENGTH_SHORT).show()
                }
            })


        }

    }
    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){
        if (it.resultCode == RESULT_OK) {
            val myFile = File(currentPhotoPath)
            myFile.let { file ->
                getFile = file
                binding.ivPreview.setImageBitmap(BitmapFactory.decodeFile(file.path))
            }
        }
    }

    private fun showLoading(isLoading : Boolean){
        if (isLoading){
            binding.loadstory.visibility = View.VISIBLE
        } else {
            binding.loadstory.visibility = View.GONE
        }
    }
}