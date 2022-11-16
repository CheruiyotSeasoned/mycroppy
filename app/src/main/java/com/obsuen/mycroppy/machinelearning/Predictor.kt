package com.obsuen.mycroppy.machinelearning

import android.content.Context
import android.graphics.Bitmap
import com.obsuen.mycroppy.ml.CropWithMeta
import org.tensorflow.lite.support.image.TensorImage

class Predictor {
        companion object {
            @JvmStatic fun predictDisease(context:Context,bitmap: Bitmap):String {
                val model = CropWithMeta.newInstance(context)
                // Creates inputs for reference.
                val image = TensorImage.fromBitmap(bitmap)

                // Runs model inference and gets result.
                val outputs = model.process(image)
                var maxx:String;
                val probability = outputs.probabilityAsCategoryList.maxByOrNull {
                    it.score
                }
                maxx = if (probability!!.score<0.8){
                    "No prediction"
                }else {
                    probability.label.toString()
                }
                model.close()
                return maxx
            }

        }
}