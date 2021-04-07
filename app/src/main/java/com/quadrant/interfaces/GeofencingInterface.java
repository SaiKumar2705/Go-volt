package com.quadrant.interfaces;



import com.quadrant.response.GeoFenceResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;


public interface GeofencingInterface {
    @GET("user/api/service/geofencing")
    Call<GeoFenceResponse> GetResponse(@Header("X-SERVICE-TOKEN") String token, @Header("Authorization") String authorization,@Query("site") int site);
}