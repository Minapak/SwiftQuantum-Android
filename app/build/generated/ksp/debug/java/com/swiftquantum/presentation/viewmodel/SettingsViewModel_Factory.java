package com.swiftquantum.presentation.viewmodel;

import android.app.Application;
import com.swiftquantum.data.auth.SharedAuthManager;
import com.swiftquantum.data.local.UserPreferences;
import com.swiftquantum.domain.usecase.GetCurrentUserUseCase;
import com.swiftquantum.domain.usecase.LogoutUseCase;
import com.swiftquantum.domain.usecase.ObserveUserTierUseCase;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
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
public final class SettingsViewModel_Factory implements Factory<SettingsViewModel> {
  private final Provider<Application> applicationProvider;

  private final Provider<GetCurrentUserUseCase> getCurrentUserUseCaseProvider;

  private final Provider<LogoutUseCase> logoutUseCaseProvider;

  private final Provider<ObserveUserTierUseCase> observeUserTierUseCaseProvider;

  private final Provider<SharedAuthManager> sharedAuthManagerProvider;

  private final Provider<UserPreferences> userPreferencesProvider;

  public SettingsViewModel_Factory(Provider<Application> applicationProvider,
      Provider<GetCurrentUserUseCase> getCurrentUserUseCaseProvider,
      Provider<LogoutUseCase> logoutUseCaseProvider,
      Provider<ObserveUserTierUseCase> observeUserTierUseCaseProvider,
      Provider<SharedAuthManager> sharedAuthManagerProvider,
      Provider<UserPreferences> userPreferencesProvider) {
    this.applicationProvider = applicationProvider;
    this.getCurrentUserUseCaseProvider = getCurrentUserUseCaseProvider;
    this.logoutUseCaseProvider = logoutUseCaseProvider;
    this.observeUserTierUseCaseProvider = observeUserTierUseCaseProvider;
    this.sharedAuthManagerProvider = sharedAuthManagerProvider;
    this.userPreferencesProvider = userPreferencesProvider;
  }

  @Override
  public SettingsViewModel get() {
    return newInstance(applicationProvider.get(), getCurrentUserUseCaseProvider.get(), logoutUseCaseProvider.get(), observeUserTierUseCaseProvider.get(), sharedAuthManagerProvider.get(), userPreferencesProvider.get());
  }

  public static SettingsViewModel_Factory create(Provider<Application> applicationProvider,
      Provider<GetCurrentUserUseCase> getCurrentUserUseCaseProvider,
      Provider<LogoutUseCase> logoutUseCaseProvider,
      Provider<ObserveUserTierUseCase> observeUserTierUseCaseProvider,
      Provider<SharedAuthManager> sharedAuthManagerProvider,
      Provider<UserPreferences> userPreferencesProvider) {
    return new SettingsViewModel_Factory(applicationProvider, getCurrentUserUseCaseProvider, logoutUseCaseProvider, observeUserTierUseCaseProvider, sharedAuthManagerProvider, userPreferencesProvider);
  }

  public static SettingsViewModel newInstance(Application application,
      GetCurrentUserUseCase getCurrentUserUseCase, LogoutUseCase logoutUseCase,
      ObserveUserTierUseCase observeUserTierUseCase, SharedAuthManager sharedAuthManager,
      UserPreferences userPreferences) {
    return new SettingsViewModel(application, getCurrentUserUseCase, logoutUseCase, observeUserTierUseCase, sharedAuthManager, userPreferences);
  }
}
