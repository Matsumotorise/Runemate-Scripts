package Bots.Helper;

public class StopWatch {

	private long startTime = 0;
	private long stopTime = 0;
	private boolean running = false;


	public void start() {
		this.startTime = System.nanoTime();
		this.running = true;
	}


	public void stop() {
		this.stopTime = System.nanoTime();
		this.running = false;
	}


	//elaspsed time in milliseconds
	public long getElapsedTimeMS() {
		long elapsed;
		if (running) {
			elapsed = (System.nanoTime() - startTime);
		} else {
			elapsed = (stopTime - startTime);
		}
		return elapsed;
	}


	//elaspsed time in seconds
	public long getElapsedTimeSecs() {
		long elapsed;
		if (running) {
			elapsed = ((System.nanoTime() - startTime) / 1000);
		} else {
			elapsed = ((stopTime - startTime) / 1000);
		}
		return elapsed;
	}

	@Override
	public String toString() {
		return "StopWatch{" + (getElapsedTimeMS() / 3600000) + " : " + (getElapsedTimeMS() / 60000) + " : " + (
				getElapsedTimeMS() % 60000) + " }";
	}
}
