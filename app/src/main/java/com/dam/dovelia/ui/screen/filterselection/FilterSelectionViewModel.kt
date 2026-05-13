package com.dam.dovelia.ui.screen.filterselection

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dam.dovelia.data.model.AccommodationTag
import com.dam.dovelia.data.model.CityResult
import com.dam.dovelia.data.network.RetrofitClient.apiService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Clase FilterSelectionViewModel:
 * Gestiona la lógica de los filtros de búsqueda para los alojamientos.

 * @author Daniel Rodríguez Pérez
 * @version 1.0
 */
@HiltViewModel
class FilterSelectionViewModel @Inject constructor() : ViewModel() {

    var state by mutableStateOf(FilterSelectionState())
        private set

    fun onCityQueryChange(newQuery: String) {
        state = state.copy(citySearchQuery = newQuery)
    }

    /**
     * Función searchCities:
     * Se conecta a la API de openmeteo para buscar ciudades que coincidanmcon el texto que el usaurio ha escrito
     * en el campo de busqueda, pero solo realiza la peticion si se han escrito al menos dos caracteres para
     * evitar sobrecargar el servidor con busquedas demasiado cortas.
     */
    fun searchCities() {
        if (state.citySearchQuery.length < 2) return

        viewModelScope.launch {
            try {
                val response = apiService.searchCity(name = state.citySearchQuery)
                if (response.isSuccessful) {
                    state = state.copy(citySearchResults = response.body()?.results ?: emptyList())
                }
            } catch (e: Exception) {
                state = state.copy(citySearchResults = emptyList())
            }
        }
    }

    /**
     * Función onCitySelected:
     * Procesa la seleccion de una ciudad del listado de resultados.
     * Construye el nombre completo de la ubicación (Ciudad, Region, Pais) para
     * que el filtro sea lo mas preciso posible y el usuario vea donde busca.
     *
     * @param city - El objeto con los datos de la ciudad seleccionada.
     */
    fun onCitySelected(city: CityResult) {
        state = if (city.id != -1) {
            val fullLocationName = listOfNotNull(
                city.name,
                city.admin1.takeIf { !it.isNullOrBlank() },
                city.country
            ).joinToString(", ")

            state.copy(
                city = fullLocationName,         // Esto es lo que se guarda/filtra
                citySearchQuery = fullLocationName, // Esto es lo que ve el usuario escrito
                citySearchResults = emptyList()
            )
        } else {
            state.copy(citySearchResults = emptyList())
        }
    }


    fun onCityChange(newVal: String) {
        state = state.copy(city = newVal)
    }

    fun onRoomsChange(newVal: Int) {
        state = state.copy(rooms = newVal)
    }

    fun onBathroomsChange(newVal: Int) {
        state = state.copy(bathrooms = newVal)
    }

    /**
     * Función onInitialTags:
     * Se encarga de mapear las etiquetas recibidas (como Strings) al enumerado
     * de AccommodationTag para que la pantalla muetsra los filtros activos.
     *
     * @param tagNames - Lista de nombres de las etiquetas a cargar.
     */
    fun onInitialTags(tagNames: List<String>) {
        val tags = tagNames.mapNotNull { name ->
            runCatching { AccommodationTag.valueOf(name) }.getOrNull()
        }
        state = state.copy(accommodationTags = tags)
    }

    fun onClearFilters() {
        state = FilterSelectionState()
    }

    fun onAccommodationTagChange(tag: AccommodationTag) {
        val currentTags = state.accommodationTags

        val newTags = if (currentTags.contains(tag)) {
            currentTags - tag
        } else {
            currentTags + tag
        }

        state = state.copy(accommodationTags = newTags)
    }
}