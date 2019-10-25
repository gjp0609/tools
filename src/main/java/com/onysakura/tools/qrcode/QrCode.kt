package com.onysakura.tools.qrcode

data class QrCode(val url: String, val code: String) {
    override fun toString(): String {
        return "QrCode(url='$url', code='$code')"
    }
}