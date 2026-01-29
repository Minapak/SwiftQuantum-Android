package com.swiftquantum.data.repository;

import com.swiftquantum.data.api.HybridEngineApi;
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
public final class HybridEngineRepositoryImpl_Factory implements Factory<HybridEngineRepositoryImpl> {
  private final Provider<HybridEngineApi> hybridEngineApiProvider;

  public HybridEngineRepositoryImpl_Factory(Provider<HybridEngineApi> hybridEngineApiProvider) {
    this.hybridEngineApiProvider = hybridEngineApiProvider;
  }

  @Override
  public HybridEngineRepositoryImpl get() {
    return newInstance(hybridEngineApiProvider.get());
  }

  public static HybridEngineRepositoryImpl_Factory create(
      Provider<HybridEngineApi> hybridEngineApiProvider) {
    return new HybridEngineRepositoryImpl_Factory(hybridEngineApiProvider);
  }

  public static HybridEngineRepositoryImpl newInstance(HybridEngineApi hybridEngineApi) {
    return new HybridEngineRepositoryImpl(hybridEngineApi);
  }
}
