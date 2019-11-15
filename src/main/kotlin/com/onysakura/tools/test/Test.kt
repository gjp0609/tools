package com.onysakura.tools.test

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
class Test(@Id
           @GeneratedValue(strategy = GenerationType.AUTO)
           var id: Int?,
           var data: Long) {

    constructor(data: Long) : this(null, data)

    constructor() : this(0)

    override fun toString(): String {
        return "Test(id=$id, data=$data)"
    }
}