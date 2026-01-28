package com.swiftquantum.di;

import com.swiftquantum.domain.repository.BillingRepository;
import com.swiftquantum.domain.usecase.PurchaseSubscriptionUseCase;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("dagger.hilt.android.scopes.ViewModelScoped")
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
public final class UseCaseModule_ProvidePurchaseSubscriptionUseCaseFactory implements Factory<PurchaseSubscriptionUseCase> {
  private final Provider<BillingRepository> billingRepositoryProvider;

  public UseCaseModule_ProvidePurchaseSubscriptionUseCaseFactory(
      Provider<BillingRepository> billingRepositoryProvider) {
    this.billingRepositoryProvider = billingRepositoryProvider;
  }

  @Override
  public PurchaseSubscriptionUseCase get() {
    return providePurchaseSubscriptionUseCase(billingRepositoryProvider.get());
  }

  public static UseCaseModule_ProvidePurchaseSubscriptionUseCaseFactory create(
      Provider<BillingRepository> billingRepositoryProvider) {
    return new UseCaseModule_ProvidePurchaseSubscriptionUseCaseFactory(billingRepositoryProvider);
  }

  public static PurchaseSubscriptionUseCase providePurchaseSubscriptionUseCase(
      BillingRepository billingRepository) {
    return Preconditions.checkNotNullFromProvides(UseCaseModule.INSTANCE.providePurchaseSubscriptionUseCase(billingRepository));
  }
}
