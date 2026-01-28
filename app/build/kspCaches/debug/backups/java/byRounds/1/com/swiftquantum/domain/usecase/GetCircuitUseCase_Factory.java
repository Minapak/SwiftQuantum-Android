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
public final class GetCircuitUseCase_Factory implements Factory<GetCircuitUseCase> {
  private final Provider<QuantumRepository> quantumRepositoryProvider;

  public GetCircuitUseCase_Factory(Provider<QuantumRepository> quantumRepositoryProvider) {
    this.quantumRepositoryProvider = quantumRepositoryProvider;
  }

  @Override
  public GetCircuitUseCase get() {
    return newInstance(quantumRepositoryProvider.get());
  }

  public static GetCircuitUseCase_Factory create(
      Provider<QuantumRepository> quantumRepositoryProvider) {
    return new GetCircuitUseCase_Factory(quantumRepositoryProvider);
  }

  public static GetCircuitUseCase newInstance(QuantumRepository quantumRepository) {
    return new GetCircuitUseCase(quantumRepository);
  }
}
