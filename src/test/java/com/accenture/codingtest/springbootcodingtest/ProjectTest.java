package com.accenture.codingtest.springbootcodingtest;

import com.accenture.codingtest.springbootcodingtest.constants.Role;
import com.accenture.codingtest.springbootcodingtest.constants.TaskStatus;
import com.accenture.codingtest.springbootcodingtest.model.ApiAuthResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.assertj.core.api.BDDAssertions;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ProjectTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private static String adminAccessToken = null;
    private static String productOwnerAccessToken = null;

    private static String project_id = null;
    private static String user_id_1 = null;
    private static String user_id_2 = null;
    private static String task_id_1 = null;
    private static String task_id_2 = null;

    private static HttpHeaders getHttpHeader(String token){
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth(token);
        return httpHeaders;
    }

    @BeforeEach
    void setUp() {
        // solution to resolve patch method error in testresttemplate
        HttpClient httpClient = HttpClientBuilder.create().build();
        RestTemplate restTemplate = testRestTemplate.getRestTemplate();
        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(httpClient));
    }

    @Test
    @Order(1)
    @DisplayName("Get Admin Token")
    void getAdminToken() throws Exception {
        Map<String, String> adminCred = new HashMap<>();
        adminCred.put("username", "admin");
        adminCred.put("password", "admin");
        ResponseEntity<String> authentication = testRestTemplate.postForEntity("/api/v1/authenticate", adminCred, String.class);
        BDDAssertions.assertThat(authentication.getStatusCodeValue()).isEqualTo(HttpStatus.OK.value());
        if (authentication.getStatusCodeValue() == HttpStatus.OK.value()) {
            ApiAuthResponse apiResponse = objectMapper.readValue(authentication.getBody(), ApiAuthResponse.class);
            adminAccessToken = apiResponse.getPayload().get("access_token");
            BDDAssertions.assertThat(adminAccessToken).isNotNull();
        }
    }

    @Test
    @Order(2)
    @DisplayName("Create Product Owner")
    void createProjectOwnerAndGetProductOwnerToken() {

        HttpHeaders httpHeaders = getHttpHeader(adminAccessToken);

        Map<String, String> userMap = new HashMap<>();
        userMap.put("username", "mike89");
        userMap.put("password", "mike89");
        userMap.put("role", Role.PRODUCT_OWNER.name());

        HttpEntity<Map<String, String>> httpEntity = new HttpEntity<>(userMap, httpHeaders);
        ResponseEntity<String> authenticatePostRequest = testRestTemplate.postForEntity(
                "/api/v1/users",
                httpEntity,
                String.class);
        BDDAssertions.assertThat(authenticatePostRequest.getStatusCodeValue()).isEqualTo(HttpStatus.CREATED.value());
    }

    @Test
    @Order(3)
    @DisplayName("Get Product Owner Token")
    void getProductOwnerToken() throws Exception {
        Map<String, String> adminCred = new HashMap<>();
        adminCred.put("username", "mike89");
        adminCred.put("password", "mike89");
        ResponseEntity<String> authentication = testRestTemplate.postForEntity("/api/v1/authenticate", adminCred, String.class);
        BDDAssertions.assertThat(authentication.getStatusCodeValue()).isEqualTo(HttpStatus.OK.value());
        if (authentication.getStatusCodeValue() == HttpStatus.OK.value()) {
            ApiAuthResponse apiResponse = objectMapper.readValue(authentication.getBody(), ApiAuthResponse.class);
            productOwnerAccessToken = apiResponse.getPayload().get("access_token");
            BDDAssertions.assertThat(adminAccessToken).isNotNull();
        }
    }

    @Test
    @Order(4)
    @DisplayName("Create Project")
    void createProject() throws Exception {

        HttpHeaders httpHeaders = getHttpHeader(productOwnerAccessToken);

        Map<String, String> projectMap = new HashMap<>();
        projectMap.put("name", "Ecommerce1 Web Application");

        HttpEntity<Map<String, String>> httpEntity = new HttpEntity<>(projectMap, httpHeaders);

        ResponseEntity<String> createProjectRequest = testRestTemplate.postForEntity("/api/v1/projects",
                httpEntity,
                String.class);
        if(createProjectRequest.getStatusCodeValue() == HttpStatus.CREATED.value()){
            ApiAuthResponse apiResponse = objectMapper.readValue(createProjectRequest.getBody(),ApiAuthResponse.class);
            project_id = apiResponse.getPayload().get("id");
        }
        BDDAssertions.assertThat(createProjectRequest.getStatusCodeValue()).isEqualTo(HttpStatus.CREATED.value());
    }

    @Test
    @Order(5)
    @DisplayName("Create Users")
    void createUsers() throws Exception {

        HttpHeaders httpHeaders = getHttpHeader(adminAccessToken);

        Map<String,String> userMap = new HashMap<>();
        userMap.put("username","john");
        userMap.put("password","john");
        userMap.put("role",Role.USER.name());

        HttpEntity<Map<String,String>> httpEntity = new HttpEntity<>(userMap,httpHeaders);
        ResponseEntity<String> createUserRequest = testRestTemplate.postForEntity("/api/v1/users", httpEntity, String.class);
        if(createUserRequest.getStatusCodeValue() == HttpStatus.CREATED.value()){
            ApiAuthResponse apiResponse = objectMapper.readValue(createUserRequest.getBody(),ApiAuthResponse.class);
            user_id_1 = apiResponse.getPayload().get("id");
        }
        BDDAssertions.assertThat(createUserRequest.getStatusCodeValue()).isEqualTo(HttpStatus.CREATED.value());

        userMap.replace("username","arnold");
        userMap.replace("password","arnold");

        httpEntity = new HttpEntity<>(userMap,httpHeaders);
        createUserRequest = testRestTemplate.postForEntity("/api/v1/users",httpEntity,String.class);
        if(createUserRequest.getStatusCodeValue() == HttpStatus.CREATED.value()){
            ApiAuthResponse apiResponse = objectMapper.readValue(createUserRequest.getBody(),ApiAuthResponse.class);
            user_id_2 = apiResponse.getPayload().get("id");
        }
        BDDAssertions.assertThat(createUserRequest.getStatusCodeValue()).isEqualTo(HttpStatus.CREATED.value());
    }

    @Test
    @Order(6)
    @DisplayName("Create tasks")
    void createTasks() throws Exception {

        HttpHeaders httpHeaders = getHttpHeader(productOwnerAccessToken);

        Map<String,String> taskMap = new HashMap<>();
        taskMap.put("title","Setup database connection");
        taskMap.put("description","Connect to PostgreSql database");
        taskMap.put("status", TaskStatus.NOT_STARTED.toString());
        taskMap.put("project_id",project_id);

        HttpEntity<Map<String,String>> httpEntity = new HttpEntity<>(taskMap,httpHeaders);
        ResponseEntity<String> createTaskRequest = testRestTemplate.postForEntity(
                "/api/v1/tasks",
                httpEntity,
                String.class);
        if(createTaskRequest.getStatusCodeValue() == HttpStatus.CREATED.value()){
            ApiAuthResponse apiAuthResponse = objectMapper.readValue(createTaskRequest.getBody(),ApiAuthResponse.class);
            task_id_1 = apiAuthResponse.getPayload().get("id");
        }
        BDDAssertions.assertThat(createTaskRequest.getStatusCodeValue()).isEqualTo(HttpStatus.CREATED.value());

        taskMap.replace("title","Design database");
        taskMap.replace("description","List tables and columns needed for the project");

        httpEntity = new HttpEntity<>(taskMap,httpHeaders);
        createTaskRequest = testRestTemplate.postForEntity(
                "/api/v1/tasks",
                httpEntity,
                String.class);
        if(createTaskRequest.getStatusCodeValue() == HttpStatus.CREATED.value()){
            ApiAuthResponse apiAuthResponse = objectMapper.readValue(createTaskRequest.getBody(),ApiAuthResponse.class);

            task_id_2 = apiAuthResponse.getPayload().get("id");
        }
        BDDAssertions.assertThat(createTaskRequest.getStatusCodeValue()).isEqualTo(HttpStatus.CREATED.value());
    }

    @Test
    @Order(7)
    @DisplayName("Assign Users to task")
    void assignUsers() throws Exception {

        HttpHeaders httpHeaders = getHttpHeader(productOwnerAccessToken);

        Map<String,String> assignUserMap = new HashMap<>();
        assignUserMap.put("user_id",user_id_1);

        HttpEntity<Map<String,String>> httpEntity = new HttpEntity<>(assignUserMap,httpHeaders);

        ResponseEntity<String> assignUserRequest = testRestTemplate.exchange(
                "/api/v1/tasks/assign/"+task_id_1,
                HttpMethod.PATCH,
                httpEntity,
                String.class
                );
        BDDAssertions.assertThat(assignUserRequest.getStatusCodeValue()).isEqualTo(HttpStatus.OK.value());


        assignUserMap.replace("user_id",user_id_2);
        assignUserRequest = testRestTemplate.exchange(
                "/api/v1/tasks/assign/"+task_id_2,
                HttpMethod.PATCH,
                httpEntity,
                String.class
        );

        BDDAssertions.assertThat(assignUserRequest.getStatusCodeValue()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    @Order(8)
    @DisplayName("User change status")
    void userChangeStatus() throws Exception {

        Map<String,String> authenticateRequest = new HashMap<>();
        authenticateRequest.put("username","john");
        authenticateRequest.put("password","john");

        ResponseEntity<String> authentication = testRestTemplate.postForEntity("/api/v1/authenticate",
                authenticateRequest,
                String.class);
        BDDAssertions.assertThat(authentication.getStatusCodeValue()).isEqualTo(HttpStatus.OK.value());

        if (authentication.getStatusCodeValue() == HttpStatus.OK.value()) {
            ApiAuthResponse apiResponse = objectMapper.readValue(authentication.getBody(), ApiAuthResponse.class);
            String userAccessToken = apiResponse.getPayload().get("access_token");
            BDDAssertions.assertThat(adminAccessToken).isNotNull();

            HttpHeaders httpHeaders = getHttpHeader(userAccessToken);

            Map<String,String> statusMap = new HashMap<>();
            statusMap.put("status",TaskStatus.IN_PROGRESS.toString());

            HttpEntity<Map<String,String>> httpEntity = new HttpEntity<>(statusMap,httpHeaders);

            ResponseEntity<String> updateStatusRequest = testRestTemplate.exchange(
                    "/api/v1/tasks/"+task_id_1,
                    HttpMethod.PATCH,
                    httpEntity,
                    String.class
            );
            BDDAssertions.assertThat(updateStatusRequest.getStatusCodeValue()).isEqualTo(HttpStatus.OK.value());
        }
    }
}
