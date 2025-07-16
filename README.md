# SpringBoot25
스프링부트 학습용 

======== application.properties ========

spring.datasource.driver-class-name=org.mariadb.jdbc.Driver
spring.datasource.url=jdbc:mariadb://localhost:3306/bootex
spring.datasource.username=?????
spring.datasource.password=?????


spring.jpa.hibernate.ddl-auto=update
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.show-sql=true

======== build.gradle ========
implementation 'org.springframework.boot:spring-boot-starter-thymeleaf' /* 프론트 관련 */
implementation 'org.springframework.boot:spring-boot-starter-web' /* String-web */
compileOnly 'org.projectlombok:lombok' /* lombok */
annotationProcessor 'org.projectlombok:lombok' /* lombok */

testCompileOnly 'org.projectlombok:lombok'
testAnnotationProcessor 'org.projectlombok:lombok'

developmentOnly 'org.springframework.boot:spring-boot-devtools' /* boot 개발용 */



/* 1단계 2단계 설정 -> src/main/resources/application.properties 에서 설정됨 */
runtimeOnly 'org.mariadb.jdbc:mariadb-java-client' /* mariaDB 드라이버 */
implementation 'org.springframework.boot:spring-boot-starter-data-jpa'/* 데이터 베이스 관련 */

testImplementation 'org.springframework.boot:spring-boot-starter-test' /* 테스트 jUnit (메서드 단위 테스트) */
testRuntimeOnly 'org.junit.platform:junit-platform-launcher' /* jUnit용 코드*/
