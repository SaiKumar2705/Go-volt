package com.quadrant.interfaces;

import com.quadrant.request.RegistrationDocumentRequest;
import com.quadrant.response.PatentResponse;
import com.quadrant.response.RegistrationDocumentResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PUT;

public interface GetAdditionalData {
    @GET("user/api/user/additional")
    Call<PatentResponse> GetResponseAddingReg(@Header("X-SERVICE-TOKEN") String token, @Header("Authorization") String authorization);
}
