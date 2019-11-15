package com.onysakura.tools.test

import org.springframework.data.jpa.repository.JpaRepository

interface TestRepository : JpaRepository<Test, String>