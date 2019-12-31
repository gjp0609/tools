package com.onysakura.tools.index

import javax.persistence.*

@Entity
class Poet {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long = 0
    @Column(length = 256, columnDefinition = "varchar(256) COMMENT '标题'")
    var title: String = ""
    @Column(length = 64, columnDefinition = "varchar(64) COMMENT '作者'")
    var author: String = ""
    @Column(length = 5120, columnDefinition = "varchar(5120) COMMENT '内容'")
    var paragraphs: String = ""
}