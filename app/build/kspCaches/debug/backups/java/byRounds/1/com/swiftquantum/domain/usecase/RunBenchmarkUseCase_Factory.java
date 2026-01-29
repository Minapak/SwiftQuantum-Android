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
public final class RunBenchmarkUseCase_Factory implements Factory<RunBenchmarkUseCase> {
  private final Provider<HybridEngineRepository> hybridEngineRepositoryProvider;

  private final Provider<BillingRepository> billingRepositoryProvider;

  public RunBenchmarkUseCase_Factory(
      Provider<HybridEngineRepository> hybridEngineRepositoryProvider,
      Provider<BillingRepository> billingRepositoryProvider) {
    this.hybridEngineRepositoryProvider = hybridEngineRepositoryProvider;
    this.billingRepositoryProvider = billingRepositoryProvider;
  }

  @Override
  public RunBenchmarkUseCase get() {
    return newInstance(hybridEngineRepositoryProvider.get(), billingRepositoryProvider.get());
  }

  public static RunBenchmarkUseCase_Factory create(
      Provider<HybridEngineRepository> hybridEngineRepositoryProvider,
      Provider<BillingRepository> billingRepositoryProvider) {
    return new RunBenchmarkUseCase_Factory(hybridEngineRepositoryProvider, billingRepositoryProvider);
  }

  public static RunBenchmarkUseCase newInstance(HybridEngineRepository hybridEngineRepository,
      BillingRepository billingRepository) {
    return new RunBenchmarkUseCase(hybridEngineRepository, billingRepository);
  }
}
