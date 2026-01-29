package com.swiftquantum.domain.usecase;

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
public final class GetBenchmarkHistoryUseCase_Factory implements Factory<GetBenchmarkHistoryUseCase> {
  private final Provider<HybridEngineRepository> hybridEngineRepositoryProvider;

  public GetBenchmarkHistoryUseCase_Factory(
      Provider<HybridEngineRepository> hybridEngineRepositoryProvider) {
    this.hybridEngineRepositoryProvider = hybridEngineRepositoryProvider;
  }

  @Override
  public GetBenchmarkHistoryUseCase get() {
    return newInstance(hybridEngineRepositoryProvider.get());
  }

  public static GetBenchmarkHistoryUseCase_Factory create(
      Provider<HybridEngineRepository> hybridEngineRepositoryProvider) {
    return new GetBenchmarkHistoryUseCase_Factory(hybridEngineRepositoryProvider);
  }

  public static GetBenchmarkHistoryUseCase newInstance(
      HybridEngineRepository hybridEngineRepository) {
    return new GetBenchmarkHistoryUseCase(hybridEngineRepository);
  }
}
