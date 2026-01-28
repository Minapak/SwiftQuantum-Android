package com.swiftquantum.data.repository;

import com.swiftquantum.data.api.QuantumApi;
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
public final class QuantumRepositoryImpl_Factory implements Factory<QuantumRepositoryImpl> {
  private final Provider<QuantumApi> quantumApiProvider;

  public QuantumRepositoryImpl_Factory(Provider<QuantumApi> quantumApiProvider) {
    this.quantumApiProvider = quantumApiProvider;
  }

  @Override
  public QuantumRepositoryImpl get() {
    return newInstance(quantumApiProvider.get());
  }

  public static QuantumRepositoryImpl_Factory create(Provider<QuantumApi> quantumApiProvider) {
    return new QuantumRepositoryImpl_Factory(quantumApiProvider);
  }

  public static QuantumRepositoryImpl newInstance(QuantumApi quantumApi) {
    return new QuantumRepositoryImpl(quantumApi);
  }
}
