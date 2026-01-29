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
public final class GetMyCircuitsUseCase_Factory implements Factory<GetMyCircuitsUseCase> {
  private final Provider<QuantumRepository> quantumRepositoryProvider;

  public GetMyCircuitsUseCase_Factory(Provider<QuantumRepository> quantumRepositoryProvider) {
    this.quantumRepositoryProvider = quantumRepositoryProvider;
  }

  @Override
  public GetMyCircuitsUseCase get() {
    return newInstance(quantumRepositoryProvider.get());
  }

  public static GetMyCircuitsUseCase_Factory create(
      Provider<QuantumRepository> quantumRepositoryProvider) {
    return new GetMyCircuitsUseCase_Factory(quantumRepositoryProvider);
  }

  public static GetMyCircuitsUseCase newInstance(QuantumRepository quantumRepository) {
    return new GetMyCircuitsUseCase(quantumRepository);
  }
}
