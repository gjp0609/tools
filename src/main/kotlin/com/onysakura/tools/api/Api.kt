package com.onysakura.tools.api

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id

@Entity
class Api {
    @Id
    @Column(columnDefinition = "varchar(10)")
    var code: String = ""

    @Column(columnDefinition = "text")
    var content: String = ""

    override fun toString(): String {
        return "UrlEntity(code='$code', content='$content')"
    }
}