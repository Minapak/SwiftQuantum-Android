package com.swiftquantum.data.repository;

import com.swiftquantum.data.api.AdminApi;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
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
public final class AdminRepositoryImpl_Factory implements Factory<AdminRepositoryImpl> {
  private final Provider<AdminApi> adminApiProvider;

  public AdminRepositoryImpl_Factory(Provider<AdminApi> adminApiProvider) {
    this.adminApiProvider = adminApiProvider;
  }

  @Override
  public AdminRepositoryImpl get() {
    return newInstance(adminApiProvider.get());
  }

  public static AdminRepositoryImpl_Factory create(Provider<AdminApi> adminApiProvider) {
    return new AdminRepositoryImpl_Factory(adminApiProvider);
  }

  public static AdminRepositoryImpl newInstance(AdminApi adminApi) {
    return new AdminRepositoryImpl(adminApi);
  }
}
