import com.android.build.gradle.internal.tasks.factory.dependsOn

plugins {
    id("com.android.application")
    id("com.google.devtools.ksp")
    kotlin("android")
}

android {
    compileSdk = 32

    defaultConfig {
        applicationId = "com.moriatsushi.launcher.sample"
        minSdk = 21
        targetSdk = 32
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures {
        compose = true
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
    }

    compileOptions {
        sourceCompatibility(JavaVersion.VERSION_1_8)
        targetCompatibility(JavaVersion.VERSION_1_8)
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.3.0"
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    sourceSets {
        getByName("main") {
            kotlin.srcDirs("src/main/kotlin")
        }
        getByName("debug") {
            val generated = "build/generated/ksp/debug"
            kotlin.srcDirs(generated, "$generated/kotlin")
            manifest.srcFile("$generated/AndroidManifest.xml")
        }
        getByName("release") {
            val generated = "build/generated/ksp/release"
            kotlin.srcDirs(generated, "$generated/kotlin")
            manifest.srcFile("$generated/AndroidManifest.xml")
        }
        getByName("test") {
            kotlin.srcDirs("src/test/kotlin")
        }
        getByName("androidTest") {
            kotlin.srcDirs("src/androidTest/kotlin")
        }
    }
}

tasks.register("setupManifestDebug") {
    val generated = "build/generated/ksp/debug"

    val dirRoot = project.projectDir.absolutePath
    val manifestDir = "${dirRoot}/$generated"
    ksp {
        arg("manifest_path", manifestDir)
    }
}

tasks.register("setupManifestRelease") {
    val generated = "build/generated/ksp/release"

    val dirRoot = project.projectDir.absolutePath
    val manifestDir = "${dirRoot}/$generated"
    ksp {
        arg("manifest_path", manifestDir)
    }
}

afterEvaluate {
    tasks.named("generateDebugBuildConfig").dependsOn("setupManifestDebug")
    tasks.named("generateReleaseBuildConfig").dependsOn("setupManifestRelease")
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("androidx.appcompat:appcompat:1.5.0")
    implementation(project(":launcher"))
    ksp(project(":launcher-processor"))

    implementation("androidx.compose.ui:ui:1.2.1")
    implementation("androidx.compose.ui:ui-tooling:1.2.1")
    implementation("androidx.compose.foundation:foundation:1.2.1")
    implementation("androidx.compose.material:material:1.2.1")

    implementation(project(":sample-features:feature-x"))
}
