package com.quadrant.interfaces;

import com.quadrant.response.UserInfoResponse;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ActivateSiteInterface {
    @PUT("user/api/service/site/{id}/activate")
    Call<JSONObject> GetResponse(@Header("X-SERVICE-TOKEN") String token, @Header("Authorization") String authorization, @Path("id") int id);
}
