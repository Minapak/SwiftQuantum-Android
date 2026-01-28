package com.swiftquantum.di;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

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
public final class NetworkModule_ProvideBridgeOkHttpClientFactory implements Factory<OkHttpClient> {
  private final Provider<HttpLoggingInterceptor> loggingInterceptorProvider;

  private final Provider<Interceptor> authInterceptorProvider;

  public NetworkModule_ProvideBridgeOkHttpClientFactory(
      Provider<HttpLoggingInterceptor> loggingInterceptorProvider,
      Provider<Interceptor> authInterceptorProvider) {
    this.loggingInterceptorProvider = loggingInterceptorProvider;
    this.authInterceptorProvider = authInterceptorProvider;
  }

  @Override
  public OkHttpClient get() {
    return provideBridgeOkHttpClient(loggingInterceptorProvider.get(), authInterceptorProvider.get());
  }

  public static NetworkModule_ProvideBridgeOkHttpClientFactory create(
      Provider<HttpLoggingInterceptor> loggingInterceptorProvider,
      Provider<Interceptor> authInterceptorProvider) {
    return new NetworkModule_ProvideBridgeOkHttpClientFactory(loggingInterceptorProvider, authInterceptorProvider);
  }

  public static OkHttpClient provideBridgeOkHttpClient(HttpLoggingInterceptor loggingInterceptor,
      Interceptor authInterceptor) {
    return Preconditions.checkNotNullFromProvides(NetworkModule.INSTANCE.provideBridgeOkHttpClient(loggingInterceptor, authInterceptor));
  }
}
