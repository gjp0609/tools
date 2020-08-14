package com.onysakura.tools.filter

import java.io.*
import javax.servlet.ServletOutputStream
import javax.servlet.WriteListener
import javax.servlet.http.HttpServletResponse
import javax.servlet.http.HttpServletResponseWrapper


class BodyReaderHttpServletResponseWrapper(response: HttpServletResponse) : HttpServletResponseWrapper(response) {
    private var buffer: ByteArrayOutputStream = ByteArrayOutputStream()
    private var writer: PrintWriter? = null
    private var out: ServletOutputStream? = null

    init {
        try {
            buffer = ByteArrayOutputStream()
            /**
             * response输出数据时是调用getOutputStream()和getWriter()方法获取输出流，再将数据输出到输出流对应的输出端的。
             * 此处指定getOutputStream()和getWriter()返回的输出流的输出端为buffer，即将数据保存到buffer中。
             */
            out = object : ServletOutputStream() {
                override fun isReady(): Boolean {
                    return false
                }

                override fun setWriteListener(writeListener: WriteListener?) {
                }

                @Throws(IOException::class)
                override fun write(b: Int) {
                    return buffer.write(b)
                }
            }
            writer = PrintWriter(OutputStreamWriter(buffer, this.characterEncoding))
        } catch (ex: IOException) {
        } finally {
            try {
                buffer.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            try {
                writer?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    override fun flushBuffer() {
        out?.flush();
        writer?.flush();
    }

    @Throws(IOException::class)
    override fun getOutputStream(): ServletOutputStream? {
        return out
    }

    override fun reset() {
        buffer.reset()
    }

    @Throws(IOException::class)
    fun getResponseData(): ByteArray {
        flushBuffer()
        return buffer.toByteArray()
    }

    @Throws(UnsupportedEncodingException::class)
    override fun getWriter(): PrintWriter? {
        return writer
    }
}