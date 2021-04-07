package com.quadrant.interfaces;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface StartRideSession {
    @GET("user/api/personal/vehicle/{id}/start")
    Call<JSONObject> GetResponse(@Header("X-SERVICE-TOKEN") String token, @Header("Authorization") String authorization, @Path("id") int id);

}
