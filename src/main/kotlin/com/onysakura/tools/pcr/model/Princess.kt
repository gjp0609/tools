package com.onysakura.tools.pcr.model

import javax.persistence.*

@Entity
@Table(name = "pcr_princess")
class Princess {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "int(20)")
    var id: Long = 0L

    @Column(columnDefinition = "varchar(20)")
    var name: String = ""

    @Column(columnDefinition = "int(1)")
    @Enumerated(EnumType.ORDINAL)
    var type: Constants.Type = Constants.Type.REAR

    @Column(columnDefinition = "int(10)")
    var priority: Int = Int.MAX_VALUE

    @Column(columnDefinition = "text")
    var img: String = ""
//
//    @Column(columnDefinition = "int(1)")
//    @Enumerated(EnumType.ORDINAL)
//    var stars: Constants.Stars = Constants.Stars.ONE
}