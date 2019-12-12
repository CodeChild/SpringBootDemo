package com.example.springbootdemo.dao

import com.example.springbootdemo.model.NewsModel
import org.springframework.data.jpa.repository.JpaRepository

interface NewsDao : JpaRepository<NewsModel, String> {

}