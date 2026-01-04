package com.zanoapps.balkanestateandroid.utils

object OnboardingDestinations {
    const val ROOT = "onboarding"
    const val CLIENT_INTENT = "client_intent"
    const val ON_BOARDING_BUYER_LIFE_SITUATION = "buyer_life_situation"
    const val ON_BOARDING_BUYER_PROPERTY_INTENT = "buyer_property_intent"
    const val ON_BOARDING_BUYER_AMENITIES = "buyer_amenities"
    const val ON_BOARDING_BUYER_FINAL_MESSAGE = "buyer_final_message"

    // Seller specific destinations
    const val ON_BOARDING_SELLER_PROPERTY_TYPE = "seller_property_type"
    const val ON_BOARDING_SELLER_SELLING_TIME = "seller_selling_time"
    const val ON_BOARDING_SELLER_MAIN_GOAL = "seller_main_goal"
    const val ON_BOARDING_SELLER_FINAL_MESSAGE = "seller_final_message"
}

object AuthDestinations {
    const val ROOT = "auth"
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val FORGOT_PASSWORD = "forgot_password"
}

object SearchDestinations {
    const val ROOT = "search"
    const val BUYER_SEARCH = "buyer_search"
    const val SELLER_DASHBOARD = "seller_dashboard"
    const val PROPERTY_DETAILS = "property_details/{propertyId}"

    fun propertyDetails(propertyId: String) = "property_details/$propertyId"
}

object ProfileDestinations {
    const val ROOT = "profile"
    const val PROFILE_MAIN = "profile_main"
    const val EDIT_PROFILE = "edit_profile"
}

object FavouritesDestinations {
    const val ROOT = "favourites"
    const val FAVOURITES_LIST = "favourites_list"
}

object MapDestinations {
    const val ROOT = "map"
    const val PROPERTY_MAP = "property_map"
}

object MessagingDestinations {
    const val ROOT = "messaging"
    const val CONVERSATIONS_LIST = "conversations_list"
    const val CHAT = "chat/{conversationId}"

    fun chat(conversationId: String) = "chat/$conversationId"
}

object AgentDestinations {
    const val ROOT = "agent"
    const val AGENT_PROFILE = "agent_profile/{agentId}"
    const val AGENT_LISTINGS = "agent_listings/{agentId}"

    fun agentProfile(agentId: String) = "agent_profile/$agentId"
    fun agentListings(agentId: String) = "agent_listings/$agentId"
}

object SettingsDestinations {
    const val ROOT = "settings"
    const val SETTINGS_MAIN = "settings_main"
    const val NOTIFICATIONS_SETTINGS = "notifications_settings"
    const val PRIVACY_SETTINGS = "privacy_settings"
    const val LANGUAGE_SETTINGS = "language_settings"
}

object NotificationDestinations {
    const val ROOT = "notifications"
    const val NOTIFICATIONS_LIST = "notifications_list"
}

object MediaDestinations {
    const val ROOT = "media"
    const val IMAGE_GALLERY = "image_gallery/{propertyId}/{initialIndex}"

    fun imageGallery(propertyId: String, initialIndex: Int = 0) = "image_gallery/$propertyId/$initialIndex"
}