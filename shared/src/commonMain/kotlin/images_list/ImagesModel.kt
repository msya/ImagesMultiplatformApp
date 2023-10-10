package images_list

import model.ImageData

sealed interface ImagesListEvent
data class OnClicked(val id: Int) : ImagesListEvent

sealed class ImagesListModel {
    data object Loading: ImagesListModel()
    data class Success(val images: List<ImageData>): ImagesListModel()
    data object Error: ImagesListModel()
}
