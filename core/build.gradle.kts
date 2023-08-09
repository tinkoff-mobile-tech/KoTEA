plugins {
    kotlin("multiplatform")
    //alias(libs.plugins.gradle.android.library)
    alias(libs.plugins.gradle.binary.validator)
}

// TODO: ADD KMP PUBLISHING 

/*android {
    namespace = "ru.tinkoff.kotea"
    compileSdk = 33

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    //kotlinOptions.jvmTarget = JavaVersion.VERSION_17.toString()
}*/

kotlin {
    jvm {
        compilations.all {
            kotlinOptions.jvmTarget = JavaVersion.VERSION_17.toString()
        }
    }

    //androidTarget()

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "kotea"
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(libs.kotlinx.coroutines.core)
            }
        }

        val commonTest by getting {
            dependencies {
                implementation(libs.test.junit4)
                implementation(libs.test.turbine)
            }
        }

        val jvmMain by getting
    }
}
