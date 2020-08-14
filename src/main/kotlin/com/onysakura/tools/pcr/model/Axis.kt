package com.onysakura.tools.pcr.model

import java.util.*
import javax.persistence.*

@Entity
@Table(name = "pcr_axis")
class Axis {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "int(20)")
    var id: Long = 0

    @Column(columnDefinition = "int(20)")
    var bossId: Long = 0L

    @Column(columnDefinition = "int(20)")
    var damageAmount: Long = 0L

    @Column(columnDefinition = "varchar(100)")
    var imgPath: String = ""

    @Column(columnDefinition = "varchar(100)")
    var princessList: String = ""

    @Column(columnDefinition = "text")
    var remark: String = ""

    @Column(columnDefinition = "datetime")
    var createTime: Date = Date()

    override fun toString(): String {
        return "Axis(id=$id, bossId=$bossId, damageAmount=$damageAmount, imgPath='$imgPath', princessList='$princessList', remark='$remark', createTime=$createTime)"
    }
}