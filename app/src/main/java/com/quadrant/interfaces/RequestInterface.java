package com.quadrant.interfaces;

import com.google.gson.JsonObject;
import com.quadrant.request.FgtPwdRequest;
import com.quadrant.request.ImageselfieRequest;
import com.quadrant.request.LoginRequest;
import com.quadrant.request.PasswordResetRequest;
import com.quadrant.request.PaymentSessionRequest;
import com.quadrant.request.PaymentVerifyRequest;
import com.quadrant.request.ProfilePatentRequest;
import com.quadrant.request.ProfileRequest;
import com.quadrant.request.QrRequest;
import com.quadrant.response.AvailableItemResponse;
import com.quadrant.response.AvatharResponse;
import com.quadrant.response.CommunityResponse;
import com.quadrant.response.FgtPwdResponse;
import com.quadrant.response.GeoFenceResponse;
import com.quadrant.response.GetBonusResponse;
import com.quadrant.response.GetOrderResponse;
import com.quadrant.response.ImageselfieResponse;
import com.quadrant.response.LoginResonse;
import com.quadrant.response.PasswordResetResponse;
import com.quadrant.response.PaymentSessionResponse;
import com.quadrant.response.PaymentVerifyResponse;
import com.quadrant.response.ProfilePatentPostResponse;
import com.quadrant.response.ProfileRequestResponse;
import com.quadrant.response.QrResponse;
import com.quadrant.response.SiteIDResponse;
import com.quadrant.response.UserInfoResponseData;
import com.quadrant.response.VehicleResponse;


import org.json.JSONObject;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by KSS on 1/8/2018.
 */

public interface RequestInterface {

    @POST("user/login") //Here dot(.) means your final URL is the same as your base URL
    Call<LoginResonse> GetResponse(@Header("X-SERVICE-TOKEN") String token, @Body LoginRequest request);

    @POST("user/api/user/password") //Here dot(.) means your final URL is the same as your base URL
    Call<PasswordResetResponse> GetResponse(@Header("X-SERVICE-TOKEN") String token, @Header("Authorization") String authorization, @Body PasswordResetRequest request);

    @PUT("user/api/user/additional") //Here dot(.) means your final URL is the same as your base URL
    Call<ProfilePatentPostResponse> GetResponsProfilePatentPost(@Header("X-SERVICE-TOKEN") String token, @Header("Authorization") String authorization, @Body ProfilePatentRequest request);

    @GET("user/api/user/coupon")
    Call<CommunityResponse> GetResponse(@Header("X-SERVICE-TOKEN") String token, @Header("Authorization") String authorization);

    @POST("user/password/reset") //Here dot(.) means your final URL is the same as your base URL
    Call<FgtPwdResponse> GetResponseFgtpwd(@Header("X-SERVICE-TOKEN") String token, @Body FgtPwdRequest request);

    @PUT("user/api/user") //Here dot(.) means your final URL is the same as your base URL
    Call<ProfileRequestResponse> GetResponsereq(@Header("X-SERVICE-TOKEN") String token,@Header("Authorization") String authorization, @Body ProfileRequest request);

    @Headers({"Accept: application/x-www-form-urlencoded"})
    @POST("user/api/user/avatar") //Here dot(.) means your final URL is the same as your base URL
    Call<ImageselfieResponse> GetselResponse(@Header("X-SERVICE-TOKEN") String token, @Header("Authorization") String authorization, @Body ImageselfieRequest file);

    @Multipart
    @POST("user/api/user/avatar") //Here dot(.) means your final URL is the same as your base URL
    Call<AvatharResponse> GetAvtharResponse(@Header("X-SERVICE-TOKEN") String token, @Header("Authorization") String authorization, @Part MultipartBody.Part file);

    @Multipart
    @POST("user/api/user/license") //Here dot(.) means your final URL is the same as your base URL
    Call<ImageselfieResponse> GetLicenceResponse(@Header("X-SERVICE-TOKEN") String token, @Header("Authorization") String authorization, @Part MultipartBody.Part file, @Part("type")int type);

    @GET("user/api/sharing/vehicle/{id}/book")
    Call<JSONObject> GetVehicleBookResponse(@Header("X-SERVICE-TOKEN") String token, @Header("Authorization") String authorization, @Path("id") int id);

    @GET("user/api/personal/vehicle/{id}/pause")
    Call<JSONObject> GetVehiclePauseResponse(@Header("X-SERVICE-TOKEN") String token, @Header("Authorization") String authorization, @Path("id") int id);

