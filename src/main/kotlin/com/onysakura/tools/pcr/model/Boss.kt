package com.onysakura.tools.pcr.model

import javax.persistence.*

@Entity
@Table(name = "pcr_boss")
class Boss {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "int(20)")
    var id: Long = 0L

    @Column(columnDefinition = "varchar(20)")
    var name: String = ""

    @Column(columnDefinition = "int(10)")
    var round: Int = 1

    @Column(columnDefinition = "int(20)")
    var activityId: Long = 0L

    @Column(columnDefinition = "varchar(20)")
    var img: String = ""

    @Column(columnDefinition = "int(1)")
    var isFurious: Boolean = false
}