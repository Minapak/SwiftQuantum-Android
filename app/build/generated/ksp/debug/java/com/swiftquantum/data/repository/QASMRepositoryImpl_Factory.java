package com.swiftquantum.data.repository;

import com.swiftquantum.data.api.QASMApi;
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
public final class QASMRepositoryImpl_Factory implements Factory<QASMRepositoryImpl> {
  private final Provider<QASMApi> qasmApiProvider;

  public QASMRepositoryImpl_Factory(Provider<QASMApi> qasmApiProvider) {
    this.qasmApiProvider = qasmApiProvider;
  }

  @Override
  public QASMRepositoryImpl get() {
    return newInstance(qasmApiProvider.get());
  }

  public static QASMRepositoryImpl_Factory create(Provider<QASMApi> qasmApiProvider) {
    return new QASMRepositoryImpl_Factory(qasmApiProvider);
  }

  public static QASMRepositoryImpl newInstance(QASMApi qasmApi) {
    return new QASMRepositoryImpl(qasmApi);
  }
}
