package ru.mes.tictactoe

import io.jsonwebtoken.Jwts
import org.junit.Test

class temp {

    @Test
    fun getToken() {
        val signingKey = "stroicontrol"
        val badAccessToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWI!@#iOiJ1c2VyMTEwIiwiZ2FtZVRva2VuIjoiYTA2NmNjMmItMGIxMC00YWExLTgwZGItNTNmMGRlZmZmOGU0IiwiaWF0IjoxNTUwNjYzMjE1fQ.z-T3rGhV-DviTucOFeZ26DgGrs-_GZWm58gw68OFsCM"
        val gameToken = try {
            Jwts.parser().setSigningKey(signingKey).parseClaimsJws(badAccessToken).body["gameToken"].toString()
        } catch (e: Exception) {
            null
        }
        println("gameToken = $gameToken")
    }
}
