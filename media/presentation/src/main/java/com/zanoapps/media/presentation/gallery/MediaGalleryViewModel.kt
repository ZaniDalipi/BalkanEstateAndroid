package com.zanoapps.media.presentation.gallery

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zanoapps.media.domain.model.MediaType
import com.zanoapps.media.domain.repository.MediaRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class MediaGalleryViewModel(
    private val mediaRepository: MediaRepository
) : ViewModel() {

    var state by mutableStateOf(MediaGalleryState())
        private set

    private val eventChannel = Channel<MediaGalleryEvent>()
    val events = eventChannel.receiveAsFlow()

    fun onAction(action: MediaGalleryAction) {
        when (action) {
            is MediaGalleryAction.OnLoadMedia -> {
                state = state.copy(propertyId = action.propertyId)
                loadMedia(action.propertyId)
            }
            is MediaGalleryAction.OnMediaSelected -> {
                val media = state.mediaItems.find { it.id == action.mediaId }
                state = state.copy(selectedMedia = media)
            }
            is MediaGalleryAction.OnDeleteMedia -> {
                viewModelScope.launch {
                    mediaRepository.deleteMedia(action.mediaId)
                    eventChannel.send(MediaGalleryEvent.MediaDeleted)
                }
            }
            MediaGalleryAction.OnDismissFullScreen -> {
                state = state.copy(selectedMedia = null)
            }
            is MediaGalleryAction.OnFilterByType -> {
                state = state.copy(typeFilter = action.typeFilter)
                applyFilter()
            }
        }
    }

    private fun loadMedia(propertyId: String) {
        state = state.copy(isLoading = true)
        mediaRepository.getMediaForProperty(propertyId)
            .onEach { items ->
                state = state.copy(
                    mediaItems = items,
                    filteredItems = items,
                    isLoading = false
                )
            }
            .launchIn(viewModelScope)
    }

    private fun applyFilter() {
        val filtered = if (state.typeFilter == null) {
            state.mediaItems
        } else {
            state.mediaItems.filter { it.type.name == state.typeFilter }
        }
        state = state.copy(filteredItems = filtered)
    }
}
