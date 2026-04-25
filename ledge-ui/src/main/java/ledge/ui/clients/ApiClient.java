package ledge.ui.clients;

import com.google.gson.Gson;
import ledge.api.shared.ApiResponse;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.lang.reflect.Type;

public class ApiClient {
    private static final String BASE_URL = "http://localhost:8080/api";
    protected final HttpClient httpClient;
    protected final Gson gson;

    public ApiClient() {
        this.httpClient = HttpClient.newHttpClient();
        this.gson = new Gson();
    }

    protected <T, R> ApiResponse<R> post(String path, T body, String token, Type responseType) {
        return sendRequest("POST", path, body, token, responseType);
    }

    protected <T, R> ApiResponse<R> put(String path, T body, String token, Type responseType) {
        return sendRequest("PUT", path, body, token, responseType);
    }

    protected <T, R> ApiResponse<R> patch(String path, T body, String token, Type responseType) {
        return sendRequest("PATCH", path, body, token, responseType);
    }

    protected <R> ApiResponse<R> get(String path, String token, Type responseType) {
        return sendRequest("GET", path, null, token, responseType);
    }

    protected <T, R> ApiResponse<R> delete(String path, T body, String token, Type responseType) {
        return sendRequest("DELETE", path, body, token, responseType);
    }

    private <T, R> ApiResponse<R> sendRequest(String method, String path, T body, String token, Type responseType) {
        try {
            HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + path))
                    .header("Content-Type", "application/json");

            if (token != null) {
                requestBuilder.header("Authorization", "Bearer " + token);
            }

            if (body != null) {
                String jsonBody = gson.toJson(body);
                requestBuilder.method(method, HttpRequest.BodyPublishers.ofString(jsonBody));
            } else {
                requestBuilder.method(method, HttpRequest.BodyPublishers.noBody());
            }

            HttpResponse<String> response = httpClient.send(requestBuilder.build(),
                    HttpResponse.BodyHandlers.ofString());

            // Handle success with no body (e.g., 204 No Content)
            if (response.statusCode() >= 200 && response.statusCode() < 300) {
                if (response.body() == null || response.body().isBlank()) {
                    return ApiResponse.success(null);
                }
                return gson.fromJson(response.body(), responseType);
            } else {
                // If the body is not empty, try to parse the error response
                if (response.body() != null && !response.body().isBlank()) {
                    return gson.fromJson(response.body(), responseType);
                }
                return ApiResponse.error("Server returned error: " + response.statusCode(), "SERVER_ERROR");
            }
        } catch (IOException | InterruptedException e) {
            return ApiResponse.error("Network error: " + e.getMessage(), "NETWORK_ERROR");
        }
    }
}
