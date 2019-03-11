package ru.mes.tictactoe.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping

@Controller
class HomeController {
    @GetMapping("/")
    fun home()
            = "redirect:/index"

    @GetMapping("index")
    fun index():String {
        return "index"

    }
}