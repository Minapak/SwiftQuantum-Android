package com.swiftquantum.di;

import com.swiftquantum.data.api.BridgeApi;
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
public final class NetworkModule_ProvideBridgeApiFactory implements Factory<BridgeApi> {
  private final Provider<Retrofit> retrofitProvider;

  public NetworkModule_ProvideBridgeApiFactory(Provider<Retrofit> retrofitProvider) {
    this.retrofitProvider = retrofitProvider;
  }

  @Override
  public BridgeApi get() {
    return provideBridgeApi(retrofitProvider.get());
  }

  public static NetworkModule_ProvideBridgeApiFactory create(Provider<Retrofit> retrofitProvider) {
    return new NetworkModule_ProvideBridgeApiFactory(retrofitProvider);
  }

  public static BridgeApi provideBridgeApi(Retrofit retrofit) {
    return Preconditions.checkNotNullFromProvides(NetworkModule.INSTANCE.provideBridgeApi(retrofit));
  }
}
