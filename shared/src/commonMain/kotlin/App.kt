import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import dev.icerock.moko.mvvm.compose.getViewModel
import dev.icerock.moko.mvvm.compose.viewModelFactory
import images_list.ImagesListEvent
import images_list.ImagesListModel
import images_list.ImagesViewModel
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import model.ImageData
import repo.ImagesRepositoryImpl

@Composable
fun App() {
    MaterialTheme {
        val viewModel = getViewModel(Unit, viewModelFactory {
            val imagesRepository = ImagesRepositoryImpl(
                httpClient = HttpClient {
                    install(ContentNegotiation) {
                        json()
                    }
                }
            )
            ImagesViewModel(imagesRepository)
        })

        val model by viewModel.models.collectAsState()
        ImagesListScreen(model = model) {
            viewModel.take(it)
        }
    }
}

@Composable
fun ImagesListScreen(
    modifier: Modifier = Modifier,
    model: ImagesListModel,
    onEvent: (ImagesListEvent) -> Unit
) {
    when(model) {
        is ImagesListModel.Success -> {
            ImagesList(modifier, model.images) {

            }
        }
        is ImagesListModel.Loading -> {
            CircularProgressIndicator(modifier.fillMaxSize())
        }
        is ImagesListModel.Error -> {
            Text("Error")
        }
    }
}

@Composable
fun ImagesList(
    modifier: Modifier = Modifier,
    images: List<ImageData>,
    onEvent: (ImagesListEvent) -> Unit
) {
    LazyColumn (
        modifier = modifier.fillMaxSize(),
        content = {
            items(images) { image ->
                Card(
                    modifier = Modifier.padding(vertical = 16.dp)
                ) {
                    Column {
                        KamelImage(
                            asyncPainterResource(
                                "https://sebastianaigner.github.io/demo-image-api/${image.path}"
                            ),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxWidth().aspectRatio(1.0f)
                        )
                        Row {
                            Text(
                                modifier = modifier.padding(start = 4.dp, top = 16.dp),
                                text = "Author:"
                            )
                            Text(
                                modifier = modifier.padding(start = 4.dp, top = 16.dp),
                                text = image.author
                            )
                        }
                        Row {
                            Text(
                                modifier = modifier.padding(start = 4.dp, top = 16.dp),
                                text = "Category:"
                            )
                            Text(
                                modifier = modifier.padding(start = 4.dp, top = 16.dp),
                                text = image.category
                            )
                        }
                    }
                }
            }
        },
    )
}


expect fun getPlatformName(): String

