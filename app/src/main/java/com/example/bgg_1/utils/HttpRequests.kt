package com.example.bgg_1.utils

import android.content.Context
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson

class HttpRequests(ctx: Context) {
    // Instantiate the RequestQueue.
    val queue = Volley.newRequestQueue(ctx)

    val gson = Gson()

    inline fun <reified T> get(
        url: String,
        crossinline onSuccess: (T) -> Unit,
        crossinline onError: (AppError) -> Unit
    ) {
        // Request a string response from the provided URL.
        val stringRequest = StringRequest(
            Request.Method.GET,
            url,
            Response.Listener<String> { response ->
                val results = "{\n results : { matches :  $response\n}\n}"
                val dto = gson.fromJson(results, T::class.java)
                onSuccess(dto)
            },
            Response.ErrorListener { err -> onError(AppError(err)) })
        // Add the request to the RequestQueue.
        queue.add(stringRequest)
    }
}