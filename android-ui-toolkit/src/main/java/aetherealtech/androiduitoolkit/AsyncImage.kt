package aetherealtech.androiduitoolkit

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import com.aetherealtech.androiduitoolkit.R
import android.graphics.Bitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.vectorResource

@Composable
fun AsyncImage(
    modifier: Modifier = Modifier,
    source: suspend () -> Bitmap,
    contentDescription: String,
    brokenImage: ImageVector = ImageVector.vectorResource(id = R.drawable.broken_image)
) {
    val loadedImage = remember { mutableStateOf<Result<ImageBitmap?>>(Result.success(null)) }

    val brokenImagePainter = rememberVectorPainter(image = brokenImage)

    LaunchedEffect(source) {
        loadedImage.value = Result.success(null)
        loadedImage.value = Result.runCatching { source().asImageBitmap() }
    }

    val currentLoadedImage by loadedImage

    currentLoadedImage.fold(
        onSuccess = { successfulImage ->
            successfulImage?.let { image ->
                Box(
                    modifier = modifier,
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        bitmap = image,
                        contentDescription = contentDescription,
                        modifier = Modifier
                            .fillMaxSize(0.75f)
                    )
                }
            } ?: run {
                Box(
                    modifier = modifier,
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .fillMaxSize(0.5f)
                    )
                }
            }
        },
        onFailure = {
            Box(
                modifier = modifier,
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = brokenImagePainter,
                    contentDescription = "Image Failed"
                )
            }
        }
    )
}