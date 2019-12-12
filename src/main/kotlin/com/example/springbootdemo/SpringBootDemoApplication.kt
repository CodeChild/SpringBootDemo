package com.example.springbootdemo

import com.example.springbootdemo.model.AudienceModel
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableConfigurationProperties(AudienceModel::class)
class SpringBootDemoApplication

fun main(args: Array<String>) {
    runApplication<SpringBootDemoApplication>(*args)
}
