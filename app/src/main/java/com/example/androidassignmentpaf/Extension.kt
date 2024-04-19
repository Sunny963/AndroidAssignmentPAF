package com.example.androidassignmentpaf

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

fun String.httpResponse(page: Int, perPage: Int, accessKey: String): String =
    (URL("$this/?client_id=$accessKey&page=$page&per_page=$perPage").openConnection() as HttpURLConnection).let { connection ->
        connection.connectTimeout = 10000
        connection.readTimeout = 15000
        connection.requestMethod = "GET"

        StringBuilder().let { response ->
            try {
                BufferedReader(InputStreamReader(connection.inputStream)).let { reader ->
                    var line: String?
                    while (reader.readLine().also { line = it } != null) {
                        response.append(line)
                    }
                    reader.close()
                }
            } catch (e: Exception) {
                // Handle any error response from server
                Log.e("UnsplashService", "Error: ${e.message}")
            } finally {
                connection.disconnect()
            }

            response.toString()
        }
    }

fun String.bitmap(): Bitmap? =
    try {
        (URL(this).openConnection() as HttpURLConnection).let { connection ->
            connection.doInput = true
            connection.connect()
            BitmapFactory.decodeStream(connection.inputStream)
        }
    } catch (e: Exception) {
        // Handle any exceptions that occur during image loading
        e.printStackTrace()
        null
    }