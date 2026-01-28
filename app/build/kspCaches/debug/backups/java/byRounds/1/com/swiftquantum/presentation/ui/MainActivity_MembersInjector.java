package com.swiftquantum.presentation.ui;

import com.swiftquantum.data.repository.BillingRepositoryImpl;
import dagger.MembersInjector;
import dagger.internal.DaggerGenerated;
import dagger.internal.InjectedFieldSignature;
import dagger.internal.QualifierMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

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
public final class MainActivity_MembersInjector implements MembersInjector<MainActivity> {
  private final Provider<BillingRepositoryImpl> billingRepositoryProvider;

  public MainActivity_MembersInjector(Provider<BillingRepositoryImpl> billingRepositoryProvider) {
    this.billingRepositoryProvider = billingRepositoryProvider;
  }

  public static MembersInjector<MainActivity> create(
      Provider<BillingRepositoryImpl> billingRepositoryProvider) {
    return new MainActivity_MembersInjector(billingRepositoryProvider);
  }

  @Override
  public void injectMembers(MainActivity instance) {
    injectBillingRepository(instance, billingRepositoryProvider.get());
  }

  @InjectedFieldSignature("com.swiftquantum.presentation.ui.MainActivity.billingRepository")
  public static void injectBillingRepository(MainActivity instance,
      BillingRepositoryImpl billingRepository) {
    instance.billingRepository = billingRepository;
  }
}
