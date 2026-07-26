package org.freedu.osakatelevison.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Order
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed interface ProductUiState {
    object Loading : ProductUiState
    data class Success(
        val products: List<Product>,
        val categories: List<String>,
        val selectedCategory: String,
        val searchQuery: String
    ) : ProductUiState
    data class Error(val message: String) : ProductUiState
}

class ProductViewModel : ViewModel() {

    private val _allProducts = MutableStateFlow<List<Product>>(emptyList())
    private val _selectedCategory = MutableStateFlow("All")
    private val _searchQuery = MutableStateFlow("")
    private val _isLoading = MutableStateFlow(true)
    private val _errorMessage = MutableStateFlow<String?>(null)

    val uiState: StateFlow<ProductUiState> = combine(
        _allProducts,
        _selectedCategory,
        _searchQuery,
        _isLoading,
        _errorMessage
    ) { products, selectedCategory, searchQuery, isLoading, error ->
        when {
            isLoading -> ProductUiState.Loading
            error != null -> ProductUiState.Error(error)
            else -> {
                // Extract unique categories from backend
                val categories = listOf("All") + products.map { it.category }.distinct().sorted()

                // Filter products based on selected category & search string
                val filteredProducts = products.filter { product ->
                    val matchesCategory = selectedCategory == "All" || product.category.equals(selectedCategory, ignoreCase = true)
                    val matchesSearch = product.name.contains(searchQuery, ignoreCase = true) ||
                            product.category.contains(searchQuery, ignoreCase = true)
                    matchesCategory && matchesSearch
                }

                ProductUiState.Success(
                    products = filteredProducts,
                    categories = categories,
                    selectedCategory = selectedCategory,
                    searchQuery = searchQuery
                )
            }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ProductUiState.Loading)

    init {
        fetchProducts()
    }

    fun fetchProducts() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                // Query Rule: Fetch rows where is_active = true and order by category ascending
                val result = SupabaseProvider.client
                    .from("products")
                    .select {
                        filter {
                            eq("is_active", true)
                        }
                        order(column = "category", order = Order.ASCENDING)
                    }
                    .decodeList<Product>()

                _allProducts.value = result
            } catch (e: Exception) {
                _errorMessage.value = e.localizedMessage ?: "Failed to load products"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun selectCategory(category: String) {
        _selectedCategory.value = category
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }
}