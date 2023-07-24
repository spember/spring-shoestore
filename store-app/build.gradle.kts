plugins {
    id("org.springframework.boot") version "3.0.5"
    id("io.spring.dependency-management") version "1.1.0"
}

dependencyManagement {
    imports {
        mavenBom("org.testcontainers:testcontainers-bom:${property("testcontainersVersion")}")
    }
}

dependencies {
    implementation(project(":store-core"))
    implementation(project(":store-details"))
    implementation("org.springframework.boot:spring-boot-starter-web")

    // It does seem strange to see these 'details' located here in the 'app' module
    // However, 1) the key thing is to make 'core' agnostic, 2) the app module is the 'dirtiest' of all and 3) the
    // spring starters do give us a great deal of convenience (e.g. the jdbc starter automatically creates a Datasource)
    implementation("org.springframework.boot:spring-boot-starter-data-redis")
    implementation("org.springframework.boot:spring-boot-starter-jdbc")

    implementation(libs.jedis)
    implementation(platform("software.amazon.awssdk:bom:2.20.26"))
    implementation("software.amazon.awssdk:dynamodb")

    runtimeOnly("org.postgresql:postgresql")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.testcontainers:junit-jupiter")
    testImplementation("org.testcontainers:postgresql")
    testImplementation("org.testcontainers:localstack")

    testImplementation ("com.amazonaws:aws-java-sdk-s3:1.12.470")

}