    @GET("user/api/personal/vehicle/{id}/resume")
    Call<JSONObject> GetVehicleResumeResponse(@Header("X-SERVICE-TOKEN") String token, @Header("Authorization") String authorization, @Path("id") int id);

    @GET("user/api/personal/vehicle/{id}/end")
    Call<JsonObject> GetVehicleEndResponse(@Header("X-SERVICE-TOKEN") String token, @Header("Authorization") String authorization, @Path("id") int id);

    @GET("user/api/personal/trip/{id}")
    Call<JsonObject> GetSingleTripResponse(@Header("X-SERVICE-TOKEN") String token, @Header("Authorization") String authorization, @Path("id") int id);

    @GET("user/api/personal/trip/{id}/position")
    Call<JsonObject> GetSingleTripResPosition(@Header("X-SERVICE-TOKEN") String token, @Header("Authorization") String authorization,  @Path("id") int id);

    @GET("user/api/personal/scooter/{id}/trunk/open")
    Call<JSONObject> GetTrunckOpen(@Header("X-SERVICE-TOKEN") String token, @Header("Authorization") String authorization, @Path("id") int id);

    @GET("user/api/personal/trip")
    Call<JsonObject> GetAllTrips(@Header("X-SERVICE-TOKEN") String token, @Header("Authorization") String authorization);

    @POST("user/api/user/recurring/setup")
    Call<PaymentSessionResponse> getPaymentSession(@Header("X-SERVICE-TOKEN") String token, @Header("Authorization") String authorization, @Body PaymentSessionRequest paymentSessionRequest);

    @POST("user/api/user/recurring/verify")
    Call<PaymentVerifyResponse> getPaymentVerification(@Header("X-SERVICE-TOKEN") String token, @Header("Authorization") String authorization, @Body PaymentVerifyRequest paymentVerifyRequest);


    @POST("user/api/personal/vehicle/{id}/damage")
    Call<JsonObject> GetPostDammageRes(@Header("X-SERVICE-TOKEN") String token, @Header("Authorization") String authorization, @Body JsonObject req, @Path("id") int id);

    @Multipart
    @POST("user/api/personal/vehicle/{vehicleid}/damage/{id}/media") //Here dot(.) means your final URL is the same as your base URL
    Call<ImageselfieResponse> GetDamageMedia(@Header("X-SERVICE-TOKEN") String token, @Header("Authorization") String authorization, @Part MultipartBody.Part file, @Path("vehicleid") int vehicleid, @Path("id") int id);

//document
    @Multipart
    @POST("user/api/user/document") //Here dot(.) means your final URL is the same as your base URL
    Call<ImageselfieResponse> GetDocumentResponse(@Header("X-SERVICE-TOKEN") String token, @Header("Authorization") String authorization, @Part MultipartBody.Part file, @Part("type")int type);


    @GET("user/api/service/site/{id}")
    Call<JsonObject> GetResponsesiteid(@Header("X-SERVICE-TOKEN") String token, @Header("Authorization") String authorization, @Path("id") int id);
//getvehiclesbysiteid
    @GET("user/api/sharing/vehicle")
    Call<VehicleResponse> GetResponsebyvehiclesiteid(@Header("X-SERVICE-TOKEN") String token, @Header("Authorization") String authorization, @Query("site") int site);


    @POST("user/api/sharing/vehicle/qrcode/book")
    Call<QrResponse> GetQrRes(@Header("X-SERVICE-TOKEN") String token, @Header("Authorization")String bearer_authorization, @Body QrRequest req);

    @GET("user/api/service/setting")
    Call<JsonObject> GetResponsepricedetails(@Header("X-SERVICE-TOKEN") String token, @Header("Authorization") String authorization, @Query("site") int site);

    @GET("user/api/sharing/bonus/shop/item")
    Call<JsonObject> GetResponsebyavailableitems(@Header("X-SERVICE-TOKEN") String token, @Header("Authorization") String authorization, @Query("site") int site);



    @POST("user/api/sharing/bonus/shop/item/{id}/buy") //Here dot(.) means your final URL is the same as your base URL
    Call<AvailableItemResponse> GetAvailableData(@Header("X-SERVICE-TOKEN") String token, @Header("Authorization") String authorization, @Path("id") int availableid, @Query("site") int site);



    @GET("user/api/sharing/bonus/active")
    Call<JsonObject> GetBonusResponse(@Header("X-SERVICE-TOKEN") String token, @Header("Authorization") String authorization);

    @GET("user/api/sharing/bonus/shop/order")
    Call<JsonObject> GetOrderResponse(@Header("X-SERVICE-TOKEN") String token, @Header("Authorization") String authorization);

}
