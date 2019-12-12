package com.example.springbootdemo.controller

import com.example.springbootdemo.dao.NewsDao
import com.example.springbootdemo.model.NewsModel
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class NewsController {
    @Autowired
    lateinit var newsDao: NewsDao

    @RequestMapping("/news")
    fun getNews() : List<NewsModel>{
        val newsList= mutableListOf<NewsModel>()
        repeat(10){
            newsList.add(NewsModel(it,"title$it","content$it ".repeat(10)))
        }
        return newsList
    }

    @RequestMapping("/news/db")
    fun getNewsDb() : List<NewsModel> {
        return newsDao.findAll()
    }
}