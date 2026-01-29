package com.swiftquantum.di;

import com.swiftquantum.domain.repository.BillingRepository;
import com.swiftquantum.domain.repository.HardwareRepository;
import com.swiftquantum.domain.usecase.SubmitHardwareJobUseCase;
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
public final class UseCaseModule_ProvideSubmitHardwareJobUseCaseFactory implements Factory<SubmitHardwareJobUseCase> {
  private final Provider<HardwareRepository> hardwareRepositoryProvider;

  private final Provider<BillingRepository> billingRepositoryProvider;

  public UseCaseModule_ProvideSubmitHardwareJobUseCaseFactory(
      Provider<HardwareRepository> hardwareRepositoryProvider,
      Provider<BillingRepository> billingRepositoryProvider) {
    this.hardwareRepositoryProvider = hardwareRepositoryProvider;
    this.billingRepositoryProvider = billingRepositoryProvider;
  }

  @Override
  public SubmitHardwareJobUseCase get() {
    return provideSubmitHardwareJobUseCase(hardwareRepositoryProvider.get(), billingRepositoryProvider.get());
  }

  public static UseCaseModule_ProvideSubmitHardwareJobUseCaseFactory create(
      Provider<HardwareRepository> hardwareRepositoryProvider,
      Provider<BillingRepository> billingRepositoryProvider) {
    return new UseCaseModule_ProvideSubmitHardwareJobUseCaseFactory(hardwareRepositoryProvider, billingRepositoryProvider);
  }

  public static SubmitHardwareJobUseCase provideSubmitHardwareJobUseCase(
      HardwareRepository hardwareRepository, BillingRepository billingRepository) {
    return Preconditions.checkNotNullFromProvides(UseCaseModule.INSTANCE.provideSubmitHardwareJobUseCase(hardwareRepository, billingRepository));
  }
}
