package org.freedu.osakatelevison.data.Repositories

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Order
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.freedu.osakatelevison.data.SupabaseProvider
import org.freedu.osakatelevison.model.HeroSlide
import org.freedu.osakatelevison.model.Product

interface HomeRepository {
    suspend fun getHeroSlides(): Result<List<HeroSlide>>
    suspend fun getHighlightProducts(): Result<List<Product>>
}

class HomeRepositoryImpl(
    private val supabaseClient: SupabaseClient = SupabaseProvider.client,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : HomeRepository {

    override suspend fun getHeroSlides(): Result<List<HeroSlide>> = withContext(ioDispatcher) {
        runCatching {
            supabaseClient
                .from("hero_slides")
                .select {
                    filter { eq("is_active", true) }
                    order(column = "display_order", order = Order.ASCENDING)
                }
                .decodeList<HeroSlide>()
        }
    }

    override suspend fun getHighlightProducts(): Result<List<Product>> = withContext(ioDispatcher) {
        runCatching {
            supabaseClient
                .from("products")
                .select {
                    filter { eq("is_active", true) }
                    // Fetch top featured/active items (limit to 6-8 items for homepage balance)
                    order(column = "created_at", order = Order.DESCENDING)
                }
                .decodeList<Product>()
        }
    }
}