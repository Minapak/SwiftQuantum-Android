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
public final class ImportQASMUseCase_Factory implements Factory<ImportQASMUseCase> {
  private final Provider<QASMRepository> qasmRepositoryProvider;

  public ImportQASMUseCase_Factory(Provider<QASMRepository> qasmRepositoryProvider) {
    this.qasmRepositoryProvider = qasmRepositoryProvider;
  }

  @Override
  public ImportQASMUseCase get() {
    return newInstance(qasmRepositoryProvider.get());
  }

  public static ImportQASMUseCase_Factory create(Provider<QASMRepository> qasmRepositoryProvider) {
    return new ImportQASMUseCase_Factory(qasmRepositoryProvider);
  }

  public static ImportQASMUseCase newInstance(QASMRepository qasmRepository) {
    return new ImportQASMUseCase(qasmRepository);
  }
}
