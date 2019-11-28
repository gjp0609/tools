package com.onysakura.tools.kindle

import org.springframework.data.jpa.repository.JpaRepository

interface ArticleRepository : JpaRepository<Article, String>