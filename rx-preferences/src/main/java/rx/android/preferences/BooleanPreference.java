package rx.android.preferences;

import android.content.SharedPreferences;

public class BooleanPreference extends Preference<Boolean> {
  public BooleanPreference(SharedPreferences preferences, String key) {
    this(preferences, key, false);
  }

  public BooleanPreference(SharedPreferences sharedPreferences, String key, boolean defaultValue) {
    super(sharedPreferences, key, defaultValue);
  }

  @Override public Boolean get() {
    return sharedPreferences.getBoolean(key, defaultValue);
  }

  @Override public void set(Boolean value) {
    sharedPreferences.edit().putBoolean(key, value).commit();
  }
}