package io.andrewohara.tinkertech.models;

public interface Download {
	
	double getProgress();
	int getDownloadSize();
	boolean isComplete();
	Listing getListing();
}
