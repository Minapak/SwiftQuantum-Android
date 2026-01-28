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
public final class GetMaxQubitsUseCase_Factory implements Factory<GetMaxQubitsUseCase> {
  private final Provider<BillingRepository> billingRepositoryProvider;

  public GetMaxQubitsUseCase_Factory(Provider<BillingRepository> billingRepositoryProvider) {
    this.billingRepositoryProvider = billingRepositoryProvider;
  }

  @Override
  public GetMaxQubitsUseCase get() {
    return newInstance(billingRepositoryProvider.get());
  }

  public static GetMaxQubitsUseCase_Factory create(
      Provider<BillingRepository> billingRepositoryProvider) {
    return new GetMaxQubitsUseCase_Factory(billingRepositoryProvider);
  }

  public static GetMaxQubitsUseCase newInstance(BillingRepository billingRepository) {
    return new GetMaxQubitsUseCase(billingRepository);
  }
}
