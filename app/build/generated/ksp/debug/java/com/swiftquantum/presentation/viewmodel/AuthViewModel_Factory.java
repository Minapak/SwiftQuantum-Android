package com.swiftquantum.presentation.viewmodel;

import com.swiftquantum.domain.usecase.ForgotPasswordUseCase;
import com.swiftquantum.domain.usecase.GetCurrentUserUseCase;
import com.swiftquantum.domain.usecase.LoginUseCase;
import com.swiftquantum.domain.usecase.LogoutUseCase;
import com.swiftquantum.domain.usecase.RegisterUseCase;
import com.swiftquantum.domain.usecase.UpdateProfileUseCase;
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
public final class AuthViewModel_Factory implements Factory<AuthViewModel> {
  private final Provider<LoginUseCase> loginUseCaseProvider;

  private final Provider<RegisterUseCase> registerUseCaseProvider;

  private final Provider<LogoutUseCase> logoutUseCaseProvider;

  private final Provider<GetCurrentUserUseCase> getCurrentUserUseCaseProvider;

  private final Provider<ForgotPasswordUseCase> forgotPasswordUseCaseProvider;

  private final Provider<UpdateProfileUseCase> updateProfileUseCaseProvider;

  public AuthViewModel_Factory(Provider<LoginUseCase> loginUseCaseProvider,
      Provider<RegisterUseCase> registerUseCaseProvider,
      Provider<LogoutUseCase> logoutUseCaseProvider,
      Provider<GetCurrentUserUseCase> getCurrentUserUseCaseProvider,
      Provider<ForgotPasswordUseCase> forgotPasswordUseCaseProvider,
      Provider<UpdateProfileUseCase> updateProfileUseCaseProvider) {
    this.loginUseCaseProvider = loginUseCaseProvider;
    this.registerUseCaseProvider = registerUseCaseProvider;
    this.logoutUseCaseProvider = logoutUseCaseProvider;
    this.getCurrentUserUseCaseProvider = getCurrentUserUseCaseProvider;
    this.forgotPasswordUseCaseProvider = forgotPasswordUseCaseProvider;
    this.updateProfileUseCaseProvider = updateProfileUseCaseProvider;
  }

  @Override
  public AuthViewModel get() {
    return newInstance(loginUseCaseProvider.get(), registerUseCaseProvider.get(), logoutUseCaseProvider.get(), getCurrentUserUseCaseProvider.get(), forgotPasswordUseCaseProvider.get(), updateProfileUseCaseProvider.get());
  }

  public static AuthViewModel_Factory create(Provider<LoginUseCase> loginUseCaseProvider,
      Provider<RegisterUseCase> registerUseCaseProvider,
      Provider<LogoutUseCase> logoutUseCaseProvider,
      Provider<GetCurrentUserUseCase> getCurrentUserUseCaseProvider,
      Provider<ForgotPasswordUseCase> forgotPasswordUseCaseProvider,
      Provider<UpdateProfileUseCase> updateProfileUseCaseProvider) {
    return new AuthViewModel_Factory(loginUseCaseProvider, registerUseCaseProvider, logoutUseCaseProvider, getCurrentUserUseCaseProvider, forgotPasswordUseCaseProvider, updateProfileUseCaseProvider);
  }

  public static AuthViewModel newInstance(LoginUseCase loginUseCase,
      RegisterUseCase registerUseCase, LogoutUseCase logoutUseCase,
      GetCurrentUserUseCase getCurrentUserUseCase, ForgotPasswordUseCase forgotPasswordUseCase,
      UpdateProfileUseCase updateProfileUseCase) {
    return new AuthViewModel(loginUseCase, registerUseCase, logoutUseCase, getCurrentUserUseCase, forgotPasswordUseCase, updateProfileUseCase);
  }
}
