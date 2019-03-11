package ru.mes.tictactoe.dto

data class NewGameResponse(
    var accessToken: String = "",
    var gameToken: String = ""

    ): Response()
