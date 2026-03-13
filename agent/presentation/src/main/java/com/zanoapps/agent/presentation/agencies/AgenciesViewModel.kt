package com.zanoapps.agent.presentation.agencies

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zanoapps.agent.domain.model.Agency
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class AgenciesViewModel : ViewModel() {

    var state by mutableStateOf(AgenciesState())
        private set

    private val eventChannel = Channel<AgenciesEvent>()
    val events = eventChannel.receiveAsFlow()

    init {
        loadAgencies()
    }

    fun onAction(action: AgenciesAction) {
        when (action) {
            AgenciesAction.OnLoadAgencies -> loadAgencies()
            is AgenciesAction.OnSearchQueryChanged -> {
                state = state.copy(searchQuery = action.query)
                filterAgencies()
            }
            is AgenciesAction.OnAgencyClick -> {
                viewModelScope.launch {
                    eventChannel.send(AgenciesEvent.NavigateToAgencyDetail(action.agency.id))
                }
            }
            is AgenciesAction.OnContactAgency -> {}
        }
    }

    private fun loadAgencies() {
        viewModelScope.launch {
            state = state.copy(isLoading = true)
            val agencies = getMockAgencies()
            state = state.copy(agencies = agencies, filteredAgencies = agencies, isLoading = false)
        }
    }

    private fun filterAgencies() {
        val query = state.searchQuery.lowercase()
        val filtered = if (query.isEmpty()) state.agencies else state.agencies.filter {
            it.name.lowercase().contains(query) || it.city.lowercase().contains(query)
        }
        state = state.copy(filteredAgencies = filtered)
    }

    private fun getMockAgencies(): List<Agency> = listOf(
        Agency("ag1", "Balkan Property Group", address = "Rruga Myslym Shyri 27", city = "Tirana", country = "Albania", phone = "+355 4 234 5678", email = "info@balkanproperty.al", website = "balkanproperty.al", rating = 4.8f, reviewsCount = 234, agentsCount = 15, listingsCount = 120, description = "Leading real estate agency in Albania specializing in residential and commercial properties.", isVerified = true),
        Agency("ag2", "Elite Realty Albania", address = "Bulevardi Zogu I 45", city = "Tirana", country = "Albania", phone = "+355 4 345 6789", email = "contact@eliterealty.al", website = "eliterealty.al", rating = 4.7f, reviewsCount = 187, agentsCount = 12, listingsCount = 95, description = "Premium real estate services for discerning clients across the Balkans.", isVerified = true),
        Agency("ag3", "Sunshine Properties", address = "Rruga Ismail Qemali 15", city = "Vlorë", country = "Albania", phone = "+355 33 456 789", email = "info@sunshineprops.al", website = "sunshineprops.al", rating = 4.6f, reviewsCount = 143, agentsCount = 8, listingsCount = 67, description = "Your trusted partner for coastal properties in Southern Albania.", isVerified = true),
        Agency("ag4", "Adriatic Real Estate", address = "Rruga e Sarandës 10", city = "Sarandë", country = "Albania", phone = "+355 85 234 567", email = "hello@adriaticre.al", website = "adriaticre.al", rating = 4.5f, reviewsCount = 98, agentsCount = 6, listingsCount = 45, description = "Specialized in beachfront and coastal properties along the Albanian Riviera.", isVerified = false),
        Agency("ag5", "Kosovo Prime Realty", address = "Bulevardi Nëna Terezë 22", city = "Pristina", country = "Kosovo", phone = "+383 38 123 456", email = "info@kosovoprime.com", website = "kosovoprime.com", rating = 4.4f, reviewsCount = 112, agentsCount = 10, listingsCount = 78, description = "The leading real estate agency in Kosovo with a focus on modern developments.", isVerified = true)
    )
}
