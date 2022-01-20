package com.android.attendme.ml

import android.content.res.AssetManager
import android.graphics.Bitmap
import com.android.attendme.ml.mTCNN.MTCNN
import com.android.attendme.ml.mobileFaceNet.MobileFaceNet
import com.android.attendme.ml.faceAntiSpoofing.FaceAntiSpoofing

interface OnFaceDetectionComplete{
    fun onSuccess(bitmap : Bitmap)
    fun onFailure(message : String)
}

interface OnCompareFacesComplete{
    fun onBothSame()
    fun onBothDiffer()
}

interface OnLiveTestComplete{
    fun isLive()
    fun isNotLive()
}


class MLHelpers() {
    companion object {
        fun detectFace(
            imgBitmap: Bitmap,
            assetManager: AssetManager,
            listener: OnFaceDetectionComplete
        ) {
            val mtcnn = MTCNN(assetManager)
            val boxes = mtcnn.detectFaces(imgBitmap, imgBitmap.width / 5)
            if (boxes.size == 0) {
                listener.onFailure("Unable to detect face")
                return
            } else if (boxes.size > 1) {
                listener.onFailure("More than one face detected")
                return
            }
            try {
                val box = boxes[0]
                box.toSquareShape()
                box.limitSquare(imgBitmap.width, imgBitmap.height)
                listener.onSuccess(MyUtil.crop(imgBitmap, box.transform2Rect()))
            } catch (e: Exception) {
                listener.onFailure("Something went wrong while detecting face " + boxes.size)
            }
        }

        fun liveTest(
            imgBitmap: Bitmap,
            assetManager: AssetManager,
            listener: OnLiveTestComplete
        ) {
            val fas = FaceAntiSpoofing(
                assetManager
            )
            val laplacian = fas.laplacian(imgBitmap)
            if (laplacian < FaceAntiSpoofing.LAPLACIAN_THRESHOLD) {
                listener.isNotLive()
            } else {
                val liveNessScore = fas.antiSpoofing(imgBitmap)
                if (liveNessScore > FaceAntiSpoofing.THRESHOLD) {
                    listener.isNotLive()
                }
                listener.isLive()
            }
        }
        fun compareFaces(bitmapimg1: Bitmap,
                         bitmapimg2: Bitmap,
                         assetManager: AssetManager,
                         listener: OnCompareFacesComplete
        ){
            val mfn =
                MobileFaceNet(assetManager)
            val comparisonScore = mfn.compare(bitmapimg1, bitmapimg2)
            if(comparisonScore > MobileFaceNet.THRESHOLD)
                listener.onBothSame()
            else
                listener.onBothDiffer()
        }
    }
}