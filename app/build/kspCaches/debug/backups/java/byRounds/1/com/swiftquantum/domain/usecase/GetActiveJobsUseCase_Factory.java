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
public final class GetActiveJobsUseCase_Factory implements Factory<GetActiveJobsUseCase> {
  private final Provider<HardwareRepository> hardwareRepositoryProvider;

  public GetActiveJobsUseCase_Factory(Provider<HardwareRepository> hardwareRepositoryProvider) {
    this.hardwareRepositoryProvider = hardwareRepositoryProvider;
  }

  @Override
  public GetActiveJobsUseCase get() {
    return newInstance(hardwareRepositoryProvider.get());
  }

  public static GetActiveJobsUseCase_Factory create(
      Provider<HardwareRepository> hardwareRepositoryProvider) {
    return new GetActiveJobsUseCase_Factory(hardwareRepositoryProvider);
  }

  public static GetActiveJobsUseCase newInstance(HardwareRepository hardwareRepository) {
    return new GetActiveJobsUseCase(hardwareRepository);
  }
}
