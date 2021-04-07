package com.quadrant.govolt.Others;

import android.content.Context;

import java.util.List;


/**
 * Utils class to save/retrieve key/value pairs to and from SharedPreferences.
 *
 * @author rameshlaavu
 */
public class PreferenceUtil {
    private static PreferenceUtil ourInstance = new PreferenceUtil();

    private PreferenceUtil() {
    }

    public static PreferenceUtil getInstance() {
        return ourInstance;
    }

    /**
     * Saves int value at given key to shared preferences.
     *
     * @param context
     * @param key
     * @param value
     */
    public void saveInt(Context context, String key, int value) {
        if (context != null) {
            context.getSharedPreferences(Constants.PREFERENCES_NAME, Context.MODE_PRIVATE).edit().putInt(key, value).commit();
        }
    }

    /**
     * Returns the int value stored at given key in shared preferences.
     *
     * @param context
     * @param key
     * @param defaultValue
     * @return int value if existed, defaultValue otherwise.
     */
    public int getInt(Context context, String key, int defaultValue) {
        if (context != null) {
            return context.getSharedPreferences(Constants.PREFERENCES_NAME, Context.MODE_PRIVATE).getInt(key, defaultValue);
        }
        return defaultValue;
    }

    /**
     * Saves long value at given key to shared preferences.
     *
     * @param context
     * @param key
     * @param value
     */
    public void saveLong(Context context, String key, long value) {
        if (context != null) {
            context.getSharedPreferences(Constants.PREFERENCES_NAME, Context.MODE_PRIVATE).edit().putLong(key, value).commit();
        }
    }

    /**
     * Returns long value stored at given key in shared preferences.
     *
     * @param context
     * @param key
     * @param defaultValue
     * @return long value if existed, defaultValue otherwise.
     */
    public long getLong(Context context, String key, long defaultValue) {
        if (context != null) {
            return context.getSharedPreferences(Constants.PREFERENCES_NAME, Context.MODE_PRIVATE).getLong(key, defaultValue);
        }
        return defaultValue;
    }

    /**
     * Saves String value at given key to shared preferences.
     *
     * @param context
     * @param key
     * @param value
     */
    public void saveString(Context context, String key, String value) {
        if (context != null) {
            context.getSharedPreferences(Constants.PREFERENCES_NAME, Context.MODE_PRIVATE).edit().putString(key, value).commit();
        }
    }

    /**
     * Returns the String value stored at given key in shared preferences.
     *
     * @param context
     * @param key
     * @param defaultValue
     * @return String value if existed, defaultValue otherwise.
     */
    public String getString(Context context, String key, String defaultValue) {
        if (context != null) {
            return context.getSharedPreferences(Constants.PREFERENCES_NAME, Context.MODE_PRIVATE).getString(key, defaultValue);
        }
        return defaultValue;
    }

    /**
     * Saves boolean value at given key to shared preferences.
     *
     * @param context
     * @param key
     * @param value
     */
    public void saveBoolean(Context context, String key, boolean value) {
        if (context != null) {
            context.getSharedPreferences(Constants.PREFERENCES_NAME, Context.MODE_PRIVATE).edit().putBoolean(key, value).commit();
        }
    }

    /**
     * Returns the boolean value stored at given key in shared preferences.
     *
     * @param context
     * @param key
     * @param defaultValue
     * @return boolean value if existed, defaultValue otherwise.
     */
    public boolean getBoolean(Context context, String key, boolean defaultValue) {
        if (context != null) {
            return context.getSharedPreferences(Constants.PREFERENCES_NAME, Context.MODE_PRIVATE).getBoolean(key, defaultValue);
        }
        return defaultValue;
    }



 /*   // This four methods are used for maintaining favorites.
    public void saveArrayListValues(Context context, String key, List<PramotionsListRequest> favorites) {
        SharedPreferences settings;
        SharedPreferences.Editor editor;

        settings = context.getSharedPreferences(Constants.PREFERENCES_NAME,
                Context.MODE_PRIVATE);
        editor = settings.edit();

        Gson gson = new Gson();
        String jsonUsers = gson.toJson(favorites);

        editor.putString(key, jsonUsers);

        editor.commit();
    }
    public ArrayList<PramotionsListRequest> getArrayListValues(Context context,String key) {
        SharedPreferences settings;
        List<PramotionsListRequest> users;

        settings = context.getSharedPreferences(Constants.PREFERENCES_NAME,
                Context.MODE_PRIVATE);

        if (settings.contains(key)) {
            String jsonUsers = settings.getString(key, null);
            Gson gson = new Gson();
            PramotionsListRequest[] userItems = gson.fromJson(jsonUsers,
                    PramotionsListRequest[].class);

            users = Arrays.asList(userItems);
            users= new ArrayList<PramotionsListRequest>(users);
        } else
            return null;

        return (ArrayList<PramotionsListRequest>) users;

    }*/
    /**
     * Clears the saved shared preferences.
     *
     * @param context
     */
    public static void clearSharedPreferences(Context context) {
        context.getSharedPreferences(Constants.PREFERENCES_NAME, Context.MODE_PRIVATE).edit().clear().commit();
    }


}
