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

    @Column(columnDefinition = "int(1)")
    @Enumerated(EnumType.ORDINAL)
    var type: Constants.Type = Constants.Type.S

    @Column(columnDefinition = "int(10)")
    var priority: Int = Int.MAX_VALUE

    @Column(columnDefinition = "datetime")
    var beginDate: Date = Date()

    @Column(columnDefinition = "datetime")
    var endDate: Date = Date()
}