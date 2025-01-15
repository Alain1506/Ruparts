package com.ruparts.main;

import static com.ruparts.MainActivity.token;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ruparts.main.exception.api.AccessDeniedException;
import com.ruparts.main.exception.api.ApiCallException;
import com.ruparts.main.exception.api.NotAuthorizedException;
import com.ruparts.main.exception.api.SpecificErrorException;
import com.ruparts.main.exception.api.ValidationFailedException;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ApiClient {

    ObjectMapper objectMapper;
    OkHttpClient client;

    public ApiClient () {
        this.objectMapper = new ObjectMapper();
        this.client = new OkHttpClient().newBuilder().build();
    }

    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    public JSONObject callEndpointAndReturnJsonObject(String action, Object dataObject) {
        ApiRequestDto requestDto = new ApiRequestDto(action, dataObject);

        final String requestBodyAsString;
        try {
            requestBodyAsString = this.objectMapper.writeValueAsString(requestDto);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        RequestBody body = RequestBody.create(requestBodyAsString, MediaType.parse("application/json"));
        Request request = new Request.Builder()
                .url("http://stage.ruparts.ru/api/endpoint?XDEBUG_TRIGGER=0")
                .method("POST", body)
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "Bearer " + token)
                .build();
        Response response = null;
        String responseString = null;
        try {
            response = this.client.newCall(request).execute();
            if (response.code() == 401) {
                throw new NotAuthorizedException();                                            // ПЕРЕХОДЫ???
            } else if (response.code() != 200) {
                throw new ApiCallException("Status code is not 200");
            }
            assert response.body() != null;
            responseString = response.body().string();

            final JSONObject[] jsonObject = {null};
            jsonObject[0] = new JSONObject(responseString);
            if (jsonObject[0].getInt("type") != 0) {                                      // не равно 1???
                JSONObject errorObject = jsonObject[0].getJSONObject("error");
                switch (errorObject.getString("code")) {
                    case "76c550abx400":
                        throw new ApiCallException("Invalid action call");
                    case "342638f7x400":
                        throw new ValidationFailedException();
                    case "691ed8ebx400":
                        throw new AccessDeniedException();
                    case "1886da8bx500":
                        throw new ApiCallException("Request decode error");
                    case "23b491dax500":
                        throw new ApiCallException("Response encode error");
                    case "f77a41dex500":
                        throw new ApiCallException("Internal server error");
                    default:
                        throw new SpecificErrorException(
                            errorObject.getString("message"),
                            errorObject.getString("code"),
                            errorObject.getString("data")
                        );
                }
            }
            return jsonObject[0].getJSONObject("data");

        } catch (IOException e) {
            throw new ApiCallException("OkHttpClient exception: " + e.getMessage(), e);
            //TODO отобразить сообщение об ошибке вызова АПИ
        } catch (JSONException e) {
            throw new ApiCallException("JSONObject exception: " + e.getMessage(), e);
        }
    }
}
