package ru.mes.tictactoe.repository

import org.springframework.data.jpa.repository.JpaRepository
import ru.mes.tictactoe.domain.Game

interface GameRepository: JpaRepository<Game, Long> {
    fun findByToken(token: String?): Game?
}
