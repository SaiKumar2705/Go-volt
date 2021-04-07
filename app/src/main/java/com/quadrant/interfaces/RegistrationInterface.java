package com.quadrant.interfaces;



import com.quadrant.request.RegistrationRequest;
import com.quadrant.response.CommunityResponse;
import com.quadrant.response.RegistrationResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;


public interface RegistrationInterface {
    @POST("user/register")
    Call<RegistrationResponse> GetResponseReg(@Header("X-SERVICE-TOKEN") String token, @Body RegistrationRequest request);
}