package com.onysakura.tools.utils

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

class JsonUtils {

    companion object {
        val mapAdapter: JsonAdapter<Map<*, *>> = Moshi.Builder().add(KotlinJsonAdapterFactory()).build().adapter(Map::class.java)

    }
}