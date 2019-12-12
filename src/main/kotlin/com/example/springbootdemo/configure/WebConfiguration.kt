package com.example.springbootdemo.configure

import com.example.springbootdemo.interceptor.JwtInterceptor
import com.example.springbootdemo.model.AudienceModel
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer


@Configuration
class WebConfiguration : WebMvcConfigurer {
    @Bean
    fun getJwtInterceptor() = JwtInterceptor()
    /**
     * 添加拦截器
     */
    override fun addInterceptors(registry: InterceptorRegistry) { //拦截路径可自行配置多个 可用 ，分隔开
        registry.addInterceptor(getJwtInterceptor()).addPathPatterns("/news/db")
    }
    override fun addCorsMappings(registry: CorsRegistry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowCredentials(true)
                .allowedMethods("GET", "POST", "PUT", "DELETE", "HEAD")
                .allowedHeaders("*")
    }
}