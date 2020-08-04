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

    @Column(columnDefinition = "varchar(20)")
    var name: String = ""

    @Column(columnDefinition = "varchar(20)")
    var activityId: Long = 0L

    @Column(columnDefinition = "varchar(20)")
    var bossRound: String = ""

    @Column(columnDefinition = "int(1)")
    var isBerserk: Boolean = false

    @Column(columnDefinition = "int(10)")
    var damageAmount: Long = 0L

    @Column(columnDefinition = "int(10)")
    var priority: Int = Int.MAX_VALUE

    @Column(columnDefinition = "text")
    var img: String = ""

    @Column(columnDefinition = "datetime")
    var createTime: Date = Date()
}