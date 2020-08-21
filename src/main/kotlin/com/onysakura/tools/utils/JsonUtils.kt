package com.onysakura.tools.utils

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

class JsonUtils {

    companion object {
        // MutableMap<String, String>
        val stringMapAdapter: JsonAdapter<MutableMap<String, String>> = Moshi
                .Builder()
                .add(KotlinJsonAdapterFactory())
                .build()
                .adapter(Types.newParameterizedType(MutableMap::class.java, String::class.java))

        // MutableList<String>
        val mutableListStringAdapter: JsonAdapter<MutableList<String>> = Moshi
                .Builder()
                .add(KotlinJsonAdapterFactory())
                .build()
                .adapter(Types.newParameterizedType(MutableList::class.java, String::class.java))
    }
}