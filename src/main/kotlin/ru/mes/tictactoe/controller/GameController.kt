package ru.mes.tictactoe.controller

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import javax.servlet.http.HttpServletResponse

@Controller
@RequestMapping("/games")
class GameController {
    @GetMapping
    fun game(@RequestParam accessToken: String, httpServletResponse: HttpServletResponse, model: Model): String {
        println("accessToken = $accessToken")
        httpServletResponse.addHeader("ACCESS-TOKEN", accessToken)
        model.addAttribute("accessToken", accessToken)
        return "game"
    }

}