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