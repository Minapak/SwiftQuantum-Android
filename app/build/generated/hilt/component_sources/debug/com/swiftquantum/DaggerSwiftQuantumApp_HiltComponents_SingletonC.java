package com.swiftquantum;

import android.app.Activity;
import android.app.Service;
import android.view.View;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;
import com.swiftquantum.data.api.AuthApi;
import com.swiftquantum.data.api.BridgeApi;
import com.swiftquantum.data.api.ExperienceApi;
import com.swiftquantum.data.api.HybridEngineApi;
import com.swiftquantum.data.api.QASMApi;
import com.swiftquantum.data.api.QuantumApi;
import com.swiftquantum.data.auth.SharedAuthManager;
import com.swiftquantum.data.local.TokenManager;
import com.swiftquantum.data.local.UserPreferences;
import com.swiftquantum.data.repository.AuthRepositoryImpl;
import com.swiftquantum.data.repository.BillingRepositoryImpl;
import com.swiftquantum.data.repository.ExperienceRepositoryImpl;
import com.swiftquantum.data.repository.HardwareRepositoryImpl;
import com.swiftquantum.data.repository.HybridEngineRepositoryImpl;
import com.swiftquantum.data.repository.QASMRepositoryImpl;
import com.swiftquantum.data.repository.QuantumRepositoryImpl;
import com.swiftquantum.di.NetworkModule_ProvideApiOkHttpClientFactory;
import com.swiftquantum.di.NetworkModule_ProvideApiRetrofitFactory;
import com.swiftquantum.di.NetworkModule_ProvideAuthApiFactory;
import com.swiftquantum.di.NetworkModule_ProvideAuthInterceptorFactory;
import com.swiftquantum.di.NetworkModule_ProvideBridgeApiFactory;
import com.swiftquantum.di.NetworkModule_ProvideBridgeOkHttpClientFactory;
import com.swiftquantum.di.NetworkModule_ProvideBridgeRetrofitFactory;
import com.swiftquantum.di.NetworkModule_ProvideExperienceApiFactory;
import com.swiftquantum.di.NetworkModule_ProvideHybridEngineApiFactory;
import com.swiftquantum.di.NetworkModule_ProvideJsonFactory;
import com.swiftquantum.di.NetworkModule_ProvideLoggingInterceptorFactory;
import com.swiftquantum.di.NetworkModule_ProvideQASMApiFactory;
import com.swiftquantum.di.NetworkModule_ProvideQuantumApiFactory;
import com.swiftquantum.di.UseCaseModule_ProvideCancelJobUseCaseFactory;
import com.swiftquantum.di.UseCaseModule_ProvideConnectToIBMQuantumUseCaseFactory;
import com.swiftquantum.di.UseCaseModule_ProvideDeleteCircuitUseCaseFactory;
import com.swiftquantum.di.UseCaseModule_ProvideDisconnectFromIBMQuantumUseCaseFactory;
import com.swiftquantum.di.UseCaseModule_ProvideExecuteWithHybridEngineUseCaseFactory;
import com.swiftquantum.di.UseCaseModule_ProvideExportQASMUseCaseFactory;
import com.swiftquantum.di.UseCaseModule_ProvideForgotPasswordUseCaseFactory;
import com.swiftquantum.di.UseCaseModule_ProvideGetActiveJobsUseCaseFactory;
import com.swiftquantum.di.UseCaseModule_ProvideGetAvailableBackendsUseCaseFactory;
import com.swiftquantum.di.UseCaseModule_ProvideGetBenchmarkHistoryUseCaseFactory;
import com.swiftquantum.di.UseCaseModule_ProvideGetCircuitUseCaseFactory;
import com.swiftquantum.di.UseCaseModule_ProvideGetCurrentSubscriptionUseCaseFactory;
import com.swiftquantum.di.UseCaseModule_ProvideGetCurrentUserUseCaseFactory;
import com.swiftquantum.di.UseCaseModule_ProvideGetEngineStatusUseCaseFactory;
import com.swiftquantum.di.UseCaseModule_ProvideGetJobResultUseCaseFactory;
import com.swiftquantum.di.UseCaseModule_ProvideGetMaxQubitsUseCaseFactory;
import com.swiftquantum.di.UseCaseModule_ProvideGetMyCircuitsUseCaseFactory;
import com.swiftquantum.di.UseCaseModule_ProvideGetSubscriptionProductsUseCaseFactory;
import com.swiftquantum.di.UseCaseModule_ProvideImportQASMUseCaseFactory;
import com.swiftquantum.di.UseCaseModule_ProvideLoadQASMTemplatesUseCaseFactory;
import com.swiftquantum.di.UseCaseModule_ProvideLoginUseCaseFactory;
import com.swiftquantum.di.UseCaseModule_ProvideLogoutUseCaseFactory;
import com.swiftquantum.di.UseCaseModule_ProvideObserveIBMConnectionUseCaseFactory;
import com.swiftquantum.di.UseCaseModule_ProvideObserveJobStatusUseCaseFactory;
import com.swiftquantum.di.UseCaseModule_ProvideObserveSubscriptionUseCaseFactory;
import com.swiftquantum.di.UseCaseModule_ProvideObserveUserTierUseCaseFactory;
import com.swiftquantum.di.UseCaseModule_ProvidePurchaseSubscriptionUseCaseFactory;
import com.swiftquantum.di.UseCaseModule_ProvideRegisterUseCaseFactory;
import com.swiftquantum.di.UseCaseModule_ProvideRestorePurchasesUseCaseFactory;
import com.swiftquantum.di.UseCaseModule_ProvideRunBenchmarkUseCaseFactory;
import com.swiftquantum.di.UseCaseModule_ProvideRunSimulationUseCaseFactory;
import com.swiftquantum.di.UseCaseModule_ProvideSaveCircuitUseCaseFactory;
import com.swiftquantum.di.UseCaseModule_ProvideSubmitHardwareJobUseCaseFactory;
import com.swiftquantum.di.UseCaseModule_ProvideUpdateProfileUseCaseFactory;
import com.swiftquantum.di.UseCaseModule_ProvideValidateQASMUseCaseFactory;
import com.swiftquantum.domain.usecase.CancelJobUseCase;
import com.swiftquantum.domain.usecase.ConnectToIBMQuantumUseCase;
import com.swiftquantum.domain.usecase.DeleteCircuitUseCase;
import com.swiftquantum.domain.usecase.DisconnectFromIBMQuantumUseCase;
import com.swiftquantum.domain.usecase.ExecuteWithHybridEngineUseCase;
import com.swiftquantum.domain.usecase.ExportQASMUseCase;
import com.swiftquantum.domain.usecase.ForgotPasswordUseCase;
import com.swiftquantum.domain.usecase.GetActiveJobsUseCase;
import com.swiftquantum.domain.usecase.GetAvailableBackendsUseCase;
import com.swiftquantum.domain.usecase.GetBenchmarkHistoryUseCase;
import com.swiftquantum.domain.usecase.GetCircuitUseCase;
import com.swiftquantum.domain.usecase.GetCurrentSubscriptionUseCase;
import com.swiftquantum.domain.usecase.GetCurrentUserUseCase;
import com.swiftquantum.domain.usecase.GetEngineStatusUseCase;
import com.swiftquantum.domain.usecase.GetJobResultUseCase;
import com.swiftquantum.domain.usecase.GetMaxQubitsUseCase;
import com.swiftquantum.domain.usecase.GetMyCircuitsUseCase;
import com.swiftquantum.domain.usecase.GetSubscriptionProductsUseCase;
import com.swiftquantum.domain.usecase.ImportQASMUseCase;
import com.swiftquantum.domain.usecase.LoadQASMTemplatesUseCase;
import com.swiftquantum.domain.usecase.LoginUseCase;
import com.swiftquantum.domain.usecase.LogoutUseCase;
import com.swiftquantum.domain.usecase.ObserveIBMConnectionUseCase;
import com.swiftquantum.domain.usecase.ObserveJobStatusUseCase;
import com.swiftquantum.domain.usecase.ObserveSubscriptionUseCase;
import com.swiftquantum.domain.usecase.ObserveUserTierUseCase;
import com.swiftquantum.domain.usecase.PurchaseSubscriptionUseCase;
import com.swiftquantum.domain.usecase.RegisterUseCase;
import com.swiftquantum.domain.usecase.RestorePurchasesUseCase;
import com.swiftquantum.domain.usecase.RunBenchmarkUseCase;
import com.swiftquantum.domain.usecase.RunSimulationUseCase;
import com.swiftquantum.domain.usecase.SaveCircuitUseCase;
import com.swiftquantum.domain.usecase.SubmitHardwareJobUseCase;
import com.swiftquantum.domain.usecase.UpdateProfileUseCase;
import com.swiftquantum.domain.usecase.ValidateQASMUseCase;
import com.swiftquantum.presentation.ui.MainActivity;
import com.swiftquantum.presentation.ui.MainActivity_MembersInjector;
import com.swiftquantum.presentation.viewmodel.AuthViewModel;
import com.swiftquantum.presentation.viewmodel.AuthViewModel_HiltModules;
import com.swiftquantum.presentation.viewmodel.AuthViewModel_HiltModules_BindsModule_Binds_LazyMapKey;
import com.swiftquantum.presentation.viewmodel.AuthViewModel_HiltModules_KeyModule_Provide_LazyMapKey;
import com.swiftquantum.presentation.viewmodel.BenchmarkViewModel;
import com.swiftquantum.presentation.viewmodel.BenchmarkViewModel_HiltModules;
import com.swiftquantum.presentation.viewmodel.BenchmarkViewModel_HiltModules_BindsModule_Binds_LazyMapKey;
import com.swiftquantum.presentation.viewmodel.BenchmarkViewModel_HiltModules_KeyModule_Provide_LazyMapKey;
import com.swiftquantum.presentation.viewmodel.CircuitViewModel;
import com.swiftquantum.presentation.viewmodel.CircuitViewModel_HiltModules;
import com.swiftquantum.presentation.viewmodel.CircuitViewModel_HiltModules_BindsModule_Binds_LazyMapKey;
import com.swiftquantum.presentation.viewmodel.CircuitViewModel_HiltModules_KeyModule_Provide_LazyMapKey;
import com.swiftquantum.presentation.viewmodel.ExperienceViewModel;
import com.swiftquantum.presentation.viewmodel.ExperienceViewModel_HiltModules;
import com.swiftquantum.presentation.viewmodel.ExperienceViewModel_HiltModules_BindsModule_Binds_LazyMapKey;
import com.swiftquantum.presentation.viewmodel.ExperienceViewModel_HiltModules_KeyModule_Provide_LazyMapKey;
import com.swiftquantum.presentation.viewmodel.HardwareViewModel;
import com.swiftquantum.presentation.viewmodel.HardwareViewModel_HiltModules;
import com.swiftquantum.presentation.viewmodel.HardwareViewModel_HiltModules_BindsModule_Binds_LazyMapKey;
import com.swiftquantum.presentation.viewmodel.HardwareViewModel_HiltModules_KeyModule_Provide_LazyMapKey;
import com.swiftquantum.presentation.viewmodel.PaywallViewModel;
import com.swiftquantum.presentation.viewmodel.PaywallViewModel_HiltModules;
import com.swiftquantum.presentation.viewmodel.PaywallViewModel_HiltModules_BindsModule_Binds_LazyMapKey;
import com.swiftquantum.presentation.viewmodel.PaywallViewModel_HiltModules_KeyModule_Provide_LazyMapKey;
import com.swiftquantum.presentation.viewmodel.ProfileViewModel;
import com.swiftquantum.presentation.viewmodel.ProfileViewModel_HiltModules;
import com.swiftquantum.presentation.viewmodel.ProfileViewModel_HiltModules_BindsModule_Binds_LazyMapKey;
import com.swiftquantum.presentation.viewmodel.ProfileViewModel_HiltModules_KeyModule_Provide_LazyMapKey;
import com.swiftquantum.presentation.viewmodel.QASMViewModel;
import com.swiftquantum.presentation.viewmodel.QASMViewModel_HiltModules;
import com.swiftquantum.presentation.viewmodel.QASMViewModel_HiltModules_BindsModule_Binds_LazyMapKey;
import com.swiftquantum.presentation.viewmodel.QASMViewModel_HiltModules_KeyModule_Provide_LazyMapKey;
import com.swiftquantum.presentation.viewmodel.SettingsViewModel;
import com.swiftquantum.presentation.viewmodel.SettingsViewModel_HiltModules;
import com.swiftquantum.presentation.viewmodel.SettingsViewModel_HiltModules_BindsModule_Binds_LazyMapKey;
import com.swiftquantum.presentation.viewmodel.SettingsViewModel_HiltModules_KeyModule_Provide_LazyMapKey;
import com.swiftquantum.presentation.viewmodel.SimulatorViewModel;
import com.swiftquantum.presentation.viewmodel.SimulatorViewModel_HiltModules;
import com.swiftquantum.presentation.viewmodel.SimulatorViewModel_HiltModules_BindsModule_Binds_LazyMapKey;
import com.swiftquantum.presentation.viewmodel.SimulatorViewModel_HiltModules_KeyModule_Provide_LazyMapKey;
import dagger.hilt.android.ActivityRetainedLifecycle;
import dagger.hilt.android.ViewModelLifecycle;
import dagger.hilt.android.internal.builders.ActivityComponentBuilder;
import dagger.hilt.android.internal.builders.ActivityRetainedComponentBuilder;
import dagger.hilt.android.internal.builders.FragmentComponentBuilder;
import dagger.hilt.android.internal.builders.ServiceComponentBuilder;
import dagger.hilt.android.internal.builders.ViewComponentBuilder;
import dagger.hilt.android.internal.builders.ViewModelComponentBuilder;
import dagger.hilt.android.internal.builders.ViewWithFragmentComponentBuilder;
import dagger.hilt.android.internal.lifecycle.DefaultViewModelFactories;
import dagger.hilt.android.internal.lifecycle.DefaultViewModelFactories_InternalFactoryFactory_Factory;
import dagger.hilt.android.internal.managers.ActivityRetainedComponentManager_LifecycleModule_ProvideActivityRetainedLifecycleFactory;
import dagger.hilt.android.internal.managers.SavedStateHandleHolder;
import dagger.hilt.android.internal.modules.ApplicationContextModule;
import dagger.hilt.android.internal.modules.ApplicationContextModule_ProvideApplicationFactory;
import dagger.hilt.android.internal.modules.ApplicationContextModule_ProvideContextFactory;
import dagger.internal.DaggerGenerated;
import dagger.internal.DoubleCheck;
import dagger.internal.LazyClassKeyMap;
import dagger.internal.MapBuilder;
import dagger.internal.Preconditions;
import dagger.internal.Provider;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;
import kotlinx.serialization.json.Json;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;

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
public final class DaggerSwiftQuantumApp_HiltComponents_SingletonC {
  private DaggerSwiftQuantumApp_HiltComponents_SingletonC() {
  }

