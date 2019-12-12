package com.example.springbootdemo.controller

import com.example.springbootdemo.model.AudienceModel
import com.example.springbootdemo.model.LoginModel
import com.example.springbootdemo.util.JwtTokenUtil
import com.example.springbootdemo.util.JwtTokenUtil.createJWT
import com.example.springbootdemo.util.JwtTokenUtil.log
import lombok.extern.slf4j.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController
import sun.rmi.runtime.Log
import java.util.*
import javax.servlet.http.HttpServletResponse


@Slf4j
@RestController
class UserController {
    @Autowired
    lateinit var audience:AudienceModel

    @PostMapping("/login")
    fun adminLogin(response: HttpServletResponse, username: String?, password: String?): LoginModel { // 这里模拟测试, 默认登录成功，返回用户ID和角色信息
        val userId = UUID.randomUUID().toString()
        val role = "admin"
        // 创建token
        val token = createJWT(userId, username, role, audience)
        log.info("### 登录成功, token={} ###", token)
        // 将token放在响应头
        response.setHeader(JwtTokenUtil.AUTH_HEADER_KEY, JwtTokenUtil.TOKEN_PREFIX + token)
        // 将token响应给客户端
        return LoginModel(token)
    }
}