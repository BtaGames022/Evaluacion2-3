package com.levelupgamer.app.viewmodel

import android.app.Application
import android.database.sqlite.SQLiteConstraintException
import com.levelupgamer.app.LevelUpGamerApplication
// Importamos el UserRepository desde la ruta correcta
import com.levelupgamer.app.data.UserRepository
import com.levelupgamer.app.util.TestCoroutineRule
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Rule

@ExperimentalCoroutinesApi
class RegistrationViewModelTest : StringSpec({

    // --- 1. Configuración (Mocks y Reglas) ---

    // Regla para manejar corrutinas y el Main dispatcher
    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    // Mocks para las dependencias
    lateinit var mockApplication: LevelUpGamerApplication
    lateinit var mockUserRepository: UserRepository

    // El ViewModel que vamos a testear
    lateinit var viewModel: RegistrationViewModel

    // 'beforeTest' se ejecuta antes de cada test ("...")
    beforeTest {
        // Creamos los mocks
        mockApplication = mockk(relaxed = true)
        mockUserRepository = mockk()

        // Simulamos que la aplicación devuelve el repositorio mockeado
        every { mockApplication.userRepository } returns mockUserRepository

        // Creamos la instancia real del ViewModel, pero inyectando los mocks
        viewModel = RegistrationViewModel(mockApplication)

        // Pausamos la recolección de 'registrationSuccess'
        // para poder probar su valor explícitamente
        testCoroutineRule.testDispatcher.pauseDispatcher()
    }

    'afterTest' {
        clearAllMocks() // Limpia los mocks
    }

    // --- 2. Casos de Prueba ---

    "registerUser debe ser exitoso si los datos son válidos y el email no existe" {
        runTest(testCoroutineRule.testDispatcher) {
            // 1. Arrange (Preparar)

            // Definimos lo que debe hacer el mock
            // coEvery es para funciones 'suspend'
            coEvery { mockUserRepository.createUser(any()) } returns Unit

            // 2. Act (Actuar)
            viewModel.onNombreChange("Usuario Test")
            viewModel.onEmailChange("test@duocuc.cl")
            viewModel.onEdadChange("20")
            viewModel.onPasswordChange("123456")
            viewModel.onConfirmPasswordChange("123456")

            viewModel.registerUser()

            // Reanudamos el dispatcher para que el Flow emita el valor
            testCoroutineRule.testDispatcher.resumeDispatcher()

            // 3. Assert (Verificar)
            val success = viewModel.registrationSuccess.first() // Obtenemos el valor emitido
            val state = viewModel.uiState.value

            success shouldBe true // El evento de éxito se disparó
            state.isLoading shouldBe false
            state.generalError shouldBe null

            // Verificamos que 'createUser' fue llamado exactamente 1 vez
            coVerify(exactly = 1) { mockUserRepository.createUser(any()) }
        }
    }

    "registerUser debe fallar si la edad es menor a 18" {
        // 1. Arrange (No se necesita mock de repositorio)

        // 2. Act
        viewModel.onNombreChange("Menor de Edad")
        viewModel.onEmailChange("menor@test.cl")
        viewModel.onEdadChange("17") // Edad inválida
        viewModel.onPasswordChange("123456")
        viewModel.onConfirmPasswordChange("123456")

        viewModel.registerUser()

        // 3. Assert
        val state = viewModel.uiState.value
        state.edadError shouldBe "Debes ser mayor de 18 años"
        state.isLoading shouldBe false

        // Verificamos que 'createUser' NUNCA fue llamado
        coVerify(exactly = 0) { mockUserRepository.createUser(any()) }
    }

    "registerUser debe fallar si el email ya existe (SQLiteConstraintException)" {
        runTest(testCoroutineRule.testDispatcher) {
            // 1. Arrange
            // Simulamos que el repositorio lanza la excepción de email duplicado
            coEvery { mockUserRepository.createUser(any()) } throws SQLiteConstraintException()

            // 2. Act
            viewModel.onNombreChange("Usuario Duplicado")
            viewModel.onEmailChange("duplicado@test.cl")
            viewModel.onEdadChange("25")
            viewModel.onPasswordChange("123456")
            viewModel.onConfirmPasswordChange("123456")

            viewModel.registerUser()

            // 3. Assert
            val state = viewModel.uiState.value
            state.isLoading shouldBe false
            state.generalError shouldContain "El email ya está registrado"
        }
    }
})