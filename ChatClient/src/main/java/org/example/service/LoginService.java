package org.example.service;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.example.entity.dto.Response;
import org.example.entity.dto.User;

import java.io.IOException;
import java.nio.charset.StandardCharsets;


public class LoginService {
    private final CloseableHttpClient client = HttpClients.createDefault();

    public Response login(String username, String password) throws IOException {
        User user = new User(username, password);
        HttpPost httpPost = new HttpPost("http://localhost:8080/api/v1/login");
        httpPost.setHeader("Content-Type", "application/json");
        HttpEntity entity = new StringEntity(user.toJson());
        httpPost.setEntity(entity);
        HttpResponse httpResponse = client.execute(httpPost);
        Response response = new Response();
        response.setCode(httpResponse.getStatusLine().getStatusCode());
        response.setMessage(new String(httpResponse.getEntity().getContent().readAllBytes(), StandardCharsets.UTF_8));
        return response;
    }
}
