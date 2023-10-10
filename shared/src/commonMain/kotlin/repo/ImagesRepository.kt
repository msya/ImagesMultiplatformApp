package repo

import model.ImageData

interface ImagesRepository {

    suspend fun getImages(): Result<List<ImageData>>

}