  public static Builder builder() {
    return new Builder();
  }

  public static final class Builder {
    private ApplicationContextModule applicationContextModule;

    private Builder() {
    }

    public Builder applicationContextModule(ApplicationContextModule applicationContextModule) {
      this.applicationContextModule = Preconditions.checkNotNull(applicationContextModule);
      return this;
    }

    public SwiftQuantumApp_HiltComponents.SingletonC build() {
      Preconditions.checkBuilderRequirement(applicationContextModule, ApplicationContextModule.class);
      return new SingletonCImpl(applicationContextModule);
    }
  }

  private static final class ActivityRetainedCBuilder implements SwiftQuantumApp_HiltComponents.ActivityRetainedC.Builder {
    private final SingletonCImpl singletonCImpl;

    private SavedStateHandleHolder savedStateHandleHolder;

    private ActivityRetainedCBuilder(SingletonCImpl singletonCImpl) {
      this.singletonCImpl = singletonCImpl;
    }

    @Override
    public ActivityRetainedCBuilder savedStateHandleHolder(
        SavedStateHandleHolder savedStateHandleHolder) {
      this.savedStateHandleHolder = Preconditions.checkNotNull(savedStateHandleHolder);
      return this;
    }

    @Override
    public SwiftQuantumApp_HiltComponents.ActivityRetainedC build() {
      Preconditions.checkBuilderRequirement(savedStateHandleHolder, SavedStateHandleHolder.class);
      return new ActivityRetainedCImpl(singletonCImpl, savedStateHandleHolder);
    }
  }

  private static final class ActivityCBuilder implements SwiftQuantumApp_HiltComponents.ActivityC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private Activity activity;

