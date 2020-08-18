package com.onysakura.tools.utils

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

class JsonUtils {

    companion object {
        val mapAdapter: JsonAdapter<Map<*, *>> = Moshi.Builder().add(KotlinJsonAdapterFactory()).build().adapter(Map::class.java)
        val listAdapter: JsonAdapter<List<*>> = Moshi.Builder().add(KotlinJsonAdapterFactory()).build().adapter(List::class.java)
        val mutableMapAdapter: JsonAdapter<MutableMap<*, *>> = Moshi.Builder().add(KotlinJsonAdapterFactory()).build().adapter(MutableMap::class.java)
        val mutableListAdapter: JsonAdapter<MutableList<*>> = Moshi.Builder().add(KotlinJsonAdapterFactory()).build().adapter(MutableList::class.java)
        val anyAdapter: JsonAdapter<Any> = Moshi.Builder().add(KotlinJsonAdapterFactory()).build().adapter(Any::class.java)
    }
}