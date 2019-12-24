package com.onysakura.tools.kindle

import javax.persistence.*

@Entity
class Article {
    @Id
    @Column(length = 32, columnDefinition = "varchar(32) COMMENT 'id'")
    var id: String = ""
    @Column(length = 128, columnDefinition = "varchar(128) COMMENT '地址'")
    var url: String = ""
    @Column(length = 1024, columnDefinition = "varchar(1024) COMMENT '标题'")
    var title: String = ""
    @Column(length = 1, columnDefinition = "tinyint(1) COMMENT '状态'")
    var status: Boolean = false

    override fun toString(): String {
        return "Article(id='$id', url='$url', title='$title', status=$status)"
    }

}