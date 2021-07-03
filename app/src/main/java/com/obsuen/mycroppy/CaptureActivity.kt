package com.obsuen.mycroppy


import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.obsuen.mycroppy.ml.CropWithMeta
import org.tensorflow.lite.support.image.TensorImage

class CaptureActivity : AppCompatActivity() {
    //    private val REQUEST_PERMISSION = 100
    private val OPERATION_CAPTURE_PHOTO = 1
    private val REQUEST_PICK_CAPTURE = 1
    private val REQUEST_PICK_IMAGE = 2
    private lateinit var select: Button
    private lateinit var predict: Button
    private lateinit var image: ImageView
    private lateinit var textview: TextView
    private lateinit var bitmap: Bitmap
    private lateinit var cam: Button
    var mUri: Uri? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_capture)
        select = findViewById(R.id.select)
        predict = findViewById(R.id.predict)
        image = findViewById(R.id.myimageview)
        textview = findViewById(R.id.textView)
        cam = findViewById(R.id.cam)
        val more = findViewById<Button>(R.id.more)
        more.setOnClickListener() {
            Toast.makeText(
                applicationContext,
                "Read more button will be implemented soon!",
                Toast.LENGTH_SHORT
            ).show()
        }


        select.setOnClickListener {
            Log.d("mssg", "button pressed")
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"

            startActivityForResult(intent, 100)
        }
        cam.setOnClickListener {
            capturePhoto()
        }


        predict.setOnClickListener {
            val model = CropWithMeta.newInstance(this)
            // Creates inputs for reference.
            val image = TensorImage.fromBitmap(bitmap)

            // Runs model inference and gets result.

            val outputs = model.process(image)
            val probability = outputs.probabilityAsCategoryList.maxByOrNull {
                it.score
            }
            val maxx = probability!!.label.toString()
            textview.setText(maxx)

            model.close()
        }


    }

    private fun capturePhoto() {
        val cIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        try {
            startActivityForResult(cIntent, REQUEST_PICK_CAPTURE)
        } catch (e: ActivityNotFoundException) {

        }
//        val captureImage = File(externalCacheDir,"")
//        if (captureImage.exists()){
//            captureImage.delete()
//        }
//        captureImage.createNewFile()
//        mUri  =
//            FileProvider.getUriForFile(this,"",captureImage)
//        val intent = Intent("android.action.IMAGE_CAPTURE")
//        intent.putExtra(MediaStore.EXTRA_OUTPUT,mUri)
//        startActivityForResult(intent,OPERATION_CAPTURE_PHOTO)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_PICK_CAPTURE) {
                val nbitmap = data?.extras?.get("data") as Bitmap
                image.setImageBitmap(nbitmap)
                bitmap= Bitmap.createBitmap(nbitmap)
            } else {


                image.setImageURI(data?.data)

                val uri: Uri? = data?.data
                bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, uri)
            }
        }
    }
}