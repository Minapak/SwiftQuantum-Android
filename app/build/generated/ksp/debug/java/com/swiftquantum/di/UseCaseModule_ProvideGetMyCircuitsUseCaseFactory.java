package com.swiftquantum.di;

import com.swiftquantum.domain.repository.QuantumRepository;
import com.swiftquantum.domain.usecase.GetMyCircuitsUseCase;
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
public final class UseCaseModule_ProvideGetMyCircuitsUseCaseFactory implements Factory<GetMyCircuitsUseCase> {
  private final Provider<QuantumRepository> quantumRepositoryProvider;

  public UseCaseModule_ProvideGetMyCircuitsUseCaseFactory(
      Provider<QuantumRepository> quantumRepositoryProvider) {
    this.quantumRepositoryProvider = quantumRepositoryProvider;
  }

  @Override
  public GetMyCircuitsUseCase get() {
    return provideGetMyCircuitsUseCase(quantumRepositoryProvider.get());
  }

  public static UseCaseModule_ProvideGetMyCircuitsUseCaseFactory create(
      Provider<QuantumRepository> quantumRepositoryProvider) {
    return new UseCaseModule_ProvideGetMyCircuitsUseCaseFactory(quantumRepositoryProvider);
  }

  public static GetMyCircuitsUseCase provideGetMyCircuitsUseCase(
      QuantumRepository quantumRepository) {
    return Preconditions.checkNotNullFromProvides(UseCaseModule.INSTANCE.provideGetMyCircuitsUseCase(quantumRepository));
  }
}
