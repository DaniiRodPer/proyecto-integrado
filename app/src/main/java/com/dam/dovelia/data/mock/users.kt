package com.dam.dovelia.data.mock

import com.dam.dovelia.data.model.Accommodation
import com.dam.dovelia.data.model.AccommodationTag
import com.dam.dovelia.data.model.UserProfile
import com.dam.dovelia.data.model.UserTag
import java.time.LocalDate

val mockUserProfileList = listOf(
    UserProfile(
        id = "1",
        name = "María de todos los cármenes",
        surname = "Rodriguez Vazquez",
        email = "maria.perez@email.com",
        birthDate = LocalDate.of(1996, 5, 12),
        profilePicUrl = "https://randomuser.me/api/portraits/women/1.jpg",
        userTags = listOf(UserTag.NON_SMOKER, UserTag.QUIET, UserTag.CLEAN_FREAK),
        userDescription = "Soy arquitecta, me encanta la pintura y el jazz. Busco una convivencia tranquila donde el respeto y la limpieza sean lo primero.",
        creationDate = System.currentTimeMillis() - 1000000,
        accommodation = Accommodation(
            description = "Busco un ambiente tranquilo y respetuoso. Mi piso está en pleno centro histórico con vistas increíbles al Tajo.",
            squareMeters = 85,
            bathrooms = 2,
            bedrooms = 3,
            picsUrls = listOf(
                "https://images.unsplash.com/photo-1522708323590-d24dbb6b0267?auto=format&fit=crop&w=1000",
                "https://images.unsplash.com/photo-1502672260266-1c1ef2d93688?auto=format&fit=crop&w=1000"
            ),
            tags = listOf(AccommodationTag.WIFI, AccommodationTag.CENTER, AccommodationTag.HEATING)
        )
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
        creationDate = System.currentTimeMillis() - 2000000,
        accommodation = Accommodation(
            description = "Teletrabajo y busco a alguien activo. El piso es moderno, muy luminoso y está cerca de gimnasios.",
            squareMeters = 70,
            bathrooms = 1,
            bedrooms = 2,
            picsUrls = listOf(
                "https://images.unsplash.com/photo-1560448204-61dc36dc98c8?auto=format&fit=crop&w=1000",
                "https://images.unsplash.com/photo-1484154218962-a197022b5858?auto=format&fit=crop&w=1000"
            ),
            tags = listOf(AccommodationTag.AIR_CONDITIONING, AccommodationTag.GYM, AccommodationTag.WIFI)
        )
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
        creationDate = System.currentTimeMillis() - 3000000,
        accommodation = Accommodation(
            description = "Ambiente zen y relajado. Tengo una habitación amplia con mucha luz. Terraza ideal para desayunar al sol.",
            squareMeters = 110,
            bathrooms = 2,
            bedrooms = 4,
            picsUrls = listOf(
                "https://images.unsplash.com/photo-1512917774080-9991f1c4c750?auto=format&fit=crop&w=1000",
                "https://images.unsplash.com/photo-1613490493576-7fde63acd811?auto=format&fit=crop&w=1000"
            ),
            tags = listOf(AccommodationTag.POOL, AccommodationTag.PARKING, AccommodationTag.BALCONY)
        )
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
        creationDate = System.currentTimeMillis() - 4000000,
        accommodation = Accommodation(
            description = "Casa grande reformada. Tengo un setup de gaming potente en el salón y fibra óptica de alta velocidad.",
            squareMeters = 130,
            bathrooms = 2,
            bedrooms = 3,
            picsUrls = listOf(
                "https://images.unsplash.com/photo-1505691938895-1758d7eaa511?auto=format&fit=crop&w=1000"
            ),
            tags = listOf(AccommodationTag.FIREPLACE, AccommodationTag.WIFI, AccommodationTag.HEATING)
        )
    ),
    UserProfile(
        id = "5",
        name = "Lucía",
        surname = "Blanco",
        email = "lucia.blanco@email.com",
        birthDate = LocalDate.of(2001, 8, 30),
        profilePicUrl = "https://randomuser.me/api/portraits/women/5.jpg",
        userTags = listOf(UserTag.EARLY_BIRD, UserTag.PET_OWNER, UserTag.INTROVERT),
        userDescription = "Estudiante de veterinaria. Madrugo mucho para pasear con Lucas y estudiar tranquila. Soy bastante reservada.",
        creationDate = System.currentTimeMillis() - 5000000,
        accommodation = Accommodation(
            description = "Vivo con mi perrito Lucas. Busco a alguien tranquilo que no le importe compartir espacio con mascotas.",
            squareMeters = 95,
            bathrooms = 1,
            bedrooms = 2,
            picsUrls = listOf(
                "https://images.unsplash.com/photo-1554995207-c18c203602cb?auto=format&fit=crop&w=1000"
            ),
            tags = listOf(AccommodationTag.PETS, AccommodationTag.SMOKE_FREE, AccommodationTag.PARKING)
        )
    ),
    UserProfile(
        id = "6",
        name = "Marcos",
        surname = "López",
        email = "marcos.lopez@email.com",
        birthDate = LocalDate.of(1995, 3, 22),
        profilePicUrl = "https://randomuser.me/api/portraits/men/6.jpg",
        userTags = listOf(UserTag.PARTY_GOER, UserTag.EXTROVERT, UserTag.SMOKER),
        userDescription = "Relaciones públicas. Casi nunca estoy en casa pero cuando estoy me gusta invitar amigos y hacer cenas.",
        creationDate = System.currentTimeMillis() - 6000000,
        accommodation = Accommodation(
            description = "Ático en el centro. Perfecto para hacer reuniones, tiene una terraza espectacular.",
            squareMeters = 120,
            bathrooms = 2,
            bedrooms = 3,
            picsUrls = listOf(
                "https://images.unsplash.com/photo-1524758631624-e2822e304c36?auto=format&fit=crop&w=1000"
            ),
            tags = listOf(AccommodationTag.BALCONY, AccommodationTag.CENTER, AccommodationTag.AIR_CONDITIONING)
        )
    ),
    UserProfile(
        id = "7",
        name = "Sofía",
        surname = "Martínez",
        email = "sofia.martinez@email.com",
        birthDate = LocalDate.of(1998, 7, 11),
        profilePicUrl = "https://randomuser.me/api/portraits/women/7.jpg",
        userTags = listOf(UserTag.VEGETARIAN, UserTag.QUIET, UserTag.CLEAN_FREAK),
        userDescription = "Enfermera de turnos rotativos. Necesito silencio absoluto por las mañanas cuando trabajo de noche.",
        creationDate = System.currentTimeMillis() - 7000000,
        accommodation = Accommodation(
            description = "Piso alejado del bullicio, muy bien aislado. La cocina está completamente equipada.",
            squareMeters = 80,
            bathrooms = 1,
            bedrooms = 2,
            picsUrls = listOf(
                "https://images.unsplash.com/photo-1493809842364-78817add7ffb?auto=format&fit=crop&w=1000"
            ),
            tags = listOf(AccommodationTag.HEATING, AccommodationTag.WIFI, AccommodationTag.SMOKE_FREE)
        )
    ),
    UserProfile(
        id = "8",
        name = "David",
        surname = "Gómez",
        email = "david.gomez@email.com",
        birthDate = LocalDate.of(1988, 1, 30),
        profilePicUrl = "https://randomuser.me/api/portraits/men/8.jpg",
        userTags = listOf(UserTag.CHILL, UserTag.GAMER, UserTag.NON_SMOKER),
        userDescription = "Profesor de instituto. Me gusta leer y jugar a juegos de mesa los fines de semana.",
        creationDate = System.currentTimeMillis() - 8000000,
        accommodation = Accommodation(
            description = "Casa con patio interior andaluz. Mucha luz y un salón muy grande.",
            squareMeters = 150,
            bathrooms = 2,
            bedrooms = 4,
            picsUrls = listOf(
                "https://images.unsplash.com/photo-1513694203232-719a280e022f?auto=format&fit=crop&w=1000"
            ),
            tags = listOf(AccommodationTag.AIR_CONDITIONING, AccommodationTag.WIFI)
        )
    ),
    UserProfile(
        id = "9",
        name = "Laura",
        surname = "Díaz",
        email = "laura.diaz@email.com",
        birthDate = LocalDate.of(2002, 9, 15),
        profilePicUrl = "https://randomuser.me/api/portraits/women/9.jpg",
        userTags = listOf(UserTag.PARTY_GOER, UserTag.TRAVELER, UserTag.EXTROVERT),
        userDescription = "Estudiante Erasmus. ¡Buscando gente internacional para intercambiar culturas y salir de tapas!",
        creationDate = System.currentTimeMillis() - 9000000,
        accommodation = Accommodation(
            description = "Piso de estudiantes en Malasaña. Siempre hay vida, ideal si quieres conocer gente.",
            squareMeters = 100,
            bathrooms = 2,
            bedrooms = 4,
            picsUrls = listOf(
                "https://images.unsplash.com/photo-1522770179533-24471fcdba45?auto=format&fit=crop&w=1000"
            ),
            tags = listOf(AccommodationTag.CENTER, AccommodationTag.WIFI, AccommodationTag.BALCONY)
        )
    ),
    UserProfile(
        id = "10",
        name = "Hugo",
        surname = "Fernández",
        email = "hugo.fernandez@email.com",
        birthDate = LocalDate.of(1994, 11, 8),
        profilePicUrl = "https://randomuser.me/api/portraits/men/10.jpg",
        userTags = listOf(UserTag.FITNESS_ENTHUSIAST, UserTag.EARLY_BIRD, UserTag.CLEAN_FREAK),
        userDescription = "Entrenador personal. Mi vida es muy rutinaria y ordenada. La limpieza de las zonas comunes es innegociable.",
        creationDate = System.currentTimeMillis() - 10000000,
        accommodation = Accommodation(
            description = "Urbanización con zonas deportivas. El piso está impecable y minimalista.",
            squareMeters = 90,
            bathrooms = 2,
            bedrooms = 2,
            picsUrls = listOf(
                "https://images.unsplash.com/photo-1585128719715-46776b56a0d1?auto=format&fit=crop&w=1000"
            ),
            tags = listOf(AccommodationTag.POOL, AccommodationTag.GYM, AccommodationTag.PARKING)
        )
    ),
    UserProfile(
        id = "11",
        name = "Ana",
        surname = "Torres",
        email = "ana.torres@email.com",
        birthDate = LocalDate.of(1997, 4, 18),
        profilePicUrl = "https://randomuser.me/api/portraits/women/11.jpg",
        userTags = listOf(UserTag.VEGAN, UserTag.PET_OWNER, UserTag.CHILL),
        userDescription = "Tengo 3 gatos rescatados. Trabajo desde casa ilustrando cuentos infantiles.",
        creationDate = System.currentTimeMillis() - 11000000,
        accommodation = Accommodation(
            description = "Piso bohemio adaptado para gatos. Muy acogedor en invierno.",
            squareMeters = 75,
            bathrooms = 1,
            bedrooms = 2,
            picsUrls = listOf(
                "https://images.unsplash.com/photo-1567496898905-af4139885770?auto=format&fit=crop&w=1000"
            ),
            tags = listOf(AccommodationTag.PETS, AccommodationTag.HEATING, AccommodationTag.WIFI)
        )
    ),
    UserProfile(
        id = "12",
        name = "Pedro",
        surname = "Sánchez",
        email = "pedro.sanchez@email.com",
        birthDate = LocalDate.of(1991, 6, 25),
        profilePicUrl = "https://randomuser.me/api/portraits/men/12.jpg",
        userTags = listOf(UserTag.OUTDOORSY, UserTag.NON_SMOKER, UserTag.EARLY_BIRD),
        userDescription = "Guía de montaña. Busco a alguien que cuide las plantas cuando estoy de ruta.",
        creationDate = System.currentTimeMillis() - 12000000,
        accommodation = Accommodation(
            description = "Casa baja muy cerca de la playa, ideal para guardar tablas de surf o bicis.",
            squareMeters = 85,
            bathrooms = 1,
            bedrooms = 2,
            picsUrls = listOf(
                "https://images.unsplash.com/photo-1499951360447-b19be8fe80f5?auto=format&fit=crop&w=1000"
            ),
            tags = listOf(AccommodationTag.PARKING, AccommodationTag.WIFI)
        )
    ),
    UserProfile(
        id = "13",
        name = "Marta",
        surname = "Romero",
        email = "marta.romero@email.com",
        birthDate = LocalDate.of(1993, 12, 12),
        profilePicUrl = "https://randomuser.me/api/portraits/women/13.jpg",
        userTags = listOf(UserTag.NIGHT_OWL, UserTag.GAMER, UserTag.INTROVERT),
        userDescription = "Desarrolladora backend. Casi no salgo de mi cueva, pero soy amigable.",
        creationDate = System.currentTimeMillis() - 13000000,
        accommodation = Accommodation(
            description = "Piso interior pero muy tranquilo. Tiene aire acondicionado en todas las estancias.",
            squareMeters = 65,
            bathrooms = 1,
            bedrooms = 2,
            picsUrls = listOf(
                "https://images.unsplash.com/photo-1598928506311-c55ded91a20c?auto=format&fit=crop&w=1000"
            ),
            tags = listOf(AccommodationTag.AIR_CONDITIONING, AccommodationTag.CENTER, AccommodationTag.SMOKE_FREE)
        )
    ),
    UserProfile(
        id = "14",
        name = "Luis",
        surname = "Navarro",
        email = "luis.navarro@email.com",
        birthDate = LocalDate.of(1989, 8, 3),
        profilePicUrl = "https://randomuser.me/api/portraits/men/14.jpg",
        userTags = listOf(UserTag.SPORTY, UserTag.EXTROVERT, UserTag.PARTY_GOER),
        userDescription = "Piloto comercial. Paso muchos días fuera, así que el piso será casi entero para ti.",
        creationDate = System.currentTimeMillis() - 14000000,
        accommodation = Accommodation(
            description = "Piso moderno cerca del aeropuerto y con buenas conexiones al centro.",
            squareMeters = 95,
            bathrooms = 2,
            bedrooms = 3,
            picsUrls = listOf(
                "https://images.unsplash.com/photo-1505692795793-20f543448bf6?auto=format&fit=crop&w=1000"
            ),
            tags = listOf(AccommodationTag.PARKING, AccommodationTag.WIFI, AccommodationTag.POOL)
        )
    ),
    UserProfile(
        id = "15",
        name = "Carmen",
        surname = "Molina",
        email = "carmen.molina@email.com",
        birthDate = LocalDate.of(2000, 2, 28),
        profilePicUrl = "https://randomuser.me/api/portraits/women/15.jpg",
        userTags = listOf(UserTag.TRAVELER, UserTag.VEGETARIAN, UserTag.CHILL),
        userDescription = "Música de profesión. Doy clases de violín en el salón por las tardes.",
        creationDate = System.currentTimeMillis() - 15000000,
        accommodation = Accommodation(
            description = "Casa antigua reformada. Excelente acústica y muy fresca en verano.",
            squareMeters = 110,
            bathrooms = 1,
            bedrooms = 3,
            picsUrls = listOf(
                "https://images.unsplash.com/photo-1484154218962-a197022b5858?auto=format&fit=crop&w=1000"
            ),
            tags = listOf(AccommodationTag.CENTER, AccommodationTag.WIFI)
        )
    ),

    // --- USUARIOS SIN ALOJAMIENTO (Buscando casa o sin publicar) ---
    UserProfile(
        id = "16",
        name = "Alejandro",
        surname = "Vázquez",
        email = "alejandro.vazquez@email.com",
        birthDate = LocalDate.of(1996, 9, 10),
        profilePicUrl = "https://randomuser.me/api/portraits/men/16.jpg",
        userTags = listOf(UserTag.NON_SMOKER, UserTag.SPORTY, UserTag.INTROVERT),
        userDescription = "Acabo de conseguir un trabajo como programador y busco piso para entrar ya. Soy ordenado y hago vida tranquila.",
        creationDate = System.currentTimeMillis() - 16000000,
        accommodation = null // No tiene casa
    ),
    UserProfile(
        id = "17",
        name = "Irene",
        surname = "Iglesias",
        email = "irene.iglesias@email.com",
        birthDate = LocalDate.of(2003, 1, 14),
        profilePicUrl = "https://randomuser.me/api/portraits/women/17.jpg",
        userTags = listOf(UserTag.PARTY_GOER, UserTag.EXTROVERT, UserTag.SMOKER),
        userDescription = "Voy a empezar mi máster y busco gente divertida con quien compartir piso cerca de la universidad.",
        creationDate = System.currentTimeMillis() - 17000000,
        accommodation = null // No tiene casa
    ),
    UserProfile(
        id = "18",
        name = "Víctor",
        surname = "Giménez",
        email = "victor.gimenez@email.com",
        birthDate = LocalDate.of(1990, 5, 5),
        profilePicUrl = "https://randomuser.me/api/portraits/men/18.jpg",
        userTags = listOf(UserTag.PET_OWNER, UserTag.OUTDOORSY, UserTag.VEGAN),
        userDescription = "Tengo un Golden Retriever muy bueno. Busco algún alojamiento donde admitan mascotas, preferiblemente con algo de patio.",
        creationDate = System.currentTimeMillis() - 18000000,
        accommodation = null // No tiene casa
    ),
    UserProfile(
        id = "19",
        name = "Paula",
        surname = "Blázquez",
        email = "paula.blazquez@email.com",
        birthDate = LocalDate.of(1985, 10, 31),
        profilePicUrl = "https://randomuser.me/api/portraits/women/19.jpg",
        userTags = listOf(UserTag.QUIET, UserTag.CLEAN_FREAK, UserTag.NON_SMOKER),
        userDescription = "Opositora a tiempo completo. Busco un ambiente de estudio, silencio y máximo respeto por los horarios.",
        creationDate = System.currentTimeMillis() - 19000000,
        accommodation = null // No tiene casa
    ),
    UserProfile(
        id = "20",
        name = "Raúl",
        surname = "Ortiz",
        email = "raul.ortiz@email.com",
        birthDate = LocalDate.of(1998, 4, 9),
        profilePicUrl = "https://randomuser.me/api/portraits/men/20.jpg",
        userTags = listOf(UserTag.GAMER, UserTag.CHILL, UserTag.NIGHT_OWL),
        userDescription = "Actualmente vivo en las afueras pero busco mudarme más cerca del centro. Soy fácil de llevar y me adapto a todo.",
        creationDate = System.currentTimeMillis() - 20000000,
        accommodation = null // No tiene casa
    )
)