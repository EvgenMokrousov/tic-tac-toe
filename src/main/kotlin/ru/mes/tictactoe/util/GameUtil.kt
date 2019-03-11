package ru.mes.tictactoe.util

import ru.mes.tictactoe.domain.Game

class GameUtil {
    companion object {
        fun isWinnerChar(game: Game, ch: Char): Boolean {
            val s = game.field

            val step = Math.sqrt(s.length.toDouble()).toInt()
            val targ = ch.toString().repeat(step)
            //check row
            var s1: String
            for (i in 0..step-1) {
                s1 = s.substring(i*step, step*(i+1))
                if (s1 == targ) return true
            }

            //check col
            for (i in 0..step-1) {
                s1 = ""
                for (j in 0..step-1) {
                    s1 += s[step*j + i]
                }
                if (s1 == targ) return true
            }

            //check diag
            s1 = ""
            var s2 = ""
            for (i in 0..step-1) {
                s1 += s[step*i + i]
                s2 += s[(step - 1) * (step - i)]
            }
            if (s1 == targ) return true
            s1 = s2
            if (s1 == targ) return true

            return false
        }

    }
}