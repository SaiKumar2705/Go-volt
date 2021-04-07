package com.quadrant.interfaces;

import com.quadrant.response.SiteIDResponse;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface GetSiteID_Interface {
    @GET("user/api/service/site/by/position")
    Call<SiteIDResponse> GetResponse(@Header("X-SERVICE-TOKEN") String token, @Header("Authorization") String authorization, @Query("latitude")double latitude, @Query("longitude")double logitude);
}
