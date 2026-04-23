import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.room)
    alias(libs.plugins.kapt)
    alias(libs.plugins.kotlin.parcelize)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.dam.proydrp"
    compileSdk = 36
    val properties = Properties()
    val propertiesFile = project.rootProject.file("local.properties")
    if (propertiesFile.exists()) {
        properties.load(propertiesFile.inputStream())
    }
    val baseUrlIp = properties.getProperty("BASE_URL_IP") ?: "10.0.2.2"

    defaultConfig {
        applicationId = "com.dam.proydrp"
        minSdk = 26
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField("String", "BASE_URL", "\"http://$baseUrlIp:8000/\"")
        buildConfigField("String", "WS_URL", "\"ws://$baseUrlIp:8000/ws/\"")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    room {
        schemaDirectory("$projectDir/schemas")
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.compose.foundation)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)

    //Librerías de hilt
    implementation(libs.hilt.android)
    implementation(libs.androidx.hilt.navigation.compose)
    kapt(libs.hilt.android.compiler)

    //Librerías de Room
    //Las dependencias runtime son las mismas
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx) // Extensión Kotlin para Room

    //Aplicar el compilador kapt
    kapt(libs.androidx.room.compiler)

    //Librerías de coil para leer fotos de urls
    implementation(libs.coil.compose)

    //lottie
    implementation(libs.lottie.compose)

    implementation(libs.accompanist.permissions)
    implementation(libs.kotlinx.serialization.json)

    //retrofit
    implementation(libs.retrofit.main)
    implementation(libs.retrofit.gson)
    implementation(libs.okhttp.logging)

    //SecureSharedPreferences
    implementation(libs.androidx.security.crypto)

    //SplashScreen
    implementation(libs.androidx.core.splashscreen)
}