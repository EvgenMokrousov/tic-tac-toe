package ru.mes.tictactoe.service

import ru.mes.tictactoe.domain.User

interface UserService {
    fun save(user: User): User
    fun findByAccessToken(accessToken: String): User?
}