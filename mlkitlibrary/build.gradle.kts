plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.maven.publish)
}

android {
    namespace = "com.extra.mlkitlibrary"
    compileSdk = 34

    defaultConfig {
        minSdk = 24
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        debug {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    buildFeatures {
        buildConfig = true
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation(libs.mlkit.chinese)
    implementation(libs.mlkit.all)
    implementation(libs.bouncycastle)
    implementation(libs.okhttp)
    implementation(libs.net)
    implementation(libs.blankj.utilcodex)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}

afterEvaluate{
    publishing {
        publications {
            register<MavenPublication>("release") {
                groupId = "com.github.extracod"
                artifactId = "mlkitmanager"
                version = "2.0.4"

                afterEvaluate {
                    from(components["release"])
                }
            }
        }

//        repositories {
//            maven {
//                setUrl("https://packages.aliyun.com/60e5087c2f222be2d940156b/maven/2119021-release-oxlyuf")
//                credentials {
//                    username = "611cb37ccd146a5e9cc23b6e"
//                    password = "J7IZlu4Xge3S"
//                }
//            }
//        }
    }
}

group = "com.github.extracod"
version = "2.0.4"