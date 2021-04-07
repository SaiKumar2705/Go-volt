package com.quadrant.govolt.Others;

import com.quadrant.response.LoginErrorResponse;
import com.quadrant.response.StartRideErrorResponse;

import java.io.IOException;
import java.lang.annotation.Annotation;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Response;

public class ErrorUtilsStartRide {


    public static StartRideErrorResponse parseError(Response<?> response) {
        Converter<ResponseBody, StartRideErrorResponse> converter =
           ServiceGenerator.retrofit()
                        .responseBodyConverter(StartRideErrorResponse.class, new Annotation[0]);

        StartRideErrorResponse error;

        try {
            error = converter.convert(response.errorBody());
        } catch (IOException e) {
            return new StartRideErrorResponse();
        }

        return error;
    }

}