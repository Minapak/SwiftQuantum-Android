package com.swiftquantum.presentation.viewmodel;

import com.swiftquantum.data.auth.SharedAuthManager;
import com.swiftquantum.domain.usecase.GetCurrentSubscriptionUseCase;
import com.swiftquantum.domain.usecase.GetCurrentUserUseCase;
import com.swiftquantum.domain.usecase.GetSubscriptionProductsUseCase;
import com.swiftquantum.domain.usecase.LogoutUseCase;
import com.swiftquantum.domain.usecase.ObserveSubscriptionUseCase;
import com.swiftquantum.domain.usecase.ObserveUserTierUseCase;
import com.swiftquantum.domain.usecase.PurchaseSubscriptionUseCase;
import com.swiftquantum.domain.usecase.RestorePurchasesUseCase;
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
public final class ProfileViewModel_Factory implements Factory<ProfileViewModel> {
  private final Provider<GetCurrentUserUseCase> getCurrentUserUseCaseProvider;

  private final Provider<LogoutUseCase> logoutUseCaseProvider;

  private final Provider<ObserveUserTierUseCase> observeUserTierUseCaseProvider;

  private final Provider<ObserveSubscriptionUseCase> observeSubscriptionUseCaseProvider;

  private final Provider<GetSubscriptionProductsUseCase> getSubscriptionProductsUseCaseProvider;

  private final Provider<PurchaseSubscriptionUseCase> purchaseSubscriptionUseCaseProvider;

  private final Provider<RestorePurchasesUseCase> restorePurchasesUseCaseProvider;

  private final Provider<GetCurrentSubscriptionUseCase> getCurrentSubscriptionUseCaseProvider;

  private final Provider<SharedAuthManager> sharedAuthManagerProvider;

  public ProfileViewModel_Factory(Provider<GetCurrentUserUseCase> getCurrentUserUseCaseProvider,
      Provider<LogoutUseCase> logoutUseCaseProvider,
      Provider<ObserveUserTierUseCase> observeUserTierUseCaseProvider,
      Provider<ObserveSubscriptionUseCase> observeSubscriptionUseCaseProvider,
      Provider<GetSubscriptionProductsUseCase> getSubscriptionProductsUseCaseProvider,
      Provider<PurchaseSubscriptionUseCase> purchaseSubscriptionUseCaseProvider,
      Provider<RestorePurchasesUseCase> restorePurchasesUseCaseProvider,
      Provider<GetCurrentSubscriptionUseCase> getCurrentSubscriptionUseCaseProvider,
      Provider<SharedAuthManager> sharedAuthManagerProvider) {
    this.getCurrentUserUseCaseProvider = getCurrentUserUseCaseProvider;
    this.logoutUseCaseProvider = logoutUseCaseProvider;
    this.observeUserTierUseCaseProvider = observeUserTierUseCaseProvider;
    this.observeSubscriptionUseCaseProvider = observeSubscriptionUseCaseProvider;
    this.getSubscriptionProductsUseCaseProvider = getSubscriptionProductsUseCaseProvider;
    this.purchaseSubscriptionUseCaseProvider = purchaseSubscriptionUseCaseProvider;
    this.restorePurchasesUseCaseProvider = restorePurchasesUseCaseProvider;
    this.getCurrentSubscriptionUseCaseProvider = getCurrentSubscriptionUseCaseProvider;
    this.sharedAuthManagerProvider = sharedAuthManagerProvider;
  }

  @Override
  public ProfileViewModel get() {
    return newInstance(getCurrentUserUseCaseProvider.get(), logoutUseCaseProvider.get(), observeUserTierUseCaseProvider.get(), observeSubscriptionUseCaseProvider.get(), getSubscriptionProductsUseCaseProvider.get(), purchaseSubscriptionUseCaseProvider.get(), restorePurchasesUseCaseProvider.get(), getCurrentSubscriptionUseCaseProvider.get(), sharedAuthManagerProvider.get());
  }

  public static ProfileViewModel_Factory create(
      Provider<GetCurrentUserUseCase> getCurrentUserUseCaseProvider,
      Provider<LogoutUseCase> logoutUseCaseProvider,
      Provider<ObserveUserTierUseCase> observeUserTierUseCaseProvider,
      Provider<ObserveSubscriptionUseCase> observeSubscriptionUseCaseProvider,
      Provider<GetSubscriptionProductsUseCase> getSubscriptionProductsUseCaseProvider,
      Provider<PurchaseSubscriptionUseCase> purchaseSubscriptionUseCaseProvider,
      Provider<RestorePurchasesUseCase> restorePurchasesUseCaseProvider,
      Provider<GetCurrentSubscriptionUseCase> getCurrentSubscriptionUseCaseProvider,
      Provider<SharedAuthManager> sharedAuthManagerProvider) {
    return new ProfileViewModel_Factory(getCurrentUserUseCaseProvider, logoutUseCaseProvider, observeUserTierUseCaseProvider, observeSubscriptionUseCaseProvider, getSubscriptionProductsUseCaseProvider, purchaseSubscriptionUseCaseProvider, restorePurchasesUseCaseProvider, getCurrentSubscriptionUseCaseProvider, sharedAuthManagerProvider);
  }

  public static ProfileViewModel newInstance(GetCurrentUserUseCase getCurrentUserUseCase,
      LogoutUseCase logoutUseCase, ObserveUserTierUseCase observeUserTierUseCase,
      ObserveSubscriptionUseCase observeSubscriptionUseCase,
      GetSubscriptionProductsUseCase getSubscriptionProductsUseCase,
      PurchaseSubscriptionUseCase purchaseSubscriptionUseCase,
      RestorePurchasesUseCase restorePurchasesUseCase,
      GetCurrentSubscriptionUseCase getCurrentSubscriptionUseCase,
      SharedAuthManager sharedAuthManager) {
    return new ProfileViewModel(getCurrentUserUseCase, logoutUseCase, observeUserTierUseCase, observeSubscriptionUseCase, getSubscriptionProductsUseCase, purchaseSubscriptionUseCase, restorePurchasesUseCase, getCurrentSubscriptionUseCase, sharedAuthManager);
  }
}
