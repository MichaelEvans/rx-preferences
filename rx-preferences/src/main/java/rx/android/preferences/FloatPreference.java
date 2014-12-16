package rx.android.preferences;

import android.content.SharedPreferences;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Action0;
import rx.functions.Func1;
import rx.subscriptions.Subscriptions;

public class FloatPreference extends Preference<Float> {

  public FloatPreference(SharedPreferences preferences, String key) {
    this(preferences, key, 0f);
  }

  public FloatPreference(SharedPreferences sharedPreferences, String key, float defaultValue) {
    super(sharedPreferences, key, defaultValue);
  }

  @Override public Float get() {
    return sharedPreferences.getFloat(key, defaultValue);
  }

  @Override public void set(Float value) {
    sharedPreferences.edit().putFloat(key, value).commit();
  }

  @Override public Observable<Float> asObservable() {
    return Observable.create(new OnSubscribeFromFloatPreference());
  }

  class OnSubscribeFromFloatPreference implements Observable.OnSubscribe<Float> {
    @Override public void call(final Subscriber<? super Float> subscriber) {
      subscriber.onNext(get());

      final Subscription subscription = SharedPreferencesObservable.observe(sharedPreferences)
          .filter(new Func1<String, Boolean>() {
            @Override public Boolean call(String s) {
              return key.equals(s);
            }
          })
          .subscribe(new EndlessObserver<String>() {
            @Override public void onNext(String s) {
              subscriber.onNext(get());
            }
          });

      subscriber.add(Subscriptions.create(new Action0() {
        @Override public void call() {
          subscription.unsubscribe();
        }
      }));
    }
  }
}