    private ActivityCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
    }

    @Override
    public ActivityCBuilder activity(Activity activity) {
      this.activity = Preconditions.checkNotNull(activity);
      return this;
    }

    @Override
    public SwiftQuantumApp_HiltComponents.ActivityC build() {
      Preconditions.checkBuilderRequirement(activity, Activity.class);
      return new ActivityCImpl(singletonCImpl, activityRetainedCImpl, activity);
    }
  }

  private static final class FragmentCBuilder implements SwiftQuantumApp_HiltComponents.FragmentC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private Fragment fragment;

    private FragmentCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
    }

    @Override
    public FragmentCBuilder fragment(Fragment fragment) {
      this.fragment = Preconditions.checkNotNull(fragment);
      return this;
    }

    @Override
    public SwiftQuantumApp_HiltComponents.FragmentC build() {
      Preconditions.checkBuilderRequirement(fragment, Fragment.class);
      return new FragmentCImpl(singletonCImpl, activityRetainedCImpl, activityCImpl, fragment);
    }
  }

  private static final class ViewWithFragmentCBuilder implements SwiftQuantumApp_HiltComponents.ViewWithFragmentC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final FragmentCImpl fragmentCImpl;

    private View view;

    private ViewWithFragmentCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl,
        FragmentCImpl fragmentCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
      this.fragmentCImpl = fragmentCImpl;
    }

    @Override
    public ViewWithFragmentCBuilder view(View view) {
      this.view = Preconditions.checkNotNull(view);
      return this;
    }

    @Override
    public SwiftQuantumApp_HiltComponents.ViewWithFragmentC build() {
      Preconditions.checkBuilderRequirement(view, View.class);
      return new ViewWithFragmentCImpl(singletonCImpl, activityRetainedCImpl, activityCImpl, fragmentCImpl, view);
    }
  }

  private static final class ViewCBuilder implements SwiftQuantumApp_HiltComponents.ViewC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private View view;

    private ViewCBuilder(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
        ActivityCImpl activityCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
    }

    @Override
    public ViewCBuilder view(View view) {
      this.view = Preconditions.checkNotNull(view);
      return this;
    }

    @Override
    public SwiftQuantumApp_HiltComponents.ViewC build() {
      Preconditions.checkBuilderRequirement(view, View.class);
      return new ViewCImpl(singletonCImpl, activityRetainedCImpl, activityCImpl, view);
    }
  }

  private static final class ViewModelCBuilder implements SwiftQuantumApp_HiltComponents.ViewModelC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private SavedStateHandle savedStateHandle;

    private ViewModelLifecycle viewModelLifecycle;

    private ViewModelCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
    }

    @Override
    public ViewModelCBuilder savedStateHandle(SavedStateHandle handle) {
      this.savedStateHandle = Preconditions.checkNotNull(handle);
      return this;
    }

    @Override
    public ViewModelCBuilder viewModelLifecycle(ViewModelLifecycle viewModelLifecycle) {
      this.viewModelLifecycle = Preconditions.checkNotNull(viewModelLifecycle);
      return this;
    }

    @Override
    public SwiftQuantumApp_HiltComponents.ViewModelC build() {
      Preconditions.checkBuilderRequirement(savedStateHandle, SavedStateHandle.class);
      Preconditions.checkBuilderRequirement(viewModelLifecycle, ViewModelLifecycle.class);
      return new ViewModelCImpl(singletonCImpl, activityRetainedCImpl, savedStateHandle, viewModelLifecycle);
    }
  }

  private static final class ServiceCBuilder implements SwiftQuantumApp_HiltComponents.ServiceC.Builder {
    private final SingletonCImpl singletonCImpl;

    private Service service;

    private ServiceCBuilder(SingletonCImpl singletonCImpl) {
      this.singletonCImpl = singletonCImpl;
    }

    @Override
    public ServiceCBuilder service(Service service) {
      this.service = Preconditions.checkNotNull(service);
      return this;
    }

    @Override
    public SwiftQuantumApp_HiltComponents.ServiceC build() {
      Preconditions.checkBuilderRequirement(service, Service.class);
      return new ServiceCImpl(singletonCImpl, service);
    }
  }

  private static final class ViewWithFragmentCImpl extends SwiftQuantumApp_HiltComponents.ViewWithFragmentC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final FragmentCImpl fragmentCImpl;

    private final ViewWithFragmentCImpl viewWithFragmentCImpl = this;

    private ViewWithFragmentCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl,
        FragmentCImpl fragmentCImpl, View viewParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
      this.fragmentCImpl = fragmentCImpl;


    }
  }

  private static final class FragmentCImpl extends SwiftQuantumApp_HiltComponents.FragmentC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final FragmentCImpl fragmentCImpl = this;

    private FragmentCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl,
        Fragment fragmentParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;


    }

    @Override
    public DefaultViewModelFactories.InternalFactoryFactory getHiltInternalFactoryFactory() {
      return activityCImpl.getHiltInternalFactoryFactory();
    }

    @Override
    public ViewWithFragmentComponentBuilder viewWithFragmentComponentBuilder() {
      return new ViewWithFragmentCBuilder(singletonCImpl, activityRetainedCImpl, activityCImpl, fragmentCImpl);
    }
  }

  private static final class ViewCImpl extends SwiftQuantumApp_HiltComponents.ViewC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final ViewCImpl viewCImpl = this;

    private ViewCImpl(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
        ActivityCImpl activityCImpl, View viewParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;


    }
  }

  private static final class ActivityCImpl extends SwiftQuantumApp_HiltComponents.ActivityC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl = this;

    private ActivityCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, Activity activityParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;


    }

    @Override
    public void injectMainActivity(MainActivity arg0) {
      injectMainActivity2(arg0);
    }

    @Override
    public DefaultViewModelFactories.InternalFactoryFactory getHiltInternalFactoryFactory() {
      return DefaultViewModelFactories_InternalFactoryFactory_Factory.newInstance(getViewModelKeys(), new ViewModelCBuilder(singletonCImpl, activityRetainedCImpl));
    }

    @Override
    public Map<Class<?>, Boolean> getViewModelKeys() {
      return LazyClassKeyMap.<Boolean>of(MapBuilder.<String, Boolean>newMapBuilder(10).put(AuthViewModel_HiltModules_KeyModule_Provide_LazyMapKey.lazyClassKeyName, AuthViewModel_HiltModules.KeyModule.provide()).put(BenchmarkViewModel_HiltModules_KeyModule_Provide_LazyMapKey.lazyClassKeyName, BenchmarkViewModel_HiltModules.KeyModule.provide()).put(CircuitViewModel_HiltModules_KeyModule_Provide_LazyMapKey.lazyClassKeyName, CircuitViewModel_HiltModules.KeyModule.provide()).put(ExperienceViewModel_HiltModules_KeyModule_Provide_LazyMapKey.lazyClassKeyName, ExperienceViewModel_HiltModules.KeyModule.provide()).put(HardwareViewModel_HiltModules_KeyModule_Provide_LazyMapKey.lazyClassKeyName, HardwareViewModel_HiltModules.KeyModule.provide()).put(PaywallViewModel_HiltModules_KeyModule_Provide_LazyMapKey.lazyClassKeyName, PaywallViewModel_HiltModules.KeyModule.provide()).put(ProfileViewModel_HiltModules_KeyModule_Provide_LazyMapKey.lazyClassKeyName, ProfileViewModel_HiltModules.KeyModule.provide()).put(QASMViewModel_HiltModules_KeyModule_Provide_LazyMapKey.lazyClassKeyName, QASMViewModel_HiltModules.KeyModule.provide()).put(SettingsViewModel_HiltModules_KeyModule_Provide_LazyMapKey.lazyClassKeyName, SettingsViewModel_HiltModules.KeyModule.provide()).put(SimulatorViewModel_HiltModules_KeyModule_Provide_LazyMapKey.lazyClassKeyName, SimulatorViewModel_HiltModules.KeyModule.provide()).build());
    }

    @Override
    public ViewModelComponentBuilder getViewModelComponentBuilder() {
      return new ViewModelCBuilder(singletonCImpl, activityRetainedCImpl);
    }

    @Override
    public FragmentComponentBuilder fragmentComponentBuilder() {
      return new FragmentCBuilder(singletonCImpl, activityRetainedCImpl, activityCImpl);
    }

    @Override
    public ViewComponentBuilder viewComponentBuilder() {
      return new ViewCBuilder(singletonCImpl, activityRetainedCImpl, activityCImpl);
    }

    private MainActivity injectMainActivity2(MainActivity instance) {
      MainActivity_MembersInjector.injectBillingRepository(instance, singletonCImpl.billingRepositoryImplProvider.get());
      MainActivity_MembersInjector.injectUserPreferences(instance, singletonCImpl.userPreferencesProvider.get());
      return instance;
    }
  }

  private static final class ViewModelCImpl extends SwiftQuantumApp_HiltComponents.ViewModelC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ViewModelCImpl viewModelCImpl = this;

    private Provider<LoginUseCase> provideLoginUseCaseProvider;

    private Provider<RegisterUseCase> provideRegisterUseCaseProvider;

    private Provider<LogoutUseCase> provideLogoutUseCaseProvider;

    private Provider<GetCurrentUserUseCase> provideGetCurrentUserUseCaseProvider;

    private Provider<ForgotPasswordUseCase> provideForgotPasswordUseCaseProvider;

    private Provider<UpdateProfileUseCase> provideUpdateProfileUseCaseProvider;

    private Provider<AuthViewModel> authViewModelProvider;

    private Provider<ExecuteWithHybridEngineUseCase> provideExecuteWithHybridEngineUseCaseProvider;

    private Provider<RunBenchmarkUseCase> provideRunBenchmarkUseCaseProvider;

    private Provider<GetBenchmarkHistoryUseCase> provideGetBenchmarkHistoryUseCaseProvider;

    private Provider<GetEngineStatusUseCase> provideGetEngineStatusUseCaseProvider;

    private Provider<ObserveUserTierUseCase> provideObserveUserTierUseCaseProvider;

    private Provider<BenchmarkViewModel> benchmarkViewModelProvider;

    private Provider<SaveCircuitUseCase> provideSaveCircuitUseCaseProvider;

    private Provider<GetCircuitUseCase> provideGetCircuitUseCaseProvider;

    private Provider<GetMyCircuitsUseCase> provideGetMyCircuitsUseCaseProvider;

    private Provider<DeleteCircuitUseCase> provideDeleteCircuitUseCaseProvider;

    private Provider<GetMaxQubitsUseCase> provideGetMaxQubitsUseCaseProvider;

    private Provider<CircuitViewModel> circuitViewModelProvider;

    private Provider<ExperienceViewModel> experienceViewModelProvider;

    private Provider<ConnectToIBMQuantumUseCase> provideConnectToIBMQuantumUseCaseProvider;

    private Provider<DisconnectFromIBMQuantumUseCase> provideDisconnectFromIBMQuantumUseCaseProvider;

    private Provider<GetAvailableBackendsUseCase> provideGetAvailableBackendsUseCaseProvider;

    private Provider<SubmitHardwareJobUseCase> provideSubmitHardwareJobUseCaseProvider;

    private Provider<CancelJobUseCase> provideCancelJobUseCaseProvider;

    private Provider<GetJobResultUseCase> provideGetJobResultUseCaseProvider;

    private Provider<GetActiveJobsUseCase> provideGetActiveJobsUseCaseProvider;

    private Provider<ObserveIBMConnectionUseCase> provideObserveIBMConnectionUseCaseProvider;

    private Provider<ObserveJobStatusUseCase> provideObserveJobStatusUseCaseProvider;

    private Provider<HardwareViewModel> hardwareViewModelProvider;

    private Provider<GetSubscriptionProductsUseCase> provideGetSubscriptionProductsUseCaseProvider;

    private Provider<PurchaseSubscriptionUseCase> providePurchaseSubscriptionUseCaseProvider;

    private Provider<RestorePurchasesUseCase> provideRestorePurchasesUseCaseProvider;

    private Provider<ObserveSubscriptionUseCase> provideObserveSubscriptionUseCaseProvider;

    private Provider<PaywallViewModel> paywallViewModelProvider;

    private Provider<GetCurrentSubscriptionUseCase> provideGetCurrentSubscriptionUseCaseProvider;

    private Provider<ProfileViewModel> profileViewModelProvider;

    private Provider<ImportQASMUseCase> provideImportQASMUseCaseProvider;

    private Provider<ExportQASMUseCase> provideExportQASMUseCaseProvider;

    private Provider<ValidateQASMUseCase> provideValidateQASMUseCaseProvider;

    private Provider<LoadQASMTemplatesUseCase> provideLoadQASMTemplatesUseCaseProvider;

    private Provider<QASMViewModel> qASMViewModelProvider;

    private Provider<SettingsViewModel> settingsViewModelProvider;

    private Provider<RunSimulationUseCase> provideRunSimulationUseCaseProvider;

    private Provider<SimulatorViewModel> simulatorViewModelProvider;

    private ViewModelCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, SavedStateHandle savedStateHandleParam,
        ViewModelLifecycle viewModelLifecycleParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;

      initialize(savedStateHandleParam, viewModelLifecycleParam);
      initialize2(savedStateHandleParam, viewModelLifecycleParam);

    }

    @SuppressWarnings("unchecked")
    private void initialize(final SavedStateHandle savedStateHandleParam,
        final ViewModelLifecycle viewModelLifecycleParam) {
      this.provideLoginUseCaseProvider = DoubleCheck.provider(new SwitchingProvider<LoginUseCase>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 1));
      this.provideRegisterUseCaseProvider = DoubleCheck.provider(new SwitchingProvider<RegisterUseCase>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 2));
      this.provideLogoutUseCaseProvider = DoubleCheck.provider(new SwitchingProvider<LogoutUseCase>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 3));
      this.provideGetCurrentUserUseCaseProvider = DoubleCheck.provider(new SwitchingProvider<GetCurrentUserUseCase>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 4));
      this.provideForgotPasswordUseCaseProvider = DoubleCheck.provider(new SwitchingProvider<ForgotPasswordUseCase>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 5));
      this.provideUpdateProfileUseCaseProvider = DoubleCheck.provider(new SwitchingProvider<UpdateProfileUseCase>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 6));
      this.authViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 0);
      this.provideExecuteWithHybridEngineUseCaseProvider = DoubleCheck.provider(new SwitchingProvider<ExecuteWithHybridEngineUseCase>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 8));
      this.provideRunBenchmarkUseCaseProvider = DoubleCheck.provider(new SwitchingProvider<RunBenchmarkUseCase>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 9));
      this.provideGetBenchmarkHistoryUseCaseProvider = DoubleCheck.provider(new SwitchingProvider<GetBenchmarkHistoryUseCase>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 10));
      this.provideGetEngineStatusUseCaseProvider = DoubleCheck.provider(new SwitchingProvider<GetEngineStatusUseCase>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 11));
      this.provideObserveUserTierUseCaseProvider = DoubleCheck.provider(new SwitchingProvider<ObserveUserTierUseCase>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 12));
      this.benchmarkViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 7);
      this.provideSaveCircuitUseCaseProvider = DoubleCheck.provider(new SwitchingProvider<SaveCircuitUseCase>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 14));
      this.provideGetCircuitUseCaseProvider = DoubleCheck.provider(new SwitchingProvider<GetCircuitUseCase>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 15));
      this.provideGetMyCircuitsUseCaseProvider = DoubleCheck.provider(new SwitchingProvider<GetMyCircuitsUseCase>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 16));
      this.provideDeleteCircuitUseCaseProvider = DoubleCheck.provider(new SwitchingProvider<DeleteCircuitUseCase>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 17));
      this.provideGetMaxQubitsUseCaseProvider = DoubleCheck.provider(new SwitchingProvider<GetMaxQubitsUseCase>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 18));
      this.circuitViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 13);
      this.experienceViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 19);
      this.provideConnectToIBMQuantumUseCaseProvider = DoubleCheck.provider(new SwitchingProvider<ConnectToIBMQuantumUseCase>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 21));
      this.provideDisconnectFromIBMQuantumUseCaseProvider = DoubleCheck.provider(new SwitchingProvider<DisconnectFromIBMQuantumUseCase>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 22));
      this.provideGetAvailableBackendsUseCaseProvider = DoubleCheck.provider(new SwitchingProvider<GetAvailableBackendsUseCase>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 23));
      this.provideSubmitHardwareJobUseCaseProvider = DoubleCheck.provider(new SwitchingProvider<SubmitHardwareJobUseCase>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 24));
      this.provideCancelJobUseCaseProvider = DoubleCheck.provider(new SwitchingProvider<CancelJobUseCase>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 25));
    }

    @SuppressWarnings("unchecked")
    private void initialize2(final SavedStateHandle savedStateHandleParam,
        final ViewModelLifecycle viewModelLifecycleParam) {
      this.provideGetJobResultUseCaseProvider = DoubleCheck.provider(new SwitchingProvider<GetJobResultUseCase>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 26));
      this.provideGetActiveJobsUseCaseProvider = DoubleCheck.provider(new SwitchingProvider<GetActiveJobsUseCase>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 27));
      this.provideObserveIBMConnectionUseCaseProvider = DoubleCheck.provider(new SwitchingProvider<ObserveIBMConnectionUseCase>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 28));
      this.provideObserveJobStatusUseCaseProvider = DoubleCheck.provider(new SwitchingProvider<ObserveJobStatusUseCase>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 29));
      this.hardwareViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 20);
      this.provideGetSubscriptionProductsUseCaseProvider = DoubleCheck.provider(new SwitchingProvider<GetSubscriptionProductsUseCase>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 31));
      this.providePurchaseSubscriptionUseCaseProvider = DoubleCheck.provider(new SwitchingProvider<PurchaseSubscriptionUseCase>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 32));
      this.provideRestorePurchasesUseCaseProvider = DoubleCheck.provider(new SwitchingProvider<RestorePurchasesUseCase>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 33));
      this.provideObserveSubscriptionUseCaseProvider = DoubleCheck.provider(new SwitchingProvider<ObserveSubscriptionUseCase>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 34));
      this.paywallViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 30);
      this.provideGetCurrentSubscriptionUseCaseProvider = DoubleCheck.provider(new SwitchingProvider<GetCurrentSubscriptionUseCase>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 36));
      this.profileViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 35);
      this.provideImportQASMUseCaseProvider = DoubleCheck.provider(new SwitchingProvider<ImportQASMUseCase>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 38));
      this.provideExportQASMUseCaseProvider = DoubleCheck.provider(new SwitchingProvider<ExportQASMUseCase>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 39));
      this.provideValidateQASMUseCaseProvider = DoubleCheck.provider(new SwitchingProvider<ValidateQASMUseCase>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 40));
      this.provideLoadQASMTemplatesUseCaseProvider = DoubleCheck.provider(new SwitchingProvider<LoadQASMTemplatesUseCase>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 41));
      this.qASMViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 37);
      this.settingsViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 42);
      this.provideRunSimulationUseCaseProvider = DoubleCheck.provider(new SwitchingProvider<RunSimulationUseCase>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 44));
      this.simulatorViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 43);
    }

    @Override
    public Map<Class<?>, javax.inject.Provider<ViewModel>> getHiltViewModelMap() {
      return LazyClassKeyMap.<javax.inject.Provider<ViewModel>>of(MapBuilder.<String, javax.inject.Provider<ViewModel>>newMapBuilder(10).put(AuthViewModel_HiltModules_BindsModule_Binds_LazyMapKey.lazyClassKeyName, ((Provider) authViewModelProvider)).put(BenchmarkViewModel_HiltModules_BindsModule_Binds_LazyMapKey.lazyClassKeyName, ((Provider) benchmarkViewModelProvider)).put(CircuitViewModel_HiltModules_BindsModule_Binds_LazyMapKey.lazyClassKeyName, ((Provider) circuitViewModelProvider)).put(ExperienceViewModel_HiltModules_BindsModule_Binds_LazyMapKey.lazyClassKeyName, ((Provider) experienceViewModelProvider)).put(HardwareViewModel_HiltModules_BindsModule_Binds_LazyMapKey.lazyClassKeyName, ((Provider) hardwareViewModelProvider)).put(PaywallViewModel_HiltModules_BindsModule_Binds_LazyMapKey.lazyClassKeyName, ((Provider) paywallViewModelProvider)).put(ProfileViewModel_HiltModules_BindsModule_Binds_LazyMapKey.lazyClassKeyName, ((Provider) profileViewModelProvider)).put(QASMViewModel_HiltModules_BindsModule_Binds_LazyMapKey.lazyClassKeyName, ((Provider) qASMViewModelProvider)).put(SettingsViewModel_HiltModules_BindsModule_Binds_LazyMapKey.lazyClassKeyName, ((Provider) settingsViewModelProvider)).put(SimulatorViewModel_HiltModules_BindsModule_Binds_LazyMapKey.lazyClassKeyName, ((Provider) simulatorViewModelProvider)).build());
    }

    @Override
    public Map<Class<?>, Object> getHiltViewModelAssistedMap() {
      return Collections.<Class<?>, Object>emptyMap();
    }

    private static final class SwitchingProvider<T> implements Provider<T> {
      private final SingletonCImpl singletonCImpl;

      private final ActivityRetainedCImpl activityRetainedCImpl;

      private final ViewModelCImpl viewModelCImpl;

      private final int id;

      SwitchingProvider(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
          ViewModelCImpl viewModelCImpl, int id) {
        this.singletonCImpl = singletonCImpl;
        this.activityRetainedCImpl = activityRetainedCImpl;
        this.viewModelCImpl = viewModelCImpl;
        this.id = id;
      }

      @SuppressWarnings("unchecked")
      @Override
      public T get() {
        switch (id) {
          case 0: // com.swiftquantum.presentation.viewmodel.AuthViewModel 
          return (T) new AuthViewModel(viewModelCImpl.provideLoginUseCaseProvider.get(), viewModelCImpl.provideRegisterUseCaseProvider.get(), viewModelCImpl.provideLogoutUseCaseProvider.get(), viewModelCImpl.provideGetCurrentUserUseCaseProvider.get(), viewModelCImpl.provideForgotPasswordUseCaseProvider.get(), viewModelCImpl.provideUpdateProfileUseCaseProvider.get());

          case 1: // com.swiftquantum.domain.usecase.LoginUseCase 
          return (T) UseCaseModule_ProvideLoginUseCaseFactory.provideLoginUseCase(singletonCImpl.authRepositoryImplProvider.get());

          case 2: // com.swiftquantum.domain.usecase.RegisterUseCase 
          return (T) UseCaseModule_ProvideRegisterUseCaseFactory.provideRegisterUseCase(singletonCImpl.authRepositoryImplProvider.get());

          case 3: // com.swiftquantum.domain.usecase.LogoutUseCase 
          return (T) UseCaseModule_ProvideLogoutUseCaseFactory.provideLogoutUseCase(singletonCImpl.authRepositoryImplProvider.get());

          case 4: // com.swiftquantum.domain.usecase.GetCurrentUserUseCase 
          return (T) UseCaseModule_ProvideGetCurrentUserUseCaseFactory.provideGetCurrentUserUseCase(singletonCImpl.authRepositoryImplProvider.get());

          case 5: // com.swiftquantum.domain.usecase.ForgotPasswordUseCase 
          return (T) UseCaseModule_ProvideForgotPasswordUseCaseFactory.provideForgotPasswordUseCase(singletonCImpl.authRepositoryImplProvider.get());

          case 6: // com.swiftquantum.domain.usecase.UpdateProfileUseCase 
          return (T) UseCaseModule_ProvideUpdateProfileUseCaseFactory.provideUpdateProfileUseCase(singletonCImpl.authRepositoryImplProvider.get());

          case 7: // com.swiftquantum.presentation.viewmodel.BenchmarkViewModel 
          return (T) new BenchmarkViewModel(viewModelCImpl.provideExecuteWithHybridEngineUseCaseProvider.get(), viewModelCImpl.provideRunBenchmarkUseCaseProvider.get(), viewModelCImpl.provideGetBenchmarkHistoryUseCaseProvider.get(), viewModelCImpl.provideGetEngineStatusUseCaseProvider.get(), viewModelCImpl.provideObserveUserTierUseCaseProvider.get());

          case 8: // com.swiftquantum.domain.usecase.ExecuteWithHybridEngineUseCase 
          return (T) UseCaseModule_ProvideExecuteWithHybridEngineUseCaseFactory.provideExecuteWithHybridEngineUseCase(singletonCImpl.hybridEngineRepositoryImplProvider.get(), singletonCImpl.billingRepositoryImplProvider.get());

          case 9: // com.swiftquantum.domain.usecase.RunBenchmarkUseCase 
          return (T) UseCaseModule_ProvideRunBenchmarkUseCaseFactory.provideRunBenchmarkUseCase(singletonCImpl.hybridEngineRepositoryImplProvider.get(), singletonCImpl.billingRepositoryImplProvider.get());

          case 10: // com.swiftquantum.domain.usecase.GetBenchmarkHistoryUseCase 
          return (T) UseCaseModule_ProvideGetBenchmarkHistoryUseCaseFactory.provideGetBenchmarkHistoryUseCase(singletonCImpl.hybridEngineRepositoryImplProvider.get());

          case 11: // com.swiftquantum.domain.usecase.GetEngineStatusUseCase 
          return (T) UseCaseModule_ProvideGetEngineStatusUseCaseFactory.provideGetEngineStatusUseCase(singletonCImpl.hybridEngineRepositoryImplProvider.get());

          case 12: // com.swiftquantum.domain.usecase.ObserveUserTierUseCase 
          return (T) UseCaseModule_ProvideObserveUserTierUseCaseFactory.provideObserveUserTierUseCase(singletonCImpl.billingRepositoryImplProvider.get());

          case 13: // com.swiftquantum.presentation.viewmodel.CircuitViewModel 
          return (T) new CircuitViewModel(viewModelCImpl.provideSaveCircuitUseCaseProvider.get(), viewModelCImpl.provideGetCircuitUseCaseProvider.get(), viewModelCImpl.provideGetMyCircuitsUseCaseProvider.get(), viewModelCImpl.provideDeleteCircuitUseCaseProvider.get(), viewModelCImpl.provideGetMaxQubitsUseCaseProvider.get());

          case 14: // com.swiftquantum.domain.usecase.SaveCircuitUseCase 
          return (T) UseCaseModule_ProvideSaveCircuitUseCaseFactory.provideSaveCircuitUseCase(singletonCImpl.quantumRepositoryImplProvider.get());

          case 15: // com.swiftquantum.domain.usecase.GetCircuitUseCase 
          return (T) UseCaseModule_ProvideGetCircuitUseCaseFactory.provideGetCircuitUseCase(singletonCImpl.quantumRepositoryImplProvider.get());

          case 16: // com.swiftquantum.domain.usecase.GetMyCircuitsUseCase 
          return (T) UseCaseModule_ProvideGetMyCircuitsUseCaseFactory.provideGetMyCircuitsUseCase(singletonCImpl.quantumRepositoryImplProvider.get());

          case 17: // com.swiftquantum.domain.usecase.DeleteCircuitUseCase 
          return (T) UseCaseModule_ProvideDeleteCircuitUseCaseFactory.provideDeleteCircuitUseCase(singletonCImpl.quantumRepositoryImplProvider.get());

          case 18: // com.swiftquantum.domain.usecase.GetMaxQubitsUseCase 
          return (T) UseCaseModule_ProvideGetMaxQubitsUseCaseFactory.provideGetMaxQubitsUseCase(singletonCImpl.billingRepositoryImplProvider.get());

          case 19: // com.swiftquantum.presentation.viewmodel.ExperienceViewModel 
          return (T) new ExperienceViewModel(singletonCImpl.experienceRepositoryImplProvider.get());

          case 20: // com.swiftquantum.presentation.viewmodel.HardwareViewModel 
          return (T) new HardwareViewModel(viewModelCImpl.provideConnectToIBMQuantumUseCaseProvider.get(), viewModelCImpl.provideDisconnectFromIBMQuantumUseCaseProvider.get(), viewModelCImpl.provideGetAvailableBackendsUseCaseProvider.get(), viewModelCImpl.provideSubmitHardwareJobUseCaseProvider.get(), viewModelCImpl.provideCancelJobUseCaseProvider.get(), viewModelCImpl.provideGetJobResultUseCaseProvider.get(), viewModelCImpl.provideGetActiveJobsUseCaseProvider.get(), viewModelCImpl.provideObserveIBMConnectionUseCaseProvider.get(), viewModelCImpl.provideObserveJobStatusUseCaseProvider.get(), viewModelCImpl.provideObserveUserTierUseCaseProvider.get());

          case 21: // com.swiftquantum.domain.usecase.ConnectToIBMQuantumUseCase 
          return (T) UseCaseModule_ProvideConnectToIBMQuantumUseCaseFactory.provideConnectToIBMQuantumUseCase(singletonCImpl.hardwareRepositoryImplProvider.get(), singletonCImpl.billingRepositoryImplProvider.get());

          case 22: // com.swiftquantum.domain.usecase.DisconnectFromIBMQuantumUseCase 
          return (T) UseCaseModule_ProvideDisconnectFromIBMQuantumUseCaseFactory.provideDisconnectFromIBMQuantumUseCase(singletonCImpl.hardwareRepositoryImplProvider.get());

          case 23: // com.swiftquantum.domain.usecase.GetAvailableBackendsUseCase 
          return (T) UseCaseModule_ProvideGetAvailableBackendsUseCaseFactory.provideGetAvailableBackendsUseCase(singletonCImpl.hardwareRepositoryImplProvider.get());

          case 24: // com.swiftquantum.domain.usecase.SubmitHardwareJobUseCase 
          return (T) UseCaseModule_ProvideSubmitHardwareJobUseCaseFactory.provideSubmitHardwareJobUseCase(singletonCImpl.hardwareRepositoryImplProvider.get(), singletonCImpl.billingRepositoryImplProvider.get());

          case 25: // com.swiftquantum.domain.usecase.CancelJobUseCase 
          return (T) UseCaseModule_ProvideCancelJobUseCaseFactory.provideCancelJobUseCase(singletonCImpl.hardwareRepositoryImplProvider.get());

          case 26: // com.swiftquantum.domain.usecase.GetJobResultUseCase 
          return (T) UseCaseModule_ProvideGetJobResultUseCaseFactory.provideGetJobResultUseCase(singletonCImpl.hardwareRepositoryImplProvider.get());

          case 27: // com.swiftquantum.domain.usecase.GetActiveJobsUseCase 
          return (T) UseCaseModule_ProvideGetActiveJobsUseCaseFactory.provideGetActiveJobsUseCase(singletonCImpl.hardwareRepositoryImplProvider.get());

          case 28: // com.swiftquantum.domain.usecase.ObserveIBMConnectionUseCase 
          return (T) UseCaseModule_ProvideObserveIBMConnectionUseCaseFactory.provideObserveIBMConnectionUseCase(singletonCImpl.hardwareRepositoryImplProvider.get());

          case 29: // com.swiftquantum.domain.usecase.ObserveJobStatusUseCase 
          return (T) UseCaseModule_ProvideObserveJobStatusUseCaseFactory.provideObserveJobStatusUseCase(singletonCImpl.hardwareRepositoryImplProvider.get());

          case 30: // com.swiftquantum.presentation.viewmodel.PaywallViewModel 
          return (T) new PaywallViewModel(viewModelCImpl.provideGetSubscriptionProductsUseCaseProvider.get(), viewModelCImpl.providePurchaseSubscriptionUseCaseProvider.get(), viewModelCImpl.provideRestorePurchasesUseCaseProvider.get(), viewModelCImpl.provideObserveSubscriptionUseCaseProvider.get(), viewModelCImpl.provideObserveUserTierUseCaseProvider.get());

          case 31: // com.swiftquantum.domain.usecase.GetSubscriptionProductsUseCase 
          return (T) UseCaseModule_ProvideGetSubscriptionProductsUseCaseFactory.provideGetSubscriptionProductsUseCase(singletonCImpl.billingRepositoryImplProvider.get());

          case 32: // com.swiftquantum.domain.usecase.PurchaseSubscriptionUseCase 
          return (T) UseCaseModule_ProvidePurchaseSubscriptionUseCaseFactory.providePurchaseSubscriptionUseCase(singletonCImpl.billingRepositoryImplProvider.get());

          case 33: // com.swiftquantum.domain.usecase.RestorePurchasesUseCase 
          return (T) UseCaseModule_ProvideRestorePurchasesUseCaseFactory.provideRestorePurchasesUseCase(singletonCImpl.billingRepositoryImplProvider.get());

          case 34: // com.swiftquantum.domain.usecase.ObserveSubscriptionUseCase 
          return (T) UseCaseModule_ProvideObserveSubscriptionUseCaseFactory.provideObserveSubscriptionUseCase(singletonCImpl.billingRepositoryImplProvider.get());

          case 35: // com.swiftquantum.presentation.viewmodel.ProfileViewModel 
          return (T) new ProfileViewModel(viewModelCImpl.provideGetCurrentUserUseCaseProvider.get(), viewModelCImpl.provideLogoutUseCaseProvider.get(), viewModelCImpl.provideObserveUserTierUseCaseProvider.get(), viewModelCImpl.provideObserveSubscriptionUseCaseProvider.get(), viewModelCImpl.provideGetSubscriptionProductsUseCaseProvider.get(), viewModelCImpl.providePurchaseSubscriptionUseCaseProvider.get(), viewModelCImpl.provideRestorePurchasesUseCaseProvider.get(), viewModelCImpl.provideGetCurrentSubscriptionUseCaseProvider.get(), singletonCImpl.sharedAuthManagerProvider.get());

          case 36: // com.swiftquantum.domain.usecase.GetCurrentSubscriptionUseCase 
          return (T) UseCaseModule_ProvideGetCurrentSubscriptionUseCaseFactory.provideGetCurrentSubscriptionUseCase(singletonCImpl.billingRepositoryImplProvider.get());

          case 37: // com.swiftquantum.presentation.viewmodel.QASMViewModel 
          return (T) new QASMViewModel(viewModelCImpl.provideImportQASMUseCaseProvider.get(), viewModelCImpl.provideExportQASMUseCaseProvider.get(), viewModelCImpl.provideValidateQASMUseCaseProvider.get(), viewModelCImpl.provideLoadQASMTemplatesUseCaseProvider.get());

          case 38: // com.swiftquantum.domain.usecase.ImportQASMUseCase 
          return (T) UseCaseModule_ProvideImportQASMUseCaseFactory.provideImportQASMUseCase(singletonCImpl.qASMRepositoryImplProvider.get());

          case 39: // com.swiftquantum.domain.usecase.ExportQASMUseCase 
          return (T) UseCaseModule_ProvideExportQASMUseCaseFactory.provideExportQASMUseCase(singletonCImpl.qASMRepositoryImplProvider.get());

          case 40: // com.swiftquantum.domain.usecase.ValidateQASMUseCase 
          return (T) UseCaseModule_ProvideValidateQASMUseCaseFactory.provideValidateQASMUseCase(singletonCImpl.qASMRepositoryImplProvider.get());

          case 41: // com.swiftquantum.domain.usecase.LoadQASMTemplatesUseCase 
          return (T) UseCaseModule_ProvideLoadQASMTemplatesUseCaseFactory.provideLoadQASMTemplatesUseCase(singletonCImpl.qASMRepositoryImplProvider.get());

          case 42: // com.swiftquantum.presentation.viewmodel.SettingsViewModel 
          return (T) new SettingsViewModel(ApplicationContextModule_ProvideApplicationFactory.provideApplication(singletonCImpl.applicationContextModule), viewModelCImpl.provideGetCurrentUserUseCaseProvider.get(), viewModelCImpl.provideLogoutUseCaseProvider.get(), viewModelCImpl.provideObserveUserTierUseCaseProvider.get(), singletonCImpl.sharedAuthManagerProvider.get(), singletonCImpl.userPreferencesProvider.get());

          case 43: // com.swiftquantum.presentation.viewmodel.SimulatorViewModel 
          return (T) new SimulatorViewModel(viewModelCImpl.provideRunSimulationUseCaseProvider.get(), viewModelCImpl.provideGetMaxQubitsUseCaseProvider.get(), viewModelCImpl.provideObserveUserTierUseCaseProvider.get());

          case 44: // com.swiftquantum.domain.usecase.RunSimulationUseCase 
          return (T) UseCaseModule_ProvideRunSimulationUseCaseFactory.provideRunSimulationUseCase(singletonCImpl.quantumRepositoryImplProvider.get(), singletonCImpl.billingRepositoryImplProvider.get());

          default: throw new AssertionError(id);
        }
      }
    }
  }

  private static final class ActivityRetainedCImpl extends SwiftQuantumApp_HiltComponents.ActivityRetainedC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl = this;

    private Provider<ActivityRetainedLifecycle> provideActivityRetainedLifecycleProvider;

    private ActivityRetainedCImpl(SingletonCImpl singletonCImpl,
        SavedStateHandleHolder savedStateHandleHolderParam) {
      this.singletonCImpl = singletonCImpl;

      initialize(savedStateHandleHolderParam);

    }

    @SuppressWarnings("unchecked")
    private void initialize(final SavedStateHandleHolder savedStateHandleHolderParam) {
      this.provideActivityRetainedLifecycleProvider = DoubleCheck.provider(new SwitchingProvider<ActivityRetainedLifecycle>(singletonCImpl, activityRetainedCImpl, 0));
    }

    @Override
    public ActivityComponentBuilder activityComponentBuilder() {
      return new ActivityCBuilder(singletonCImpl, activityRetainedCImpl);
    }

    @Override
    public ActivityRetainedLifecycle getActivityRetainedLifecycle() {
      return provideActivityRetainedLifecycleProvider.get();
    }

    private static final class SwitchingProvider<T> implements Provider<T> {
      private final SingletonCImpl singletonCImpl;

      private final ActivityRetainedCImpl activityRetainedCImpl;

      private final int id;

      SwitchingProvider(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
          int id) {
        this.singletonCImpl = singletonCImpl;
        this.activityRetainedCImpl = activityRetainedCImpl;
        this.id = id;
      }

      @SuppressWarnings("unchecked")
      @Override
      public T get() {
        switch (id) {
          case 0: // dagger.hilt.android.ActivityRetainedLifecycle 
          return (T) ActivityRetainedComponentManager_LifecycleModule_ProvideActivityRetainedLifecycleFactory.provideActivityRetainedLifecycle();

          default: throw new AssertionError(id);
        }
      }
    }
  }

  private static final class ServiceCImpl extends SwiftQuantumApp_HiltComponents.ServiceC {
    private final SingletonCImpl singletonCImpl;

    private final ServiceCImpl serviceCImpl = this;

    private ServiceCImpl(SingletonCImpl singletonCImpl, Service serviceParam) {
      this.singletonCImpl = singletonCImpl;


    }
  }

  private static final class SingletonCImpl extends SwiftQuantumApp_HiltComponents.SingletonC {
    private final ApplicationContextModule applicationContextModule;

    private final SingletonCImpl singletonCImpl = this;

    private Provider<UserPreferences> userPreferencesProvider;

    private Provider<BillingRepositoryImpl> billingRepositoryImplProvider;

    private Provider<HttpLoggingInterceptor> provideLoggingInterceptorProvider;

    private Provider<TokenManager> tokenManagerProvider;

    private Provider<Interceptor> provideAuthInterceptorProvider;

    private Provider<OkHttpClient> provideApiOkHttpClientProvider;

    private Provider<Json> provideJsonProvider;

    private Provider<Retrofit> provideApiRetrofitProvider;

    private Provider<AuthApi> provideAuthApiProvider;

    private Provider<AuthRepositoryImpl> authRepositoryImplProvider;

    private Provider<HybridEngineApi> provideHybridEngineApiProvider;

    private Provider<HybridEngineRepositoryImpl> hybridEngineRepositoryImplProvider;

    private Provider<QuantumApi> provideQuantumApiProvider;

    private Provider<QuantumRepositoryImpl> quantumRepositoryImplProvider;

    private Provider<ExperienceApi> provideExperienceApiProvider;

    private Provider<ExperienceRepositoryImpl> experienceRepositoryImplProvider;

    private Provider<OkHttpClient> provideBridgeOkHttpClientProvider;

    private Provider<Retrofit> provideBridgeRetrofitProvider;

    private Provider<BridgeApi> provideBridgeApiProvider;

    private Provider<HardwareRepositoryImpl> hardwareRepositoryImplProvider;

    private Provider<SharedAuthManager> sharedAuthManagerProvider;

    private Provider<QASMApi> provideQASMApiProvider;

    private Provider<QASMRepositoryImpl> qASMRepositoryImplProvider;

    private SingletonCImpl(ApplicationContextModule applicationContextModuleParam) {
      this.applicationContextModule = applicationContextModuleParam;
      initialize(applicationContextModuleParam);

    }

    @SuppressWarnings("unchecked")
    private void initialize(final ApplicationContextModule applicationContextModuleParam) {
      this.userPreferencesProvider = DoubleCheck.provider(new SwitchingProvider<UserPreferences>(singletonCImpl, 1));
      this.billingRepositoryImplProvider = DoubleCheck.provider(new SwitchingProvider<BillingRepositoryImpl>(singletonCImpl, 0));
      this.provideLoggingInterceptorProvider = DoubleCheck.provider(new SwitchingProvider<HttpLoggingInterceptor>(singletonCImpl, 6));
      this.tokenManagerProvider = DoubleCheck.provider(new SwitchingProvider<TokenManager>(singletonCImpl, 8));
      this.provideAuthInterceptorProvider = DoubleCheck.provider(new SwitchingProvider<Interceptor>(singletonCImpl, 7));
      this.provideApiOkHttpClientProvider = DoubleCheck.provider(new SwitchingProvider<OkHttpClient>(singletonCImpl, 5));
      this.provideJsonProvider = DoubleCheck.provider(new SwitchingProvider<Json>(singletonCImpl, 9));
      this.provideApiRetrofitProvider = DoubleCheck.provider(new SwitchingProvider<Retrofit>(singletonCImpl, 4));
      this.provideAuthApiProvider = DoubleCheck.provider(new SwitchingProvider<AuthApi>(singletonCImpl, 3));
      this.authRepositoryImplProvider = DoubleCheck.provider(new SwitchingProvider<AuthRepositoryImpl>(singletonCImpl, 2));
      this.provideHybridEngineApiProvider = DoubleCheck.provider(new SwitchingProvider<HybridEngineApi>(singletonCImpl, 11));
      this.hybridEngineRepositoryImplProvider = DoubleCheck.provider(new SwitchingProvider<HybridEngineRepositoryImpl>(singletonCImpl, 10));
      this.provideQuantumApiProvider = DoubleCheck.provider(new SwitchingProvider<QuantumApi>(singletonCImpl, 13));
      this.quantumRepositoryImplProvider = DoubleCheck.provider(new SwitchingProvider<QuantumRepositoryImpl>(singletonCImpl, 12));
      this.provideExperienceApiProvider = DoubleCheck.provider(new SwitchingProvider<ExperienceApi>(singletonCImpl, 15));
      this.experienceRepositoryImplProvider = DoubleCheck.provider(new SwitchingProvider<ExperienceRepositoryImpl>(singletonCImpl, 14));
      this.provideBridgeOkHttpClientProvider = DoubleCheck.provider(new SwitchingProvider<OkHttpClient>(singletonCImpl, 19));
      this.provideBridgeRetrofitProvider = DoubleCheck.provider(new SwitchingProvider<Retrofit>(singletonCImpl, 18));
      this.provideBridgeApiProvider = DoubleCheck.provider(new SwitchingProvider<BridgeApi>(singletonCImpl, 17));
      this.hardwareRepositoryImplProvider = DoubleCheck.provider(new SwitchingProvider<HardwareRepositoryImpl>(singletonCImpl, 16));
      this.sharedAuthManagerProvider = DoubleCheck.provider(new SwitchingProvider<SharedAuthManager>(singletonCImpl, 20));
      this.provideQASMApiProvider = DoubleCheck.provider(new SwitchingProvider<QASMApi>(singletonCImpl, 22));
      this.qASMRepositoryImplProvider = DoubleCheck.provider(new SwitchingProvider<QASMRepositoryImpl>(singletonCImpl, 21));
    }

    @Override
    public void injectSwiftQuantumApp(SwiftQuantumApp arg0) {
    }

    @Override
    public Set<Boolean> getDisableFragmentGetContextFix() {
      return Collections.<Boolean>emptySet();
    }

    @Override
    public ActivityRetainedComponentBuilder retainedComponentBuilder() {
      return new ActivityRetainedCBuilder(singletonCImpl);
    }

    @Override
    public ServiceComponentBuilder serviceComponentBuilder() {
      return new ServiceCBuilder(singletonCImpl);
    }

    private static final class SwitchingProvider<T> implements Provider<T> {
      private final SingletonCImpl singletonCImpl;

      private final int id;

      SwitchingProvider(SingletonCImpl singletonCImpl, int id) {
        this.singletonCImpl = singletonCImpl;
        this.id = id;
      }

      @SuppressWarnings("unchecked")
      @Override
      public T get() {
        switch (id) {
          case 0: // com.swiftquantum.data.repository.BillingRepositoryImpl 
          return (T) new BillingRepositoryImpl(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule), singletonCImpl.userPreferencesProvider.get());

          case 1: // com.swiftquantum.data.local.UserPreferences 
          return (T) new UserPreferences(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));

          case 2: // com.swiftquantum.data.repository.AuthRepositoryImpl 
          return (T) new AuthRepositoryImpl(singletonCImpl.provideAuthApiProvider.get(), singletonCImpl.tokenManagerProvider.get(), singletonCImpl.userPreferencesProvider.get());

          case 3: // com.swiftquantum.data.api.AuthApi 
          return (T) NetworkModule_ProvideAuthApiFactory.provideAuthApi(singletonCImpl.provideApiRetrofitProvider.get());

          case 4: // @javax.inject.Named("ApiRetrofit") retrofit2.Retrofit 
          return (T) NetworkModule_ProvideApiRetrofitFactory.provideApiRetrofit(singletonCImpl.provideApiOkHttpClientProvider.get(), singletonCImpl.provideJsonProvider.get());

          case 5: // @javax.inject.Named("ApiClient") okhttp3.OkHttpClient 
          return (T) NetworkModule_ProvideApiOkHttpClientFactory.provideApiOkHttpClient(singletonCImpl.provideLoggingInterceptorProvider.get(), singletonCImpl.provideAuthInterceptorProvider.get());

          case 6: // okhttp3.logging.HttpLoggingInterceptor 
          return (T) NetworkModule_ProvideLoggingInterceptorFactory.provideLoggingInterceptor();

          case 7: // @javax.inject.Named("AuthInterceptor") okhttp3.Interceptor 
          return (T) NetworkModule_ProvideAuthInterceptorFactory.provideAuthInterceptor(singletonCImpl.tokenManagerProvider.get());

          case 8: // com.swiftquantum.data.local.TokenManager 
          return (T) new TokenManager(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));

          case 9: // kotlinx.serialization.json.Json 
          return (T) NetworkModule_ProvideJsonFactory.provideJson();

          case 10: // com.swiftquantum.data.repository.HybridEngineRepositoryImpl 
          return (T) new HybridEngineRepositoryImpl(singletonCImpl.provideHybridEngineApiProvider.get());

          case 11: // com.swiftquantum.data.api.HybridEngineApi 
          return (T) NetworkModule_ProvideHybridEngineApiFactory.provideHybridEngineApi(singletonCImpl.provideApiRetrofitProvider.get());

          case 12: // com.swiftquantum.data.repository.QuantumRepositoryImpl 
          return (T) new QuantumRepositoryImpl(singletonCImpl.provideQuantumApiProvider.get());

          case 13: // com.swiftquantum.data.api.QuantumApi 
          return (T) NetworkModule_ProvideQuantumApiFactory.provideQuantumApi(singletonCImpl.provideApiRetrofitProvider.get());

          case 14: // com.swiftquantum.data.repository.ExperienceRepositoryImpl 
          return (T) new ExperienceRepositoryImpl(singletonCImpl.provideExperienceApiProvider.get());

          case 15: // com.swiftquantum.data.api.ExperienceApi 
          return (T) NetworkModule_ProvideExperienceApiFactory.provideExperienceApi(singletonCImpl.provideApiRetrofitProvider.get());

          case 16: // com.swiftquantum.data.repository.HardwareRepositoryImpl 
          return (T) new HardwareRepositoryImpl(singletonCImpl.provideBridgeApiProvider.get(), singletonCImpl.tokenManagerProvider.get());

          case 17: // com.swiftquantum.data.api.BridgeApi 
          return (T) NetworkModule_ProvideBridgeApiFactory.provideBridgeApi(singletonCImpl.provideBridgeRetrofitProvider.get());

          case 18: // @javax.inject.Named("BridgeRetrofit") retrofit2.Retrofit 
          return (T) NetworkModule_ProvideBridgeRetrofitFactory.provideBridgeRetrofit(singletonCImpl.provideBridgeOkHttpClientProvider.get(), singletonCImpl.provideJsonProvider.get());

          case 19: // @javax.inject.Named("BridgeClient") okhttp3.OkHttpClient 
          return (T) NetworkModule_ProvideBridgeOkHttpClientFactory.provideBridgeOkHttpClient(singletonCImpl.provideLoggingInterceptorProvider.get(), singletonCImpl.provideAuthInterceptorProvider.get());

          case 20: // com.swiftquantum.data.auth.SharedAuthManager 
          return (T) new SharedAuthManager(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));

          case 21: // com.swiftquantum.data.repository.QASMRepositoryImpl 
          return (T) new QASMRepositoryImpl(singletonCImpl.provideQASMApiProvider.get());

          case 22: // com.swiftquantum.data.api.QASMApi 
          return (T) NetworkModule_ProvideQASMApiFactory.provideQASMApi(singletonCImpl.provideApiRetrofitProvider.get());

          default: throw new AssertionError(id);
        }
      }
    }
  }
}
