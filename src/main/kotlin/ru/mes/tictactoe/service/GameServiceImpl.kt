package ru.mes.tictactoe.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.mes.tictactoe.domain.Game
import ru.mes.tictactoe.repository.GameRepository

@Service
@Transactional(readOnly = true)
class GameServiceImpl: GameService {
    @Autowired lateinit var gameRepository: GameRepository

    override fun findAll(): List<Game> {
        return gameRepository.findAll()
    }

    override fun getOne(gameId: Long): Game {
        return gameRepository.getOne(gameId)
    }

    @Transactional
    override fun save(game: Game): Game {
        return gameRepository.save(game)
    }

    override fun findByToken(token: String?): Game? {
        return gameRepository.findByToken(token)
    }

    @Transactional
    override fun delete(game: Game) {
        gameRepository.delete(game)
    }
}