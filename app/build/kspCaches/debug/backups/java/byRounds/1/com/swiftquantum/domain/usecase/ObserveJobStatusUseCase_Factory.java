package com.swiftquantum.domain.usecase;

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
public final class ObserveJobStatusUseCase_Factory implements Factory<ObserveJobStatusUseCase> {
  private final Provider<HardwareRepository> hardwareRepositoryProvider;

  public ObserveJobStatusUseCase_Factory(Provider<HardwareRepository> hardwareRepositoryProvider) {
    this.hardwareRepositoryProvider = hardwareRepositoryProvider;
  }

  @Override
  public ObserveJobStatusUseCase get() {
    return newInstance(hardwareRepositoryProvider.get());
  }

  public static ObserveJobStatusUseCase_Factory create(
      Provider<HardwareRepository> hardwareRepositoryProvider) {
    return new ObserveJobStatusUseCase_Factory(hardwareRepositoryProvider);
  }

  public static ObserveJobStatusUseCase newInstance(HardwareRepository hardwareRepository) {
    return new ObserveJobStatusUseCase(hardwareRepository);
  }
}
