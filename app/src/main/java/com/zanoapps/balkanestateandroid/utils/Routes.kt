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


object SearchDestinations {
    const val ROOT = "search"
    const val BUYER_SEARCH = "buyer_search"
    const val SELLER_DASHBOARD = "seller_dashboard"
}

object MainDestinations {
    const val ROOT = "main"
    const val HOME = "home"
    const val SEARCH = "main_search"
    const val SAVED = "saved"
    const val INBOX = "inbox"
    const val PROFILE = "profile"
    const val PROPERTY_DETAILS = "property_details/{propertyId}"
    const val FILTERS = "filters"
    const val NEW_LISTING = "new_listing"
    const val SUBSCRIPTION = "subscription"
    const val SAVED_SEARCHES = "saved_searches"
    const val TOP_AGENTS = "top_agents"
    const val AGENCIES = "agencies"

    fun propertyDetails(propertyId: String) = "property_details/$propertyId"
}