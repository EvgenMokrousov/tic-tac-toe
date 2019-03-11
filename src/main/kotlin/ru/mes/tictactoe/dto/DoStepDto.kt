package ru.mes.tictactoe.dto

import javax.validation.constraints.Max
import javax.validation.constraints.Min

data class DoStepDto(
        @field:Min(value = 0, message = "must in range 0..9")
        @field:Max(value = 9, message = "must in range 0..9")
        val col: Int = 0,

        @field:Min(value = 0, message = "must in range 0..9")
        @field:Max(value = 9, message = "must in range 0..9")
        val row: Int = 0
)