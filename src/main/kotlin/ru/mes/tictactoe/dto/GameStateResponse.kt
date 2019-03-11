package ru.mes.tictactoe.dto

data class GameStateResponse(
    var youTurn: Boolean = false,
    var gameDuration: Long = 0,
    var field: String = "",
    var winner: String? = "",

    var ownerName: String? = "",
    var opponentName: String? = "",
    var whoIs: String = "viewer"
): Response()
