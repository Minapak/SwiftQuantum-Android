package com.swiftquantum.presentation.viewmodel;

import com.swiftquantum.domain.repository.ExperienceRepository;
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
public final class ExperienceViewModel_Factory implements Factory<ExperienceViewModel> {
  private final Provider<ExperienceRepository> experienceRepositoryProvider;

  public ExperienceViewModel_Factory(Provider<ExperienceRepository> experienceRepositoryProvider) {
    this.experienceRepositoryProvider = experienceRepositoryProvider;
  }

  @Override
  public ExperienceViewModel get() {
    return newInstance(experienceRepositoryProvider.get());
  }

  public static ExperienceViewModel_Factory create(
      Provider<ExperienceRepository> experienceRepositoryProvider) {
    return new ExperienceViewModel_Factory(experienceRepositoryProvider);
  }

  public static ExperienceViewModel newInstance(ExperienceRepository experienceRepository) {
    return new ExperienceViewModel(experienceRepository);
  }
}
