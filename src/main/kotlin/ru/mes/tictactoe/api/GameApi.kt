package ru.mes.tictactoe.api

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.validation.Errors

import ru.mes.tictactoe.domain.Game
import ru.mes.tictactoe.domain.User
import ru.mes.tictactoe.dto.*
import ru.mes.tictactoe.service.GameService
import ru.mes.tictactoe.service.UserService
import ru.mes.tictactoe.util.GameUtil
import ru.mes.tictactoe.util.JwtUtil
import ru.mes.tictactoe.util.ErrorUtil

import java.util.*
import javax.servlet.http.HttpServletRequest
import javax.validation.Valid

@RestController
@RequestMapping("/games")
class GameApi {
    @Autowired lateinit var gameService: GameService
    @Autowired lateinit var userService: UserService

    @Value("\${my.signingKey}")
    lateinit var signingKey: String

    @PostMapping("/new")
    fun newGame(@Valid @RequestBody newGame: NewGameDto, error: Errors): ResponseEntity<NewGameResponse> {
        val result = NewGameResponse()
        if (error.hasErrors()) {
            ErrorUtil.setResultWithWrongInputDataError(result, error)
            return ResponseEntity.badRequest().body(result)
        }
        result.gameToken = UUID.randomUUID().toString()
        result.accessToken = JwtUtil.generateAccessToken(signingKey, newGame.userName, result.gameToken)
        val owner = userService.save(User(newGame.userName, result.accessToken))
        gameService.save(Game(token = result.gameToken, size = newGame.size, ownerName = newGame.userName, state = "ready", owner = owner))
        println(newGame)
        return ResponseEntity.ok()
                .header("ACCESS-TOKEN", result.accessToken)
                .body(result)
    }

    @GetMapping("/list")
    fun getGames(): ResponseEntity<GameListResponse> {
        val result = GameListResponse()
        result.games = gameService.findAll()
        return ResponseEntity.ok(result)
    }

    @PostMapping("/join")
    fun joinGame(@Valid @RequestBody joinGame: JoinGameDto, error: Errors): ResponseEntity<JoinGameResponse> {
        val result = JoinGameResponse()
        val game = gameService.findByToken(joinGame.gameToken)
        if (game == null) {
            ErrorUtil.setResultWithBadGameError(result)
            return ResponseEntity.badRequest().body(result)
        }
        if (error.hasErrors() && !isViewer(game)) {
            ErrorUtil.setResultWithWrongInputDataError(result, error)
            return ResponseEntity.badRequest().body(result)
        }
        if (isOpponent(game)) { //game begin
            result.accessToken = JwtUtil.generateAccessToken(signingKey, joinGame.userName, joinGame.gameToken)
            game.opponent = userService.save(User(joinGame.userName, result.accessToken))
            game.state = "playing"
            game.start = Date()
            game.lastAccess = game.start
            gameService.save(game)
        } else { //view only
            result.accessToken = JwtUtil.generateAccessToken(signingKey, "viewer", joinGame.gameToken)
            result.message = "view mode"
        }

        return ResponseEntity.ok(result)
    }

    fun isViewer(game: Game) =
            (game.state == "playing" || game.state ==  "done")

    fun isOpponent(game: Game) =
            (game.opponent == null && game.state == "ready")

