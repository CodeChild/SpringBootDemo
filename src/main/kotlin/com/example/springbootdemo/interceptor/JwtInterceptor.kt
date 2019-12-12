package com.example.springbootdemo.interceptor

import com.example.springbootdemo.constant.ResultCode
import com.example.springbootdemo.exception.AuthException
import com.example.springbootdemo.model.AudienceModel
import com.example.springbootdemo.util.JwtTokenUtil
import com.example.springbootdemo.util.JwtTokenUtil.log
import com.example.springbootdemo.util.JwtTokenUtil.parseJWT
import lombok.extern.slf4j.Slf4j
import org.apache.naming.factory.BeanFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Component
import org.springframework.web.context.support.WebApplicationContextUtils
import org.springframework.web.method.HandlerMethod
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


@Slf4j
class JwtInterceptor : HandlerInterceptorAdapter() {
    @Autowired
    lateinit var audience: AudienceModel

    @Throws(Exception::class)
    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        if (HttpMethod.OPTIONS.toString() == request.method) {
            response.status = HttpServletResponse.SC_OK
            return true
        }
        // 获取请求头信息authorization信息
        val authHeader = request.getHeader(JwtTokenUtil.AUTH_HEADER_KEY)
        log.info("## authHeader= {}", authHeader)
        if (authHeader.isBlank() || !authHeader.startsWith(JwtTokenUtil.TOKEN_PREFIX)) {
            log.info("### 用户未登录，请先登录 ###")
            throw AuthException(ResultCode.USER_NOT_LOGGED_IN)
        }
        // 获取token
        val token = authHeader.substring(7)
        if (audience.base64Secret == null) {
            val factory = WebApplicationContextUtils.getRequiredWebApplicationContext(request.servletContext)
            audience = factory.getBean("audience") as AudienceModel
        }
        // 验证token是否有效--无效已做异常抛出，由全局异常处理后返回对应信息
        parseJWT(token, audience.base64Secret)
        return true
    }
}