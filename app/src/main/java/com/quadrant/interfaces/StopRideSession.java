package com.quadrant.interfaces;

import com.google.gson.JsonObject;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

public interface StopRideSession {
    @GET("user/api/personal/vehicle/{id}/stop")
    Call<JsonObject> GetResponse(@Header("X-SERVICE-TOKEN") String token, @Header("Authorization") String authorization, @Path("id") int id);

}
