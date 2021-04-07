package com.quadrant.interfaces;



import com.quadrant.request.RegistrationAddingRequest;
import com.quadrant.request.RegistrationRequest;
import com.quadrant.response.RegistrationAddingResponse;
import com.quadrant.response.RegistrationResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;


public interface RegistrationAddingInterface {
    @PUT("user/api/user")
    Call<RegistrationAddingResponse> GetResponseAddingReg(@Header("X-SERVICE-TOKEN") String token,@Header("Authorization") String authorization, @Body RegistrationAddingRequest request);
}

