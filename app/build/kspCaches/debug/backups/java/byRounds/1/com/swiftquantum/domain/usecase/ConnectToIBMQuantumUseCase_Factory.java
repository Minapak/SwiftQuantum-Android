package com.swiftquantum.domain.usecase;

import com.swiftquantum.domain.repository.BillingRepository;
import com.swiftquantum.domain.repository.HardwareRepository;
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
public final class ConnectToIBMQuantumUseCase_Factory implements Factory<ConnectToIBMQuantumUseCase> {
  private final Provider<HardwareRepository> hardwareRepositoryProvider;

  private final Provider<BillingRepository> billingRepositoryProvider;

  public ConnectToIBMQuantumUseCase_Factory(Provider<HardwareRepository> hardwareRepositoryProvider,
      Provider<BillingRepository> billingRepositoryProvider) {
    this.hardwareRepositoryProvider = hardwareRepositoryProvider;
    this.billingRepositoryProvider = billingRepositoryProvider;
  }

  @Override
  public ConnectToIBMQuantumUseCase get() {
    return newInstance(hardwareRepositoryProvider.get(), billingRepositoryProvider.get());
  }

  public static ConnectToIBMQuantumUseCase_Factory create(
      Provider<HardwareRepository> hardwareRepositoryProvider,
      Provider<BillingRepository> billingRepositoryProvider) {
    return new ConnectToIBMQuantumUseCase_Factory(hardwareRepositoryProvider, billingRepositoryProvider);
  }

  public static ConnectToIBMQuantumUseCase newInstance(HardwareRepository hardwareRepository,
      BillingRepository billingRepository) {
    return new ConnectToIBMQuantumUseCase(hardwareRepository, billingRepository);
  }
}
