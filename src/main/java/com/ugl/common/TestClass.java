package com.ugl.common;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 * @author e1042631 Class to run tests.
 */
public abstract class TestClass implements Runnable {
    public static final int RUN_IDLE = 0;

    public static final int RUN_RUN = 1;

    public static final int RUN_CANCEL = 2;

    /**
     * The run state.
     */
    protected int nRunState = RUN_IDLE;

    /**
     * Indicator if the thread is running.
     */
    protected boolean bRunning = false;

    /**
     * The debug text.
     */
    protected static JTextArea debugGUITextArea = null;

    /**
     * The test number
     */
    protected int nTestNumber = 0;

    /**
     * The test name.
     */
    protected String testName = null;

    /**
     * Properties file.
     */
    protected String propertiesFile = "";

    /**
     * Constructor, pass the test number and test name.
     * 
     * @param testId
     * @param name
     */
    public TestClass(int testId, String name) {
	nTestNumber = testId;
	testName = name;
    }

    // @Override
    public abstract void run();

    public abstract void Break();

    // public abstract void Save(TestRackProperties Properties);
    public abstract void Load();

    public int getTestNumber() {
	return nTestNumber;
    }

    public static void setDebugGUI(JTextArea Text) {
	debugGUITextArea = Text;
    }

    public static JTextArea getDebugGUI() {
	return debugGUITextArea;
    }

    /**
     * Browse folder.
     * 
     * @param Item
     * @return
     */
    public static boolean BrowseFolder(JTextField Item) {
	boolean Updated = true;
	String folderText = Item.getText();
	JFileChooser fileChooser = new JFileChooser(folderText);
	fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

	int Ret = fileChooser.showSaveDialog(Item);
	if (Ret == JFileChooser.APPROVE_OPTION) {
	    File file = fileChooser.getSelectedFile();
	    String temp1 = file.getAbsolutePath();
	    if (temp1.contentEquals(folderText) == false) {
		Updated = true;
		Item.setText(temp1);
	    }
	}
	return Updated;
    }

    /**
     * Browse for a file.
     * 
     * @param Item
     * @return
     */
    public static boolean BrowseFile(JTextField Item) {
	boolean Updated = true;
	String folderText = Item.getText();
	JFileChooser fileChooser = new JFileChooser(folderText);

	int Ret = fileChooser.showSaveDialog(Item);
	if (Ret == JFileChooser.APPROVE_OPTION) {
	    File file = fileChooser.getSelectedFile();
	    String temp1 = file.getAbsolutePath();
	    if (temp1.contentEquals(folderText) == false) {
		Updated = true;
		Item.setText(temp1);
	    }
	}
	return Updated;
    }

    public void setIsRunning(boolean runFlag) {
	bRunning = runFlag;
    }

    public boolean isRunning() {
	return bRunning;
    }

    public String getTestName() {
	return testName;
    }

    public int getRunState() {
	return nRunState;
    }

    public void setRunState(int state) {
	nRunState = state;
    }

    public String getPropertiesFile() {
	return propertiesFile;
    }

    public void setPropertiesFile(String propertiesFile) {
	this.propertiesFile = propertiesFile;
    }

    public static String[] Split(String Input, String Div) {
	String[] Output = null;
	if (Input.contains(Div)) {
	    Output = Input.split(Div);
	} else {
	    Output = new String[1];
	    Output[0] = Input;
	}
	return Output;
    }

    /**
     * Indicate that the run is completed.
     */
    protected void runComplete() {
	bRunning = false;
	setRunState(RUN_IDLE);

    }

}
