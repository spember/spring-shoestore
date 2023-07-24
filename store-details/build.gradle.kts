dependencies {
    implementation(project(":store-core"))
    implementation("org.springframework:spring-jdbc:6.0.7")
//    implementation("redis.clients:jedis:${project.extra["jedisVersion"]}")
    implementation(libs.jedis)
    implementation(platform("software.amazon.awssdk:bom:2.20.26"))
    implementation("software.amazon.awssdk:dynamodb")
}