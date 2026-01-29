package com.swiftquantum.di;

import com.swiftquantum.data.api.HybridEngineApi;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;
import retrofit2.Retrofit;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata("javax.inject.Named")
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
public final class NetworkModule_ProvideHybridEngineApiFactory implements Factory<HybridEngineApi> {
  private final Provider<Retrofit> retrofitProvider;

  public NetworkModule_ProvideHybridEngineApiFactory(Provider<Retrofit> retrofitProvider) {
    this.retrofitProvider = retrofitProvider;
  }

  @Override
  public HybridEngineApi get() {
    return provideHybridEngineApi(retrofitProvider.get());
  }

  public static NetworkModule_ProvideHybridEngineApiFactory create(
      Provider<Retrofit> retrofitProvider) {
    return new NetworkModule_ProvideHybridEngineApiFactory(retrofitProvider);
  }

  public static HybridEngineApi provideHybridEngineApi(Retrofit retrofit) {
    return Preconditions.checkNotNullFromProvides(NetworkModule.INSTANCE.provideHybridEngineApi(retrofit));
  }
}
