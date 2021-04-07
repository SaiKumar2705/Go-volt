package com.quadrant.interfaces;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

public interface DeleteRideSession {
    @DELETE("user/api/sharing/booking/current")
    Call<JSONObject> GetDeleteResponse(@Header("X-SERVICE-TOKEN") String token, @Header("Authorization") String authorization);

}
