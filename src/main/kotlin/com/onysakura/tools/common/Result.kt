package com.onysakura.tools.common

class Result<Data> private constructor() {
    var success: Boolean = true
    var msg: String = "success"
    var data: Data? = null

    companion object {
        fun success(): Result<String> {
            return success("success")
        }

        fun success(msg: String): Result<String> {
            return Result<String>().msg(msg)
        }

        fun error(): Result<String> {
            return error("error")
        }

        fun error(msg: String): Result<String> {
            return Result<String>().error().msg(msg)
        }
    }

    constructor(data: Data) : this() {
        this.data = data
    }

    fun success(): Result<Data> {
        this.success = true
        return this
    }

    fun error(): Result<Data> {
        this.success = false
        return this
    }

    fun msg(msg: String): Result<Data> {
        this.msg = msg
        return this
    }

    fun data(data: Data): Result<Data> {
        this.data = data
        return this
    }
}