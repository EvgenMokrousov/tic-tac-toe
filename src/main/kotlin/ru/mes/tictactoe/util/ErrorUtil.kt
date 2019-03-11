package ru.mes.tictactoe.util

import org.springframework.validation.Errors
import ru.mes.tictactoe.dto.Response

class ErrorUtil {
    companion object {
        fun setResultWithWrongInputDataError(result: Response, error: Errors) {
            result.message = error.allErrors.asSequence().map{it.defaultMessage}.joinToString(separator = "; ")
            result.status = "error"
            result.code = 400
        }

        fun setResultWithBadGameError(result: Response) {
            result.message = "bad game"
            result.status = "error"
            result.code = 410
        }

        fun setResultWithBadGameOrUserError(result: Response) {
            result.message = "bad game or user"
            result.status = "error"
            result.code = 411
        }

        fun setResultWithBadStep(result: Response) {
            result.message = "Bad step"
            result.status = "error"
            result.code = 420
        }
    }
}
