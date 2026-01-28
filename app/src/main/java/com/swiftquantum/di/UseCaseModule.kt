package com.swiftquantum.di

import com.swiftquantum.domain.repository.AuthRepository
import com.swiftquantum.domain.repository.BillingRepository
import com.swiftquantum.domain.repository.HardwareRepository
import com.swiftquantum.domain.repository.HybridEngineRepository
import com.swiftquantum.domain.repository.QASMRepository
import com.swiftquantum.domain.repository.QuantumRepository
import com.swiftquantum.domain.usecase.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object UseCaseModule {

    // Auth Use Cases
    @Provides
    @ViewModelScoped
    fun provideLoginUseCase(authRepository: AuthRepository): LoginUseCase {
        return LoginUseCase(authRepository)
    }

    @Provides
    @ViewModelScoped
    fun provideRegisterUseCase(authRepository: AuthRepository): RegisterUseCase {
        return RegisterUseCase(authRepository)
    }

    @Provides
    @ViewModelScoped
    fun provideLogoutUseCase(authRepository: AuthRepository): LogoutUseCase {
        return LogoutUseCase(authRepository)
    }

    @Provides
    @ViewModelScoped
    fun provideGetCurrentUserUseCase(authRepository: AuthRepository): GetCurrentUserUseCase {
        return GetCurrentUserUseCase(authRepository)
    }

    @Provides
    @ViewModelScoped
    fun provideForgotPasswordUseCase(authRepository: AuthRepository): ForgotPasswordUseCase {
        return ForgotPasswordUseCase(authRepository)
    }

    @Provides
    @ViewModelScoped
    fun provideUpdateProfileUseCase(authRepository: AuthRepository): UpdateProfileUseCase {
        return UpdateProfileUseCase(authRepository)
    }

    // Quantum Use Cases
    @Provides
    @ViewModelScoped
    fun provideRunSimulationUseCase(
        quantumRepository: QuantumRepository,
        billingRepository: BillingRepository
    ): RunSimulationUseCase {
        return RunSimulationUseCase(quantumRepository, billingRepository)
    }

    @Provides
    @ViewModelScoped
    fun provideSaveCircuitUseCase(quantumRepository: QuantumRepository): SaveCircuitUseCase {
        return SaveCircuitUseCase(quantumRepository)
    }

    @Provides
    @ViewModelScoped
    fun provideGetMyCircuitsUseCase(quantumRepository: QuantumRepository): GetMyCircuitsUseCase {
        return GetMyCircuitsUseCase(quantumRepository)
    }

    @Provides
    @ViewModelScoped
    fun provideGetCircuitUseCase(quantumRepository: QuantumRepository): GetCircuitUseCase {
        return GetCircuitUseCase(quantumRepository)
    }

    @Provides
    @ViewModelScoped
    fun provideDeleteCircuitUseCase(quantumRepository: QuantumRepository): DeleteCircuitUseCase {
        return DeleteCircuitUseCase(quantumRepository)
    }

    @Provides
    @ViewModelScoped
    fun provideGetExecutionHistoryUseCase(quantumRepository: QuantumRepository): GetExecutionHistoryUseCase {
        return GetExecutionHistoryUseCase(quantumRepository)
    }

    @Provides
    @ViewModelScoped
    fun provideGetMaxQubitsUseCase(billingRepository: BillingRepository): GetMaxQubitsUseCase {
        return GetMaxQubitsUseCase(billingRepository)
    }

    // Hardware Use Cases
    @Provides
    @ViewModelScoped
    fun provideConnectToIBMQuantumUseCase(
        hardwareRepository: HardwareRepository,
        billingRepository: BillingRepository
    ): ConnectToIBMQuantumUseCase {
        return ConnectToIBMQuantumUseCase(hardwareRepository, billingRepository)
    }

    @Provides
    @ViewModelScoped
    fun provideDisconnectFromIBMQuantumUseCase(
        hardwareRepository: HardwareRepository
    ): DisconnectFromIBMQuantumUseCase {
        return DisconnectFromIBMQuantumUseCase(hardwareRepository)
    }

    @Provides
    @ViewModelScoped
    fun provideGetAvailableBackendsUseCase(
        hardwareRepository: HardwareRepository
    ): GetAvailableBackendsUseCase {
        return GetAvailableBackendsUseCase(hardwareRepository)
    }

    @Provides
    @ViewModelScoped
    fun provideSubmitHardwareJobUseCase(
        hardwareRepository: HardwareRepository,
        billingRepository: BillingRepository
    ): SubmitHardwareJobUseCase {
        return SubmitHardwareJobUseCase(hardwareRepository, billingRepository)
    }

    @Provides
    @ViewModelScoped
    fun provideGetJobStatusUseCase(hardwareRepository: HardwareRepository): GetJobStatusUseCase {
        return GetJobStatusUseCase(hardwareRepository)
    }

    @Provides
    @ViewModelScoped
    fun provideCancelJobUseCase(hardwareRepository: HardwareRepository): CancelJobUseCase {
        return CancelJobUseCase(hardwareRepository)
    }

    @Provides
    @ViewModelScoped
    fun provideGetJobResultUseCase(hardwareRepository: HardwareRepository): GetJobResultUseCase {
        return GetJobResultUseCase(hardwareRepository)
    }

    @Provides
    @ViewModelScoped
    fun provideObserveIBMConnectionUseCase(
        hardwareRepository: HardwareRepository
    ): ObserveIBMConnectionUseCase {
        return ObserveIBMConnectionUseCase(hardwareRepository)
    }

    @Provides
    @ViewModelScoped
    fun provideObserveJobStatusUseCase(hardwareRepository: HardwareRepository): ObserveJobStatusUseCase {
        return ObserveJobStatusUseCase(hardwareRepository)
    }

    @Provides
    @ViewModelScoped
    fun provideGetActiveJobsUseCase(hardwareRepository: HardwareRepository): GetActiveJobsUseCase {
        return GetActiveJobsUseCase(hardwareRepository)
    }

    // Billing Use Cases
    @Provides
    @ViewModelScoped
    fun provideGetSubscriptionProductsUseCase(
        billingRepository: BillingRepository
    ): GetSubscriptionProductsUseCase {
        return GetSubscriptionProductsUseCase(billingRepository)
    }

    @Provides
    @ViewModelScoped
    fun providePurchaseSubscriptionUseCase(
        billingRepository: BillingRepository
    ): PurchaseSubscriptionUseCase {
        return PurchaseSubscriptionUseCase(billingRepository)
    }

    @Provides
    @ViewModelScoped
    fun provideRestorePurchasesUseCase(billingRepository: BillingRepository): RestorePurchasesUseCase {
        return RestorePurchasesUseCase(billingRepository)
    }

    @Provides
    @ViewModelScoped
    fun provideGetCurrentSubscriptionUseCase(
        billingRepository: BillingRepository
    ): GetCurrentSubscriptionUseCase {
        return GetCurrentSubscriptionUseCase(billingRepository)
    }

    @Provides
    @ViewModelScoped
    fun provideCancelSubscriptionUseCase(billingRepository: BillingRepository): CancelSubscriptionUseCase {
        return CancelSubscriptionUseCase(billingRepository)
    }

    @Provides
    @ViewModelScoped
    fun provideObserveUserTierUseCase(billingRepository: BillingRepository): ObserveUserTierUseCase {
        return ObserveUserTierUseCase(billingRepository)
    }

    @Provides
    @ViewModelScoped
    fun provideObserveSubscriptionUseCase(
        billingRepository: BillingRepository
    ): ObserveSubscriptionUseCase {
        return ObserveSubscriptionUseCase(billingRepository)
    }

    // QASM Use Cases
    @Provides
    @ViewModelScoped
    fun provideImportQASMUseCase(qasmRepository: QASMRepository): ImportQASMUseCase {
        return ImportQASMUseCase(qasmRepository)
    }

    @Provides
    @ViewModelScoped
    fun provideExportQASMUseCase(qasmRepository: QASMRepository): ExportQASMUseCase {
        return ExportQASMUseCase(qasmRepository)
    }

    @Provides
    @ViewModelScoped
    fun provideValidateQASMUseCase(qasmRepository: QASMRepository): ValidateQASMUseCase {
        return ValidateQASMUseCase(qasmRepository)
    }

    @Provides
    @ViewModelScoped
    fun provideLoadQASMTemplatesUseCase(qasmRepository: QASMRepository): LoadQASMTemplatesUseCase {
        return LoadQASMTemplatesUseCase(qasmRepository)
    }

    @Provides
    @ViewModelScoped
    fun provideLoadQASMTemplateUseCase(qasmRepository: QASMRepository): LoadQASMTemplateUseCase {
        return LoadQASMTemplateUseCase(qasmRepository)
    }

    // Hybrid Engine Use Cases
    @Provides
    @ViewModelScoped
    fun provideExecuteWithHybridEngineUseCase(
        hybridEngineRepository: HybridEngineRepository,
        billingRepository: BillingRepository
    ): ExecuteWithHybridEngineUseCase {
        return ExecuteWithHybridEngineUseCase(hybridEngineRepository, billingRepository)
    }

    @Provides
    @ViewModelScoped
    fun provideRunBenchmarkUseCase(
        hybridEngineRepository: HybridEngineRepository,
        billingRepository: BillingRepository
    ): RunBenchmarkUseCase {
        return RunBenchmarkUseCase(hybridEngineRepository, billingRepository)
    }

    @Provides
    @ViewModelScoped
    fun provideGetBenchmarkHistoryUseCase(
        hybridEngineRepository: HybridEngineRepository
    ): GetBenchmarkHistoryUseCase {
        return GetBenchmarkHistoryUseCase(hybridEngineRepository)
    }

    @Provides
    @ViewModelScoped
    fun provideGetEngineStatusUseCase(
        hybridEngineRepository: HybridEngineRepository
    ): GetEngineStatusUseCase {
        return GetEngineStatusUseCase(hybridEngineRepository)
    }
}
