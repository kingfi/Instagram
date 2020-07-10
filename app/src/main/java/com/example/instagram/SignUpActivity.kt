package com.example.instagram

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.bumptech.glide.Glide
import com.example.instagram.fragments.ComposeFragment
import com.parse.ParseUser
import jp.wasabeef.glide.transformations.RoundedCornersTransformation
import java.io.File
import java.lang.Exception

class SignUpActivity : AppCompatActivity() {

    companion object{
        const val CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 42
    }

    private var editUserSignUp: EditText? = null
    private var editPasswordSignUp: EditText? = null
    private var editEmailAddress: EditText? = null
    private var imageSignUp: ImageView? = null
    private var photoFile : File? = null
    private val photoFileName = "profile.jpg"



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        editUserSignUp = findViewById(R.id.editUserSignUp)
        editPasswordSignUp = findViewById(R.id.editPasswordSignUp)
        editEmailAddress = findViewById(R.id.editEmailAddress)
        imageSignUp = findViewById(R.id.imageSignUp)
        var buttonSignUp = findViewById<Button>(R.id.buttonSignUp)

        imageSignUp!!.setOnClickListener { launchCamera() }

        buttonSignUp.setOnClickListener {
            // Create the ParseUser
            val user = ParseUser()

            // with statement allows access properties of User without needing to repeat
            // i.e. user.username =
            //user.setPassword
            with(user) {
                username = editUserSignUp?.text.toString()
                setPassword(editPasswordSignUp?.text.toString())
                email = editEmailAddress?.text.toString()
                signUpInBackground { e ->
                    if (e == null) {
                        // Sign up Successful
                        Toast.makeText(applicationContext, "Sign Up Successful!", Toast.LENGTH_SHORT).show()
                        val i = Intent(applicationContext, LoginActivity::class.java)
                        startActivity(i)

                    } else {
                        // Sign up didn't succeed. Look at the ParseException
                        // to figure out what went wrong
                        Toast.makeText(applicationContext, "Sign Up Failed!", Toast.LENGTH_SHORT).show()

                    }
                }
            }
        }


    }

    private fun launchCamera() {
        // create Intent to take a picture and return control to the calling application
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        // Create a File reference for future access
        photoFile = getPhotoFileUri(photoFileName)

        // wrap File object into a content provider
        // required for API >= 24
        // See https://guides.codepath.com/android/Sharing-Content-with-Intents#sharing-files-with-api-24-or-higher
        val fileProvider = FileProvider.getUriForFile(this, "com.codepath.fileprovider", photoFile!!)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider)

        // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
        // So as long as the result is not null, it's safe to use the intent.
        if (intent.resolveActivity(this.packageManager) != null) {
            // Start the image capture intent to take photo
            startActivityForResult(intent, ComposeFragment.CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE)
        }
    }

    // When camera intent returns to MainActivity, take the image and set it
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ComposeFragment.CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                // by this point we have the camera photo on disk
                var takenImage = BitmapFactory.decodeFile(photoFile!!.absolutePath)
                // RESIZE BITMAP, see section below
                // Load the taken image into a preview
//                imageSignUp!!.setImageBitmap(takenImage)

                // Rotate bitmap image
                try{
                    val exif: ExifInterface = ExifInterface(photoFile!!.absolutePath)
                    val orientation: Int = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1)
                    val matrix: Matrix = Matrix()
                    if (orientation == 6) {
                        matrix.postRotate(90F)
                    } else if (orientation == 3) {
                        matrix.postRotate(180F)
                    } else if (orientation == 8) {
                        matrix.postRotate(270F)
                    }
                    //rotating bitmap
                    takenImage = Bitmap.createBitmap(takenImage, 0, 0, takenImage.width, takenImage.height, matrix, true)
                } catch (e : Exception) {
                    Log.e("SignUpActivity", "Error in rotating bitmap")
                }
                Glide.with(this).load(takenImage).into(imageSignUp!!)
            } else { // Result was a failure
                Toast.makeText(this, "Picture wasn't taken!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Returns the File for a photo stored on disk given the fileName
    private fun getPhotoFileUri(fileName: String): File {
        // Get safe storage directory for photos
        // Use `getExternalFilesDir` on Context to access package-specific directories.
        // This way, we don't need to request external read/write runtime permissions.
        val mediaStorageDir = File(this.getExternalFilesDir(Environment.DIRECTORY_PICTURES), ComposeFragment.TAG)

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()) {
            Log.d(ComposeFragment.TAG, "failed to create directory")
        }

        // Return the file target for the photo based on filename
        return File(mediaStorageDir.path + File.separator + fileName)
    }
}