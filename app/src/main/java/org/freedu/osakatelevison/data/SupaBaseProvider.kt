package org.freedu.osakatelevison.data



import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.serializer.KotlinXSerializer
import io.github.jan.supabase.storage.Storage
import kotlinx.serialization.json.Json

object SupabaseProvider {
    val client = createSupabaseClient(
        supabaseUrl = "https://dhijfufoefirkecwsvla.supabase.co", // Replace with your full URL
        supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImRoaWpmdWZvZWZpcmtlY3dzdmxhIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NzUwNDAwODUsImV4cCI6MjA5MDYxNjA4NX0.jgAiDCieCFJbh8coQ-yWr7LR96Lzk-N6SJ69w7fLszM"                        // Replace with your full Anon Key
    ) {
        defaultSerializer = KotlinXSerializer(
            Json {
                ignoreUnknownKeys = true
                isLenient = true
            }
        )
        install(Postgrest)
        install(Storage)
    }
}