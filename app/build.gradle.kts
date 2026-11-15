plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.roborazzi)
    // AGP 9+ sudah punya Kotlin bawaan, jadi plugin org.jetbrains.kotlin.android tidak dipakai.
}

val signingKeys = listOf(
    "RELEASE_STORE_FILE",
    "RELEASE_STORE_PASSWORD",
    "RELEASE_KEY_ALIAS",
    "RELEASE_KEY_PASSWORD",
)
val releaseSigning: Map<String, String> = signingKeys.mapNotNull { name ->
    (providers.environmentVariable(name).orNull ?: providers.gradleProperty(name).orNull ?: System.getenv(name) ?: null)
        ?.takeIf { it.isNotBlank() }
        ?.let { name to it }
}.toMap()

check(releaseSigning.isEmpty() || releaseSigning.size == signingKeys.size) {
    "Incomplete release signing config. Missing: ${signingKeys - releaseSigning.keys}"
}
if (releaseSigning.isEmpty() &&
    gradle.startParameter.taskNames.any { it.contains("release", ignoreCase = true) }
) {
    logger.warn("No RELEASE_* signing config found: signing release with the debug key (not Play-publishable).")
}

android {
    namespace = "com.ehan.htmlrunner"
    compileSdk = 37

    defaultConfig {
        applicationId = "com.ehan.htmlrunner"
        minSdk = 26
        targetSdk = 37
        versionCode = 1
        versionName = "1.0"
    }

    signingConfigs {
        getByName("debug") {
          val localDebugKs = file("${rootDir}/debug.keystore")
          if (localDebugKs.exists()) {
            storeFile = localDebugKs
          }
        }
        if (releaseSigning.isNotEmpty()) {
            create("release") {
                storeFile = file(releaseSigning.getValue("RELEASE_STORE_FILE"))
                storePassword = releaseSigning.getValue("RELEASE_STORE_PASSWORD")
                keyAlias = releaseSigning.getValue("RELEASE_KEY_ALIAS")
                keyPassword = releaseSigning.getValue("RELEASE_KEY_PASSWORD")
            }
        }
    }

    buildTypes {
        release {
            // Real key when configured, otherwise the debug key: never an unsigned APK.
            signingConfig = signingConfigs.findByName("release") ?: signingConfigs.getByName("debug")
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
        debug {
          signingConfig = signingConfigs.getByName("debug")
        }
    }

    buildTypes {
        release {
            // Real key when configured, otherwise the debug key: never an unsigned APK.
            signingConfig = signingConfigs.findByName("release") ?: signingConfigs.getByName("debug")
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
        debug {
          signingConfig = signingConfigs.getByName("debug")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    buildFeatures {
        compose = true
    }
}

roborazzi {
    // Screenshots are written here instead of build/outputs/roborazzi.
    // Recording on plain `testDebugUnitTest` is switched on in gradle.properties.
    outputDir.set(file("src/test/screenshots"))
}

tasks.withType<Test>().configureEach {
    // Gradle's default 512 MB test heap is tight for Robolectric + Compose.
    maxHeapSize = "2g"
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.navigation.compose)

    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.compose.material3)

    implementation(libs.androidx.datastore.preferences)
    implementation(libs.androidx.documentfile)
    implementation(libs.androidx.webkit)
    implementation(libs.androidx.swiperefreshlayout)
    implementation(libs.kotlinx.coroutines.android)

    debugImplementation(libs.androidx.compose.ui.tooling)
    // Registers ComponentActivity in the manifest so Compose tests can launch it.
    debugImplementation(libs.androidx.compose.ui.test.manifest)

    //testImplementation(platform(libs.androidx.compose.bom))
    testImplementation(libs.junit)
    testImplementation(libs.androidx.test.ext.junit)
    testImplementation(libs.androidx.compose.ui.test.junit4)
    testImplementation(libs.robolectric)
    testImplementation(libs.roborazzi)
    testImplementation(libs.roborazzi.compose)
    testImplementation(libs.roborazzi.junit.rule)
}
