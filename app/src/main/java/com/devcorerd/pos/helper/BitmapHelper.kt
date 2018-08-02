package com.devcorerd.pos.helper

import android.content.Context
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.Matrix
import android.net.Uri
import android.provider.MediaStore
import java.io.File
import java.io.FileNotFoundException

/**
 * @author Ing. Wilson Garcia
 * Created on 8/1/18
 */
class BitmapHelper private constructor(){
    companion object {
        fun rotate(bitmap: Bitmap, degree: Int): Bitmap {
            val w = bitmap.width
            val h = bitmap.height

            val mtx = Matrix()
            mtx.setRotate(degree.toFloat())
            return Bitmap.createBitmap(bitmap, 0, 0, w, h, mtx, true)
        }

        @Throws(FileNotFoundException::class)
        fun decodeBitmapFile(ctx: Context, uri: Uri): File {
            val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
            val cursor: Cursor = ctx.contentResolver.query(uri, filePathColumn,
                    null, null, null)

            cursor.moveToFirst()
            val columnIndex: Int = cursor.getColumnIndex(filePathColumn[0])
            return File(cursor.getString(columnIndex))
        }
    }
}