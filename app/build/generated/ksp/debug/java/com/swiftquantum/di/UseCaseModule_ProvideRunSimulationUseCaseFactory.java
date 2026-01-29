package com.swiftquantum.di;

import com.swiftquantum.domain.repository.BillingRepository;
import com.swiftquantum.domain.repository.QuantumRepository;
import com.swiftquantum.domain.usecase.RunSimulationUseCase;
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
public final class UseCaseModule_ProvideRunSimulationUseCaseFactory implements Factory<RunSimulationUseCase> {
  private final Provider<QuantumRepository> quantumRepositoryProvider;

  private final Provider<BillingRepository> billingRepositoryProvider;

  public UseCaseModule_ProvideRunSimulationUseCaseFactory(
      Provider<QuantumRepository> quantumRepositoryProvider,
      Provider<BillingRepository> billingRepositoryProvider) {
    this.quantumRepositoryProvider = quantumRepositoryProvider;
    this.billingRepositoryProvider = billingRepositoryProvider;
  }

  @Override
  public RunSimulationUseCase get() {
    return provideRunSimulationUseCase(quantumRepositoryProvider.get(), billingRepositoryProvider.get());
  }

  public static UseCaseModule_ProvideRunSimulationUseCaseFactory create(
      Provider<QuantumRepository> quantumRepositoryProvider,
      Provider<BillingRepository> billingRepositoryProvider) {
    return new UseCaseModule_ProvideRunSimulationUseCaseFactory(quantumRepositoryProvider, billingRepositoryProvider);
  }

  public static RunSimulationUseCase provideRunSimulationUseCase(
      QuantumRepository quantumRepository, BillingRepository billingRepository) {
    return Preconditions.checkNotNullFromProvides(UseCaseModule.INSTANCE.provideRunSimulationUseCase(quantumRepository, billingRepository));
  }
}
