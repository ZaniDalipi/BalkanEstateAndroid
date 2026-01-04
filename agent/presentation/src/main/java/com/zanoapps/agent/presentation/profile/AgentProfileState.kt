package com.zanoapps.agent.presentation.profile

import com.zanoapps.agent.domain.model.Agent
import com.zanoapps.core.domain.model.BalkanEstateProperty

data class AgentProfileState(
    val agent: Agent? = null,
    val listings: List<BalkanEstateProperty> = emptyList(),
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val errorMessage: String? = null,
    val isContactFormVisible: Boolean = false,
    val contactName: String = "",
    val contactEmail: String = "",
    val contactPhone: String = "",
    val contactMessage: String = "",
    val isSendingMessage: Boolean = false,
    val messageSentSuccess: Boolean = false
)
