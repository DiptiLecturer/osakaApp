package org.freedu.osakatelevison.data.Repositories

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Order
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.freedu.osakatelevison.model.Product
import org.freedu.osakatelevison.data.SupabaseProvider


interface ProductRepository {
    suspend fun getActiveProducts(): Result<List<Product>>
    suspend fun getProductById(id: String): Result<Product?>
}

/**
 * Supabase PostgREST implementation of [ProductRepository].
 */
class ProductRepositoryImpl(
    private val supabaseClient: SupabaseClient = SupabaseProvider.client,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : ProductRepository {

    override suspend fun getActiveProducts(): Result<List<Product>> = withContext(ioDispatcher) {
        runCatching {
            supabaseClient
                .from("products")
                .select {
                    filter {
                        eq("is_active", true)
                    }
                    order(column = "category", order = Order.ASCENDING)
                }
                .decodeList<Product>()
        }
    }

    override suspend fun getProductById(id: String): Result<Product?> = withContext(ioDispatcher) {
        runCatching {
            supabaseClient
                .from("products")
                .select {
                    filter {
                        eq("id", id)
                    }
                }
                .decodeSingleOrNull<Product>()
        }
    }
}