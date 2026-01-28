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
public final class GetExecutionHistoryUseCase_Factory implements Factory<GetExecutionHistoryUseCase> {
  private final Provider<QuantumRepository> quantumRepositoryProvider;

  public GetExecutionHistoryUseCase_Factory(Provider<QuantumRepository> quantumRepositoryProvider) {
    this.quantumRepositoryProvider = quantumRepositoryProvider;
  }

  @Override
  public GetExecutionHistoryUseCase get() {
    return newInstance(quantumRepositoryProvider.get());
  }

  public static GetExecutionHistoryUseCase_Factory create(
      Provider<QuantumRepository> quantumRepositoryProvider) {
    return new GetExecutionHistoryUseCase_Factory(quantumRepositoryProvider);
  }

  public static GetExecutionHistoryUseCase newInstance(QuantumRepository quantumRepository) {
    return new GetExecutionHistoryUseCase(quantumRepository);
  }
}
