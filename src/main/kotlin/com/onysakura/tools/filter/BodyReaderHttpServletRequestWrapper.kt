package com.onysakura.tools.filter

import java.io.*
import javax.servlet.ReadListener
import javax.servlet.ServletInputStream
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletRequestWrapper


class BodyReaderHttpServletRequestWrapper(request: HttpServletRequest) : HttpServletRequestWrapper(request) {
    private var body: String? = null

    init {
        val stringBuilder = StringBuilder()
        var bufferedReader: BufferedReader? = null
        var inputStream: InputStream? = null
        try {
            inputStream = request.inputStream
            if (inputStream != null) {
                bufferedReader = BufferedReader(InputStreamReader(inputStream))
                val charBuffer = CharArray(128)
                var bytesRead = -1
                while (bufferedReader.read(charBuffer).also { bytesRead = it } > 0) {
                    stringBuilder.append(charBuffer, 0, bytesRead)
                }
            } else {
                stringBuilder.append("")
            }
        } catch (ex: IOException) {
        } finally {
            try {
                inputStream?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            try {
                bufferedReader?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        body = stringBuilder.toString()
    }

    override fun getInputStream(): ServletInputStream {
        val byteArrayInputStream = ByteArrayInputStream(body!!.toByteArray())
        return object : ServletInputStream() {

            override fun isFinished(): Boolean {
                return false
            }

            override fun isReady(): Boolean {
                return false
            }

            override fun setReadListener(readListener: ReadListener) {}

            @Throws(IOException::class)
            override fun read(): Int {
                return byteArrayInputStream.read()
            }
        }
    }

    @Throws(IOException::class)
    override fun getReader(): BufferedReader? {
        return BufferedReader(InputStreamReader(this.inputStream))
    }

    fun getBody(): String? {
        return body
    }
}