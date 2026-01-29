package com.swiftquantum.di;

import com.swiftquantum.domain.repository.QASMRepository;
import com.swiftquantum.domain.usecase.ValidateQASMUseCase;
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
public final class UseCaseModule_ProvideValidateQASMUseCaseFactory implements Factory<ValidateQASMUseCase> {
  private final Provider<QASMRepository> qasmRepositoryProvider;

  public UseCaseModule_ProvideValidateQASMUseCaseFactory(
      Provider<QASMRepository> qasmRepositoryProvider) {
    this.qasmRepositoryProvider = qasmRepositoryProvider;
  }

  @Override
  public ValidateQASMUseCase get() {
    return provideValidateQASMUseCase(qasmRepositoryProvider.get());
  }

  public static UseCaseModule_ProvideValidateQASMUseCaseFactory create(
      Provider<QASMRepository> qasmRepositoryProvider) {
    return new UseCaseModule_ProvideValidateQASMUseCaseFactory(qasmRepositoryProvider);
  }

  public static ValidateQASMUseCase provideValidateQASMUseCase(QASMRepository qasmRepository) {
    return Preconditions.checkNotNullFromProvides(UseCaseModule.INSTANCE.provideValidateQASMUseCase(qasmRepository));
  }
}
