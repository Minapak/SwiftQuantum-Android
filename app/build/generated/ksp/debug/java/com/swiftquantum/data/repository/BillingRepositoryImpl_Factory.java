package com.swiftquantum.data.repository;

import android.content.Context;
import com.swiftquantum.data.local.UserPreferences;
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
public final class BillingRepositoryImpl_Factory implements Factory<BillingRepositoryImpl> {
  private final Provider<Context> contextProvider;

  private final Provider<UserPreferences> userPreferencesProvider;

  public BillingRepositoryImpl_Factory(Provider<Context> contextProvider,
      Provider<UserPreferences> userPreferencesProvider) {
    this.contextProvider = contextProvider;
    this.userPreferencesProvider = userPreferencesProvider;
  }

  @Override
  public BillingRepositoryImpl get() {
    return newInstance(contextProvider.get(), userPreferencesProvider.get());
  }

  public static BillingRepositoryImpl_Factory create(Provider<Context> contextProvider,
      Provider<UserPreferences> userPreferencesProvider) {
    return new BillingRepositoryImpl_Factory(contextProvider, userPreferencesProvider);
  }

  public static BillingRepositoryImpl newInstance(Context context,
      UserPreferences userPreferences) {
    return new BillingRepositoryImpl(context, userPreferences);
  }
}
