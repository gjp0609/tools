package com.onysakura.tools.url

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id

@Entity
class UrlEntity() {
    @Id
    @Column(length = 10, columnDefinition = "varchar(10) COMMENT '编码'")
    var code: String = ""

    @Column(length = 1024, columnDefinition = "varchar(1024) COMMENT '原始地址'")
    var url: String = ""

    constructor(code: String, url: String) : this() {
        this.code = code
        this.url = url
    }

    override fun toString(): String {
        return "UrlEntity(code='$code', url='$url')"
    }
}