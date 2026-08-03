package org.freedu.osakatelevison.data.Repositories

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Order
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.freedu.osakatelevison.model.SupabaseGalleryItem
import org.freedu.osakatelevison.data.SupabaseProvider

/**
 * Repository interface defining operations for gallery items.
 */
interface GalleryRepository {
    suspend fun getActiveGalleryItems(): Result<List<SupabaseGalleryItem>>
}

/**
 * PostgREST Supabase implementation of [GalleryRepository].
 */
class GalleryRepositoryImpl(
    private val supabaseClient: SupabaseClient = SupabaseProvider.client,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : GalleryRepository {

    override suspend fun getActiveGalleryItems(): Result<List<SupabaseGalleryItem>> =
        withContext(ioDispatcher) {
            runCatching {
                supabaseClient
                    .from("gallery")
                    .select {
                        filter {
                            eq("is_active", true)
                        }
                        order(column = "display_order", order = Order.ASCENDING)
                    }
                    .decodeList<SupabaseGalleryItem>()
            }
        }
}