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
public final class GetAvailableBackendsUseCase_Factory implements Factory<GetAvailableBackendsUseCase> {
  private final Provider<HardwareRepository> hardwareRepositoryProvider;

  public GetAvailableBackendsUseCase_Factory(
      Provider<HardwareRepository> hardwareRepositoryProvider) {
    this.hardwareRepositoryProvider = hardwareRepositoryProvider;
  }

  @Override
  public GetAvailableBackendsUseCase get() {
    return newInstance(hardwareRepositoryProvider.get());
  }

  public static GetAvailableBackendsUseCase_Factory create(
      Provider<HardwareRepository> hardwareRepositoryProvider) {
    return new GetAvailableBackendsUseCase_Factory(hardwareRepositoryProvider);
  }

  public static GetAvailableBackendsUseCase newInstance(HardwareRepository hardwareRepository) {
    return new GetAvailableBackendsUseCase(hardwareRepository);
  }
}
