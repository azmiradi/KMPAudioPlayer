plugins {
    kotlin("multiplatform")
    id("com.android.library")
    id("maven-publish")
    id("signing")
}

kotlin {
    android()
    jvm("desktop") {
        jvmToolchain(11)
    }
    js(IR) {
        browser()
        nodejs()
    }

    iosX64()
    iosArm64()
    iosSimulatorArm64()

    sourceSets {
        val commonMain by getting

        val desktopMain by getting {
            dependencies {
                implementation("uk.co.caprica:vlcj:4.8.2")
            }
        }

        val androidMain by getting
        val iosX64Main by getting
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting
        val iosMain by creating {
            dependsOn(commonMain)
            iosX64Main.dependsOn(this)
            iosArm64Main.dependsOn(this)
            iosSimulatorArm64Main.dependsOn(this)
        }
    }
}

android {
    namespace = "com.azmiradi.kmpaudioplayer"
    compileSdk = 33
    defaultConfig {
        minSdk = 24
    }
}



group = "com.azmiradi.kmpaudioplayer"
version = "0.1.2"

apply(plugin = "maven-publish")
apply(plugin = "signing")

extensions.configure<PublishingExtension> {
    repositories {
        maven {
            val isSnapshot = version.toString().endsWith("SNAPSHOT")
            url = uri(
                if (!isSnapshot) "https://s01.oss.sonatype.org/service/local/staging/deploy/maven2"
                else "https://s01.oss.sonatype.org/content/repositories/snapshots"
            )

            credentials {
                username = System.getenv("OssrhUsername")
                password = System.getenv("OssrhPassword")
            }
        }
    }

    val javadocJar = tasks.register<Jar>("javadocJar") {
        archiveClassifier.set("javadoc")
    }

    publications {
        withType<MavenPublication> {
            artifact(javadocJar)

            pom {
                name.set("KMPAudioPlayer")
                description.set("A Kotlin Multiplatform library for MediaPlayer")
                licenses {
                    license {
                        name.set("Apache-2.0")
                        url.set("https://opensource.org/licenses/Apache-2.0")
                    }
                }
                url.set("https://github.com/azmiradi/KMPAudioPlayer")
                issueManagement {
                    system.set("Github")
                    url.set("https://github.com/azmiradi/KMPAudioPlayer/issues")
                }
                scm {
                    connection.set("https://github.com/azmiradi/KMPAudioPlayer.git")
                    url.set("https://github.com/azmiradi/KMPAudioPlayer")
                }
                developers {
                    developer {
                        name.set("Azmi Radi")
                        email.set("azmiradi97@gmail.com")
                    }
                }
            }
        }
    }
}

val publishing = extensions.getByType<PublishingExtension>()
extensions.configure<SigningExtension> {
    useInMemoryPgpKeys(
        System.getenv("SigningKeyId"),
        System.getenv("SigningKey"),
        System.getenv("SigningPassword"),
    )

    sign(publishing.publications)
}

project.tasks.withType(AbstractPublishToMaven::class.java).configureEach {
    dependsOn(project.tasks.withType(Sign::class.java))
}