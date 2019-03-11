package ru.mes.tictactoe.dto

import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

data class JoinGameDto(
        @field:NotBlank(message = "game token can't empty!")
        @field:Size(min = 36, max = 36, message = "bad game token")
        val gameToken: String = "",

        @field:NotBlank(message = "user name can't empty!")
        @field:Size(min = 3, max = 15, message = "user name must more then {min} and less then {max}")
        val userName: String = ""
)