package com.swiftquantum.di;

import com.swiftquantum.domain.repository.QASMRepository;
import com.swiftquantum.domain.usecase.ExportQASMUseCase;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("dagger.hilt.android.scopes.ViewModelScoped")
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
public final class UseCaseModule_ProvideExportQASMUseCaseFactory implements Factory<ExportQASMUseCase> {
  private final Provider<QASMRepository> qasmRepositoryProvider;

  public UseCaseModule_ProvideExportQASMUseCaseFactory(
      Provider<QASMRepository> qasmRepositoryProvider) {
    this.qasmRepositoryProvider = qasmRepositoryProvider;
  }

  @Override
  public ExportQASMUseCase get() {
    return provideExportQASMUseCase(qasmRepositoryProvider.get());
  }

  public static UseCaseModule_ProvideExportQASMUseCaseFactory create(
      Provider<QASMRepository> qasmRepositoryProvider) {
    return new UseCaseModule_ProvideExportQASMUseCaseFactory(qasmRepositoryProvider);
  }

  public static ExportQASMUseCase provideExportQASMUseCase(QASMRepository qasmRepository) {
    return Preconditions.checkNotNullFromProvides(UseCaseModule.INSTANCE.provideExportQASMUseCase(qasmRepository));
  }
}
