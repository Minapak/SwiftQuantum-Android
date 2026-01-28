package com.swiftquantum.di;

import com.swiftquantum.domain.repository.QuantumRepository;
import com.swiftquantum.domain.usecase.SaveCircuitUseCase;
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
public final class UseCaseModule_ProvideSaveCircuitUseCaseFactory implements Factory<SaveCircuitUseCase> {
  private final Provider<QuantumRepository> quantumRepositoryProvider;

  public UseCaseModule_ProvideSaveCircuitUseCaseFactory(
      Provider<QuantumRepository> quantumRepositoryProvider) {
    this.quantumRepositoryProvider = quantumRepositoryProvider;
  }

  @Override
  public SaveCircuitUseCase get() {
    return provideSaveCircuitUseCase(quantumRepositoryProvider.get());
  }

  public static UseCaseModule_ProvideSaveCircuitUseCaseFactory create(
      Provider<QuantumRepository> quantumRepositoryProvider) {
    return new UseCaseModule_ProvideSaveCircuitUseCaseFactory(quantumRepositoryProvider);
  }

  public static SaveCircuitUseCase provideSaveCircuitUseCase(QuantumRepository quantumRepository) {
    return Preconditions.checkNotNullFromProvides(UseCaseModule.INSTANCE.provideSaveCircuitUseCase(quantumRepository));
  }
}
