package com.swiftquantum.di;

import com.swiftquantum.domain.repository.AuthRepository;
import com.swiftquantum.domain.usecase.GetCurrentUserUseCase;
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
public final class UseCaseModule_ProvideGetCurrentUserUseCaseFactory implements Factory<GetCurrentUserUseCase> {
  private final Provider<AuthRepository> authRepositoryProvider;

  public UseCaseModule_ProvideGetCurrentUserUseCaseFactory(
      Provider<AuthRepository> authRepositoryProvider) {
    this.authRepositoryProvider = authRepositoryProvider;
  }

  @Override
  public GetCurrentUserUseCase get() {
    return provideGetCurrentUserUseCase(authRepositoryProvider.get());
  }

  public static UseCaseModule_ProvideGetCurrentUserUseCaseFactory create(
      Provider<AuthRepository> authRepositoryProvider) {
    return new UseCaseModule_ProvideGetCurrentUserUseCaseFactory(authRepositoryProvider);
  }

  public static GetCurrentUserUseCase provideGetCurrentUserUseCase(AuthRepository authRepository) {
    return Preconditions.checkNotNullFromProvides(UseCaseModule.INSTANCE.provideGetCurrentUserUseCase(authRepository));
  }
}
