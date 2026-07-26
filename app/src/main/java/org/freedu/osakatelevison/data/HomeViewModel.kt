package org.freedu.osakatelevison.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Order
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


sealed interface HomeUiState {
    object Loading : HomeUiState
    data class Success(val heroSlides: List<HeroSlide>) : HomeUiState
    data class Error(val message: String) : HomeUiState
}

class HomeViewModel : ViewModel() {

    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        fetchHeroSlides()
    }

    fun fetchHeroSlides() {
        viewModelScope.launch {
            _uiState.value = HomeUiState.Loading
            try {
                // Fetch rows where is_active = true and order by display_order ascending
                val slides = SupabaseProvider.client
                    .from("hero_slides")
                    .select {
                        filter {
                            eq("is_active", true)
                        }
                        order(column = "display_order", order = Order.ASCENDING)
                    }
                    .decodeList<HeroSlide>()

                _uiState.value = HomeUiState.Success(slides)
            } catch (e: Exception) {
                _uiState.value = HomeUiState.Error(e.localizedMessage ?: "Failed to load hero slides")
            }
        }
    }
}