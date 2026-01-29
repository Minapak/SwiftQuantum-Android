package com.swiftquantum.presentation.viewmodel;

import com.swiftquantum.domain.usecase.ExecuteWithHybridEngineUseCase;
import com.swiftquantum.domain.usecase.GetBenchmarkHistoryUseCase;
import com.swiftquantum.domain.usecase.GetEngineStatusUseCase;
import com.swiftquantum.domain.usecase.ObserveUserTierUseCase;
import com.swiftquantum.domain.usecase.RunBenchmarkUseCase;
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
public final class BenchmarkViewModel_Factory implements Factory<BenchmarkViewModel> {
  private final Provider<ExecuteWithHybridEngineUseCase> executeWithHybridEngineUseCaseProvider;

  private final Provider<RunBenchmarkUseCase> runBenchmarkUseCaseProvider;

  private final Provider<GetBenchmarkHistoryUseCase> getBenchmarkHistoryUseCaseProvider;

  private final Provider<GetEngineStatusUseCase> getEngineStatusUseCaseProvider;

  private final Provider<ObserveUserTierUseCase> observeUserTierUseCaseProvider;

  public BenchmarkViewModel_Factory(
      Provider<ExecuteWithHybridEngineUseCase> executeWithHybridEngineUseCaseProvider,
      Provider<RunBenchmarkUseCase> runBenchmarkUseCaseProvider,
      Provider<GetBenchmarkHistoryUseCase> getBenchmarkHistoryUseCaseProvider,
      Provider<GetEngineStatusUseCase> getEngineStatusUseCaseProvider,
      Provider<ObserveUserTierUseCase> observeUserTierUseCaseProvider) {
    this.executeWithHybridEngineUseCaseProvider = executeWithHybridEngineUseCaseProvider;
    this.runBenchmarkUseCaseProvider = runBenchmarkUseCaseProvider;
    this.getBenchmarkHistoryUseCaseProvider = getBenchmarkHistoryUseCaseProvider;
    this.getEngineStatusUseCaseProvider = getEngineStatusUseCaseProvider;
    this.observeUserTierUseCaseProvider = observeUserTierUseCaseProvider;
  }

  @Override
  public BenchmarkViewModel get() {
    return newInstance(executeWithHybridEngineUseCaseProvider.get(), runBenchmarkUseCaseProvider.get(), getBenchmarkHistoryUseCaseProvider.get(), getEngineStatusUseCaseProvider.get(), observeUserTierUseCaseProvider.get());
  }

  public static BenchmarkViewModel_Factory create(
      Provider<ExecuteWithHybridEngineUseCase> executeWithHybridEngineUseCaseProvider,
      Provider<RunBenchmarkUseCase> runBenchmarkUseCaseProvider,
      Provider<GetBenchmarkHistoryUseCase> getBenchmarkHistoryUseCaseProvider,
      Provider<GetEngineStatusUseCase> getEngineStatusUseCaseProvider,
      Provider<ObserveUserTierUseCase> observeUserTierUseCaseProvider) {
    return new BenchmarkViewModel_Factory(executeWithHybridEngineUseCaseProvider, runBenchmarkUseCaseProvider, getBenchmarkHistoryUseCaseProvider, getEngineStatusUseCaseProvider, observeUserTierUseCaseProvider);
  }

  public static BenchmarkViewModel newInstance(
      ExecuteWithHybridEngineUseCase executeWithHybridEngineUseCase,
      RunBenchmarkUseCase runBenchmarkUseCase,
      GetBenchmarkHistoryUseCase getBenchmarkHistoryUseCase,
      GetEngineStatusUseCase getEngineStatusUseCase,
      ObserveUserTierUseCase observeUserTierUseCase) {
    return new BenchmarkViewModel(executeWithHybridEngineUseCase, runBenchmarkUseCase, getBenchmarkHistoryUseCase, getEngineStatusUseCase, observeUserTierUseCase);
  }
}
