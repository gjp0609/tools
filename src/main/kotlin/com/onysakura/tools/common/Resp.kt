package com.onysakura.tools.common

open class Resp<Data> private constructor() {
    var success: Boolean = true
    var msg: String = "success"
    var data: Data? = null

    companion object {
        fun success(): Resp<*> {
            return success("success")
        }

        fun success(msg: String): Resp<*> {
            return Resp(null).msg(msg)
        }

        fun error(): Resp<*> {
            return error("error")
        }

        fun error(msg: String): Resp<*> {
            return Resp(null).error().msg(msg)
        }
    }

    constructor(data: Data) : this() {
        this.data = data
    }

    fun success(): Resp<Data> {
        this.success = true
        return this
    }

    fun error(): Resp<Data> {
        this.success = false
        return this
    }

    fun msg(msg: String): Resp<Data> {
        this.msg = msg
        return this
    }

    fun data(data: Data): Resp<Data> {
        this.data = data
        return this
    }

    override fun toString(): String {
        return "Result(success=$success, msg='$msg', data=$data)"
    }
}