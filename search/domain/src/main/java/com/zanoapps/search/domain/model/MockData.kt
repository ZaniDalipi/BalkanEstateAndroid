package com.zanoapps.search.domain.model

import com.zanoapps.core.domain.model.BalkanEstateProperty

object MockData {

    fun getMockProperties(): List<BalkanEstateProperty> {
        return listOf(
            BalkanEstateProperty(
                id = "1",
                title = "Beautiful 3BR Villa in Tirana",
                price = 350000.0,
                currency = "EUR",
                imageUrl = "https://images.unsplash.com/photo-1580587771525-78b9dba3b914",
                bedrooms = 3,
                bathrooms = 2,
                squareFootage = 1800,
                address = "Rruga Myslym Shyri 27",
                city = "Tirana",
                country = "Albania",
                latitude = 41.3275,
                longitude = 19.8187,
                propertyType = "Villa",
                listingType = "Sale",
                agentName = "Besmir Kola",
                isFeatured = true,
                isUrgent = false
            ),
            BalkanEstateProperty(
                id = "2",
                title = "Modern 2BR Apartment in Blloku",
                price = 750.0,
                currency = "EUR",
                imageUrl = "https://images.unsplash.com/photo-1522708323590-d24dbb6b0267",
                bedrooms = 2,
                bathrooms = 1,
                squareFootage = 1200,
                address = "Rruga Skënderbeu 7",
                city = "Tirana",
                country = "Albania",
                latitude = 41.3280,
                longitude = 19.8200,
                propertyType = "Apartment",
                listingType = "Rent",
                agentName = "Eglantina Dervishi",
                isFeatured = false,
                isUrgent = true
            ),
            BalkanEstateProperty(
                id = "3",
                title = "Luxury Penthouse with City Views",
                price = 580000.0,
                currency = "EUR",
                imageUrl = "https://images.unsplash.com/photo-1512917774080-9991f1c4c750",
                bedrooms = 4,
                bathrooms = 3,
                squareFootage = 2500,
                address = "Rruga e Durrësit 45",
                city = "Tirana",
                country = "Albania",
                latitude = 41.3290,
                longitude = 19.8150,
                propertyType = "Apartment",
                listingType = "Sale",
                agentName = "Arben Dedja",
                isFeatured = true,
                isUrgent = false
            ),
            BalkanEstateProperty(
                id = "4",
                title = "Cozy Studio near University",
                price = 450.0,
                currency = "EUR",
                imageUrl = "https://images.unsplash.com/photo-1502672260266-1c1ef2d93688",
                bedrooms = 1,
                bathrooms = 1,
                squareFootage = 600,
                address = "Rruga e Kavajës 120",
                city = "Tirana",
                country = "Albania",
                latitude = 41.3250,
                longitude = 19.8220,
                propertyType = "Studio",
                listingType = "Rent",
                agentName = "Mirela Hoxha",
                isFeatured = false,
                isUrgent = false
            ),
            BalkanEstateProperty(
                id = "5",
                title = "Family House with Garden",
                price = 420000.0,
                currency = "EUR",
                imageUrl = "https://images.unsplash.com/photo-1564013799919-ab600027ffc6",
                bedrooms = 5,
                bathrooms = 3,
                squareFootage = 3000,
                address = "Rruga Pjetër Bogdani 15",
                city = "Tirana",
                country = "Albania",
                latitude = 41.3310,
                longitude = 19.8170,
                propertyType = "House",
                listingType = "Sale",
                agentName = "Gentian Leka",
                isFeatured = false,
                isUrgent = true
            ),
            BalkanEstateProperty(
                id = "6",
                title = "Commercial Space in Prime Location",
                price = 2500.0,
                currency = "EUR",
                imageUrl = "https://images.unsplash.com/photo-1497366216548-37526070297c",
                bedrooms = 0,
                bathrooms = 2,
                squareFootage = 1500,
                address = "Bulevardi Zogu I",
                city = "Tirana",
                country = "Albania",
                latitude = 41.3265,
                longitude = 19.8195,
                propertyType = "Commercial",
                listingType = "Rent",
                agentName = "Klodian Mëhilli",
                isFeatured = true,
                isUrgent = false
            )
        )
    }

    fun getMockMapLocation(): MapLocation {
        return MapLocation(
            latitude = 41.3275,
            longitude = 19.8187,
            zoom = 13f
        )
    }

    fun getMockSearchFilters(): SearchFilters {
        return SearchFilters(
            query = "",
            minPrice = null,
            maxPrice = null,
            propertyTypes = emptySet(),
            bedrooms = null,
            bathrooms = null
        )
    }
}
