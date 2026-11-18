import java.util.regex.Pattern.compile

plugins {
    id("com.android.application")
}

android {
    namespace = "com.example.tcc"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.tcc"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
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

//    kotlinOptions {
//        jvmTarget = "17"
//    }

    buildFeatures {
        viewBinding = true



        //noinspection WrongGradleMethod
        dependencies {
            implementation("androidx.core:core-ktx:1.15.0")
            implementation("androidx.appcompat:appcompat:1.7.0")
            implementation("com.google.android.material:material:1.12.0")
            implementation("androidx.constraintlayout:constraintlayout:2.2.1")

            // Lifecycle
            implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.0")
            implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.0")
            implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.8.0")

            // Navegação
            implementation("androidx.navigation:navigation-fragment-ktx:2.8.9")
            implementation("androidx.navigation:navigation-ui-ktx:2.8.9")

            // SSH
            implementation("com.jcraft:jsch:0.1.55")

            // PDF Viewer
            // Source: https://mvnrepository.com/artifact/com.github.barteksc/AndroidPdfViewerV1
            implementation("com.github.barteksc:AndroidPdfViewerV1:1.6.0")

            // Source: https://mvnrepository.com/artifact/com.github.chrisbanes/PhotoView
            implementation("com.github.chrisbanes:PhotoView:2.0.0")

            //runtimeOnly("com.github.chrisbanes:PhotoView:2.3.0")

        }
    }
}

