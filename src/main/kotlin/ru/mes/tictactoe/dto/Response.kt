package ru.mes.tictactoe.dto

abstract class Response (
    var status: String = "ok",
    var code: Int = 0,
    var message: String = "ok"
    )