package com.quadrant.govolt.Others;

import com.quadrant.response.LoginErrorResponse;
import com.quadrant.response.RegistrationErrorResponse;
import com.quadrant.responses.PachettiErrorResponse;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.concurrent.TimeUnit;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Converter;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ErrorUtils {


    public static LoginErrorResponse parseError(Response<?> response) {
        Converter<ResponseBody, LoginErrorResponse> converter =
           ServiceGenerator.retrofit()
                        .responseBodyConverter(LoginErrorResponse.class, new Annotation[0]);

        LoginErrorResponse error;

        try {
            error = converter.convert(response.errorBody());
        } catch (IOException e) {
            return new LoginErrorResponse();
        }

        return error;
    }



    public static RegistrationErrorResponse parseErrorReg(Response<?> response) {
        Converter<ResponseBody, RegistrationErrorResponse> converter =
                ServiceGenerator.retrofit()
                        .responseBodyConverter(RegistrationErrorResponse.class, new Annotation[0]);

        RegistrationErrorResponse error;

        try {
            error = converter.convert(response.errorBody());
        } catch (IOException e) {
            return new RegistrationErrorResponse();
        }

        return error;
    }



    public static PachettiErrorResponse parseErrorPac(Response<?> response) {
        Converter<ResponseBody, PachettiErrorResponse> converter =
                ServiceGenerator.retrofit()
                        .responseBodyConverter(PachettiErrorResponse.class, new Annotation[0]);

        PachettiErrorResponse error;

        try {
            error = converter.convert(response.errorBody());
        } catch (IOException e) {
            return new PachettiErrorResponse();
        }

        return error;
    }

}