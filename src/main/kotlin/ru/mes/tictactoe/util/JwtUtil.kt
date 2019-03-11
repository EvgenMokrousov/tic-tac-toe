package ru.mes.tictactoe.util

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm

import java.util.Date

class JwtUtil {
    companion object {
        fun generateAccessToken(signingKey: String, subject: String, gameToken: String): String {
            val nowMillis = System.currentTimeMillis()
            val now = Date(nowMillis)

            val builder = Jwts.builder()
                    .setSubject(subject)
                    .claim("gameToken", gameToken)
                    .setIssuedAt(now)
                    .signWith(SignatureAlgorithm.HS256, signingKey)

            return builder.compact()
        }

        fun parseAccessToken(signingKey: String?, accessToken: String?) =
                try {
                    Jwts.parser().setSigningKey(signingKey).parseClaimsJws(accessToken).body["gameToken"].toString()
                } catch (e: Exception) {
                    null
                }
    }
}
