package repo

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import model.ImageData

class ImagesRepositoryImpl(
    private val httpClient: HttpClient
) : ImagesRepository {

    override suspend fun getImages(): Result<List<ImageData>> =
        kotlin.runCatching {
            httpClient
                .get(url)
                .body()
        }

    companion object {
        private const val url =
            "https://sebastianaigner.github.io/demo-image-api/pictures.json"
    }
}
