package com.swiftquantum.presentation.viewmodel;

import com.swiftquantum.domain.usecase.CancelJobUseCase;
import com.swiftquantum.domain.usecase.ConnectToIBMQuantumUseCase;
import com.swiftquantum.domain.usecase.DisconnectFromIBMQuantumUseCase;
import com.swiftquantum.domain.usecase.GetActiveJobsUseCase;
import com.swiftquantum.domain.usecase.GetAvailableBackendsUseCase;
import com.swiftquantum.domain.usecase.GetJobResultUseCase;
import com.swiftquantum.domain.usecase.ObserveIBMConnectionUseCase;
import com.swiftquantum.domain.usecase.ObserveJobStatusUseCase;
import com.swiftquantum.domain.usecase.ObserveUserTierUseCase;
import com.swiftquantum.domain.usecase.SubmitHardwareJobUseCase;
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
public final class HardwareViewModel_Factory implements Factory<HardwareViewModel> {
  private final Provider<ConnectToIBMQuantumUseCase> connectToIBMQuantumUseCaseProvider;

  private final Provider<DisconnectFromIBMQuantumUseCase> disconnectFromIBMQuantumUseCaseProvider;

  private final Provider<GetAvailableBackendsUseCase> getAvailableBackendsUseCaseProvider;

  private final Provider<SubmitHardwareJobUseCase> submitHardwareJobUseCaseProvider;

  private final Provider<CancelJobUseCase> cancelJobUseCaseProvider;

  private final Provider<GetJobResultUseCase> getJobResultUseCaseProvider;

  private final Provider<GetActiveJobsUseCase> getActiveJobsUseCaseProvider;

  private final Provider<ObserveIBMConnectionUseCase> observeIBMConnectionUseCaseProvider;

  private final Provider<ObserveJobStatusUseCase> observeJobStatusUseCaseProvider;

  private final Provider<ObserveUserTierUseCase> observeUserTierUseCaseProvider;

  public HardwareViewModel_Factory(
      Provider<ConnectToIBMQuantumUseCase> connectToIBMQuantumUseCaseProvider,
      Provider<DisconnectFromIBMQuantumUseCase> disconnectFromIBMQuantumUseCaseProvider,
      Provider<GetAvailableBackendsUseCase> getAvailableBackendsUseCaseProvider,
      Provider<SubmitHardwareJobUseCase> submitHardwareJobUseCaseProvider,
      Provider<CancelJobUseCase> cancelJobUseCaseProvider,
      Provider<GetJobResultUseCase> getJobResultUseCaseProvider,
      Provider<GetActiveJobsUseCase> getActiveJobsUseCaseProvider,
      Provider<ObserveIBMConnectionUseCase> observeIBMConnectionUseCaseProvider,
      Provider<ObserveJobStatusUseCase> observeJobStatusUseCaseProvider,
      Provider<ObserveUserTierUseCase> observeUserTierUseCaseProvider) {
    this.connectToIBMQuantumUseCaseProvider = connectToIBMQuantumUseCaseProvider;
    this.disconnectFromIBMQuantumUseCaseProvider = disconnectFromIBMQuantumUseCaseProvider;
    this.getAvailableBackendsUseCaseProvider = getAvailableBackendsUseCaseProvider;
    this.submitHardwareJobUseCaseProvider = submitHardwareJobUseCaseProvider;
    this.cancelJobUseCaseProvider = cancelJobUseCaseProvider;
    this.getJobResultUseCaseProvider = getJobResultUseCaseProvider;
    this.getActiveJobsUseCaseProvider = getActiveJobsUseCaseProvider;
    this.observeIBMConnectionUseCaseProvider = observeIBMConnectionUseCaseProvider;
    this.observeJobStatusUseCaseProvider = observeJobStatusUseCaseProvider;
    this.observeUserTierUseCaseProvider = observeUserTierUseCaseProvider;
  }

  @Override
  public HardwareViewModel get() {
    return newInstance(connectToIBMQuantumUseCaseProvider.get(), disconnectFromIBMQuantumUseCaseProvider.get(), getAvailableBackendsUseCaseProvider.get(), submitHardwareJobUseCaseProvider.get(), cancelJobUseCaseProvider.get(), getJobResultUseCaseProvider.get(), getActiveJobsUseCaseProvider.get(), observeIBMConnectionUseCaseProvider.get(), observeJobStatusUseCaseProvider.get(), observeUserTierUseCaseProvider.get());
  }

  public static HardwareViewModel_Factory create(
      Provider<ConnectToIBMQuantumUseCase> connectToIBMQuantumUseCaseProvider,
      Provider<DisconnectFromIBMQuantumUseCase> disconnectFromIBMQuantumUseCaseProvider,
      Provider<GetAvailableBackendsUseCase> getAvailableBackendsUseCaseProvider,
      Provider<SubmitHardwareJobUseCase> submitHardwareJobUseCaseProvider,
      Provider<CancelJobUseCase> cancelJobUseCaseProvider,
      Provider<GetJobResultUseCase> getJobResultUseCaseProvider,
      Provider<GetActiveJobsUseCase> getActiveJobsUseCaseProvider,
      Provider<ObserveIBMConnectionUseCase> observeIBMConnectionUseCaseProvider,
      Provider<ObserveJobStatusUseCase> observeJobStatusUseCaseProvider,
      Provider<ObserveUserTierUseCase> observeUserTierUseCaseProvider) {
    return new HardwareViewModel_Factory(connectToIBMQuantumUseCaseProvider, disconnectFromIBMQuantumUseCaseProvider, getAvailableBackendsUseCaseProvider, submitHardwareJobUseCaseProvider, cancelJobUseCaseProvider, getJobResultUseCaseProvider, getActiveJobsUseCaseProvider, observeIBMConnectionUseCaseProvider, observeJobStatusUseCaseProvider, observeUserTierUseCaseProvider);
  }

  public static HardwareViewModel newInstance(ConnectToIBMQuantumUseCase connectToIBMQuantumUseCase,
      DisconnectFromIBMQuantumUseCase disconnectFromIBMQuantumUseCase,
      GetAvailableBackendsUseCase getAvailableBackendsUseCase,
      SubmitHardwareJobUseCase submitHardwareJobUseCase, CancelJobUseCase cancelJobUseCase,
      GetJobResultUseCase getJobResultUseCase, GetActiveJobsUseCase getActiveJobsUseCase,
      ObserveIBMConnectionUseCase observeIBMConnectionUseCase,
      ObserveJobStatusUseCase observeJobStatusUseCase,
      ObserveUserTierUseCase observeUserTierUseCase) {
    return new HardwareViewModel(connectToIBMQuantumUseCase, disconnectFromIBMQuantumUseCase, getAvailableBackendsUseCase, submitHardwareJobUseCase, cancelJobUseCase, getJobResultUseCase, getActiveJobsUseCase, observeIBMConnectionUseCase, observeJobStatusUseCase, observeUserTierUseCase);
  }
}
