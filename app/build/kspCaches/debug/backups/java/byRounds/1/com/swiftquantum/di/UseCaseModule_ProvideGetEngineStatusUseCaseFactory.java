package com.swiftquantum.di;

import com.swiftquantum.domain.repository.HybridEngineRepository;
import com.swiftquantum.domain.usecase.GetEngineStatusUseCase;
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
public final class UseCaseModule_ProvideGetEngineStatusUseCaseFactory implements Factory<GetEngineStatusUseCase> {
  private final Provider<HybridEngineRepository> hybridEngineRepositoryProvider;

  public UseCaseModule_ProvideGetEngineStatusUseCaseFactory(
      Provider<HybridEngineRepository> hybridEngineRepositoryProvider) {
    this.hybridEngineRepositoryProvider = hybridEngineRepositoryProvider;
  }

  @Override
  public GetEngineStatusUseCase get() {
    return provideGetEngineStatusUseCase(hybridEngineRepositoryProvider.get());
  }

  public static UseCaseModule_ProvideGetEngineStatusUseCaseFactory create(
      Provider<HybridEngineRepository> hybridEngineRepositoryProvider) {
    return new UseCaseModule_ProvideGetEngineStatusUseCaseFactory(hybridEngineRepositoryProvider);
  }

  public static GetEngineStatusUseCase provideGetEngineStatusUseCase(
      HybridEngineRepository hybridEngineRepository) {
    return Preconditions.checkNotNullFromProvides(UseCaseModule.INSTANCE.provideGetEngineStatusUseCase(hybridEngineRepository));
  }
}
