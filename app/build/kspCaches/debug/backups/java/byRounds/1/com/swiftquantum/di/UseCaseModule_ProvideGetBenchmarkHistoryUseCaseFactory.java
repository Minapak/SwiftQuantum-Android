package com.swiftquantum.di;

import com.swiftquantum.domain.repository.HybridEngineRepository;
import com.swiftquantum.domain.usecase.GetBenchmarkHistoryUseCase;
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
public final class UseCaseModule_ProvideGetBenchmarkHistoryUseCaseFactory implements Factory<GetBenchmarkHistoryUseCase> {
  private final Provider<HybridEngineRepository> hybridEngineRepositoryProvider;

  public UseCaseModule_ProvideGetBenchmarkHistoryUseCaseFactory(
      Provider<HybridEngineRepository> hybridEngineRepositoryProvider) {
    this.hybridEngineRepositoryProvider = hybridEngineRepositoryProvider;
  }

  @Override
  public GetBenchmarkHistoryUseCase get() {
    return provideGetBenchmarkHistoryUseCase(hybridEngineRepositoryProvider.get());
  }

  public static UseCaseModule_ProvideGetBenchmarkHistoryUseCaseFactory create(
      Provider<HybridEngineRepository> hybridEngineRepositoryProvider) {
    return new UseCaseModule_ProvideGetBenchmarkHistoryUseCaseFactory(hybridEngineRepositoryProvider);
  }

  public static GetBenchmarkHistoryUseCase provideGetBenchmarkHistoryUseCase(
      HybridEngineRepository hybridEngineRepository) {
    return Preconditions.checkNotNullFromProvides(UseCaseModule.INSTANCE.provideGetBenchmarkHistoryUseCase(hybridEngineRepository));
  }
}
