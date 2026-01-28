package com.swiftquantum.di;

import com.swiftquantum.domain.repository.QuantumRepository;
import com.swiftquantum.domain.usecase.GetCircuitUseCase;
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
public final class UseCaseModule_ProvideGetCircuitUseCaseFactory implements Factory<GetCircuitUseCase> {
  private final Provider<QuantumRepository> quantumRepositoryProvider;

  public UseCaseModule_ProvideGetCircuitUseCaseFactory(
      Provider<QuantumRepository> quantumRepositoryProvider) {
    this.quantumRepositoryProvider = quantumRepositoryProvider;
  }

  @Override
  public GetCircuitUseCase get() {
    return provideGetCircuitUseCase(quantumRepositoryProvider.get());
  }

  public static UseCaseModule_ProvideGetCircuitUseCaseFactory create(
      Provider<QuantumRepository> quantumRepositoryProvider) {
    return new UseCaseModule_ProvideGetCircuitUseCaseFactory(quantumRepositoryProvider);
  }

  public static GetCircuitUseCase provideGetCircuitUseCase(QuantumRepository quantumRepository) {
    return Preconditions.checkNotNullFromProvides(UseCaseModule.INSTANCE.provideGetCircuitUseCase(quantumRepository));
  }
}
