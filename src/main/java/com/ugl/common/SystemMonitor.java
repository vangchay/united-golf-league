package com.ugl.common;

import java.util.Timer;

import javax.swing.JTextArea;

/**
 *
 * @author e1042631
 */
public class SystemMonitor {
	private static final String TIMER_NAME = "System Monitor";

	private static final int STARTUP_DELAY_MS = 100;

	private static final int TIMER_PERIOD_MS = 10;

	private Timer systemTimer = null;

	private DebugWindowTimerTask debugTask = null;

	private JTextArea systemText = null;

	public SystemMonitor(JTextArea debugArea) {
		systemText = debugArea;
	}

	public void start() {
		debugTask = new DebugWindowTimerTask();
		debugTask.setText(systemText);
		systemTimer = new Timer(TIMER_NAME);
		systemTimer.schedule(debugTask, STARTUP_DELAY_MS, TIMER_PERIOD_MS);
	}

	public void stop() {
		if (systemTimer != null) {
			debugTask.reset();
			systemTimer.cancel();
		}
	}

	public DebugWindowTimerTask getDebugWindowTimerTask() {
		return debugTask;
	}
}
