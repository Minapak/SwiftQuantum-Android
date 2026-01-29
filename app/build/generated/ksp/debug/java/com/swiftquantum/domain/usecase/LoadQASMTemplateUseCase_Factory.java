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
public final class LoadQASMTemplateUseCase_Factory implements Factory<LoadQASMTemplateUseCase> {
  private final Provider<QASMRepository> qasmRepositoryProvider;

  public LoadQASMTemplateUseCase_Factory(Provider<QASMRepository> qasmRepositoryProvider) {
    this.qasmRepositoryProvider = qasmRepositoryProvider;
  }

  @Override
  public LoadQASMTemplateUseCase get() {
    return newInstance(qasmRepositoryProvider.get());
  }

  public static LoadQASMTemplateUseCase_Factory create(
      Provider<QASMRepository> qasmRepositoryProvider) {
    return new LoadQASMTemplateUseCase_Factory(qasmRepositoryProvider);
  }

  public static LoadQASMTemplateUseCase newInstance(QASMRepository qasmRepository) {
    return new LoadQASMTemplateUseCase(qasmRepository);
  }
}
