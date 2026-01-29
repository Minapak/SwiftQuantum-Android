package com.swiftquantum.di;

import com.swiftquantum.domain.repository.QASMRepository;
import com.swiftquantum.domain.usecase.LoadQASMTemplatesUseCase;
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
public final class UseCaseModule_ProvideLoadQASMTemplatesUseCaseFactory implements Factory<LoadQASMTemplatesUseCase> {
  private final Provider<QASMRepository> qasmRepositoryProvider;

  public UseCaseModule_ProvideLoadQASMTemplatesUseCaseFactory(
      Provider<QASMRepository> qasmRepositoryProvider) {
    this.qasmRepositoryProvider = qasmRepositoryProvider;
  }

  @Override
  public LoadQASMTemplatesUseCase get() {
    return provideLoadQASMTemplatesUseCase(qasmRepositoryProvider.get());
  }

  public static UseCaseModule_ProvideLoadQASMTemplatesUseCaseFactory create(
      Provider<QASMRepository> qasmRepositoryProvider) {
    return new UseCaseModule_ProvideLoadQASMTemplatesUseCaseFactory(qasmRepositoryProvider);
  }

  public static LoadQASMTemplatesUseCase provideLoadQASMTemplatesUseCase(
      QASMRepository qasmRepository) {
    return Preconditions.checkNotNullFromProvides(UseCaseModule.INSTANCE.provideLoadQASMTemplatesUseCase(qasmRepository));
  }
}
