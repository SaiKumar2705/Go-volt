package com.quadrant.govolt.Others;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.google.android.gms.maps.model.LatLng;

import java.util.HashMap;

public class Constants {
   // public static String BASE_URL = "https://testing-core.2hire.io/v4/";
    //  public static String TOKEN = "342dd9d1-35cb-4a60-a3dd-a45905fc795d";

    public static String BASE_URL = "https://core-govolt.2hire.io/v4/";
    public static String TOKEN = "93849a89-f00a-4e7c-bb7d-bfbdf2347137";


    public static int CURRENT_NETWORK = ConnectivityManager.TYPE_WIFI;
    public static String NETORK_FAILURE_REASON = null;

    public static String PREFERENCES_NAME = "MyPreferences";
    public static String TAXID_CF = "cf";

    public static String AVATHAR_LOC_IMG = "image";

    public static String LICENCE_FRONT = "front";
    public static String LICENCE_BACK = "back";



    public static String STATUS_OF_RIDE = "status";
    public static String RIDE_LATITUDE = "latitude";
    public static String RIDE_LANGITUDE = "longitude";


    public static String USER_CODE = "code";
    public static String USER_ID = "id";
    public static String SITE_ID = "siteID";

    public static String REG_TOKEN = "token";


    public static String PAYMENT_CREDITED = "paymentCredited";
    public static String USER_NAME = "user_name";
    public static String USER_EMAIL = "user_email";
    public static String USER_PWD = "user_name";
    public static String NAME = "name";
    public static String SUR_NAME = "surname";
    public static String ROLE = "role";
    public static String TELEPHONE = "telephone";
    public static String SEX = "sex";
    public static String BIRTH = "birth";
    public static String RESIDENCY = "recidency";
    public static String ZIPCODE_CAP = "cap";
    public static String COUNTRY = "residence_country";
    public static String CITY = "residence_city";
    public static String STATE = "residence_state";
    public static String COMPANY_NAME = "company_name";
    public static String CREATED_AT = "created_at";
    public static String UPDATED_AT = "updated_at";
    public static String DELETED_AT = "deleted_at";

    public static String PATENT_Tipo_Guy = "tipo";
    public static String PATENT_DRIVING_LICENCE_NO = "num";
    public static String PATENT_RELICA_ISSUEDON = "relica";
    public static String PATENT_SCENDENA_EXP_DATE = "scendena";
    public static String PATENT_RILICA_RELEASE = "rilica";
    public static String ISPUBLIC = "isPublic";
    public static String ISBUSINESS = "isBusiness";
    public static String ISSESSION = "isSESSION";

    public static String PATENT_HOLDER_NAME="driving_lincense_holder_name";


    public static String PATENT_NATIONALITY="drivingLicenseNationality";

    public static String LOGIN_URL = "user/login";

    public static double LATITUDE = 45.464204;
    public static double LONGITUDE = 9.189982;

    //For testing
    public static long FIVFTEEN_MINUTES = 120000;  //1min
    public static int FOURTEEN = 0;
    public static int FIFTY_NINE = 40;

    //For Production
    /*public static long FIVFTEEN_MINUTES = 900000;
    public static int FOURTEEN = 0;
    public static int FIFTY_NINE = 59;*/

    public static final String GEOFENCE_ID_STAN_UNI = "STAN_UNI";
    public static final float GEOFENCE_RADIUS_IN_METERS = 100;

    /*public static  final double CUSTOM_LAT = 45.49010086;
    public static final double CUSTOM_LANG = 9.1693449;*/
    public static  final double CUSTOM_LAT = 45.4913043;
    public static final double CUSTOM_LANG = 9.1693449;



    /**
     * Map for storing information about stanford university in the Stanford.
     */
    public static final HashMap<String, LatLng> AREA_LANDMARKS = new HashMap<String, LatLng>();

    static {
        // stanford university.
        AREA_LANDMARKS.put(GEOFENCE_ID_STAN_UNI, new LatLng(45.4900322, 9.16918945));
    }

    public static boolean isInternetAvailable(Context context) {
        try {
            ConnectivityManager conMgr = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            final NetworkInfo wifi = conMgr
                    .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            final NetworkInfo mobile = conMgr
                    .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            NETORK_FAILURE_REASON = null;
            if (wifi.getState() == NetworkInfo.State.CONNECTED
                    || wifi.getState() == NetworkInfo.State.CONNECTING) {
                CURRENT_NETWORK = ConnectivityManager.TYPE_WIFI;
                return true;

            } else if (mobile.getState() == NetworkInfo.State.CONNECTED
                    || mobile.getState() == NetworkInfo.State.CONNECTING) {
                CURRENT_NETWORK = ConnectivityManager.TYPE_MOBILE;
                return true;
            } else if (conMgr.getNetworkInfo(0).getState() == NetworkInfo.State.CONNECTED
                    || conMgr.getNetworkInfo(1).getState() == NetworkInfo.State.CONNECTED) {
                return true;

            } else if (conMgr.getNetworkInfo(0).getState() == NetworkInfo.State.CONNECTED
                    || conMgr.getNetworkInfo(1).getState() == NetworkInfo.State.CONNECTING) {
                return true;

            } else if (mobile.getState() == NetworkInfo.State.DISCONNECTED
                    || mobile.getState() == NetworkInfo.State.DISCONNECTING) {

                NETORK_FAILURE_REASON = mobile.getReason();
                CURRENT_NETWORK = ConnectivityManager.TYPE_MOBILE;
            } else if (wifi.getState() == NetworkInfo.State.DISCONNECTED
                    || wifi.getState() == NetworkInfo.State.DISCONNECTING) {
                CURRENT_NETWORK = ConnectivityManager.TYPE_WIFI;
                NETORK_FAILURE_REASON = wifi.getReason();

            } else if (mobile.getState() == NetworkInfo.State.DISCONNECTED
                    || mobile.getState() == NetworkInfo.State.DISCONNECTING) {

                NETORK_FAILURE_REASON = mobile.getReason();
                CURRENT_NETWORK = ConnectivityManager.TYPE_MOBILE;
            } else if (wifi.getState() == NetworkInfo.State.DISCONNECTED
                    || wifi.getState() == NetworkInfo.State.DISCONNECTING) {
                CURRENT_NETWORK = ConnectivityManager.TYPE_WIFI;
                NETORK_FAILURE_REASON = wifi.getReason();

            }
        } catch (NullPointerException e) {
            return true;
        } catch (Exception e) {
            return true;
        }

        return false;
    }



}
