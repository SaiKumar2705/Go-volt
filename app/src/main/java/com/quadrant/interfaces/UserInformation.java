package com.quadrant.interfaces;

import com.quadrant.response.GeoFenceResponse;
import com.quadrant.response.UserInfoResponse;
import com.quadrant.response.UserInfoResponseData;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface UserInformation {
    @GET("user/api/user/status")
    Call<UserInfoResponse> GetResponse(@Header("X-SERVICE-TOKEN") String token, @Header("Authorization") String authorization);


    @GET("user/api/user/status")
    Call<UserInfoResponseData> GetResponseData(@Header("X-SERVICE-TOKEN") String token, @Header("Authorization") String authorization);
}
