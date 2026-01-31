package com.swiftquantum.presentation.viewmodel;

import com.swiftquantum.domain.repository.AdminRepository;
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
public final class AdminViewModel_Factory implements Factory<AdminViewModel> {
  private final Provider<AdminRepository> adminRepositoryProvider;

  public AdminViewModel_Factory(Provider<AdminRepository> adminRepositoryProvider) {
    this.adminRepositoryProvider = adminRepositoryProvider;
  }

  @Override
  public AdminViewModel get() {
    return newInstance(adminRepositoryProvider.get());
  }

  public static AdminViewModel_Factory create(Provider<AdminRepository> adminRepositoryProvider) {
    return new AdminViewModel_Factory(adminRepositoryProvider);
  }

  public static AdminViewModel newInstance(AdminRepository adminRepository) {
    return new AdminViewModel(adminRepository);
  }
}
