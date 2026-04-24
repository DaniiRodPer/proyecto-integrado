package com.dam.dovelia.ui.utils

import com.dam.dovelia.BuildConfig

/**
 * Convierte una ruta relativa de la BD en una URL completa para Coil.
 * Si la URlya es absoluta (Google Auth, galería del móvil), la deja intacta.
 */
fun toFullImageUrl(url: String): String {
    if (url.isBlank()) return ""

    if (url.startsWith("http://") ||
        url.startsWith("https://") ||
        url.startsWith("content://") ||
        url.startsWith("file://")) {
        return url
    }

    return "${BuildConfig.BASE_URL}$url"
}