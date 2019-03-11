package ru.mes.tictactoe.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.mes.tictactoe.domain.User
import ru.mes.tictactoe.repository.UserRepository

@Service
@Transactional(readOnly = true)
class UserServiceImpl: UserService {
    @Autowired lateinit var userRepository: UserRepository

    @Transactional
    override fun save(user: User): User {
        return userRepository.save(user)
    }

    override fun findByAccessToken(accessToken: String): User? {
        return userRepository.findByAccessToken(accessToken)
    }
}