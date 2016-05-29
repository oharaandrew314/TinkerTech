package io.andrewohara.tinkertech;

import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import com.google.common.util.concurrent.Service;
import com.google.common.util.concurrent.ServiceManager;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.multibindings.Multibinder;

import io.andrewohara.tinkertech.config.Config;
import io.andrewohara.tinkertech.config.ConfigLoader;
import io.andrewohara.tinkertech.config.EditableConfig;
import io.andrewohara.tinkertech.config.PropertiesConfig;
import io.andrewohara.tinkertech.config.PropertiesConfigLoader;
import io.andrewohara.tinkertech.mediators.FactorioModsMediator;
import io.andrewohara.tinkertech.mediators.Mediator;
import io.andrewohara.tinkertech.mediators.UnirestWebClient;
import io.andrewohara.tinkertech.mediators.WebClient;
import io.andrewohara.tinkertech.services.DirectoryWatchService;
import io.andrewohara.tinkertech.services.DirectoryWatchServiceImpl;
import io.andrewohara.tinkertech.views.ConfigController;
import io.andrewohara.tinkertech.views.ConfigControllerImpl;
import io.andrewohara.tinkertech.views.DialogErrorHandler;
import io.andrewohara.tinkertech.views.ErrorHandler;
import javafx.application.Application;

public class MainModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(Application.class).to(Main.class);
		bind(WebClient.class).to(UnirestWebClient.class);
		bind(Mediator.class).to(FactorioModsMediator.class);
		bind(ErrorHandler.class).to(DialogErrorHandler.class);
		bind(DirectoryWatchService.class).to(DirectoryWatchServiceImpl.class);
		bind(Config.class).to(PropertiesConfig.class);
		bind(EditableConfig.class).to(PropertiesConfig.class);
		bind(ConfigLoader.class).to(PropertiesConfigLoader.class);
		bind(ConfigController.class).to(ConfigControllerImpl.class);

		// Services
		Multibinder<Service> services = Multibinder.newSetBinder(binder(), Service.class);
		services.addBinding().to(DirectoryWatchService.class);
	}

	@Singleton
	@Provides
	public ServiceManager provideServiceManager(Set<Service> services) {
		return new ServiceManager(services);
	}

	@Provides
	public Executor provideDownloadExecutor() {
		return Executors.newFixedThreadPool(4);
	}
}
