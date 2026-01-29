package com.swiftquantum.di;

import com.swiftquantum.domain.repository.BillingRepository;
import com.swiftquantum.domain.repository.HybridEngineRepository;
import com.swiftquantum.domain.usecase.ExecuteWithHybridEngineUseCase;
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
public final class UseCaseModule_ProvideExecuteWithHybridEngineUseCaseFactory implements Factory<ExecuteWithHybridEngineUseCase> {
  private final Provider<HybridEngineRepository> hybridEngineRepositoryProvider;

  private final Provider<BillingRepository> billingRepositoryProvider;

  public UseCaseModule_ProvideExecuteWithHybridEngineUseCaseFactory(
      Provider<HybridEngineRepository> hybridEngineRepositoryProvider,
      Provider<BillingRepository> billingRepositoryProvider) {
    this.hybridEngineRepositoryProvider = hybridEngineRepositoryProvider;
    this.billingRepositoryProvider = billingRepositoryProvider;
  }

  @Override
  public ExecuteWithHybridEngineUseCase get() {
    return provideExecuteWithHybridEngineUseCase(hybridEngineRepositoryProvider.get(), billingRepositoryProvider.get());
  }

  public static UseCaseModule_ProvideExecuteWithHybridEngineUseCaseFactory create(
      Provider<HybridEngineRepository> hybridEngineRepositoryProvider,
      Provider<BillingRepository> billingRepositoryProvider) {
    return new UseCaseModule_ProvideExecuteWithHybridEngineUseCaseFactory(hybridEngineRepositoryProvider, billingRepositoryProvider);
  }

  public static ExecuteWithHybridEngineUseCase provideExecuteWithHybridEngineUseCase(
      HybridEngineRepository hybridEngineRepository, BillingRepository billingRepository) {
    return Preconditions.checkNotNullFromProvides(UseCaseModule.INSTANCE.provideExecuteWithHybridEngineUseCase(hybridEngineRepository, billingRepository));
  }
}
