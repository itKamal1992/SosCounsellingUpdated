package com.soscubs.soscounsellingapp.remote

import android.R.string
import android.content.Context
import android.graphics.Bitmap
import androidx.collection.LruCache
import com.android.volley.Cache
import com.android.volley.Network
import com.android.volley.RequestQueue
import com.android.volley.toolbox.BasicNetwork
import com.android.volley.toolbox.DiskBasedCache
import com.android.volley.toolbox.HurlStack
import com.android.volley.toolbox.ImageLoader



class CustomVolleyRequest {
    private var customVolleyRequest: CustomVolleyRequest? = null
    private var context: Context? = null
    private var requestQueue: RequestQueue? = null
    private var imageLoader: ImageLoader? = null

    private fun CustomVolleyRequest(context: Context) {
        this.context = context
        requestQueue = getRequestQueue()
        imageLoader = ImageLoader(requestQueue, object : ImageLoader.ImageCache {
            private val cache: LruCache<String, Bitmap> = LruCache<String, Bitmap>(20)

            override fun getBitmap(url: String?): Bitmap? {
                return cache.get(url!!)

            }

            override fun putBitmap(url: String, bitmap: Bitmap) {
                cache.put(url, bitmap)
            }
        })
    }

    @Synchronized
    fun getInstance(context: Context): CustomVolleyRequest? {
        if (customVolleyRequest == null) {
            customVolleyRequest = CustomVolleyRequest()
        }
        return customVolleyRequest
    }

    fun getRequestQueue(): RequestQueue? {
        if (requestQueue == null) {
            val cache: Cache = DiskBasedCache(context!!.getCacheDir(), 10 * 1024 * 1024)
            val network: Network = BasicNetwork(HurlStack())
            requestQueue = RequestQueue(cache, network)
            requestQueue!!.start()
        }
        return requestQueue
    }

    fun getImageLoader(): ImageLoader? {
        return imageLoader
    }

}