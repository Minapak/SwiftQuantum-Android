package com.swiftquantum.data.repository;

import com.swiftquantum.data.api.ExperienceApi;
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
public final class ExperienceRepositoryImpl_Factory implements Factory<ExperienceRepositoryImpl> {
  private final Provider<ExperienceApi> experienceApiProvider;

  public ExperienceRepositoryImpl_Factory(Provider<ExperienceApi> experienceApiProvider) {
    this.experienceApiProvider = experienceApiProvider;
  }

  @Override
  public ExperienceRepositoryImpl get() {
    return newInstance(experienceApiProvider.get());
  }

  public static ExperienceRepositoryImpl_Factory create(
      Provider<ExperienceApi> experienceApiProvider) {
    return new ExperienceRepositoryImpl_Factory(experienceApiProvider);
  }

  public static ExperienceRepositoryImpl newInstance(ExperienceApi experienceApi) {
    return new ExperienceRepositoryImpl(experienceApi);
  }
}
