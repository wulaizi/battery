import org.gradle.internal.impldep.bsh.commands.dir

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.maven.publish)
}

android {
    namespace = "com.extra.mlkitlib"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.extra.mlkitlib"
        minSdk = 24
        targetSdk = 34
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(project(":mlkitlibrary"))
}

afterEvaluate {
    publishing {
        publications {
            // 创建一个名为 "release" 的 Maven 发布
            create<MavenPublication>("release") {
                // 然后可以根据下面的示例自定义发布的属性
                groupId = "com.github.ZhangBingbin"
                artifactId = "mlkitmanager"
                version = "v0.0.3"
            }
        }
    }
}


