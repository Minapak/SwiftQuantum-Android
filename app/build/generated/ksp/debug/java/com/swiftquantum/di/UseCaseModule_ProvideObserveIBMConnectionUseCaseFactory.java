package com.swiftquantum.di;

import com.swiftquantum.domain.repository.HardwareRepository;
import com.swiftquantum.domain.usecase.ObserveIBMConnectionUseCase;
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
public final class UseCaseModule_ProvideObserveIBMConnectionUseCaseFactory implements Factory<ObserveIBMConnectionUseCase> {
  private final Provider<HardwareRepository> hardwareRepositoryProvider;

  public UseCaseModule_ProvideObserveIBMConnectionUseCaseFactory(
      Provider<HardwareRepository> hardwareRepositoryProvider) {
    this.hardwareRepositoryProvider = hardwareRepositoryProvider;
  }

  @Override
  public ObserveIBMConnectionUseCase get() {
    return provideObserveIBMConnectionUseCase(hardwareRepositoryProvider.get());
  }

  public static UseCaseModule_ProvideObserveIBMConnectionUseCaseFactory create(
      Provider<HardwareRepository> hardwareRepositoryProvider) {
    return new UseCaseModule_ProvideObserveIBMConnectionUseCaseFactory(hardwareRepositoryProvider);
  }

  public static ObserveIBMConnectionUseCase provideObserveIBMConnectionUseCase(
      HardwareRepository hardwareRepository) {
    return Preconditions.checkNotNullFromProvides(UseCaseModule.INSTANCE.provideObserveIBMConnectionUseCase(hardwareRepository));
  }
}
