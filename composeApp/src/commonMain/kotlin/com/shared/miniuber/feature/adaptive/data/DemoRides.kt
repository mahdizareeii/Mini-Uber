package com.shared.miniuber.feature.adaptive.data

import com.shared.miniuber.feature.adaptive.model.DemoRide

/** Local fixtures for the adaptive demo, independent of ride-request state. */
internal object DemoRides {
    val rides = listOf(
        DemoRide(
            id = "ride-01",
            date = "28 Oct",
            time = "09:42",
            pickup = "City Centre",
            destination = "Riverside Park",
            fare = "$12.40",
            distance = "4.8 km",
            duration = "14 min",
            driver = "Alex",
            vehicle = "Electric · Comfort"
        ),
        DemoRide(
            id = "ride-02",
            date = "27 Oct",
            time = "18:16",
            pickup = "North Station",
            destination = "Modern Art Gallery",
            fare = "$16.80",
            distance = "6.2 km",
            duration = "21 min",
            driver = "Sam",
            vehicle = "Hybrid · Comfort"
        ),
        DemoRide(
            id = "ride-03",
            date = "26 Oct",
            time = "08:30",
            pickup = "Oak Street",
            destination = "University Library",
            fare = "$9.60",
            distance = "3.1 km",
            duration = "11 min",
            driver = "Jordan",
            vehicle = "Electric · Standard"
        ),
        DemoRide(
            id = "ride-04",
            date = "25 Oct",
            time = "20:05",
            pickup = "Market Square",
            destination = "Harbour Walk",
            fare = "$14.20",
            distance = "5.4 km",
            duration = "18 min",
            driver = "Taylor",
            vehicle = "Hybrid · Comfort"
        ),
        DemoRide(
            id = "ride-05",
            date = "24 Oct",
            time = "12:48",
            pickup = "Botanical Gardens",
            destination = "Central Museum",
            fare = "$11.30",
            distance = "4.2 km",
            duration = "13 min",
            driver = "Casey",
            vehicle = "Electric · Standard"
        ),
        DemoRide(
            id = "ride-06",
            date = "23 Oct",
            time = "07:15",
            pickup = "West End",
            destination = "Airport Terminal",
            fare = "$28.50",
            distance = "17.6 km",
            duration = "32 min",
            driver = "Morgan",
            vehicle = "Electric · Comfort"
        )
    )
}
