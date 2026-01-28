package com.swiftquantum.di;

import com.swiftquantum.domain.repository.HardwareRepository;
import com.swiftquantum.domain.usecase.DisconnectFromIBMQuantumUseCase;
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
public final class UseCaseModule_ProvideDisconnectFromIBMQuantumUseCaseFactory implements Factory<DisconnectFromIBMQuantumUseCase> {
  private final Provider<HardwareRepository> hardwareRepositoryProvider;

  public UseCaseModule_ProvideDisconnectFromIBMQuantumUseCaseFactory(
      Provider<HardwareRepository> hardwareRepositoryProvider) {
    this.hardwareRepositoryProvider = hardwareRepositoryProvider;
  }

  @Override
  public DisconnectFromIBMQuantumUseCase get() {
    return provideDisconnectFromIBMQuantumUseCase(hardwareRepositoryProvider.get());
  }

  public static UseCaseModule_ProvideDisconnectFromIBMQuantumUseCaseFactory create(
      Provider<HardwareRepository> hardwareRepositoryProvider) {
    return new UseCaseModule_ProvideDisconnectFromIBMQuantumUseCaseFactory(hardwareRepositoryProvider);
  }

  public static DisconnectFromIBMQuantumUseCase provideDisconnectFromIBMQuantumUseCase(
      HardwareRepository hardwareRepository) {
    return Preconditions.checkNotNullFromProvides(UseCaseModule.INSTANCE.provideDisconnectFromIBMQuantumUseCase(hardwareRepository));
  }
}
