package com.quadrant.govolt;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.adyen.checkout.core.CheckoutException;
import com.adyen.checkout.core.PaymentMethodHandler;
import com.adyen.checkout.core.PaymentResult;
import com.adyen.checkout.core.StartPaymentParameters;
import com.adyen.checkout.core.handler.StartPaymentParametersHandler;
import com.adyen.checkout.ui.CheckoutController;
import com.adyen.checkout.ui.CheckoutSetupParameters;
import com.adyen.checkout.ui.CheckoutSetupParametersHandler;
import com.quadrant.govolt.Others.Constants;
import com.quadrant.govolt.Others.PreferenceUtil;
import com.quadrant.govolt.Others.RetrofitClient;
import com.quadrant.interfaces.RequestInterface;
import com.quadrant.progressbar.KProgressHUD;
import com.quadrant.request.PaymentSessionRequest;
import com.quadrant.request.PaymentVerifyRequest;
import com.quadrant.response.PaymentResponse;
import com.quadrant.response.PaymentSessionResponse;
import com.quadrant.response.PaymentVerifyResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ProfilePaymentActivity extends AppCompatActivity implements View.OnClickListener {

    private Context context;
    Button mPayButton;
    private KProgressHUD progressbar_hud;
    private String TAG = "ProfilePaymentActivity";
    public static final int REQUEST_CODE_CHECKOUT = 100;

    private AlertDialog alertDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_payment);
        context = this;
        mPayButton = (Button) findViewById(R.id.done_btn);
        mPayButton.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.done_btn:
                showProgressbar();
                CheckoutController.startPayment(/*Activity*/ this, new CheckoutSetupParametersHandler() {
                    @Override
                    public void onRequestPaymentSession(@NonNull CheckoutSetupParameters checkoutSetupParameters) {

                        // Forward to your own server and request the payment session from Adyen with the given CheckoutSetupParameters.
                        String returnUrl = checkoutSetupParameters.getReturnUrl();
                        String token = checkoutSetupParameters.getSdkToken();
                        String channel = "Android";

                        PaymentSessionRequest paymentSessionRequest = new PaymentSessionRequest();
                        paymentSessionRequest.setChannel(channel);
                        paymentSessionRequest.setToken(token);
                        paymentSessionRequest.setReturnUrl(returnUrl);


                        Retrofit retrofit = RetrofitClient.getClient(Constants.BASE_URL);

                        RequestInterface register_details = retrofit.create(RequestInterface.class);
                        String authorization = PreferenceUtil.getInstance().getString(context, Constants.USER_CODE, "");
                        String bearer_authorization = "Bearer " + authorization;

                        Call<PaymentSessionResponse> resultRes = register_details.getPaymentSession(Constants.TOKEN, bearer_authorization, paymentSessionRequest);
                        resultRes.enqueue(new Callback<PaymentSessionResponse>() {
                            @Override
                            public void onResponse(Call<PaymentSessionResponse> call, Response<PaymentSessionResponse> response) {

                                dismissProgressbar();

                                if (!response.isSuccessful()) {
                                    Log.e(TAG, "--Response code---" + response.code());
                                    Log.e(TAG, "--Response ---" + response.body());
                                    if (response.code() != 200) {
                                        if (response.code() != 500) {
                                           // Toast.makeText(getApplicationContext(), "C'è qualche errore", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                } else {
                                    Log.e(TAG, "--Success---");


                                    if (response.code() == 200) {
                                        PaymentSessionResponse res = response.body();
                                        String encodedPaymentSession = null;
                                        if (res != null && res.getData() != null && res.getData().getPaymentSession() != null) {
                                            Log.e(TAG, "--Payment Session ---" + res.getData().getPaymentSession());
                                            encodedPaymentSession = res.getData().getPaymentSession();
                                        } else {
                                            Log.e(TAG, "--Payment Session --- Not received");
                                        }

                                        handlePaymentSessionResponse(encodedPaymentSession);
                                    }


                                }


                            }

                            @Override
                            public void onFailure(Call<PaymentSessionResponse> call, Throwable t) {
                                dismissProgressbar();
                              //  Toast.makeText(getApplicationContext(), "C'è qualche errore" + t.getMessage(), Toast.LENGTH_SHORT).show();

                            }


                        });
                    }

                    @Override
                    public void onError(@NonNull CheckoutException checkoutException) {
                        // TODO: Handle error.
                        dismissProgressbar();
                       // Toast.makeText(getApplicationContext(), "checkOut Exception Occurred " + checkoutException.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

                break;
        }
    }

    private void showProgressbar() {
        progressbar_hud = KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setCancellable(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface
                                                 dialogInterface) {
                       /* Toast.makeText(context, "You " +
                                "cancelled manually!", Toast
                                .LENGTH_SHORT).show();*/
                    }
                });
        progressbar_hud.show();
    }

    /*
     *Method for calling payment Cards UI
     * */
    private void handlePaymentSessionResponse(String encodedPaymentSession) {
        CheckoutController.handlePaymentSessionResponse(/*Activity*/ this, encodedPaymentSession, new StartPaymentParametersHandler() {

            @Override
            public void onError(@NonNull CheckoutException error) {
                Log.e(TAG, "--CheckoutException --- error" + error.getMessage());
            }

            @Override
            public void onPaymentInitialized(@NonNull StartPaymentParameters startPaymentParameters) {
                PaymentMethodHandler paymentMethodHandler = CheckoutController.getCheckoutHandler(startPaymentParameters);
                paymentMethodHandler.handlePaymentMethodDetails(/* Activity */ ProfilePaymentActivity.this, REQUEST_CODE_CHECKOUT);
            }
        });
    }


    private void dismissProgressbar() {
        progressbar_hud.dismiss();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_CHECKOUT) {
            if (resultCode == PaymentMethodHandler.RESULT_CODE_OK) {
                showProgressbar();
                PaymentResult paymentResult = PaymentMethodHandler.Util.getPaymentResult(data);

                Retrofit retrofit = RetrofitClient.getClient(Constants.BASE_URL);

                RequestInterface register_details = retrofit.create(RequestInterface.class);
                String authorization = PreferenceUtil.getInstance().getString(context, Constants.USER_CODE, "");
                String bearer_authorization = "Bearer " + authorization;

                PaymentVerifyRequest paymentVerifyRequest = new PaymentVerifyRequest();
                paymentVerifyRequest.setPayload(paymentResult.getPayload());
                // Handle PaymentResult.
                Call<PaymentVerifyResponse> resultRes = register_details.getPaymentVerification(Constants.TOKEN, bearer_authorization, paymentVerifyRequest);

                resultRes.enqueue(new Callback<PaymentVerifyResponse>() {
                    @Override
                    public void onResponse(Call<PaymentVerifyResponse> call, Response<PaymentVerifyResponse> response) {

                        dismissProgressbar();

                        if (!response.isSuccessful()) {
                            Log.e(TAG, "--Response code---" + response.code());
                            Log.e(TAG, "--Response ---" + response.body());

                            if (response.code() != 200) {
                                if (response.code() != 500) {
                                 //   Toast.makeText(getApplicationContext(), "C'è qualche errore nei dettagli inseriti", Toast.LENGTH_SHORT).show();
                                }
                            }


                        } else {
                            Log.e(TAG, "--Success---");


                            if (response.code() == 200) {
                                PaymentVerifyResponse res = response.body();


                               /* if(res.getStatus())
                                    Toast.makeText(getApplicationContext(), "Pagamento verificato", Toast.LENGTH_SHORT);
                                else
                                    Toast.makeText(getApplicationContext(), "Pagamento non verificato", Toast.LENGTH_SHORT);*/




                               ComplementAlert();

                            } else {
                               // Toast.makeText(getApplicationContext(), "Pagamento non verificato", Toast.LENGTH_SHORT);

                            }


                        }

                    }

                    @Override
                    public void onFailure(Call<PaymentVerifyResponse> call, Throwable t) {
                        dismissProgressbar();
                      //  Toast.makeText(getApplicationContext(), "C'è qualche errore: " + t.getMessage(), Toast.LENGTH_SHORT).show();

                    }


                });


            } else {
                CheckoutException checkoutException = PaymentMethodHandler.Util.getCheckoutException(data);
               // Toast.makeText(getApplicationContext(), "Pagamento fallito" , Toast.LENGTH_SHORT).show();
                if (resultCode == PaymentMethodHandler.RESULT_CODE_CANCELED) {
                    // Handle cancellation and optional CheckoutException.
                } else {

                    // Handle CheckoutException.
                }
            }
        }
    }

    private void ComplementAlert() {
        ViewGroup viewGroup = findViewById(android.R.id.content);

        //then we will inflate the custom alert dialog xml that we created
        View dialogView = LayoutInflater.from(ProfilePaymentActivity.this).inflate(R.layout.compliment_registration, viewGroup, false);

        Button btn_done = (Button) dialogView.findViewById(R.id.done_btn);

        TextView tv_error = (TextView) dialogView.findViewById(R.id.title);

       // tv_error.setText("" + error_message);

        //Now we need an AlertDialog.Builder object
        AlertDialog.Builder builder = new AlertDialog.Builder(context, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);

        //setting the view of the builder to our custom view that we already inflated
        builder.setView(dialogView);

        //finally creating the alert dialog and displaying it
        alertDialog = builder.create();

        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertDialog.setCancelable(true);
        alertDialog.getWindow().setBackgroundDrawableResource(R.color.blackTransparent);


        alertDialog.show();


        btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertDialog.dismiss();

                Intent I = new Intent(ProfilePaymentActivity.this,HomeActivity.class);
                startActivity(I);


            }
        });
    }

}
