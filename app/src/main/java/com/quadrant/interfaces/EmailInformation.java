package com.quadrant.interfaces;

import com.adyen.checkout.base.internal.JsonObject;
import com.quadrant.request.EmailRequest;
import com.quadrant.response.EmailResponse;
import com.quadrant.response.UserInfoResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PUT;

public interface EmailInformation {
    @PUT("user/api/service/site/by/email")
    Call<EmailResponse> GetResponse(@Header("X-SERVICE-TOKEN") String token, @Header("Authorization") String authorization, @Body EmailRequest req);
}
