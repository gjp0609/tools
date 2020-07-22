package com.onysakura.tools.utils

import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.nio.ByteBuffer

open class FileUtil {
    companion object {

        /**
         * 把文件[filePath]分割为[fileCount]个等大的文件
         */
        @Throws(IOException::class)
        fun splitFile(filePath: String, fileCount: Int) {
            println("start")
            val fis = FileInputStream(filePath)
            val inputChannel = fis.channel
            val fileSize = inputChannel.size()
            val average = fileSize / fileCount //平均值
            val bufferSize: Long = 200 //缓存块大小，自行调整
            val byteBuffer: ByteBuffer = ByteBuffer.allocate((bufferSize.toString() + "").toInt()) // 申请一个缓存区
            var startPosition: Long = 0 //子文件开始位置
            var endPosition = if (average < bufferSize) 0 else average - bufferSize //子文件结束位置
            for (i in 0 until fileCount) {
                println("i: $i")
                if (i + 1 != fileCount) {
                    var read = inputChannel.read(byteBuffer, endPosition) // 读取数据
                    readW@ while (read != -1) {
                        byteBuffer.flip() //切换读模式
                        val array: ByteArray = byteBuffer.array()
                        for (j in array.indices) {
                            val b = array[j]
                            if (b.toInt() == 10 || b.toInt() == 13) { //判断\r\n
                                endPosition += j.toLong()
                                break@readW
                            }
                        }
                        endPosition += bufferSize
                        byteBuffer.clear() //重置缓存块指针
                        read = inputChannel.read(byteBuffer, endPosition)
                    }
                } else {
                    endPosition = fileSize //最后一个文件直接指向文件末尾
                }
                val fos = FileOutputStream(filePath.substring(0, filePath.lastIndexOf(".")) + (i + 1) + filePath.substring(filePath.lastIndexOf(".")))
                val outputChannel = fos.channel
                inputChannel.transferTo(startPosition, endPosition - startPosition, outputChannel) //通道传输文件数据
                outputChannel.close()
                fos.close()
                startPosition = endPosition + 1
                endPosition += average
            }
            inputChannel.close()
            fis.close()
        }

        @JvmStatic
        fun main(args: Array<String>) {
            val list = "a.b.c.d.asd".split(".")
            println(list)
            val slice = list.slice(IntRange(0, list.size - 2))
            println(slice.joinToString("."))
        }
    }
}