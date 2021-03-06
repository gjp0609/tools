package com.onysakura.tools.pcr.model

import java.util.*
import javax.persistence.*

@Entity
@Table(name = "pcr_activity")
class Activity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "int(20)")
    var id: Long = 0L

    @Column(columnDefinition = "varchar(20)")
    var name: String = ""

    @Column(columnDefinition = "datetime")
    var beginDate: Date = Date()

    @Column(columnDefinition = "datetime")
    var endDate: Date = Date()
}