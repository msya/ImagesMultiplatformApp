package images_list

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import app.cash.molecule.RecompositionMode
import app.cash.molecule.launchMolecule
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import getDisplayFrameClock
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.StateFlow
import repo.ImagesRepository

class ImagesViewModel(
    private val imagesRepository: ImagesRepository
): ViewModel() {

    private val events = MutableSharedFlow<ImagesListEvent>(extraBufferCapacity = 20)

    val models: StateFlow<ImagesListModel> by lazy(LazyThreadSafetyMode.NONE) {
        val scope = CoroutineScope(viewModelScope.coroutineContext + getDisplayFrameClock()!!)
        scope.launchMolecule(mode = RecompositionMode.ContextClock) {
            models(events)
        }
    }

    fun take(event: ImagesListEvent) {
        if (!events.tryEmit(event)) {
            error("Event buffer overflow.")
        }
    }

    @Composable
    fun models(events: Flow<ImagesListEvent>): ImagesListModel {
        var imagesViewModel by remember {
            mutableStateOf<ImagesListModel>(ImagesListModel.Loading)
        }

        LaunchedEffect(Unit) {
            val result = imagesRepository.getImages()
            result.fold({
                imagesViewModel = ImagesListModel.Success(it)
            }) {
                it.printStackTrace()
                imagesViewModel = ImagesListModel.Error
            }
        }

        LaunchedEffect(Unit) {
            events.collect { event ->
                when (event) {
                    is OnClicked -> {

                    }
                }
            }
        }
        return imagesViewModel
    }
}
