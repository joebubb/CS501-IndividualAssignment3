package com.example.staggeredphotogallery

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import com.example.staggeredphotogallery.ui.theme.StaggeredPhotoGalleryTheme
import com.example.staggeredphotogallery.data.Photo
import com.example.staggeredphotogallery.data.PhotoRepository
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            StaggeredPhotoGalleryTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    PhotoGrid(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun PhotoGrid(modifier: Modifier) {
    val photos = PhotoRepository().getPhotos(context = LocalContext.current) // load photos
    val scope = rememberCoroutineScope() // coroutine scope
    var selectedPhoto by remember { mutableStateOf<String?>(null) } // keep track
    val scale = remember { Animatable(1f) }

    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 130.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier.padding(4.dp)
    ) {
        items(photos) { photo -> // render all photos from the list into the grid
            Image(
                painter = painterResource(when (photo.title) {
                    "Aliens" -> R.drawable.aliens
                    "Axe" -> R.drawable.axe
                    "Banana" -> R.drawable.banana
                    "Bear" -> R.drawable.bear
                    "Blue Orange" -> R.drawable.blueorange
                    "Cat" -> R.drawable.cat
                    "Eggs" -> R.drawable.eggs
                    "Paper" -> R.drawable.paper // use the name to get the id for the photo
                    "Paratroopers" -> R.drawable.para
                    "Pencils" -> R.drawable.pencils
                    "Rose" -> R.drawable.rose
                    "Tracks" -> R.drawable.tracks
                    else -> R.drawable.ic_launcher_background
                }),
                contentDescription = photo.title, // description is the title of the photo
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 120.dp, max = 180.dp) // added to fix stretching
                    .scale(if (selectedPhoto == photo.title) scale.value else 1f)
                    .clickable {
                        scope.launch { // enlarge the selected photo with a spring animation. makes sure that
                            if (selectedPhoto == photo.title) { // the previous one shrinks
                                scale.animateTo(
                                    targetValue = 1f,
                                    animationSpec = spring(
                                        dampingRatio = Spring.DampingRatioMediumBouncy,
                                        stiffness = Spring.StiffnessLow
                                    )
                                )
                                selectedPhoto = null
                            } else {
                                scale.animateTo(1f)
                                selectedPhoto = photo.title
                                scale.animateTo(
                                    targetValue = 1.5f,
                                    animationSpec = spring(
                                        dampingRatio = Spring.DampingRatioMediumBouncy,
                                        stiffness = Spring.StiffnessLow
                                    )
                                )
                            }
                        }
                    }
            )
        }
    }
}