import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "3.0.5"
	id("io.spring.dependency-management") version "1.1.0"
	kotlin("jvm") version "1.7.22"
	kotlin("plugin.spring") version "1.7.22"
}

allprojects {
	apply(plugin = "org.jetbrains.kotlin.jvm")
	apply(plugin = "org.jetbrains.kotlin.plugin.spring")


	group = "io.spring"
	version = "0.1.0-SNAPSHOT"

	java {
		sourceCompatibility = JavaVersion.VERSION_17
		targetCompatibility = JavaVersion.VERSION_17
	}

	repositories {
		mavenLocal()
		mavenCentral()
	}

	tasks.withType<KotlinCompile> {
		kotlinOptions {
			freeCompilerArgs = listOf("-Xjsr305=strict")
			jvmTarget = "17"
		}
	}

	tasks.withType<Test> {
		useJUnitPlatform()
	}
}

subprojects {
	extra["testcontainersVersion"] = "1.17.6"
	extra["junitVersion"] = "5.9.2"



	dependencies {
		implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.14.2")
		implementation("org.flywaydb:flyway-core:9.16.3")
		implementation("org.jetbrains.kotlin:kotlin-reflect:1.8.20")

		testImplementation("org.junit.jupiter:junit-jupiter-api:${project.extra["junitVersion"]}")
		testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:${project.extra["junitVersion"]}")

	}
}


