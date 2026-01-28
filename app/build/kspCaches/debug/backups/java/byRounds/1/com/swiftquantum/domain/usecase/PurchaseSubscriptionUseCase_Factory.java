package com.swiftquantum.domain.usecase;

import com.swiftquantum.domain.repository.BillingRepository;
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
public final class PurchaseSubscriptionUseCase_Factory implements Factory<PurchaseSubscriptionUseCase> {
  private final Provider<BillingRepository> billingRepositoryProvider;

  public PurchaseSubscriptionUseCase_Factory(
      Provider<BillingRepository> billingRepositoryProvider) {
    this.billingRepositoryProvider = billingRepositoryProvider;
  }

  @Override
  public PurchaseSubscriptionUseCase get() {
    return newInstance(billingRepositoryProvider.get());
  }

  public static PurchaseSubscriptionUseCase_Factory create(
      Provider<BillingRepository> billingRepositoryProvider) {
    return new PurchaseSubscriptionUseCase_Factory(billingRepositoryProvider);
  }

  public static PurchaseSubscriptionUseCase newInstance(BillingRepository billingRepository) {
    return new PurchaseSubscriptionUseCase(billingRepository);
  }
}
