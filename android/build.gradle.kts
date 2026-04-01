plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

val gdxVersion: String by project

val nativeDeps by configurations.creating

android {
    namespace = "com.terraindash"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.terraindash"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "0.1.0"
        ndk {
            abiFilters += listOf("armeabi-v7a", "arm64-v8a", "x86", "x86_64")
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        debug {
            isMinifyEnabled = false
            applicationIdSuffix = ".debug"
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    sourceSets {
        getByName("main") {
            assets.srcDirs(rootProject.file("assets"))
            jniLibs.srcDirs("libs")
        }
    }
}

dependencies {
    implementation(project(":core"))
    implementation("com.badlogicgames.gdx:gdx-backend-android:$gdxVersion")

    nativeDeps("com.badlogicgames.gdx:gdx-platform:$gdxVersion:natives-armeabi-v7a")
    nativeDeps("com.badlogicgames.gdx:gdx-platform:$gdxVersion:natives-arm64-v8a")
    nativeDeps("com.badlogicgames.gdx:gdx-platform:$gdxVersion:natives-x86")
    nativeDeps("com.badlogicgames.gdx:gdx-platform:$gdxVersion:natives-x86_64")
    nativeDeps("com.badlogicgames.gdx:gdx-box2d-platform:$gdxVersion:natives-armeabi-v7a")
    nativeDeps("com.badlogicgames.gdx:gdx-box2d-platform:$gdxVersion:natives-arm64-v8a")
    nativeDeps("com.badlogicgames.gdx:gdx-box2d-platform:$gdxVersion:natives-x86")
    nativeDeps("com.badlogicgames.gdx:gdx-box2d-platform:$gdxVersion:natives-x86_64")
    nativeDeps("com.badlogicgames.gdx:gdx-freetype-platform:$gdxVersion:natives-armeabi-v7a")
    nativeDeps("com.badlogicgames.gdx:gdx-freetype-platform:$gdxVersion:natives-arm64-v8a")
    nativeDeps("com.badlogicgames.gdx:gdx-freetype-platform:$gdxVersion:natives-x86")
    nativeDeps("com.badlogicgames.gdx:gdx-freetype-platform:$gdxVersion:natives-x86_64")
}

tasks.register("copyNatives") {
    doLast {
        val abis = mapOf(
            "armeabi-v7a" to "armeabi-v7a",
            "arm64-v8a" to "arm64-v8a",
            "x86_64" to "x86_64",
            "x86" to "x86"
        )

        nativeDeps.files.forEach { jar ->
            val name = jar.name
            val abi = abis.keys.firstOrNull { name.contains(it) } ?: return@forEach
            val targetDir = file("libs/$abi")
            targetDir.mkdirs()

            zipTree(jar).files.filter { it.name.endsWith(".so") }.forEach { so ->
                so.copyTo(File(targetDir, so.name), overwrite = true)
            }
        }
    }
}

tasks.named("preBuild") {
    dependsOn("copyNatives")
}
