package com.dam.dovelia.ui.screen.profile

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dam.dovelia.data.network.BaseResult
import com.dam.dovelia.data.network.SessionManager
import com.dam.dovelia.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Clase ProfileViewModel:
 * Se encarga de gestionar la lógica para cargar y mostrar los datos del perfil.
 * Puede recuperar tanto la información del usuario logueado como la de cualquier otro usuario mediante su ID.
 *
 * @property repository
 * @property sessionManager
 *
 * @author Daniel Rodríguez Pérez
 * @version 1.0
 */
@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repository: UserRepository,
    private val sessionManager: SessionManager
): ViewModel() {
    var state : ProfileState by mutableStateOf(ProfileState.Loading)
        private set

    /**
     * Función loadUser:
     * Realiza la petición al servidor para obtener los datos de un usuario concreto.
     * Si no se pasa un ID por parámetro, intenta cargar el perfil del usuario
     * que tiene la sesion iniciada actualmente.
     *
     * * Gestiona la carga de tokens y IDs de forma asíncrona y cambia el estado
     * a NoData si falta algun dato esencial o la perición falla.
     *
     * @param targetUserId - ID del usuario a buscar (opcional).
     *
     * @author Daniel Rodríguez Pérez
     * @version 1.0
     */
    fun loadUser(targetUserId: String? = null) {
        viewModelScope.launch {
            state = ProfileState.Loading

            val token = withContext(Dispatchers.IO) { sessionManager.getAuthToken() }

            val userIdToFind = targetUserId ?: withContext(Dispatchers.IO) { sessionManager.fetchUserId() }

            if (token.isNullOrEmpty() || userIdToFind.isNullOrEmpty()) {
                state = ProfileState.NoData
                return@launch
            }

            state = when (val result = repository.getUserProfile(token, userIdToFind)) {
                is BaseResult.Success -> {
                    ProfileState.Success(user = result.data)
                }

                is BaseResult.Error -> {
                    ProfileState.NoData
                }
            }
        }
    }
}