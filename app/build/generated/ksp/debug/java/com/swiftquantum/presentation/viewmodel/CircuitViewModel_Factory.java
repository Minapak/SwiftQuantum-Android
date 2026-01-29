package com.swiftquantum.presentation.viewmodel;

import com.swiftquantum.domain.usecase.DeleteCircuitUseCase;
import com.swiftquantum.domain.usecase.GetCircuitUseCase;
import com.swiftquantum.domain.usecase.GetMaxQubitsUseCase;
import com.swiftquantum.domain.usecase.GetMyCircuitsUseCase;
import com.swiftquantum.domain.usecase.SaveCircuitUseCase;
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
public final class CircuitViewModel_Factory implements Factory<CircuitViewModel> {
  private final Provider<SaveCircuitUseCase> saveCircuitUseCaseProvider;

  private final Provider<GetCircuitUseCase> getCircuitUseCaseProvider;

  private final Provider<GetMyCircuitsUseCase> getMyCircuitsUseCaseProvider;

  private final Provider<DeleteCircuitUseCase> deleteCircuitUseCaseProvider;

  private final Provider<GetMaxQubitsUseCase> getMaxQubitsUseCaseProvider;

  public CircuitViewModel_Factory(Provider<SaveCircuitUseCase> saveCircuitUseCaseProvider,
      Provider<GetCircuitUseCase> getCircuitUseCaseProvider,
      Provider<GetMyCircuitsUseCase> getMyCircuitsUseCaseProvider,
      Provider<DeleteCircuitUseCase> deleteCircuitUseCaseProvider,
      Provider<GetMaxQubitsUseCase> getMaxQubitsUseCaseProvider) {
    this.saveCircuitUseCaseProvider = saveCircuitUseCaseProvider;
    this.getCircuitUseCaseProvider = getCircuitUseCaseProvider;
    this.getMyCircuitsUseCaseProvider = getMyCircuitsUseCaseProvider;
    this.deleteCircuitUseCaseProvider = deleteCircuitUseCaseProvider;
    this.getMaxQubitsUseCaseProvider = getMaxQubitsUseCaseProvider;
  }

  @Override
  public CircuitViewModel get() {
    return newInstance(saveCircuitUseCaseProvider.get(), getCircuitUseCaseProvider.get(), getMyCircuitsUseCaseProvider.get(), deleteCircuitUseCaseProvider.get(), getMaxQubitsUseCaseProvider.get());
  }

  public static CircuitViewModel_Factory create(
      Provider<SaveCircuitUseCase> saveCircuitUseCaseProvider,
      Provider<GetCircuitUseCase> getCircuitUseCaseProvider,
      Provider<GetMyCircuitsUseCase> getMyCircuitsUseCaseProvider,
      Provider<DeleteCircuitUseCase> deleteCircuitUseCaseProvider,
      Provider<GetMaxQubitsUseCase> getMaxQubitsUseCaseProvider) {
    return new CircuitViewModel_Factory(saveCircuitUseCaseProvider, getCircuitUseCaseProvider, getMyCircuitsUseCaseProvider, deleteCircuitUseCaseProvider, getMaxQubitsUseCaseProvider);
  }

  public static CircuitViewModel newInstance(SaveCircuitUseCase saveCircuitUseCase,
      GetCircuitUseCase getCircuitUseCase, GetMyCircuitsUseCase getMyCircuitsUseCase,
      DeleteCircuitUseCase deleteCircuitUseCase, GetMaxQubitsUseCase getMaxQubitsUseCase) {
    return new CircuitViewModel(saveCircuitUseCase, getCircuitUseCase, getMyCircuitsUseCase, deleteCircuitUseCase, getMaxQubitsUseCase);
  }
}
