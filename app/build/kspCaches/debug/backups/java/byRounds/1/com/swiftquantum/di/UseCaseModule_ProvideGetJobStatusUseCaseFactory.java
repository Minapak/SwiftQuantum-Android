package com.swiftquantum.di;

import com.swiftquantum.domain.repository.HardwareRepository;
import com.swiftquantum.domain.usecase.GetJobStatusUseCase;
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
public final class UseCaseModule_ProvideGetJobStatusUseCaseFactory implements Factory<GetJobStatusUseCase> {
  private final Provider<HardwareRepository> hardwareRepositoryProvider;

  public UseCaseModule_ProvideGetJobStatusUseCaseFactory(
      Provider<HardwareRepository> hardwareRepositoryProvider) {
    this.hardwareRepositoryProvider = hardwareRepositoryProvider;
  }

  @Override
  public GetJobStatusUseCase get() {
    return provideGetJobStatusUseCase(hardwareRepositoryProvider.get());
  }

  public static UseCaseModule_ProvideGetJobStatusUseCaseFactory create(
      Provider<HardwareRepository> hardwareRepositoryProvider) {
    return new UseCaseModule_ProvideGetJobStatusUseCaseFactory(hardwareRepositoryProvider);
  }

  public static GetJobStatusUseCase provideGetJobStatusUseCase(
      HardwareRepository hardwareRepository) {
    return Preconditions.checkNotNullFromProvides(UseCaseModule.INSTANCE.provideGetJobStatusUseCase(hardwareRepository));
  }
}