    @GetMapping("/state")
    fun stateGame(httpRequest: HttpServletRequest): ResponseEntity<GameStateResponse> {
        val accessToken = httpRequest.getHeader("ACCESS-TOKEN")
        val gameToken = JwtUtil.parseAccessToken(signingKey, accessToken)
        val result = GameStateResponse()
        val user = userService.findByAccessToken(accessToken)
        val game = gameService.findByToken(gameToken)
        if (game == null) {
            ErrorUtil.setResultWithBadGameError(result)
            return ResponseEntity.badRequest().body(result)
        }
        result.whoIs = getCurrentKindOfPlayer(user, game)
        result.ownerName = game.owner?.name
        result.opponentName = game.opponent?.name
        when (game.state) {
            "done" -> {
                result.winner = getWinnerName(game.result, result)
                result.message = "Game over, winner is ${result.winner}"
                result.gameDuration = getGameTotalTime(game)
            }
            "playing" -> {
                game.result = getGameResult(game)
                if (isGameOver(game)) {
                    result.message = "Game over"
                    game.state = "done"
                    game.duration = getGameTotalTime(game)
                    gameService.save(game)
                } else {
                    result.gameDuration = getGameDuration(game)
                    game.duration = result.gameDuration
                    result.youTurn = if (isOwnerTurn(game)) {result.whoIs == "owner"}
                                     else {result.whoIs == "opponent"}
                    gameService.save(game)
                }
                if (getInactiveGameTime(game) >= 5*60*1000) {
                    gameService.delete(game)
                }
            }
            else -> {result.winner = ""}
        }
        result.field = game.field

        return ResponseEntity.ok(result)
    }

    fun getCurrentKindOfPlayer(user: User?, game: Game) =
        when (user) {
            game.owner -> "owner"
            game.opponent -> "opponent"
            else -> "viewer"
        }

    fun getWinnerName(gameResult: String, result: GameStateResponse) =
        when (gameResult) {
            "owner" -> result.ownerName
            "opponent" -> result.opponentName
            "draw" -> "draw"
            else -> ""
        }

    fun getGameResult(game: Game) =
        when {
            GameUtil.isWinnerChar(game, 'X') -> "owner"
            GameUtil.isWinnerChar(game, '0') -> "opponent"
            game.field.count{it=='?'} == 0       -> "draw"
            else -> ""
        }

    fun isGameOver(game: Game) =
            (game.result != "")

    fun getGameDuration(game: Game) =
            Date().time - game.start.time

    fun getGameTotalTime(game: Game) =
            game.lastAccess.time - game.start.time

    fun getInactiveGameTime(game: Game) =
            Date().time - game.lastAccess.time

    @PostMapping("/do_step")
    fun doStep(@Valid @RequestBody step: DoStepDto, httpRequest: HttpServletRequest, error: Errors): ResponseEntity<DoStepResponse> {
        val result = DoStepResponse()
        if (error.hasErrors()) {
            ErrorUtil.setResultWithWrongInputDataError(result, error)
            return ResponseEntity.badRequest().body(result)
        }
        val accessToken = httpRequest.getHeader("ACCESS-TOKEN")
        val gameToken = JwtUtil.parseAccessToken(signingKey, accessToken)
        val game = gameService.findByToken(gameToken)
        val user = userService.findByAccessToken(accessToken)
        if (game == null || user == null) {
            ErrorUtil.setResultWithBadGameOrUserError(result)
            return ResponseEntity.badRequest().body(result)
        }
        if (game.state != "playing") {
            result.code = 440
            return ResponseEntity.badRequest().body(result)
        }
        game.field = setGameField(step, game, user)
        if (game.field == "") {
            ErrorUtil.setResultWithBadStep(result)
            return ResponseEntity.badRequest().body(result)
        }
        game.lastAccess = Date()
        gameService.save(game)

        return ResponseEntity.ok(result)
    }

    fun setGameField(step: DoStepDto, game: Game, user: User): String {
        var field = game.field
        val side = Math.sqrt(field.length.toDouble()).toInt()
        val positionIndex = step.row*(side)+step.col
        if (field[positionIndex] != '?') return ""
        val build = StringBuilder(field)
        if (!isOwnerTurn(game) && game.opponent == user) {
            //opponent turn
            build.setCharAt(positionIndex, '0')
        } else if (isOwnerTurn(game) && game.owner == user) {
            //owner turn
            build.setCharAt(positionIndex, 'X')
        } else {
            return ""
        }
        return build.toString()
    }

    fun isOwnerTurn(game: Game): Boolean {
        val numberOfCrosses = game.field.count { it == 'X' }
        val numberOfZeros = game.field.count { it == '0' }
        return (numberOfCrosses <= numberOfZeros)
    }

}