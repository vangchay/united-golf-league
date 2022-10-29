/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ugl.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintStream;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JLabel;
import javax.swing.JTextArea;

/**
 * This class re-directs standard out to a TextArea.
 * 
 * @author e1042631
 */
public class DebugWindowTimerTask extends TimerTask {
	private static final String CR = "\n";

	private JTextArea debugText = null;

	private JLabel runLabel = null;

	private int runState = 0;

	private int tenMsCounter = 0;

	private PipedOutputStream debugOutputStream = null;

	private PipedInputStream debugInputStream = null;

	private BufferedReader stdoutReader = null;

	DebugWindowTimerTask() {
		init();
	}

	public final void init() {
		debugOutputStream = new PipedOutputStream();
		System.setOut(new PrintStream(debugOutputStream));
		try {
			debugInputStream = new PipedInputStream(debugOutputStream);
		} catch (IOException ex) {
			Logger.getLogger(DebugWindowTimerTask.class.getName()).log(Level.SEVERE, null, ex);
		}
		stdoutReader = new BufferedReader(new InputStreamReader(debugInputStream));
	}

	public void setText(JTextArea text) {
		debugText = text;
	}

	public void reset() {
		stdoutReader = null;
		PrintStream org = System.out;
		System.setOut(org);
	}

	private void updateRunState() {
		if (runLabel != null) {
			switch (runState) {
			case 0:
				runLabel.setText("Run.");
				runState++;
				break;
			case 1:
				runLabel.setText("Run..");
				runState++;
				break;
			case 2:
				runLabel.setText("Run...");
				runState++;
				break;
			case 3:
				runLabel.setText("Run....");
				runState = 0;
				break;
			default:
				runState = 0;
				break;
			}
		}
	}

	@Override
	public void run() {
		if (stdoutReader == null) {
			return;
		}

		if (++tenMsCounter == 5) {
			tenMsCounter = 0;
			updateRunState();
		}

		try {
			// if (stdoutReader.ready()) {
			String debugLine = stdoutReader.readLine();
			if (!debugLine.isEmpty()) {
				String temp = debugText.getText();
				debugText.setText(temp + debugLine + CR);
			}
			// }
		} catch (IOException ex) {
			// Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE,
			// null, ex);
		}
	}

	public void setRunLabel(JLabel runLabel) {
		this.runLabel = runLabel;
	}
}