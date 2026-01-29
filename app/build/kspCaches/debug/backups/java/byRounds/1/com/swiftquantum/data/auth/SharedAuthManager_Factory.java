package com.swiftquantum.data.auth;

import android.content.Context;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata("dagger.hilt.android.qualifiers.ApplicationContext")
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava",
    "cast",
    "deprecation",
    "nullness:initialization.field.uninitialized"
})
public final class SharedAuthManager_Factory implements Factory<SharedAuthManager> {
  private final Provider<Context> contextProvider;

  public SharedAuthManager_Factory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public SharedAuthManager get() {
    return newInstance(contextProvider.get());
  }

  public static SharedAuthManager_Factory create(Provider<Context> contextProvider) {
    return new SharedAuthManager_Factory(contextProvider);
  }

  public static SharedAuthManager newInstance(Context context) {
    return new SharedAuthManager(context);
  }
}
