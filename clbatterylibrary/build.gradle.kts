plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.maven.publish)
}

android {
    namespace = "com.extra.clbatterylibrary"
    compileSdk = 34

    defaultConfig {
        minSdk = 24
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
        
        externalNativeBuild {
            cmake {
                cppFlags("")
            }
        }
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

    externalNativeBuild {
        cmake {
            path = file("src/main/cpp/CMakeLists.txt")
        }
    }
}

dependencies {
    api(libs.mlkit.chinese)
    api(libs.mlkit.all)
    api(libs.bouncycastle)
    api(libs.okhttp)
    api(libs.net)
    api(libs.blankj.utilcodex)
    api(libs.junit)
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