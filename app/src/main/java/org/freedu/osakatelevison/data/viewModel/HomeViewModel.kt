package org.freedu.osakatelevison.data.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.freedu.osakatelevison.data.Repositories.HomeRepository
import org.freedu.osakatelevison.data.Repositories.HomeRepositoryImpl
import org.freedu.osakatelevison.model.HeroSlide
import org.freedu.osakatelevison.model.Product


sealed interface HomeUiState {
    object Loading : HomeUiState
    data class Success(val heroSlides: List<HeroSlide>) : HomeUiState
    data class Error(val message: String) : HomeUiState
}


class HomeViewModel(
    private val repository: HomeRepository = HomeRepositoryImpl()
) : ViewModel() {

    private val _heroSlidesState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val heroSlidesState: StateFlow<HomeUiState> = _heroSlidesState.asStateFlow()

    private val _highlightProducts = MutableStateFlow<List<Product>>(emptyList())
    private val _selectedHighlightTab = MutableStateFlow("All")
    private val _isHighlightsLoading = MutableStateFlow(true)

    // Filtered list based on active highlight tab
    val filteredHighlights: StateFlow<List<Product>> = combine(
        _highlightProducts,
        _selectedHighlightTab
    ) { products, selectedTab ->
        when (selectedTab) {
            "Fan" -> products.filter { isFanProduct(it) }
            "TV" -> products.filter { !isFanProduct(it) }
            else -> products
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val selectedHighlightTab: StateFlow<String> = _selectedHighlightTab.asStateFlow()
    val isHighlightsLoading: StateFlow<Boolean> = _isHighlightsLoading.asStateFlow()

    init {
        loadHomeScreenData()
    }

    fun loadHomeScreenData() {
        fetchHeroSlides()
        fetchHighlights()
    }

    private fun fetchHeroSlides() {
        viewModelScope.launch {
            _heroSlidesState.value = HomeUiState.Loading
            repository.getHeroSlides()
                .onSuccess { slides -> _heroSlidesState.value = HomeUiState.Success(slides) }
                .onFailure { exception ->
                    _heroSlidesState.value = HomeUiState.Error(
                        exception.localizedMessage ?: "Failed to load hero slides"
                    )
                }
        }
    }

    private fun fetchHighlights() {
        viewModelScope.launch {
            _isHighlightsLoading.value = true
            repository.getHighlightProducts()
                .onSuccess { products -> _highlightProducts.value = products }
                .onFailure { /* Optionally handle failure */ }
            _isHighlightsLoading.value = false
        }
    }

    fun selectHighlightTab(tab: String) {
        _selectedHighlightTab.value = tab
    }

    private fun isFanProduct(product: Product): Boolean {
        val cat = product.category.lowercase()
        val name = product.name.lowercase()
        val size = product.size?.trim() ?: ""
        return cat.contains("fan") ||
                name.contains("fan") ||
                size in listOf("12", "16", "18", "12\"", "16\"", "18\"")
    }
}