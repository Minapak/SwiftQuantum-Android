package com.swiftquantum.di;

import com.swiftquantum.domain.repository.AuthRepository;
import com.swiftquantum.domain.usecase.ForgotPasswordUseCase;
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
public final class UseCaseModule_ProvideForgotPasswordUseCaseFactory implements Factory<ForgotPasswordUseCase> {
  private final Provider<AuthRepository> authRepositoryProvider;

  public UseCaseModule_ProvideForgotPasswordUseCaseFactory(
      Provider<AuthRepository> authRepositoryProvider) {
    this.authRepositoryProvider = authRepositoryProvider;
  }

  @Override
  public ForgotPasswordUseCase get() {
    return provideForgotPasswordUseCase(authRepositoryProvider.get());
  }

  public static UseCaseModule_ProvideForgotPasswordUseCaseFactory create(
      Provider<AuthRepository> authRepositoryProvider) {
    return new UseCaseModule_ProvideForgotPasswordUseCaseFactory(authRepositoryProvider);
  }

  public static ForgotPasswordUseCase provideForgotPasswordUseCase(AuthRepository authRepository) {
    return Preconditions.checkNotNullFromProvides(UseCaseModule.INSTANCE.provideForgotPasswordUseCase(authRepository));
  }
}
