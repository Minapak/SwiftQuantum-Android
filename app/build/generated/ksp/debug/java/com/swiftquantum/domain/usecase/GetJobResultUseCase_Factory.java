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
public final class GetJobResultUseCase_Factory implements Factory<GetJobResultUseCase> {
  private final Provider<HardwareRepository> hardwareRepositoryProvider;

  public GetJobResultUseCase_Factory(Provider<HardwareRepository> hardwareRepositoryProvider) {
    this.hardwareRepositoryProvider = hardwareRepositoryProvider;
  }

  @Override
  public GetJobResultUseCase get() {
    return newInstance(hardwareRepositoryProvider.get());
  }

  public static GetJobResultUseCase_Factory create(
      Provider<HardwareRepository> hardwareRepositoryProvider) {
    return new GetJobResultUseCase_Factory(hardwareRepositoryProvider);
  }

  public static GetJobResultUseCase newInstance(HardwareRepository hardwareRepository) {
    return new GetJobResultUseCase(hardwareRepository);
  }
}
