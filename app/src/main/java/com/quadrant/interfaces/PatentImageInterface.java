package com.quadrant.interfaces;



import com.quadrant.request.PatentImgeRequesOne;
import com.quadrant.request.RegistrationDocumentRequest;
import com.quadrant.response.RegistrationDocumentResponse;
import com.quadrant.response.patentImgResponseOne;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;


public interface PatentImageInterface {
    @FormUrlEncoded
    @PUT("user/api/user/license")

   /* @Headers({
            "Content-Type:form-data",

    })*/
    //Call<patentImgResponseOne> GetResponseAddingReg(@Header("X-SERVICE-TOKEN") String token, @Header("Authorization") String authorization, @Body PatentImgeRequesOne request);

    Call<patentImgResponseOne> GetResponseAddingReg(@Header("X-SERVICE-TOKEN") String token, @Header("Authorization") String authorization, @Field("type") int type, @Field("file") String file);




}

