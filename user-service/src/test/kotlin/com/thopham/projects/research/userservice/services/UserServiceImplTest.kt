package com.thopham.projects.research.userservice.services

import com.thopham.projects.research.userservice.core.PasswordDoesNotMatchException
import com.thopham.projects.research.userservice.core.UserAlreadyExistsException
import com.thopham.projects.research.userservice.core.UserDoesNotExistsException
import com.thopham.projects.research.userservice.entities.User
import com.thopham.projects.research.userservice.repositories.UserRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner
import org.springframework.test.context.ActiveProfiles
import reactor.core.publisher.Mono
import kotlin.math.exp

@RunWith(MockitoJUnitRunner::class)
@ActiveProfiles("dev")
class UserServiceImplTest {
    @Mock
    lateinit var userRepository: UserRepository
    lateinit var userService: UserService
    @Before
    fun setup() {
        userService = UserServiceImpl(
            userRepository
        )
    }
    fun generateUser(): User {
        return User(
            "1",
            "0965784238",
            "thopvna@gmail.com",
            "thiendia98"
        )
    }
    @Test
    fun `Register happy path`() {
        val expected = generateUser()
        val params = RegisterUserParams(
            expected.phone,
            expected.email,
            expected.password
        )
        //Given
        `when`(userRepository.existsByPhone(expected.phone))
            .thenReturn(Mono.just(false))
        `when`(userRepository.save(any<User>()))
            .thenReturn(Mono.just(expected))

        //When
        val actual = userService.register(
            params
        )
        //Verify
        actual.subscribe {result ->
            assertThat(result.user).isEqualTo(expected)
        }
        verify(userRepository, times(1)).existsByPhone(expected.phone)
        verify(userRepository, times(1)).save(ArgumentMatchers.any<User>())

    }
    @Test
    fun `Register failed path(user already exists)` () {
        val expected = generateUser()
        `when`(userRepository.existsByPhone(expected.phone))
            .thenReturn(Mono.just(true))

        val params = RegisterUserParams(
            expected.phone,
            expected.email,
            expected.password
        )
        val actual = userService.register(
            params
        )


        actual
            .doOnError {err ->
                assertThat(err).isInstanceOf(UserAlreadyExistsException::class.java)
            }
            .subscribe()
        verify(userRepository, times(1)).existsByPhone(expected.phone)
    }
    @Test
    fun `Login happy path`() {
        //Arrange
        val expected = generateUser()
        `when`(userRepository.existsByPhone(expected.phone))
            .thenReturn(Mono.just(true))
        `when`(userRepository.findByPhone(expected.phone))
            .thenReturn(Mono.just(expected))

        val params = LoginUserParams(
            phone = expected.phone,
            password = expected.password
        )
        //Do
        val actual = userService.login(
            params
        )
        //Verify
        actual.subscribe {result ->
            assertThat(result.user).isEqualTo(expected)
        }
        verify(userRepository, times(1)).existsByPhone(expected.phone)
        verify(userRepository, times(1)).findByPhone(expected.phone)
    }
    @Test
    fun `Login failed path(user doesn't exists)`() {
        val expected = generateUser()
        `when`(userRepository.existsByPhone(expected.phone))
            .thenReturn(Mono.just(false))

        val params = LoginUserParams(
            phone = expected.phone,
            password = expected.password
        )
        val actual = userService.login(
            params
        )
        actual
            .doOnError {err ->
                assertThat(err).isInstanceOf(UserDoesNotExistsException::class.java)
            }
            .subscribe()

        verify(userRepository, times(1)).existsByPhone(expected.phone)
    }
    @Test
    fun `Login failed path(password doesn't match)`() {
        val expected = generateUser()
        `when`(userRepository.existsByPhone(expected.phone))
            .thenReturn(Mono.just(true))
        `when`(userRepository.findByPhone(expected.phone))
            .thenReturn(Mono.just(expected.copy(password = expected.password + "1")))

        val params = LoginUserParams(
            phone = expected.phone,
            password = expected.password
        )
        val actual = userService.login(
            params
        )
        actual
            .doOnError {err ->
                assertThat(err).isInstanceOf(PasswordDoesNotMatchException::class.java)
            }
            .subscribe()

        verify(userRepository, times(1)).existsByPhone(expected.phone)
        verify(userRepository, times(1)).findByPhone(expected.phone)
    }
}