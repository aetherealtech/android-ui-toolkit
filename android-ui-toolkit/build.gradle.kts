plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    `maven-publish`
    signing
}

group = "com.aetherealtech"
version = "0.0.1"

android {
    namespace = "com.aetherealtech.androiduitoolkit"
    compileSdk = 34

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.4"
    }
    publishing {
        singleVariant("release") {
            withSourcesJar()
        }
    }
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-collections-immutable:0.3.7")
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("androidx.compose.ui:ui:1.5.4")
    implementation("androidx.compose.material3:material3:1.1.2")
    implementation("com.aetherealtech:kotlin-flows-extensions:0.0.2")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}

val NEXUS_USERNAME: String by properties
val NEXUS_PASSWORD: String by properties

afterEvaluate {
    publishing {
        repositories {
            maven {
                credentials {
                    username = NEXUS_USERNAME
                    password = NEXUS_PASSWORD
                }
                val releasesRepoUrl =
                    "https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/"
                val snapshotsRepoUrl =
                    "https://s01.oss.sonatype.org/content/repositories/snapshots/"
                url = uri(
                    if (version.toString()
                            .endsWith("SNAPSHOT")
                    ) snapshotsRepoUrl else releasesRepoUrl
                )
            }
        }

        publications {
            register<MavenPublication>("release") {
                artifactId = "android-ui-toolkit"
                from(components["release"])
            }
            withType<MavenPublication> {
                pom {
                    name.set("android-ui-toolkit")
                    description.set("Android UI Toolkit")
                    url.set("github.com/aetherealtech/android-ui-toolkit")
                    licenses {
                        license {
                            name.set("GPLv3 license")
                            url.set("https://www.gnu.org/licenses/gpl-3.0.en.html")
                        }
                    }
                    issueManagement {
                        system.set("Github")
                        url.set("https://github.com/aetherealtech/android-ui-toolkit/issues")
                    }
                    scm {
                        connection.set("scm:git:git://github.com/aetherealtech/android-ui-toolkit.git")
                        developerConnection.set("scm:git:git@github.com:aetherealtech/android-ui-toolkit.git")
                        url.set("https://github.com/aetherealtech/android-ui-toolkit")
                    }
                    developers {
                        developer {
                            name.set("Dan Coleman")
                            email.set("dan@aetherealtech.com")
                        }
                    }
                }
            }
        }
    }

    signing {
        sign(publishing.publications)
    }
}