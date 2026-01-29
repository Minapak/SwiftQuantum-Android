package com.swiftquantum.domain.usecase;

import com.swiftquantum.domain.repository.QuantumRepository;
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
public final class DeleteCircuitUseCase_Factory implements Factory<DeleteCircuitUseCase> {
  private final Provider<QuantumRepository> quantumRepositoryProvider;

  public DeleteCircuitUseCase_Factory(Provider<QuantumRepository> quantumRepositoryProvider) {
    this.quantumRepositoryProvider = quantumRepositoryProvider;
  }

  @Override
  public DeleteCircuitUseCase get() {
    return newInstance(quantumRepositoryProvider.get());
  }

  public static DeleteCircuitUseCase_Factory create(
      Provider<QuantumRepository> quantumRepositoryProvider) {
    return new DeleteCircuitUseCase_Factory(quantumRepositoryProvider);
  }

  public static DeleteCircuitUseCase newInstance(QuantumRepository quantumRepository) {
    return new DeleteCircuitUseCase(quantumRepository);
  }
}
