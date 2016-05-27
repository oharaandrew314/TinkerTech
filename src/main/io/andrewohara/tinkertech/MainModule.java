package io.andrewohara.tinkertech;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.multibindings.Multibinder;

import io.andrewohara.tinkertech.config.Config;
import io.andrewohara.tinkertech.config.ConfigLoader;
import io.andrewohara.tinkertech.config.PropertiesConfig;
import io.andrewohara.tinkertech.config.PropertiesConfigLoader;
import io.andrewohara.tinkertech.mediators.FactorioModsMediator;
import io.andrewohara.tinkertech.mediators.Mediator;
import io.andrewohara.tinkertech.mediators.UnirestWebClient;
import io.andrewohara.tinkertech.mediators.WebClient;
import io.andrewohara.tinkertech.services.DirectoryWatchService;
import io.andrewohara.tinkertech.services.DirectoryWatchServiceImpl;
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
		bind(ConfigLoader.class).to(PropertiesConfigLoader.class);

		// Startup Tasks
		Multibinder<StartupTask> startupTasks = Multibinder.newSetBinder(binder(), StartupTask.class);
		startupTasks.addBinding().to(PropertiesConfigLoader.class);
		startupTasks.addBinding().to(DirectoryWatchServiceImpl.class);

		// Shutdown Tasks
		Multibinder<ShutdownTask> shutdownTasks = Multibinder.newSetBinder(binder(), ShutdownTask.class);
		shutdownTasks.addBinding().to(DirectoryWatchServiceImpl.class);
	}

	@Provides
	public Executor provideDownloadExecutor() {
		return Executors.newFixedThreadPool(4);
	}
}
