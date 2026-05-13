package com.dam.dovelia.ui.screen.discover

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
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Clase DiscoverViewModel:
 * Se encarga de gestionar la lógica de descubrimiento de usuarios.
 * Controla la carga de perfiles, aplicación de filtros y las interacciones de swipe.
 *
 * @property userRepository - Repositorio para peticiones de red y gestión de usuarios.
 * @property sessionManager - Gestor de sesión para obtener el token de autenticación.
 *
 * @author Daniel Rodríguez Pérez
 * @version 1.0
 */
@HiltViewModel
class DiscoverViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    var state by mutableStateOf<DiscoverState>(DiscoverState.Loading)
        private set

    private var loadedToken: String? = null
    private var currentCity: String? = null
    private var currentRooms: Int? = null
    private var currentBathrooms: Int? = null
    private var currentTags: List<String>? = null

    /**
     * Función verifySessionAndLoad:
     * Comprueba si la sesión es válida y si los filtros han cambiado antes de cargar.
     * Evita peticiones innecesarias si los datos de busqueda son los mismos que antes.
     *
     * @param city - Ciudad para filtrar resultados.
     * @param rooms - Número de habitaciones deseadas.
     * @param bathrooms - Número de baños deseados.
     * @param tags - Lista de etiquetas
     * @param forceRefresh - Obliga a recargar los datos aunque no cambien filtros.
     */
    fun verifySessionAndLoad(
        city: String?,
        rooms: Int?,
        bathrooms: Int?,
        tags: List<String>?,
        forceRefresh: Boolean = false
    ) {
        viewModelScope.launch {
            val token = withContext(Dispatchers.IO) { sessionManager.getAuthToken() }

            val tokenChanged = token != loadedToken
            val filtersChanged =
                city != currentCity || rooms != currentRooms || bathrooms != currentBathrooms || tags != currentTags

            if (tokenChanged || filtersChanged || forceRefresh) {
                loadedToken = token
                currentCity = city
                currentRooms = rooms
                currentBathrooms = bathrooms
                currentTags = tags

                loadUsers(city, rooms, bathrooms, tags)
            }
        }
    }


    /**
     * Función loadUsers:
     * Realiza la petición al repositorio para obtener nuevos perfiles de usuario.
     * Gestiona los estados de carga, éxito y falta de datos según la respuesta de la API.
     *
     * @param city - Ciudad para el filtrado.
     * @param rooms - Habitaciones mínimas.
     * @param bathrooms - Baños mínimos.
     * @param tags - Etiquetas de configuración.
     */
    fun loadUsers(city: String? = null, rooms: Int? = null, bathrooms: Int? = null, tags: List<String>? = null) {
        viewModelScope.launch {
            state = DiscoverState.Loading

            delay(1500)

            val token = withContext(Dispatchers.IO) {
                sessionManager.getAuthToken()
            }

            if (token.isNullOrEmpty()) {
                state = DiscoverState.NoData
                return@launch
            }

            when (val result = userRepository.getDiscoverUsers(token, city, rooms, bathrooms, tags)) {                is BaseResult.Success -> {
                    val users = result.data
                    state = if (users.isEmpty()) {
                        DiscoverState.NoData
                    } else {
                        DiscoverState.Success(
                            cards = users,
                            currentCard = users.firstOrNull()
                        )
                    }
                }

                is BaseResult.Error -> {
                    state = DiscoverState.NoData
                }
            }
        }
    }

    /**
     * Función onButtonPressed:
     * Gestiona la pulsación de los botones de Like o Dislike de la interfaz.
     * Activa los triggers de animación para simular el deslizamiento de la tajreta.
     *
     * @param isLike - Define si la acción es un like o un dislike.
     */
    fun onButtonPressed(isLike: Boolean) {
        val currentState = state
        if (currentState is DiscoverState.Success && !currentState.isSwipeLoading) {
            if (currentState.cards.isEmpty()) return

            state = currentState.copy(
                isSwipeLoading = true,
                swipeRightTrigger = isLike,
                swipeLeftTrigger = !isLike
            )
        }
    }


    /**
     * Función onSwipe:
     * Procesa la acción de deslizado y actualiza la lista de cartas restantes.
     * Envia la interacción al servidor y gestiona la lógica de match si este ocurre.
     *
     * Muestra una animación de match durante unos segundos si la respuesta es positiva
     * y luego rfresca el estado para seguir descubriendo gente.
     *
     * @param isLike - Indica la dirección del swipe realizado por el usuario.
     */
    fun onSwipe(isLike: Boolean) {
        val currentState = state as? DiscoverState.Success ?: return
        val swipedUser = currentState.currentCard ?: return
        val remaining = currentState.cards.drop(1)

        state = if (remaining.isEmpty()) {
            DiscoverState.NoData
        } else {
            currentState.copy(
                cards = remaining,
                currentCard = remaining.firstOrNull(),
                swipeLeftTrigger = false,
                swipeRightTrigger = false,
                isSwipeLoading = false
            )
        }

        viewModelScope.launch {
            val token =
                withContext(Dispatchers.IO) { sessionManager.getAuthToken() } ?: return@launch

            when (val result = userRepository.swipeUser(token, swipedUser.id, isLike)) {
                is BaseResult.Success -> {
                    if (result.data.mutual_match) {

                        val latestState = state as? DiscoverState.Success ?: return@launch

                        state = latestState.copy(
                            matchUser = swipedUser,
                            showMatchAnimation = true
                        )

                        delay(4000)

                        val afterDelayState = state as? DiscoverState.Success
                        if (afterDelayState != null) {
                            state = afterDelayState.copy(
                                showMatchAnimation = false,
                                matchUser = null
                            )
                        }
                    }
                }

                is BaseResult.Error -> {}
            }
        }
    }
}