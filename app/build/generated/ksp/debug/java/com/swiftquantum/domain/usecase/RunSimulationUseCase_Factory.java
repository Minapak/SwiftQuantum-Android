package com.swiftquantum.domain.usecase;

import com.swiftquantum.domain.repository.BillingRepository;
import com.swiftquantum.domain.repository.QuantumRepository;
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
public final class RunSimulationUseCase_Factory implements Factory<RunSimulationUseCase> {
  private final Provider<QuantumRepository> quantumRepositoryProvider;

  private final Provider<BillingRepository> billingRepositoryProvider;

  public RunSimulationUseCase_Factory(Provider<QuantumRepository> quantumRepositoryProvider,
      Provider<BillingRepository> billingRepositoryProvider) {
    this.quantumRepositoryProvider = quantumRepositoryProvider;
    this.billingRepositoryProvider = billingRepositoryProvider;
  }

  @Override
  public RunSimulationUseCase get() {
    return newInstance(quantumRepositoryProvider.get(), billingRepositoryProvider.get());
  }

  public static RunSimulationUseCase_Factory create(
      Provider<QuantumRepository> quantumRepositoryProvider,
      Provider<BillingRepository> billingRepositoryProvider) {
    return new RunSimulationUseCase_Factory(quantumRepositoryProvider, billingRepositoryProvider);
  }

  public static RunSimulationUseCase newInstance(QuantumRepository quantumRepository,
      BillingRepository billingRepository) {
    return new RunSimulationUseCase(quantumRepository, billingRepository);
  }
}
