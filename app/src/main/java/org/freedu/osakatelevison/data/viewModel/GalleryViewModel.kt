package org.freedu.osakatelevison.data.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.freedu.osakatelevison.data.Repositories.GalleryRepository
import org.freedu.osakatelevison.data.Repositories.GalleryRepositoryImpl
import org.freedu.osakatelevison.model.SupabaseGalleryItem
import kotlin.time.Duration.Companion.milliseconds


sealed interface GalleryUiState {
    object Loading : GalleryUiState
    data class Success(val items: List<SupabaseGalleryItem>) : GalleryUiState
    data class Error(val message: String) : GalleryUiState
}

class GalleryViewModel(
    private val repository: GalleryRepository = GalleryRepositoryImpl()
) : ViewModel() {

    private val _uiState = MutableStateFlow<GalleryUiState>(GalleryUiState.Loading)
    val uiState: StateFlow<GalleryUiState> = _uiState.asStateFlow()

    init {
        fetchGalleryImages()
    }

    fun fetchGalleryImages() {
        viewModelScope.launch {
            _uiState.value = GalleryUiState.Loading

            val startTime = System.currentTimeMillis()

            repository.getActiveGalleryItems().onSuccess { items ->
                    // Calculate remaining time needed to hit 3 seconds (3000 ms)
                    val elapsedTime = System.currentTimeMillis() - startTime
                    val remainingDelay = 500L - elapsedTime

                    if (remainingDelay > 0) {
                        delay(remainingDelay.milliseconds)
                    }

                    _uiState.value = GalleryUiState.Success(items)
                }.onFailure { exception ->
                    val elapsedTime = System.currentTimeMillis() - startTime
                    val remainingDelay = 500L - elapsedTime

                    if (remainingDelay > 0) {
                        delay(remainingDelay.milliseconds)
                    }

                    _uiState.value = GalleryUiState.Error(
                        exception.localizedMessage ?: "Failed to fetch gallery"
                    )
                }
        }
    }
}