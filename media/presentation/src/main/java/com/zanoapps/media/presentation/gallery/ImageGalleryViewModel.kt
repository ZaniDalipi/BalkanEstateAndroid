package com.zanoapps.media.presentation.gallery

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zanoapps.media.domain.model.MediaImage
import com.zanoapps.media.domain.repository.MediaRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class ImageGalleryViewModel(
    savedStateHandle: SavedStateHandle,
    private val mediaRepository: MediaRepository
) : ViewModel() {

    var state by mutableStateOf(ImageGalleryState())
        private set

    private val eventChannel = Channel<ImageGalleryEvent>()
    val events = eventChannel.receiveAsFlow()

    private val propertyId: String? = savedStateHandle["propertyId"]
    private val initialIndex: Int = savedStateHandle["initialIndex"] ?: 0
    private val imageUrls: List<String> = savedStateHandle.get<String>("imageUrls")
        ?.split(",")
        ?.filter { it.isNotBlank() }
        ?: emptyList()

    init {
        loadImages()
    }

    private fun loadImages() {
        viewModelScope.launch {
            state = state.copy(isLoading = true)

            try {
                val images = if (imageUrls.isNotEmpty()) {
                    // Use passed image URLs
                    imageUrls.mapIndexed { index, url ->
                        MediaImage(
                            id = index.toString(),
                            url = url
                        )
                    }
                } else if (propertyId != null) {
                    // Fetch from repository
                    mediaRepository.getPropertyImages(propertyId)
                } else {
                    emptyList()
                }

                state = state.copy(
                    images = images,
                    currentIndex = initialIndex.coerceIn(0, (images.size - 1).coerceAtLeast(0)),
                    isLoading = false
                )
            } catch (e: Exception) {
                state = state.copy(
                    isLoading = false,
                    errorMessage = e.message ?: "Failed to load images"
                )
            }
        }
    }

    fun onAction(action: ImageGalleryAction) {
        when (action) {
            ImageGalleryAction.OnBackClick -> {
                viewModelScope.launch {
                    eventChannel.send(ImageGalleryEvent.NavigateBack)
                }
            }
            is ImageGalleryAction.OnPageChange -> {
                state = state.copy(currentIndex = action.index)
            }
            ImageGalleryAction.OnShareClick -> {
                val currentImage = state.images.getOrNull(state.currentIndex)
                if (currentImage != null) {
                    viewModelScope.launch {
                        eventChannel.send(ImageGalleryEvent.ShareImage(currentImage.url))
                    }
                }
            }
            ImageGalleryAction.OnDownloadClick -> {
                val currentImage = state.images.getOrNull(state.currentIndex)
                if (currentImage != null) {
                    viewModelScope.launch {
                        eventChannel.send(ImageGalleryEvent.DownloadImage(currentImage.url))
                    }
                }
            }
            ImageGalleryAction.OnToggleZoom -> {
                state = state.copy(isZoomed = !state.isZoomed)
            }
        }
    }
}
