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
public final class RestorePurchasesUseCase_Factory implements Factory<RestorePurchasesUseCase> {
  private final Provider<BillingRepository> billingRepositoryProvider;

  public RestorePurchasesUseCase_Factory(Provider<BillingRepository> billingRepositoryProvider) {
    this.billingRepositoryProvider = billingRepositoryProvider;
  }

  @Override
  public RestorePurchasesUseCase get() {
    return newInstance(billingRepositoryProvider.get());
  }

  public static RestorePurchasesUseCase_Factory create(
      Provider<BillingRepository> billingRepositoryProvider) {
    return new RestorePurchasesUseCase_Factory(billingRepositoryProvider);
  }

  public static RestorePurchasesUseCase newInstance(BillingRepository billingRepository) {
    return new RestorePurchasesUseCase(billingRepository);
  }
}
