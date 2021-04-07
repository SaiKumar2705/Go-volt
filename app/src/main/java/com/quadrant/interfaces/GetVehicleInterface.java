package com.quadrant.interfaces;



import com.quadrant.response.GeoFenceResponse;
import com.quadrant.response.VehicleResponse;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;


public interface GetVehicleInterface {
    @GET("user/api/sharing/vehicle")
    Call<VehicleResponse> GetResponse(@Header("X-SERVICE-TOKEN") String token, @Header("Authorization") String authorization, @Query("site") int site);
}