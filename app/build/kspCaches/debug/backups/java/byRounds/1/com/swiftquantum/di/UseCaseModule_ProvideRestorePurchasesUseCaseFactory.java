package com.swiftquantum.di;

import com.swiftquantum.domain.repository.BillingRepository;
import com.swiftquantum.domain.usecase.RestorePurchasesUseCase;
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
public final class UseCaseModule_ProvideRestorePurchasesUseCaseFactory implements Factory<RestorePurchasesUseCase> {
  private final Provider<BillingRepository> billingRepositoryProvider;

  public UseCaseModule_ProvideRestorePurchasesUseCaseFactory(
      Provider<BillingRepository> billingRepositoryProvider) {
    this.billingRepositoryProvider = billingRepositoryProvider;
  }

  @Override
  public RestorePurchasesUseCase get() {
    return provideRestorePurchasesUseCase(billingRepositoryProvider.get());
  }

  public static UseCaseModule_ProvideRestorePurchasesUseCaseFactory create(
      Provider<BillingRepository> billingRepositoryProvider) {
    return new UseCaseModule_ProvideRestorePurchasesUseCaseFactory(billingRepositoryProvider);
  }

  public static RestorePurchasesUseCase provideRestorePurchasesUseCase(
      BillingRepository billingRepository) {
    return Preconditions.checkNotNullFromProvides(UseCaseModule.INSTANCE.provideRestorePurchasesUseCase(billingRepository));
  }
}
