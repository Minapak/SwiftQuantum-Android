package com.swiftquantum.di;

import com.swiftquantum.domain.repository.BillingRepository;
import com.swiftquantum.domain.repository.HybridEngineRepository;
import com.swiftquantum.domain.usecase.RunBenchmarkUseCase;
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
public final class UseCaseModule_ProvideRunBenchmarkUseCaseFactory implements Factory<RunBenchmarkUseCase> {
  private final Provider<HybridEngineRepository> hybridEngineRepositoryProvider;

  private final Provider<BillingRepository> billingRepositoryProvider;

  public UseCaseModule_ProvideRunBenchmarkUseCaseFactory(
      Provider<HybridEngineRepository> hybridEngineRepositoryProvider,
      Provider<BillingRepository> billingRepositoryProvider) {
    this.hybridEngineRepositoryProvider = hybridEngineRepositoryProvider;
    this.billingRepositoryProvider = billingRepositoryProvider;
  }

  @Override
  public RunBenchmarkUseCase get() {
    return provideRunBenchmarkUseCase(hybridEngineRepositoryProvider.get(), billingRepositoryProvider.get());
  }

  public static UseCaseModule_ProvideRunBenchmarkUseCaseFactory create(
      Provider<HybridEngineRepository> hybridEngineRepositoryProvider,
      Provider<BillingRepository> billingRepositoryProvider) {
    return new UseCaseModule_ProvideRunBenchmarkUseCaseFactory(hybridEngineRepositoryProvider, billingRepositoryProvider);
  }

  public static RunBenchmarkUseCase provideRunBenchmarkUseCase(
      HybridEngineRepository hybridEngineRepository, BillingRepository billingRepository) {
    return Preconditions.checkNotNullFromProvides(UseCaseModule.INSTANCE.provideRunBenchmarkUseCase(hybridEngineRepository, billingRepository));
  }
}
