buildscript {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }

    dependencies {
        classpath(libs.bundles.plugins)
    }
}
plugins {
    id("com.android.application").version("8.0.1").apply(false)
    id("com.android.library").version("8.0.1").apply(false)
    kotlin("android").version("1.8.10").apply(false)
}

allprojects {
    repositories {
        google()
        mavenCentral()
    }

    apply(plugin = "maven-publish")
    apply(plugin = "signing")

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
}