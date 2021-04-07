package com.quadrant.interfaces;



import com.quadrant.request.RegistrationAddingRequest;
import com.quadrant.request.RegistrationDocumentRequest;
import com.quadrant.response.RegistrationAddingResponse;
import com.quadrant.response.RegistrationDocumentResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.PUT;


public interface RegistrationDocumentInterface {
    @PUT("user/api/user/additional")
    Call<RegistrationDocumentResponse> GetResponseAddingReg(@Header("X-SERVICE-TOKEN") String token, @Header("Authorization") String authorization, @Body RegistrationDocumentRequest request);
}

