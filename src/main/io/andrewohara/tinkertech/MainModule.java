package io.andrewohara.tinkertech;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.WatchService;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;

import io.andrewohara.tinkertech.config.Config;
import io.andrewohara.tinkertech.config.DevConfig;
import io.andrewohara.tinkertech.mediators.FactorioModsMediator;
import io.andrewohara.tinkertech.mediators.Mediator;
import io.andrewohara.tinkertech.mediators.UnirestWebClient;
import io.andrewohara.tinkertech.mediators.WebClient;
import io.andrewohara.tinkertech.views.DialogErrorHandler;
import io.andrewohara.tinkertech.views.ErrorHandler;
import javafx.application.Application;

public class MainModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(Application.class).to(Main.class);
		bind(Config.class).to(DevConfig.class);
		bind(WebClient.class).to(UnirestWebClient.class);
		bind(Mediator.class).to(FactorioModsMediator.class);
		bind(ErrorHandler.class).to(DialogErrorHandler.class);
	}

	@Provides
	public Executor provideDownloadExecutor() {
		return Executors.newFixedThreadPool(4);
	}

	@Provides
	public WatchService provideDirectoryWatcher(ErrorHandler errorHandler) {
		try {
			return FileSystems.getDefault().newWatchService();
		} catch (IOException e) {
			errorHandler.handleError(e);
			throw new RuntimeException(e);
		}
	}

}
