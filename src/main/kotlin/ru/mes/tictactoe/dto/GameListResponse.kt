package ru.mes.tictactoe.dto

import ru.mes.tictactoe.domain.Game

data class GameListResponse(
    var games: List<Game>? = null

    ): Response()
