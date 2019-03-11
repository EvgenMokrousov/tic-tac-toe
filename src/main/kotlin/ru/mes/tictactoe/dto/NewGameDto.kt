package ru.mes.tictactoe.dto

import javax.validation.constraints.Max
import javax.validation.constraints.Min
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

data class NewGameDto(
        @field:NotBlank(message = "user name can't empty!")
        @field:Size(min = 3, max = 15, message = "user name must more then {min} and less then {max}")
        val userName: String = "",

        @field:Min(value = 3, message = "size must in range 3..10")
        @field:Max(value = 10, message = "size must in range 3..10")
        val size: Int = 0
)