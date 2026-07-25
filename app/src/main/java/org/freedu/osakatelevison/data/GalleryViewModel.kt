package org.freedu.osakatelevison.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Order
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface GalleryUiState {
    object Loading : GalleryUiState
    data class Success(val items: List<SupabaseGalleryItem>) : GalleryUiState
    data class Error(val message: String) : GalleryUiState
}

class GalleryViewModel : ViewModel() {

    private val _uiState = MutableStateFlow<GalleryUiState>(GalleryUiState.Loading)
    val uiState: StateFlow<GalleryUiState> = _uiState.asStateFlow()

    init {
        fetchGalleryImages()
    }

    fun fetchGalleryImages() {
        viewModelScope.launch {
            _uiState.value = GalleryUiState.Loading
            try {
                // Fetch rows where is_active = true and order by display_order ascending
                val result = SupabaseProvider.client
                    .from("gallery")
                    .select {
                        filter {
                            eq("is_active", true)
                        }
                        order(column = "display_order", order = Order.ASCENDING)
                    }
                    .decodeList<SupabaseGalleryItem>()

                _uiState.value = GalleryUiState.Success(result)
            } catch (e: Exception) {
                _uiState.value = GalleryUiState.Error(e.localizedMessage ?: "Failed to fetch gallery")
            }
        }
    }
}