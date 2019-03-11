package ru.mes.tictactoe.domain

import javax.persistence.*
import java.util.Date

@Entity
data class Game(
        val token: String = "",
        val size: Int = 3,
        val ownerName: String = "", //temp
        @ManyToOne @JoinColumn(name = "owner_id")
        val owner: User? = null,
        @ManyToOne @JoinColumn(name = "opponent_id")
        var opponent: User? = null,
        var start: Date = Date(),
        var lastAccess: Date = Date(),
        var duration: Long = 0L,
        var result: String = "", //"” || "owner” || "opponent” || "draw”
        var state: String = "ready", //"ready” || "playing” || "done”
        var field: String = "?".repeat(size*size),

        @Id @GeneratedValue
        val id: Long = 0L
)