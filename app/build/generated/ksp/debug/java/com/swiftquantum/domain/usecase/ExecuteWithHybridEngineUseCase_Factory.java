package com.swiftquantum.domain.usecase;

import com.swiftquantum.domain.repository.BillingRepository;
import com.swiftquantum.domain.repository.HybridEngineRepository;
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
public final class ExecuteWithHybridEngineUseCase_Factory implements Factory<ExecuteWithHybridEngineUseCase> {
  private final Provider<HybridEngineRepository> hybridEngineRepositoryProvider;

  private final Provider<BillingRepository> billingRepositoryProvider;

  public ExecuteWithHybridEngineUseCase_Factory(
      Provider<HybridEngineRepository> hybridEngineRepositoryProvider,
      Provider<BillingRepository> billingRepositoryProvider) {
    this.hybridEngineRepositoryProvider = hybridEngineRepositoryProvider;
    this.billingRepositoryProvider = billingRepositoryProvider;
  }

  @Override
  public ExecuteWithHybridEngineUseCase get() {
    return newInstance(hybridEngineRepositoryProvider.get(), billingRepositoryProvider.get());
  }

  public static ExecuteWithHybridEngineUseCase_Factory create(
      Provider<HybridEngineRepository> hybridEngineRepositoryProvider,
      Provider<BillingRepository> billingRepositoryProvider) {
    return new ExecuteWithHybridEngineUseCase_Factory(hybridEngineRepositoryProvider, billingRepositoryProvider);
  }

  public static ExecuteWithHybridEngineUseCase newInstance(
      HybridEngineRepository hybridEngineRepository, BillingRepository billingRepository) {
    return new ExecuteWithHybridEngineUseCase(hybridEngineRepository, billingRepository);
  }
}
