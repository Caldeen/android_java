package helpers;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.preference.PreferenceManager;

import java.util.Locale;

public class LocaleManager {
    public static void setLocale(Context context, String language) {
        saveLanguage(context, language);
        updateResources(context, language);
    }

    private static void saveLanguage(Context context, String language) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("lang", language);
        editor.apply();
    }

    private static void updateResources(Context context, String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Configuration configuration = context.getResources().getConfiguration();
        configuration.setLocale(locale);
        context.createConfigurationContext(configuration);
        context.getResources().updateConfiguration(configuration,
                context.getResources().getDisplayMetrics());
    }
}
