package com.dam.proydrp.data.mock

import com.dam.proydrp.data.model.AccommodationTag
import com.dam.proydrp.data.model.UserProfile
import com.dam.proydrp.data.model.UserTag
import java.time.LocalDate

val mockUserProfileList = listOf(
    UserProfile(
        id = "1",
        name = "María",
        surname = "Pérez",
        email = "maria.perez@email.com",
        birthDate = LocalDate.of(1996, 5, 12),
        profilePicUrl = "https://randomuser.me/api/portraits/women/1.jpg",
        userTags = listOf(UserTag.NON_SMOKER, UserTag.QUIET),
        userDescription = "Soy arquitecta, me encanta la pintura y el jazz. Busco una convivencia tranquila donde el respeto y la limpieza sean lo primero.",
        city = "Ronda",
        accommodationDescription = "Busco un ambiente tranquilo y respetuoso. Mi piso está en pleno centro histórico con vistas increíbles al Tajo.",
        squareMeters = 85,
        bathrooms = 2,
        bedrooms = 3,
        accommodationPicsUrls = listOf(
            "https://images.unsplash.com/photo-1522708323590-d24dbb6b0267?auto=format&fit=crop&w=1000",
            "https://images.unsplash.com/photo-1502672260266-1c1ef2d93688?auto=format&fit=crop&w=1000",
            "https://images.unsplash.com/photo-1493809842364-78817add7ffb?auto=format&fit=crop&w=1000",
            "https://images.unsplash.com/photo-1484154218962-a197022b5858?auto=format&fit=crop&w=1000"
        ),
        accommodationTags = listOf(AccommodationTag.WIFI, AccommodationTag.CENTER, AccommodationTag.HEATING),
        creationDate = System.currentTimeMillis()
    ),
    UserProfile(
        id = "2",
        name = "Carlos",
        surname = "García",
        email = "carlos.garcia@email.com",
        birthDate = LocalDate.of(1992, 10, 20),
        profilePicUrl = "https://randomuser.me/api/portraits/men/2.jpg",
        userTags = listOf(UserTag.SPORTY, UserTag.FITNESS_ENTHUSIAST, UserTag.OUTDOORSY),
        userDescription = "Ingeniero de software y amante del CrossFit. Los fines de semana suelo estar en la montaña o haciendo surf.",
        city = "Málaga Centro",
        accommodationDescription = "Teletrabajo y busco a alguien activo. El piso es moderno, muy luminoso y está cerca de gimnasios y la Malagueta.",
        squareMeters = 70,
        bathrooms = 1,
        bedrooms = 2,
        accommodationPicsUrls = listOf(
            "https://images.unsplash.com/photo-1560448204-61dc36dc98c8?auto=format&fit=crop&w=1000",
            "https://images.unsplash.com/photo-1484154218962-a197022b5858?auto=format&fit=crop&w=1000",
            "https://images.unsplash.com/photo-1499951360447-b19be8fe80f5?auto=format&fit=crop&w=1000",
            "https://images.unsplash.com/photo-1505692795793-20f543448bf6?auto=format&fit=crop&w=1000"
        ),
        accommodationTags = listOf(AccommodationTag.AIR_CONDITIONING, AccommodationTag.GYM, AccommodationTag.WIFI),
        creationDate = System.currentTimeMillis()
    ),
    UserProfile(
        id = "3",
        name = "Elena",
        surname = "Ruiz",
        email = "elena.ruiz@email.com",
        birthDate = LocalDate.of(1999, 2, 14),
        profilePicUrl = "https://randomuser.me/api/portraits/women/3.jpg",
        userTags = listOf(UserTag.VEGAN, UserTag.TRAVELER, UserTag.CHILL),
        userDescription = "Instructora de yoga. Me apasiona viajar por el sudeste asiático y la cocina creativa basada en plantas.",
        city = "Marbella",
        accommodationDescription = "Ambiente zen y relajado. Tengo una habitación amplia con mucha luz. Terraza ideal para desayunar al sol.",
        squareMeters = 110,
        bathrooms = 2,
        bedrooms = 4,
        accommodationPicsUrls = listOf(
            "https://images.unsplash.com/photo-1512917774080-9991f1c4c750?auto=format&fit=crop&w=1000",
            "https://images.unsplash.com/photo-1613490493576-7fde63acd811?auto=format&fit=crop&w=1000",
            "https://images.unsplash.com/photo-1513694203232-719a280e022f?auto=format&fit=crop&w=1000",
            "https://images.unsplash.com/photo-1524758631624-e2822e304c36?auto=format&fit=crop&w=1000"
        ),
        accommodationTags = listOf(AccommodationTag.POOL, AccommodationTag.PARKING, AccommodationTag.BALCONY),
        creationDate = System.currentTimeMillis()
    ),
    UserProfile(
        id = "4",
        name = "Javier",
        surname = "Sanz",
        email = "javier.sanz@email.com",
        birthDate = LocalDate.of(1990, 12, 5),
        profilePicUrl = "https://randomuser.me/api/portraits/men/4.jpg",
        userTags = listOf(UserTag.GAMER, UserTag.NIGHT_OWL, UserTag.EXTROVERT),
        userDescription = "Diseñador gráfico. Me paso las noches jugando o editando videos. Busco alguien con quien compartir alguna que otra partida y pizza.",
        city = "Antequera",
        accommodationDescription = "Casa grande reformada. Tengo una setup de gaming potente en el salón y fibra óptica de alta velocidad.",
        squareMeters = 130,
        bathrooms = 2,
        bedrooms = 3,
        accommodationPicsUrls = listOf(
            "https://images.unsplash.com/photo-1505691938895-1758d7eaa511?auto=format&fit=crop&w=1000",
            "https://images.unsplash.com/photo-1524758631624-e2822e304c36?auto=format&fit=crop&w=1000",
            "https://images.unsplash.com/photo-1554995207-c18c203602cb?auto=format&fit=crop&w=1000",
            "https://images.unsplash.com/photo-1585128719715-46776b56a0d1?auto=format&fit=crop&w=1000"
        ),
        accommodationTags = listOf(AccommodationTag.FIREPLACE, AccommodationTag.WIFI, AccommodationTag.HEATING),
        creationDate = System.currentTimeMillis()
    ),
    UserProfile(
        id = "5",
        name = "Lucía",
        surname = "Blanco",
        email = "lucia.blanco@email.com",
        birthDate = LocalDate.of(2001, 8, 30),
        profilePicUrl = "https://randomuser.me/api/portraits/women/5.jpg",
        userTags = listOf(UserTag.EARLY_BIRD, UserTag.PET_OWNER, UserTag.INTROVERT),
        userDescription = "Estudiante de veterinaria. Madrugo mucho para pasear con Lucas y estudiar tranquila. Soy bastante reservada pero muy amigable.",
        city = "Estepona",
        accommodationDescription = "Vivo con mi perrito Lucas. Busco a alguien tranquilo que no le importe compartir espacio con mascotas. Piso muy acogedor.",
        squareMeters = 95,
        bathrooms = 1,
        bedrooms = 2,
        accommodationPicsUrls = listOf(
            "https://images.unsplash.com/photo-1554995207-c18c203602cb?auto=format&fit=crop&w=1000",
            "https://images.unsplash.com/photo-1567496898905-af4139885770?auto=format&fit=crop&w=1000",
            "https://images.unsplash.com/photo-1598928506311-c55ded91a20c?auto=format&fit=crop&w=1000",
            "https://images.unsplash.com/photo-1522770179533-24471fcdba45?auto=format&fit=crop&w=1000"
        ),
        accommodationTags = listOf(AccommodationTag.PETS, AccommodationTag.SMOKE_FREE, AccommodationTag.PARKING),
        creationDate = System.currentTimeMillis()
    )
)