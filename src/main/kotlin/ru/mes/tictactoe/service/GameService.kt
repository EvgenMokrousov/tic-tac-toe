package ru.mes.tictactoe.service

import ru.mes.tictactoe.domain.Game

interface GameService {
    fun findAll(): List<Game>
    fun getOne(gameId: Long): Game
    fun save(game: Game): Game
    fun findByToken(token: String?): Game?
    fun delete(game: Game)
}