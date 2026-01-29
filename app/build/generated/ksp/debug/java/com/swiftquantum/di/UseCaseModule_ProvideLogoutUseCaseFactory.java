package com.swiftquantum.di;

import com.swiftquantum.domain.repository.AuthRepository;
import com.swiftquantum.domain.usecase.LogoutUseCase;
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
public final class UseCaseModule_ProvideLogoutUseCaseFactory implements Factory<LogoutUseCase> {
  private final Provider<AuthRepository> authRepositoryProvider;

  public UseCaseModule_ProvideLogoutUseCaseFactory(
      Provider<AuthRepository> authRepositoryProvider) {
    this.authRepositoryProvider = authRepositoryProvider;
  }

  @Override
  public LogoutUseCase get() {
    return provideLogoutUseCase(authRepositoryProvider.get());
  }

  public static UseCaseModule_ProvideLogoutUseCaseFactory create(
      Provider<AuthRepository> authRepositoryProvider) {
    return new UseCaseModule_ProvideLogoutUseCaseFactory(authRepositoryProvider);
  }

  public static LogoutUseCase provideLogoutUseCase(AuthRepository authRepository) {
    return Preconditions.checkNotNullFromProvides(UseCaseModule.INSTANCE.provideLogoutUseCase(authRepository));
  }
}
