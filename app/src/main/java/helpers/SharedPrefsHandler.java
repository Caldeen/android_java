package helpers;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.am.R;

import java.util.UUID;

import static android.content.Context.MODE_PRIVATE;


public class SharedPrefsHandler {
    public static boolean ifLoggedIn(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("tokenPref", MODE_PRIVATE);
        return sharedPreferences.contains("token");
    }

    public static void logPrefs(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("tokenPref", MODE_PRIVATE);
        Log.println(Log.INFO, "info", "SHARED PREF" + sharedPreferences.getAll().toString());
    }

    public static UUID getToken(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("tokenPref", MODE_PRIVATE);
        String token = sharedPreferences.getString("token", "not logged");
        if (token.equals("not logged"))
            return null;
        return UUID.fromString(token);
    }
    public static void setToken(Context context, String UUID) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("tokenPref", MODE_PRIVATE);
        SharedPreferences.Editor myEdit = sharedPreferences.edit();
        myEdit.putString("token",UUID );
        myEdit.apply();
    }


    public static void logOut(Context applicationContext) {
        SharedPreferences sharedPreferences = applicationContext.getSharedPreferences("tokenPref", MODE_PRIVATE);
        SharedPreferences.Editor myEdit = sharedPreferences.edit();
        myEdit.remove("token");
        myEdit.apply();
    }

    public static void loadTheme(Context context) {
        logPrefs(context);
        SharedPreferences sharedPreferences = context.getSharedPreferences("tokenPref", MODE_PRIVATE);
        String theme = sharedPreferences.getString("theme", "light");
        if (theme.equals("light")) {
            context.setTheme(R.style.Theme_MaterialComponents_Light_NoActionBar);
        }else {
            context.setTheme(R.style.Theme_MaterialComponents_NoActionBar);
        }
    }
    public static void saveTheme(Context context,String theme){
        SharedPreferences sharedPreferences = context.getSharedPreferences("tokenPref", MODE_PRIVATE);
        SharedPreferences.Editor myEdit = sharedPreferences.edit();
        myEdit.putString("theme",theme);
        myEdit.apply();
        logPrefs(context);
    }
    public static void saveLocale(Context context, String locale){
        SharedPreferences sharedPreferences = context.getSharedPreferences("tokenPref", MODE_PRIVATE);
        SharedPreferences.Editor myEdit = sharedPreferences.edit();
        myEdit.putString("locale",locale);
        myEdit.apply();
    }
    public static boolean ifLightTheme(Context context){
        logPrefs(context);
        SharedPreferences sharedPreferences = context.getSharedPreferences("tokenPref", MODE_PRIVATE);
        String theme = sharedPreferences.getString("theme", "light");
        return theme.equals("light");
    }
    public static boolean ifEnLocale(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("tokenPref", MODE_PRIVATE);
        return sharedPreferences.getString("locale","en").equals("en");
    }
}
