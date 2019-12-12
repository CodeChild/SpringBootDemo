package com.example.springbootdemo.util

import com.example.springbootdemo.constant.ResultCode
import com.example.springbootdemo.exception.AuthException
import com.example.springbootdemo.model.AudienceModel
import io.jsonwebtoken.*
import org.apache.logging.log4j.util.Base64Util
import org.slf4j.LoggerFactory
import java.security.Key
import java.util.*
import javax.crypto.spec.SecretKeySpec
import javax.xml.bind.DatatypeConverter


object JwtTokenUtil {
    val log = LoggerFactory.getLogger(JwtTokenUtil :: class.java)
    const val AUTH_HEADER_KEY = "Authorization"
    const val TOKEN_PREFIX = "Bearer "
    /**
     * 解析jwt
     * @param jsonWebToken
     * @param base64Security
     * @return
     */
    fun parseJWT(jsonWebToken: String?, base64Security: String?): Claims {
        return try {
            Jwts.parser()
                    .setSigningKey(DatatypeConverter.parseBase64Binary(base64Security))
                    .parseClaimsJws(jsonWebToken).getBody()
        } catch (eje: ExpiredJwtException) {
            log.error("===== Token过期 =====", eje)
            throw AuthException(ResultCode.PERMISSION_TOKEN_EXPIRED)
        } catch (e: Exception) {
            log.error("===== token解析异常 =====", e)
            throw AuthException(ResultCode.PERMISSION_TOKEN_INVALID)
        }
    }

    /**
     * 构建jwt
     * @param userId
     * @param username
     * @param role
     * @param audience
     * @return
     */
    fun createJWT(userId: String?, username: String?, role: String?, audience: AudienceModel): String? {
        return try { // 使用HS256加密算法
            val signatureAlgorithm: SignatureAlgorithm = SignatureAlgorithm.HS256
            val nowMillis = System.currentTimeMillis()
            val now = Date(nowMillis)
            //生成签名密钥
            val apiKeySecretBytes: ByteArray = DatatypeConverter.parseBase64Binary(audience.base64Secret)
            val signingKey: Key = SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName())
            //userId是重要信息，进行加密下
            val encryId = Base64Util.encode(userId)
            //添加构成JWT的参数
            val builder: JwtBuilder = Jwts.builder().setHeaderParam("typ", "JWT") // 可以将基本不重要的对象信息放到claims
                    .claim("role", role)
                    .claim("userId", userId)
                    .setSubject(username) // 代表这个JWT的主体，即它的所有人
                    .setIssuer(audience.clientId) // 代表这个JWT的签发主体；
                    .setIssuedAt(Date()) // 是一个时间戳，代表这个JWT的签发时间；
                    .setAudience(audience.name) // 代表这个JWT的接收对象；
                    .signWith(signatureAlgorithm, signingKey)
            //添加Token过期时间
            val TTLMillis: Int = audience.expiresSecond ?: -1
            if (TTLMillis >= 0) {
                val expMillis = nowMillis + TTLMillis
                val exp = Date(expMillis)
                builder.setExpiration(exp) // 是一个时间戳，代表这个JWT的过期时间；
                        .setNotBefore(now) // 是一个时间戳，代表这个JWT生效的开始时间，意味着在这个时间之前验证JWT是会失败的
            }
            //生成JWT
            builder.compact()
        } catch (e: Exception) {
            log.error("签名失败", e)
            throw AuthException(ResultCode.PERMISSION_SIGNATURE_ERROR)
        }
    }

    /**
     * 从token中获取用户名
     * @param token
     * @param base64Security
     * @return
     */
    fun getUsername(token: String?, base64Security: String?): String? {
        return parseJWT(token, base64Security).subject
    }

    /**
     * 从token中获取用户ID
     * @param token
     * @param base64Security
     * @return
     */
    fun getUserId(token: String?, base64Security: String?): String? {
        val userId = parseJWT(token, base64Security).get("userId", String::class.java)
        return  Base64.getDecoder().decode(userId).toString()
    }

    /**
     * 是否已过期
     * @param token
     * @param base64Security
     * @return
     */
    fun isExpiration(token: String?, base64Security: String?): Boolean {
        return parseJWT(token, base64Security).expiration.before(Date())
    }
}