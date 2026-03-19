package com.zanoapps.balkanestateandroid.data

import com.zanoapps.core.database.dao.AgencyDao
import com.zanoapps.core.database.dao.AgentDao
import com.zanoapps.core.database.dao.ConversationDao
import com.zanoapps.core.database.dao.MessageDao
import com.zanoapps.core.database.dao.PropertyDao
import com.zanoapps.core.database.dao.SavedSearchDao
import com.zanoapps.core.database.entity.AgencyEntity
import com.zanoapps.core.database.entity.AgentEntity
import com.zanoapps.core.database.entity.ConversationEntity
import com.zanoapps.core.database.entity.MessageEntity
import com.zanoapps.core.database.entity.PropertyEntity
import com.zanoapps.core.database.entity.SavedSearchEntity
import kotlinx.coroutines.flow.first

class DataSeeder(
    private val propertyDao: PropertyDao,
    private val agentDao: AgentDao,
    private val agencyDao: AgencyDao,
    private val conversationDao: ConversationDao,
    private val messageDao: MessageDao,
    private val savedSearchDao: SavedSearchDao
) {
    suspend fun seedIfEmpty() {
        val existingProperties = propertyDao.getAllProperties().first()
        if (existingProperties.isNotEmpty()) return

        seedProperties()
        seedAgents()
        seedAgencies()
        seedConversations()
        seedMessages()
        seedSavedSearches()
    }

    private suspend fun seedProperties() {
        val properties = listOf(
            PropertyEntity(
                id = "p1", title = "Modern 3BR Apartment in Blloku",
                price = 185000.0, currency = "EUR",
                imageUrl = "https://images.unsplash.com/photo-1522708323590-d24dbb6b0267?w=800",
                bedrooms = 3, bathrooms = 2, squareFootage = 120,
                address = "Rruga Ismail Qemali 15", city = "Tirana", country = "Albania",
                latitude = 41.3214, longitude = 19.8192,
                propertyType = "Apartment", listingType = "For Sale",
                agentName = "Besmir Kola", isFeatured = true,
                description = "Beautiful modern apartment in the heart of Blloku district. Features high ceilings, hardwood floors, and a spacious balcony overlooking the city.",
                agentPhone = "+355 69 123 4567", agentEmail = "besmir@balkanproperty.al",
                yearBuilt = 2019, floorNumber = 4, totalFloors = 8, furnished = "Fully Furnished", parking = "1 Underground",
                agentId = "a1"
            ),
            PropertyEntity(
                id = "p2", title = "Luxury Villa with Pool in Durr\u00ebs",
                price = 450000.0, currency = "EUR",
                imageUrl = "https://images.unsplash.com/photo-1580587771525-78b9dba3b914?w=800",
                bedrooms = 5, bathrooms = 3, squareFootage = 350,
                address = "Rruga e Plazhit 42", city = "Durr\u00ebs", country = "Albania",
                latitude = 41.3233, longitude = 19.4543,
                propertyType = "Villa", listingType = "For Sale",
                agentName = "Arben Dedja", isFeatured = true, isUrgent = true,
                description = "Stunning seaside villa with private pool, garden, and panoramic views of the Adriatic Sea.",
                agentPhone = "+355 69 234 5678", agentEmail = "arben@balkanproperty.al",
                yearBuilt = 2021, totalFloors = 2, furnished = "Fully Furnished", parking = "2 Garage",
                agentId = "a3"
            ),
            PropertyEntity(
                id = "p3", title = "Cozy Studio near University of Tirana",
                price = 350.0, currency = "EUR",
                imageUrl = "https://images.unsplash.com/photo-1502672260266-1c1ef2d93688?w=800",
                bedrooms = 1, bathrooms = 1, squareFootage = 45,
                address = "Rruga e Elbasanit 78", city = "Tirana", country = "Albania",
                latitude = 41.3275, longitude = 19.8219,
                propertyType = "Studio", listingType = "For Rent",
                agentName = "Mirela Hoxha",
                description = "Perfect student accommodation near the university campus. Fully furnished with modern amenities.",
                agentPhone = "+355 69 345 6789", agentEmail = "mirela@sunshineprops.al",
                yearBuilt = 2018, floorNumber = 2, totalFloors = 5, furnished = "Fully Furnished", parking = "None",
                agentId = "a4"
            ),
            PropertyEntity(
                id = "p4", title = "Penthouse with Panoramic City Views",
                price = 320000.0, currency = "EUR",
                imageUrl = "https://images.unsplash.com/photo-1512917774080-9991f1c4c750?w=800",
                bedrooms = 4, bathrooms = 3, squareFootage = 200,
                address = "Bulevardi Zogu I 120", city = "Tirana", country = "Albania",
                latitude = 41.3291, longitude = 19.8180,
                propertyType = "Penthouse", listingType = "For Sale",
                agentName = "Eglantina Dervishi", isFeatured = true,
                description = "Spectacular penthouse apartment with 360-degree views of Tirana. Features a rooftop terrace and premium finishes.",
                agentPhone = "+355 69 456 7890", agentEmail = "eglantina@eliterealty.al",
                yearBuilt = 2022, floorNumber = 12, totalFloors = 12, furnished = "Semi-Furnished", parking = "2 Underground",
                agentId = "a2"
            ),
            PropertyEntity(
                id = "p5", title = "Commercial Space in City Center",
                price = 2500.0, currency = "EUR",
                imageUrl = "https://images.unsplash.com/photo-1497366216548-37526070297c?w=800",
                bedrooms = 0, bathrooms = 2, squareFootage = 150,
                address = "Rruga Myslym Shyri 45", city = "Tirana", country = "Albania",
                latitude = 41.3260, longitude = 19.8175,
                propertyType = "Commercial", listingType = "For Rent",
                agentName = "Klodian M\u00ebhilli",
                description = "Prime commercial space perfect for retail or office use. High foot traffic location in Myslym Shyri.",
                agentPhone = "+355 69 567 8901", agentEmail = "klodian@eliterealty.al",
                yearBuilt = 2015, floorNumber = 1, totalFloors = 6, furnished = "Unfurnished", parking = "Street",
                agentId = "a6"
            ),
            PropertyEntity(
                id = "p6", title = "Beachfront Apartment in Sarand\u00eb",
                price = 145000.0, currency = "EUR",
                imageUrl = "https://images.unsplash.com/photo-1499793983394-e58d62ba7443?w=800",
                bedrooms = 2, bathrooms = 1, squareFootage = 85,
                address = "Rruga Mitat Hoxha 8", city = "Sarand\u00eb", country = "Albania",
                latitude = 39.8755, longitude = 20.0053,
                propertyType = "Apartment", listingType = "For Sale",
                agentName = "Gentian Leka", isUrgent = true,
                description = "Charming beachfront apartment with stunning sea views. Walking distance to restaurants and nightlife.",
                agentPhone = "+355 69 678 9012", agentEmail = "gentian@adriaticre.al",
                yearBuilt = 2020, floorNumber = 3, totalFloors = 5, furnished = "Semi-Furnished", parking = "1 Space",
                agentId = "a5"
            ),
            PropertyEntity(
                id = "p7", title = "New Development 2BR in Vlor\u00eb",
                price = 95000.0, currency = "EUR",
                imageUrl = "https://images.unsplash.com/photo-1545324418-cc1a3fa10c00?w=800",
                bedrooms = 2, bathrooms = 1, squareFootage = 75,
                address = "Lungomare 25", city = "Vlor\u00eb", country = "Albania",
                latitude = 40.4607, longitude = 19.4908,
                propertyType = "Apartment", listingType = "For Sale",
                agentName = "Mirela Hoxha",
                description = "Brand new apartment in an exciting coastal development. Modern design with energy-efficient features.",
                agentPhone = "+355 69 345 6789", agentEmail = "mirela@sunshineprops.al",
                yearBuilt = 2024, floorNumber = 5, totalFloors = 10, furnished = "Unfurnished", parking = "1 Underground",
                agentId = "a4"
            ),
            PropertyEntity(
                id = "p8", title = "Traditional Stone House in Gjirokast\u00ebr",
                price = 120000.0, currency = "EUR",
                imageUrl = "https://images.unsplash.com/photo-1518780664697-55e3ad937233?w=800",
                bedrooms = 3, bathrooms = 2, squareFootage = 180,
                address = "Lagjia Palorto", city = "Gjirokast\u00ebr", country = "Albania",
                latitude = 40.0758, longitude = 20.1389,
                propertyType = "House", listingType = "For Sale",
                agentName = "Besmir Kola",
                description = "Beautifully restored traditional stone house in UNESCO World Heritage city. Original features with modern comforts.",
                agentPhone = "+355 69 123 4567", agentEmail = "besmir@balkanproperty.al",
                yearBuilt = 1850, totalFloors = 2, furnished = "Semi-Furnished", parking = "Garden",
                agentId = "a1"
            ),
            PropertyEntity(
                id = "p9", title = "Modern Office Space in Pristina",
                price = 1800.0, currency = "EUR",
                imageUrl = "https://images.unsplash.com/photo-1497366811353-6870744d04b2?w=800",
                bedrooms = 0, bathrooms = 2, squareFootage = 200,
                address = "Bulevardi N\u00ebn\u00eb Terez\u00eb 30", city = "Pristina", country = "Kosovo",
                latitude = 42.6629, longitude = 21.1655,
                propertyType = "Office", listingType = "For Rent",
                agentName = "Eglantina Dervishi",
                description = "Modern open-plan office in the business district of Pristina. Ready for immediate occupancy.",
                agentPhone = "+355 69 456 7890", agentEmail = "eglantina@eliterealty.al",
                yearBuilt = 2017, floorNumber = 3, totalFloors = 8, furnished = "Unfurnished", parking = "2 Spaces",
                agentId = "a2"
            ),
            PropertyEntity(
                id = "p10", title = "Land Plot with Sea View in Himara",
                price = 75000.0, currency = "EUR",
                imageUrl = "https://images.unsplash.com/photo-1500382017468-9049fed747ef?w=800",
                bedrooms = 0, bathrooms = 0, squareFootage = 500,
                address = "Rruga Nacionale Vlor\u00eb-Sarand\u00eb", city = "Himar\u00eb", country = "Albania",
                latitude = 40.1024, longitude = 19.7478,
                propertyType = "Land", listingType = "For Sale",
                agentName = "Gentian Leka", isFeatured = true,
                description = "Prime land plot with breathtaking sea views. Perfect for building your dream villa on the Albanian Riviera.",
                agentPhone = "+355 69 678 9012", agentEmail = "gentian@adriaticre.al",
                parking = "Open",
                agentId = "a5"
            ),
            PropertyEntity(
                id = "p11", title = "Spacious 4BR Apartment in Vra\u010dar",
                price = 230000.0, currency = "EUR",
                imageUrl = "https://images.unsplash.com/photo-1560448204-e02f11c3d0e2?w=800",
                bedrooms = 4, bathrooms = 2, squareFootage = 140,
                address = "Bulevar Kralja Aleksandra 78", city = "Belgrade", country = "Serbia",
                latitude = 44.7936, longitude = 20.4717,
                propertyType = "Apartment", listingType = "For Sale",
                agentName = "Besmir Kola", isFeatured = true,
                description = "Elegant apartment in the sought-after Vra\u010dar neighborhood. Renovated with premium materials, close to St. Sava Temple.",
                agentPhone = "+355 69 123 4567", agentEmail = "besmir@balkanproperty.al",
                yearBuilt = 2020, floorNumber = 5, totalFloors = 7, furnished = "Semi-Furnished", parking = "1 Underground",
                agentId = "a1"
            ),
            PropertyEntity(
                id = "p12", title = "Modern Loft in Belgrade Waterfront",
                price = 1200.0, currency = "EUR",
                imageUrl = "https://images.unsplash.com/photo-1493809842364-78817add7ffb?w=800",
                bedrooms = 2, bathrooms = 1, squareFootage = 90,
                address = "Savski Trg 5", city = "Belgrade", country = "Serbia",
                latitude = 44.8057, longitude = 20.4530,
                propertyType = "Apartment", listingType = "For Rent",
                agentName = "Eglantina Dervishi",
                description = "Trendy loft-style apartment in the new Belgrade Waterfront district. Floor-to-ceiling windows with river views.",
                agentPhone = "+355 69 456 7890", agentEmail = "eglantina@eliterealty.al",
                yearBuilt = 2023, floorNumber = 8, totalFloors = 15, furnished = "Fully Furnished", parking = "1 Underground",
                agentId = "a2"
            ),
            PropertyEntity(
                id = "p13", title = "Charming House in Zagreb Upper Town",
                price = 380000.0, currency = "EUR",
                imageUrl = "https://images.unsplash.com/photo-1564013799919-ab600027ffc6?w=800",
                bedrooms = 3, bathrooms = 2, squareFootage = 160,
                address = "Tkal\u010di\u0107eva ulica 22", city = "Zagreb", country = "Croatia",
                latitude = 45.8150, longitude = 15.9785,
                propertyType = "House", listingType = "For Sale",
                agentName = "Arben Dedja", isFeatured = true,
                description = "Beautifully restored townhouse in Zagreb's historic Upper Town. Combines period charm with contemporary comfort.",
                agentPhone = "+355 69 234 5678", agentEmail = "arben@balkanproperty.al",
                yearBuilt = 1920, totalFloors = 2, furnished = "Semi-Furnished", parking = "Street",
                agentId = "a3"
            ),
            PropertyEntity(
                id = "p14", title = "Seafront Villa in Split",
                price = 620000.0, currency = "EUR",
                imageUrl = "https://images.unsplash.com/photo-1600596542815-ffad4c1539a9?w=800",
                bedrooms = 5, bathrooms = 4, squareFootage = 280,
                address = "Obala Hrvatskog Narodnog Preporoda 12", city = "Split", country = "Croatia",
                latitude = 43.5081, longitude = 16.4402,
                propertyType = "Villa", listingType = "For Sale",
                agentName = "Gentian Leka", isFeatured = true, isUrgent = true,
                description = "Magnificent Mediterranean villa steps from Diocletian's Palace. Private terrace with breathtaking Adriatic views.",
                agentPhone = "+355 69 678 9012", agentEmail = "gentian@adriaticre.al",
                yearBuilt = 2018, totalFloors = 3, furnished = "Fully Furnished", parking = "2 Garage",
                agentId = "a5"
            ),
            PropertyEntity(
                id = "p15", title = "New 2BR Apartment in Podgorica",
                price = 115000.0, currency = "EUR",
                imageUrl = "https://images.unsplash.com/photo-1574362848149-11496d93a7c7?w=800",
                bedrooms = 2, bathrooms = 1, squareFootage = 72,
                address = "Bulevar Svetog Petra Cetinjskog 18", city = "Podgorica", country = "Montenegro",
                latitude = 42.4304, longitude = 19.2594,
                propertyType = "Apartment", listingType = "For Sale",
                agentName = "Mirela Hoxha",
                description = "Modern apartment in a new residential complex in Podgorica. Open floor plan with high-quality finishes and city views.",
                agentPhone = "+355 69 345 6789", agentEmail = "mirela@sunshineprops.al",
                yearBuilt = 2024, floorNumber = 6, totalFloors = 10, furnished = "Unfurnished", parking = "1 Underground",
                agentId = "a4"
            ),
            PropertyEntity(
                id = "p16", title = "Luxury Apartment in Budva Old Town",
                price = 2800.0, currency = "EUR",
                imageUrl = "https://images.unsplash.com/photo-1600607687939-ce8a6c25118c?w=800",
                bedrooms = 3, bathrooms = 2, squareFootage = 110,
                address = "Mediteranska 7", city = "Budva", country = "Montenegro",
                latitude = 42.2889, longitude = 18.8403,
                propertyType = "Apartment", listingType = "For Rent",
                agentName = "Klodian M\u00ebhilli", isUrgent = true,
                description = "Premium furnished apartment in the heart of Budva's Old Town. Stone walls meet modern luxury with sea views from the balcony.",
                agentPhone = "+355 69 567 8901", agentEmail = "klodian@eliterealty.al",
                yearBuilt = 2017, floorNumber = 2, totalFloors = 4, furnished = "Fully Furnished", parking = "None",
                agentId = "a6"
            ),
            PropertyEntity(
                id = "p17", title = "Central Apartment in Skopje",
                price = 89000.0, currency = "EUR",
                imageUrl = "https://images.unsplash.com/photo-1600585154340-be6161a56a0c?w=800",
                bedrooms = 2, bathrooms = 1, squareFootage = 68,
                address = "Bulevar Makedonija 24", city = "Skopje", country = "North Macedonia",
                latitude = 41.9981, longitude = 21.4254,
                propertyType = "Apartment", listingType = "For Sale",
                agentName = "Besmir Kola",
                description = "Well-located apartment near Macedonia Square. Bright and airy with a modern kitchen and views of the Vardar River.",
                agentPhone = "+355 69 123 4567", agentEmail = "besmir@balkanproperty.al",
                yearBuilt = 2016, floorNumber = 3, totalFloors = 6, furnished = "Semi-Furnished", parking = "Street",
                agentId = "a1"
            ),
            PropertyEntity(
                id = "p18", title = "Commercial Space in Sarajevo",
                price = 1500.0, currency = "EUR",
                imageUrl = "https://images.unsplash.com/photo-1604328698692-f76ea9498e76?w=800",
                bedrooms = 0, bathrooms = 1, squareFootage = 120,
                address = "Ferhadija 15", city = "Sarajevo", country = "Bosnia and Herzegovina",
                latitude = 43.8589, longitude = 18.4318,
                propertyType = "Commercial", listingType = "For Rent",
                agentName = "Eglantina Dervishi",
                description = "Prime retail space on Sarajevo's famous Ferhadija pedestrian street. High visibility and foot traffic, ideal for boutique or caf\u00e9.",
                agentPhone = "+355 69 456 7890", agentEmail = "eglantina@eliterealty.al",
                yearBuilt = 2010, floorNumber = 1, totalFloors = 5, furnished = "Unfurnished", parking = "None",
                agentId = "a2"
            ),
            PropertyEntity(
                id = "p19", title = "Stylish 3BR in Athens Kolonaki",
                price = 340000.0, currency = "EUR",
                imageUrl = "https://images.unsplash.com/photo-1600047509807-ba8f99d2cdde?w=800",
                bedrooms = 3, bathrooms = 2, squareFootage = 105,
                address = "Patriarchou Ioakeim 32", city = "Athens", country = "Greece",
                latitude = 37.9755, longitude = 23.7428,
                propertyType = "Apartment", listingType = "For Sale",
                agentName = "Arben Dedja", isFeatured = true,
                description = "Elegant apartment in the upscale Kolonaki district. Neoclassical building with high ceilings, marble floors, and Acropolis views.",
                agentPhone = "+355 69 234 5678", agentEmail = "arben@balkanproperty.al",
                yearBuilt = 2015, floorNumber = 4, totalFloors = 6, furnished = "Semi-Furnished", parking = "1 Underground",
                agentId = "a3"
            ),
            PropertyEntity(
                id = "p20", title = "Renovated House in Sofia Center",
                price = 195000.0, currency = "EUR",
                imageUrl = "https://images.unsplash.com/photo-1605276374104-dee2a0ed3cd6?w=800",
                bedrooms = 4, bathrooms = 2, squareFootage = 170,
                address = "Ulitsa Vitosha 88", city = "Sofia", country = "Bulgaria",
                latitude = 42.6886, longitude = 23.3189,
                propertyType = "House", listingType = "For Sale",
                agentName = "Mirela Hoxha",
                description = "Fully renovated family house on Sofia's main boulevard. Spacious garden, modern interiors, and excellent access to parks and schools.",
                agentPhone = "+355 69 345 6789", agentEmail = "mirela@sunshineprops.al",
                yearBuilt = 1975, totalFloors = 2, furnished = "Unfurnished", parking = "Garden",
                agentId = "a4"
            )
        )
        propertyDao.insertAll(properties)
    }

    private suspend fun seedAgents() {
        val agents = listOf(
            AgentEntity("a1", "Besmir Kola", agency = "Balkan Property Group", specialization = "Residential", location = "Tirana", rating = 4.9f, reviewsCount = 156, listingsCount = 42, soldCount = 89, yearsExperience = 12, languages = "Albanian,English,Italian", isVerified = true, isPremium = true, bio = "Specializing in luxury residential properties in Tirana.", phone = "+355 69 123 4567", email = "besmir@balkanproperty.al"),
            AgentEntity("a2", "Eglantina Dervishi", agency = "Elite Realty", specialization = "Commercial", location = "Tirana", rating = 4.8f, reviewsCount = 98, listingsCount = 35, soldCount = 67, yearsExperience = 8, languages = "Albanian,English,Greek", isVerified = true, isPremium = true, bio = "Expert in commercial real estate and investment properties.", phone = "+355 69 456 7890", email = "eglantina@eliterealty.al"),
            AgentEntity("a3", "Arben Dedja", agency = "Balkan Property Group", specialization = "Luxury", location = "Durr\u00ebs", rating = 4.7f, reviewsCount = 124, listingsCount = 28, soldCount = 54, yearsExperience = 15, languages = "Albanian,English", isVerified = true, bio = "Luxury property specialist with extensive market knowledge.", phone = "+355 69 234 5678", email = "arben@balkanproperty.al"),
            AgentEntity("a4", "Mirela Hoxha", agency = "Sunshine Properties", specialization = "Residential", location = "Vlor\u00eb", rating = 4.6f, reviewsCount = 87, listingsCount = 23, soldCount = 45, yearsExperience = 6, languages = "Albanian,English,French", isVerified = true, bio = "Dedicated to finding the perfect home for every client.", phone = "+355 69 345 6789", email = "mirela@sunshineprops.al"),
            AgentEntity("a5", "Gentian Leka", agency = "Adriatic Real Estate", specialization = "Land", location = "Sarand\u00eb", rating = 4.5f, reviewsCount = 65, listingsCount = 19, soldCount = 38, yearsExperience = 10, languages = "Albanian,English,German", isVerified = true, isPremium = true, bio = "Specialized in coastal properties and land development.", phone = "+355 69 678 9012", email = "gentian@adriaticre.al"),
            AgentEntity("a6", "Klodian M\u00ebhilli", agency = "Elite Realty", specialization = "Commercial", location = "Tirana", rating = 4.4f, reviewsCount = 56, listingsCount = 31, soldCount = 42, yearsExperience = 7, languages = "Albanian,English", bio = "Commercial and office space specialist in central Tirana.", phone = "+355 69 567 8901", email = "klodian@eliterealty.al")
        )
        agentDao.insertAll(agents)
    }

    private suspend fun seedAgencies() {
        val agencies = listOf(
            AgencyEntity("ag1", "Balkan Property Group", address = "Rruga Myslym Shyri 27", city = "Tirana", country = "Albania", phone = "+355 4 234 5678", email = "info@balkanproperty.al", website = "balkanproperty.al", rating = 4.8f, reviewsCount = 234, agentsCount = 15, listingsCount = 120, description = "Leading real estate agency in Albania specializing in residential and commercial properties.", isVerified = true),
            AgencyEntity("ag2", "Elite Realty Albania", address = "Bulevardi Zogu I 45", city = "Tirana", country = "Albania", phone = "+355 4 345 6789", email = "contact@eliterealty.al", website = "eliterealty.al", rating = 4.7f, reviewsCount = 187, agentsCount = 12, listingsCount = 95, description = "Premium real estate services for discerning clients across the Balkans.", isVerified = true),
            AgencyEntity("ag3", "Sunshine Properties", address = "Rruga Ismail Qemali 15", city = "Vlor\u00eb", country = "Albania", phone = "+355 33 456 789", email = "info@sunshineprops.al", website = "sunshineprops.al", rating = 4.6f, reviewsCount = 143, agentsCount = 8, listingsCount = 67, description = "Your trusted partner for coastal properties in Southern Albania.", isVerified = true),
            AgencyEntity("ag4", "Adriatic Real Estate", address = "Rruga e Sarand\u00ebs 10", city = "Sarand\u00eb", country = "Albania", phone = "+355 85 234 567", email = "hello@adriaticre.al", website = "adriaticre.al", rating = 4.5f, reviewsCount = 98, agentsCount = 6, listingsCount = 45, description = "Specialized in beachfront and coastal properties along the Albanian Riviera."),
            AgencyEntity("ag5", "Kosovo Prime Realty", address = "Bulevardi N\u00ebn\u00eb Terez\u00eb 22", city = "Pristina", country = "Kosovo", phone = "+383 38 123 456", email = "info@kosovoprime.com", website = "kosovoprime.com", rating = 4.4f, reviewsCount = 112, agentsCount = 10, listingsCount = 78, description = "The leading real estate agency in Kosovo with a focus on modern developments.", isVerified = true)
        )
        agencyDao.insertAll(agencies)
    }

    private suspend fun seedConversations() {
        val conversations = listOf(
            ConversationEntity("conv1", "Besmir Kola", lastMessage = "The property is still available. Would you like to schedule a viewing?", lastMessageTime = "2 min ago", unreadCount = 2, propertyTitle = "Modern 3BR Apartment in Blloku", propertyImageUrl = "https://images.unsplash.com/photo-1522708323590-d24dbb6b0267?w=400", isOnline = true),
            ConversationEntity("conv2", "Eglantina Dervishi", lastMessage = "I've sent you the documents for review.", lastMessageTime = "1 hour ago", propertyTitle = "Penthouse with Panoramic City Views", propertyImageUrl = "https://images.unsplash.com/photo-1512917774080-9991f1c4c750?w=400"),
            ConversationEntity("conv3", "Arben Dedja", lastMessage = "The price is negotiable. Let me know your offer.", lastMessageTime = "Yesterday", unreadCount = 1, propertyTitle = "Luxury Villa with Pool in Durr\u00ebs", propertyImageUrl = "https://images.unsplash.com/photo-1580587771525-78b9dba3b914?w=400", isOnline = true),
            ConversationEntity("conv4", "Mirela Hoxha", lastMessage = "Thank you for your interest! I'll get back to you shortly.", lastMessageTime = "3 days ago", propertyTitle = "Cozy Studio near University")
        )
        conversationDao.insertAll(conversations)
    }

    private suspend fun seedMessages() {
        val convIds = listOf("conv1", "conv2", "conv3", "conv4")
        for (convId in convIds) {
            val messages = listOf(
                MessageEntity("${convId}_m1", convId, "Hi, I'm interested in this property.", "10:00 AM", true),
                MessageEntity("${convId}_m2", convId, "Hello! Thank you for your interest. The property is currently available for viewing.", "10:05 AM", false),
                MessageEntity("${convId}_m3", convId, "Great! What's the earliest available time?", "10:10 AM", true),
                MessageEntity("${convId}_m4", convId, "We can arrange a viewing this Saturday at 2 PM. Does that work for you?", "10:15 AM", false),
                MessageEntity("${convId}_m5", convId, "That works perfectly. I'll be there.", "10:20 AM", true),
                MessageEntity("${convId}_m6", convId, "Excellent! I'll send you the exact address and directions. See you Saturday!", "10:25 AM", false)
            )
            messageDao.insertAll(messages)
        }
    }

    private suspend fun seedSavedSearches() {
        val searches = listOf(
            SavedSearchEntity("s1", "Tirana Apartments", query = "apartment", location = "Tirana", propertyType = "Apartment", priceRange = "\u20ac50,000 - \u20ac200,000", bedrooms = "2+", matchCount = 24, newCount = 3),
            SavedSearchEntity("s2", "Coastal Villas", query = "villa", location = "Durr\u00ebs, Vlor\u00eb", propertyType = "Villa", priceRange = "\u20ac200,000 - \u20ac500,000", bedrooms = "3+", matchCount = 12, newCount = 1),
            SavedSearchEntity("s3", "Commercial Tirana", query = "office", location = "Tirana", propertyType = "Commercial", priceRange = "\u20ac1,000 - \u20ac5,000/mo", bedrooms = "N/A", notificationsEnabled = false, matchCount = 8),
            SavedSearchEntity("s4", "Student Rentals", query = "studio", location = "Tirana", propertyType = "Studio", priceRange = "\u20ac200 - \u20ac500/mo", bedrooms = "1", matchCount = 15, newCount = 5)
        )
        for (search in searches) {
            savedSearchDao.insert(search)
        }
    }
}
