package com.swiftquantum.di;

import com.swiftquantum.domain.repository.HardwareRepository;
import com.swiftquantum.domain.usecase.CancelJobUseCase;
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
public final class UseCaseModule_ProvideCancelJobUseCaseFactory implements Factory<CancelJobUseCase> {
  private final Provider<HardwareRepository> hardwareRepositoryProvider;

  public UseCaseModule_ProvideCancelJobUseCaseFactory(
      Provider<HardwareRepository> hardwareRepositoryProvider) {
    this.hardwareRepositoryProvider = hardwareRepositoryProvider;
  }

  @Override
  public CancelJobUseCase get() {
    return provideCancelJobUseCase(hardwareRepositoryProvider.get());
  }

  public static UseCaseModule_ProvideCancelJobUseCaseFactory create(
      Provider<HardwareRepository> hardwareRepositoryProvider) {
    return new UseCaseModule_ProvideCancelJobUseCaseFactory(hardwareRepositoryProvider);
  }

  public static CancelJobUseCase provideCancelJobUseCase(HardwareRepository hardwareRepository) {
    return Preconditions.checkNotNullFromProvides(UseCaseModule.INSTANCE.provideCancelJobUseCase(hardwareRepository));
  }
}
