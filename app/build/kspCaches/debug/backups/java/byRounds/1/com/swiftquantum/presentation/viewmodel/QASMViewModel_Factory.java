package com.swiftquantum.presentation.viewmodel;

import com.swiftquantum.domain.usecase.ExportQASMUseCase;
import com.swiftquantum.domain.usecase.ImportQASMUseCase;
import com.swiftquantum.domain.usecase.LoadQASMTemplatesUseCase;
import com.swiftquantum.domain.usecase.ValidateQASMUseCase;
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
public final class QASMViewModel_Factory implements Factory<QASMViewModel> {
  private final Provider<ImportQASMUseCase> importQASMUseCaseProvider;

  private final Provider<ExportQASMUseCase> exportQASMUseCaseProvider;

  private final Provider<ValidateQASMUseCase> validateQASMUseCaseProvider;

  private final Provider<LoadQASMTemplatesUseCase> loadQASMTemplatesUseCaseProvider;

  public QASMViewModel_Factory(Provider<ImportQASMUseCase> importQASMUseCaseProvider,
      Provider<ExportQASMUseCase> exportQASMUseCaseProvider,
      Provider<ValidateQASMUseCase> validateQASMUseCaseProvider,
      Provider<LoadQASMTemplatesUseCase> loadQASMTemplatesUseCaseProvider) {
    this.importQASMUseCaseProvider = importQASMUseCaseProvider;
    this.exportQASMUseCaseProvider = exportQASMUseCaseProvider;
    this.validateQASMUseCaseProvider = validateQASMUseCaseProvider;
    this.loadQASMTemplatesUseCaseProvider = loadQASMTemplatesUseCaseProvider;
  }

  @Override
  public QASMViewModel get() {
    return newInstance(importQASMUseCaseProvider.get(), exportQASMUseCaseProvider.get(), validateQASMUseCaseProvider.get(), loadQASMTemplatesUseCaseProvider.get());
  }

  public static QASMViewModel_Factory create(Provider<ImportQASMUseCase> importQASMUseCaseProvider,
      Provider<ExportQASMUseCase> exportQASMUseCaseProvider,
      Provider<ValidateQASMUseCase> validateQASMUseCaseProvider,
      Provider<LoadQASMTemplatesUseCase> loadQASMTemplatesUseCaseProvider) {
    return new QASMViewModel_Factory(importQASMUseCaseProvider, exportQASMUseCaseProvider, validateQASMUseCaseProvider, loadQASMTemplatesUseCaseProvider);
  }

  public static QASMViewModel newInstance(ImportQASMUseCase importQASMUseCase,
      ExportQASMUseCase exportQASMUseCase, ValidateQASMUseCase validateQASMUseCase,
      LoadQASMTemplatesUseCase loadQASMTemplatesUseCase) {
    return new QASMViewModel(importQASMUseCase, exportQASMUseCase, validateQASMUseCase, loadQASMTemplatesUseCase);
  }
}
