package ru.mes.tictactoe.domain

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
data class User(
        val name: String = "",
        val accessToken: String = "",

        @Id @GeneratedValue
        val id: Long = 0L
)