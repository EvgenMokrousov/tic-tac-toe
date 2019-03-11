package ru.mes.tictactoe.repository

import org.springframework.data.jpa.repository.JpaRepository
import ru.mes.tictactoe.domain.User

interface UserRepository: JpaRepository<User, Long> {
    fun findByAccessToken(accessToken: String): User?
}