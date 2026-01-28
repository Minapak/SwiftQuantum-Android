package com.swiftquantum.di;

import com.swiftquantum.domain.repository.QuantumRepository;
import com.swiftquantum.domain.usecase.DeleteCircuitUseCase;
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
public final class UseCaseModule_ProvideDeleteCircuitUseCaseFactory implements Factory<DeleteCircuitUseCase> {
  private final Provider<QuantumRepository> quantumRepositoryProvider;

  public UseCaseModule_ProvideDeleteCircuitUseCaseFactory(
      Provider<QuantumRepository> quantumRepositoryProvider) {
    this.quantumRepositoryProvider = quantumRepositoryProvider;
  }

  @Override
  public DeleteCircuitUseCase get() {
    return provideDeleteCircuitUseCase(quantumRepositoryProvider.get());
  }

  public static UseCaseModule_ProvideDeleteCircuitUseCaseFactory create(
      Provider<QuantumRepository> quantumRepositoryProvider) {
    return new UseCaseModule_ProvideDeleteCircuitUseCaseFactory(quantumRepositoryProvider);
  }

  public static DeleteCircuitUseCase provideDeleteCircuitUseCase(
      QuantumRepository quantumRepository) {
    return Preconditions.checkNotNullFromProvides(UseCaseModule.INSTANCE.provideDeleteCircuitUseCase(quantumRepository));
  }
}
