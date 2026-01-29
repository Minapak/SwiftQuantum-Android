package com.swiftquantum.domain.usecase;

import com.swiftquantum.domain.repository.QASMRepository;
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
public final class ExportQASMUseCase_Factory implements Factory<ExportQASMUseCase> {
  private final Provider<QASMRepository> qasmRepositoryProvider;

  public ExportQASMUseCase_Factory(Provider<QASMRepository> qasmRepositoryProvider) {
    this.qasmRepositoryProvider = qasmRepositoryProvider;
  }

  @Override
  public ExportQASMUseCase get() {
    return newInstance(qasmRepositoryProvider.get());
  }

  public static ExportQASMUseCase_Factory create(Provider<QASMRepository> qasmRepositoryProvider) {
    return new ExportQASMUseCase_Factory(qasmRepositoryProvider);
  }

  public static ExportQASMUseCase newInstance(QASMRepository qasmRepository) {
    return new ExportQASMUseCase(qasmRepository);
  }
}
