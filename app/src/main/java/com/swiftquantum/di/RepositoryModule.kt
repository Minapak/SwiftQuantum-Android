package com.swiftquantum.di

import com.swiftquantum.data.repository.AdminRepositoryImpl
import com.swiftquantum.data.repository.AuthRepositoryImpl
import com.swiftquantum.data.repository.BillingRepositoryImpl
import com.swiftquantum.data.repository.ExperienceRepositoryImpl
import com.swiftquantum.data.repository.HardwareRepositoryImpl
import com.swiftquantum.data.repository.HybridEngineRepositoryImpl
import com.swiftquantum.data.repository.QASMRepositoryImpl
import com.swiftquantum.data.repository.QuantumRepositoryImpl
import com.swiftquantum.domain.repository.AdminRepository
import com.swiftquantum.domain.repository.AuthRepository
import com.swiftquantum.domain.repository.BillingRepository
import com.swiftquantum.domain.repository.ExperienceRepository
import com.swiftquantum.domain.repository.HardwareRepository
import com.swiftquantum.domain.repository.HybridEngineRepository
import com.swiftquantum.domain.repository.QASMRepository
import com.swiftquantum.domain.repository.QuantumRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        authRepositoryImpl: AuthRepositoryImpl
    ): AuthRepository

    @Binds
    @Singleton
    abstract fun bindQuantumRepository(
        quantumRepositoryImpl: QuantumRepositoryImpl
    ): QuantumRepository

    @Binds
    @Singleton
    abstract fun bindHardwareRepository(
        hardwareRepositoryImpl: HardwareRepositoryImpl
    ): HardwareRepository

    @Binds
    @Singleton
    abstract fun bindBillingRepository(
        billingRepositoryImpl: BillingRepositoryImpl
    ): BillingRepository

    @Binds
    @Singleton
    abstract fun bindQASMRepository(
        qasmRepositoryImpl: QASMRepositoryImpl
    ): QASMRepository

    @Binds
    @Singleton
    abstract fun bindHybridEngineRepository(
        hybridEngineRepositoryImpl: HybridEngineRepositoryImpl
    ): HybridEngineRepository

    @Binds
    @Singleton
    abstract fun bindExperienceRepository(
        experienceRepositoryImpl: ExperienceRepositoryImpl
    ): ExperienceRepository

    @Binds
    @Singleton
    abstract fun bindAdminRepository(
        adminRepositoryImpl: AdminRepositoryImpl
    ): AdminRepository
}
