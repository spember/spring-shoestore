dependencies {
    implementation(project(":store-core"))
    implementation("org.springframework:spring-jdbc:6.0.7")
    implementation("redis.clients:jedis:${project.extra["jedisVersion"]}")
}