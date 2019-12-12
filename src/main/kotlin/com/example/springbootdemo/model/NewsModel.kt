package com.example.springbootdemo.model

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "news")
data class NewsModel(
        @Id
        @Column(length = 32)
        val id: Int = -1,
        @Column(length = 256)
        val title: String = "",
        val content: String = ""
)