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
public final class GetEngineStatusUseCase_Factory implements Factory<GetEngineStatusUseCase> {
  private final Provider<HybridEngineRepository> hybridEngineRepositoryProvider;

  public GetEngineStatusUseCase_Factory(
      Provider<HybridEngineRepository> hybridEngineRepositoryProvider) {
    this.hybridEngineRepositoryProvider = hybridEngineRepositoryProvider;
  }

  @Override
  public GetEngineStatusUseCase get() {
    return newInstance(hybridEngineRepositoryProvider.get());
  }

  public static GetEngineStatusUseCase_Factory create(
      Provider<HybridEngineRepository> hybridEngineRepositoryProvider) {
    return new GetEngineStatusUseCase_Factory(hybridEngineRepositoryProvider);
  }

  public static GetEngineStatusUseCase newInstance(HybridEngineRepository hybridEngineRepository) {
    return new GetEngineStatusUseCase(hybridEngineRepository);
  }
}
