package com.swiftquantum.data.repository;

import com.swiftquantum.data.api.BridgeApi;
import com.swiftquantum.data.local.TokenManager;
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
public final class HardwareRepositoryImpl_Factory implements Factory<HardwareRepositoryImpl> {
  private final Provider<BridgeApi> bridgeApiProvider;

  private final Provider<TokenManager> tokenManagerProvider;

  public HardwareRepositoryImpl_Factory(Provider<BridgeApi> bridgeApiProvider,
      Provider<TokenManager> tokenManagerProvider) {
    this.bridgeApiProvider = bridgeApiProvider;
    this.tokenManagerProvider = tokenManagerProvider;
  }

  @Override
  public HardwareRepositoryImpl get() {
    return newInstance(bridgeApiProvider.get(), tokenManagerProvider.get());
  }

  public static HardwareRepositoryImpl_Factory create(Provider<BridgeApi> bridgeApiProvider,
      Provider<TokenManager> tokenManagerProvider) {
    return new HardwareRepositoryImpl_Factory(bridgeApiProvider, tokenManagerProvider);
  }

  public static HardwareRepositoryImpl newInstance(BridgeApi bridgeApi, TokenManager tokenManager) {
    return new HardwareRepositoryImpl(bridgeApi, tokenManager);
  }
}
