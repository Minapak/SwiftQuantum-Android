package com.swiftquantum.presentation.viewmodel;

import com.swiftquantum.domain.usecase.GetMaxQubitsUseCase;
import com.swiftquantum.domain.usecase.ObserveUserTierUseCase;
import com.swiftquantum.domain.usecase.RunSimulationUseCase;
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
public final class SimulatorViewModel_Factory implements Factory<SimulatorViewModel> {
  private final Provider<RunSimulationUseCase> runSimulationUseCaseProvider;

  private final Provider<GetMaxQubitsUseCase> getMaxQubitsUseCaseProvider;

  private final Provider<ObserveUserTierUseCase> observeUserTierUseCaseProvider;

  public SimulatorViewModel_Factory(Provider<RunSimulationUseCase> runSimulationUseCaseProvider,
      Provider<GetMaxQubitsUseCase> getMaxQubitsUseCaseProvider,
      Provider<ObserveUserTierUseCase> observeUserTierUseCaseProvider) {
    this.runSimulationUseCaseProvider = runSimulationUseCaseProvider;
    this.getMaxQubitsUseCaseProvider = getMaxQubitsUseCaseProvider;
    this.observeUserTierUseCaseProvider = observeUserTierUseCaseProvider;
  }

  @Override
  public SimulatorViewModel get() {
    return newInstance(runSimulationUseCaseProvider.get(), getMaxQubitsUseCaseProvider.get(), observeUserTierUseCaseProvider.get());
  }

  public static SimulatorViewModel_Factory create(
      Provider<RunSimulationUseCase> runSimulationUseCaseProvider,
      Provider<GetMaxQubitsUseCase> getMaxQubitsUseCaseProvider,
      Provider<ObserveUserTierUseCase> observeUserTierUseCaseProvider) {
    return new SimulatorViewModel_Factory(runSimulationUseCaseProvider, getMaxQubitsUseCaseProvider, observeUserTierUseCaseProvider);
  }

  public static SimulatorViewModel newInstance(RunSimulationUseCase runSimulationUseCase,
      GetMaxQubitsUseCase getMaxQubitsUseCase, ObserveUserTierUseCase observeUserTierUseCase) {
    return new SimulatorViewModel(runSimulationUseCase, getMaxQubitsUseCase, observeUserTierUseCase);
  }
}
