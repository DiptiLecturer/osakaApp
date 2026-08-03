package org.freedu.osakatelevison.data.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.freedu.osakatelevison.data.Repositories.ProductRepository
import org.freedu.osakatelevison.data.Repositories.ProductRepositoryImpl
import org.freedu.osakatelevison.model.Product

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


class ProductViewModel(
    private val repository: ProductRepository = ProductRepositoryImpl()
) : ViewModel() {

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
                val totalCount = products.size
                val fanCount = products.count { isFanProduct(it) }
                val tvCount = products.count { isTvProduct(it) }

                val categories = listOf(
                    "All ($totalCount)",
                    "Fan ($fanCount)",
                    "TV ($tvCount)"
                )

                val filteredProducts = products.filter { product ->
                    val matchesCategory = when {
                        selectedCategory.startsWith("Fan") -> isFanProduct(product)
                        selectedCategory.startsWith("TV") -> isTvProduct(product)
                        else -> true
                    }

                    val matchesSearch = product.name.contains(searchQuery, ignoreCase = true) ||
                            product.category.contains(searchQuery, ignoreCase = true)

                    matchesCategory && matchesSearch
                }

                val activeCategory = categories.find {
                    it.startsWith(selectedCategory.takeWhile { char -> char != ' ' && char != '(' })
                } ?: categories.first()

                ProductUiState.Success(
                    products = filteredProducts,
                    categories = categories,
                    selectedCategory = activeCategory,
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

            repository.getActiveProducts()
                .onSuccess { result ->
                    _allProducts.value = result
                }
                .onFailure { exception ->
                    _errorMessage.value = exception.localizedMessage ?: "Failed to load products"
                }

            _isLoading.value = false
        }
    }

    private fun isFanProduct(product: Product): Boolean {
        val cat = product.category.lowercase()
        val name = product.name.lowercase()
        val size = product.size.trim()

        return cat.contains("fan") ||
                name.contains("fan") ||
                size in listOf("12", "16", "18", "12\"", "16\"", "18\"") ||
                listOf("12", "16", "18").any { name.contains(it) }
    }

    private fun isTvProduct(product: Product): Boolean {
        return !isFanProduct(product)
    }

    fun selectCategory(category: String) {
        _selectedCategory.value = category
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }
}