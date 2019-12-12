package com.example.springbootdemo.model

import lombok.Data
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Primary
import org.springframework.stereotype.Component

@Data
@ConstructorBinding
@ConfigurationProperties(prefix = "audience")
data class AudienceModel(
        val clientId: String? = null,
        val base64Secret: String? = null,
        val name : String? = null,
        val expiresSecond: Int? = null
)