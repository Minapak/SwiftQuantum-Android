package com.swiftquantum.data.repository;

import com.swiftquantum.data.api.AuthApi;
import com.swiftquantum.data.local.TokenManager;
import com.swiftquantum.data.local.UserPreferences;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata
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
public final class AuthRepositoryImpl_Factory implements Factory<AuthRepositoryImpl> {
  private final Provider<AuthApi> authApiProvider;

  private final Provider<TokenManager> tokenManagerProvider;

  private final Provider<UserPreferences> userPreferencesProvider;

  public AuthRepositoryImpl_Factory(Provider<AuthApi> authApiProvider,
      Provider<TokenManager> tokenManagerProvider,
      Provider<UserPreferences> userPreferencesProvider) {
    this.authApiProvider = authApiProvider;
    this.tokenManagerProvider = tokenManagerProvider;
    this.userPreferencesProvider = userPreferencesProvider;
  }

  @Override
  public AuthRepositoryImpl get() {
    return newInstance(authApiProvider.get(), tokenManagerProvider.get(), userPreferencesProvider.get());
  }

  public static AuthRepositoryImpl_Factory create(Provider<AuthApi> authApiProvider,
      Provider<TokenManager> tokenManagerProvider,
      Provider<UserPreferences> userPreferencesProvider) {
    return new AuthRepositoryImpl_Factory(authApiProvider, tokenManagerProvider, userPreferencesProvider);
  }

  public static AuthRepositoryImpl newInstance(AuthApi authApi, TokenManager tokenManager,
      UserPreferences userPreferences) {
    return new AuthRepositoryImpl(authApi, tokenManager, userPreferences);
  }
}
