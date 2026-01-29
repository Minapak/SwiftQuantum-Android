package com.swiftquantum.presentation.viewmodel;

import com.swiftquantum.domain.usecase.GetSubscriptionProductsUseCase;
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
public final class PaywallViewModel_Factory implements Factory<PaywallViewModel> {
  private final Provider<GetSubscriptionProductsUseCase> getSubscriptionProductsUseCaseProvider;

  private final Provider<PurchaseSubscriptionUseCase> purchaseSubscriptionUseCaseProvider;

  private final Provider<RestorePurchasesUseCase> restorePurchasesUseCaseProvider;

  private final Provider<ObserveSubscriptionUseCase> observeSubscriptionUseCaseProvider;

  private final Provider<ObserveUserTierUseCase> observeUserTierUseCaseProvider;

  public PaywallViewModel_Factory(
      Provider<GetSubscriptionProductsUseCase> getSubscriptionProductsUseCaseProvider,
      Provider<PurchaseSubscriptionUseCase> purchaseSubscriptionUseCaseProvider,
      Provider<RestorePurchasesUseCase> restorePurchasesUseCaseProvider,
      Provider<ObserveSubscriptionUseCase> observeSubscriptionUseCaseProvider,
      Provider<ObserveUserTierUseCase> observeUserTierUseCaseProvider) {
    this.getSubscriptionProductsUseCaseProvider = getSubscriptionProductsUseCaseProvider;
    this.purchaseSubscriptionUseCaseProvider = purchaseSubscriptionUseCaseProvider;
    this.restorePurchasesUseCaseProvider = restorePurchasesUseCaseProvider;
    this.observeSubscriptionUseCaseProvider = observeSubscriptionUseCaseProvider;
    this.observeUserTierUseCaseProvider = observeUserTierUseCaseProvider;
  }

  @Override
  public PaywallViewModel get() {
    return newInstance(getSubscriptionProductsUseCaseProvider.get(), purchaseSubscriptionUseCaseProvider.get(), restorePurchasesUseCaseProvider.get(), observeSubscriptionUseCaseProvider.get(), observeUserTierUseCaseProvider.get());
  }

  public static PaywallViewModel_Factory create(
      Provider<GetSubscriptionProductsUseCase> getSubscriptionProductsUseCaseProvider,
      Provider<PurchaseSubscriptionUseCase> purchaseSubscriptionUseCaseProvider,
      Provider<RestorePurchasesUseCase> restorePurchasesUseCaseProvider,
      Provider<ObserveSubscriptionUseCase> observeSubscriptionUseCaseProvider,
      Provider<ObserveUserTierUseCase> observeUserTierUseCaseProvider) {
    return new PaywallViewModel_Factory(getSubscriptionProductsUseCaseProvider, purchaseSubscriptionUseCaseProvider, restorePurchasesUseCaseProvider, observeSubscriptionUseCaseProvider, observeUserTierUseCaseProvider);
  }

  public static PaywallViewModel newInstance(
      GetSubscriptionProductsUseCase getSubscriptionProductsUseCase,
      PurchaseSubscriptionUseCase purchaseSubscriptionUseCase,
      RestorePurchasesUseCase restorePurchasesUseCase,
      ObserveSubscriptionUseCase observeSubscriptionUseCase,
      ObserveUserTierUseCase observeUserTierUseCase) {
    return new PaywallViewModel(getSubscriptionProductsUseCase, purchaseSubscriptionUseCase, restorePurchasesUseCase, observeSubscriptionUseCase, observeUserTierUseCase);
  }
}
