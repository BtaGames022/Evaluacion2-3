package com.levelupgamer.app.data

import com.levelupgamer.app.model.User

/**
 * Repositorio que maneja las operaciones de datos para el Usuario.
 * Actúa como intermediario entre el ViewModel y el UserDao.
 */
class UserRepository(private val userDao: UserDao) {

    /**
     * Intenta insertar un nuevo usuario en la base de datos.
     * Esto es una función 'suspend' porque el DAO es 'suspend'.
     */
    suspend fun createUser(user: User) {
        userDao.insert(user)
    }

    /**
     * Busca un usuario por email.
     * (Lo usaremos para el Login más adelante)
     */
    suspend fun getUserByEmail(email: String): User? {
        return userDao.getByEmail(email)
    }
}