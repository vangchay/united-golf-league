package com.unitedgolfleague;

import java.awt.Choice;
import java.awt.EventQueue;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.prefs.Preferences;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import com.ugl.common.Cout;
import com.ugl.common.SystemMonitor;
import com.ugl.common.ToolCSS;
import com.ugl.handicap.HMGReport;
import com.ugl.handicap.Player;
import com.ugl.handicap.Schedule;

import net.miginfocom.swing.MigLayout;

public class SchedulerGUI {
	private final static String CLASS_NAME = "SchedulerGUI.";

	private final static String PREF_X = CLASS_NAME + "X";

	private final static String PREF_DX = CLASS_NAME + "DX";

	private final static String PREF_Y = CLASS_NAME + "Y";

	private final static String PREF_DY = CLASS_NAME + "DY";

	private final static String PREF_PLAYER1 = CLASS_NAME + "PLAYER1";

	private final static String PREF_PLAYER2 = CLASS_NAME + "PLAYER2";

	private final static String PREF_MATCH = CLASS_NAME + "MATCH";

	private final static String PREF_WEEK = CLASS_NAME + "WEEK";

	private String MAIN = CLASS_NAME + "main() start...";

	private JFrame frmSchedulerGuiV;

	private Choice choicePlayer1;

	private Choice choicePlayer2;

	private Choice choiceMatchNumber;

	private Choice choiceWeek;

	private JLabel lblMatch;

	private JLabel lblWeek;

	private SystemMonitor MD = null;

	private List<Schedule> matchSchedule;

	private List<Player> playerLst;

	private String scheduleFileName;

	private JTextArea txtrDebug;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SchedulerGUI window = new SchedulerGUI();
					window.frmSchedulerGuiV.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public SchedulerGUI() {
		initialize();
		// **CUSTOM**
		initSettings();
	}

	/**
	 * Init customer settings.
	 */
	private void initSettings() {
		HMGProperties.load(null);
		int Weeks = HMGProperties.numberOfWeeks;
		int numPlayers = HMGProperties.numberOfPlayers;
		int holes = HMGProperties.numberHoles;
		for (int i = 1; i <= Weeks; i++) {
			choiceWeek.add(Integer.toString(i));
		}
		for (int i = 1; i < numPlayers; i++) {
			choiceMatchNumber.add(Integer.toString(i));
		}
		HMGReport.setTotalWeeks(Weeks);
		if (holes == 9) {
			HMGReport.setNineHoles(true);
		} else {
			HMGReport.setNineHoles(false);
		}
		String dir = HMGProperties.defaultDir;
		String playerFile = dir + File.separator + "properties" + File.separator + HMGProperties.PLAYER_FILE;
		playerLst = new ArrayList<Player>();
		Player.parsePlayers(playerFile, playerLst);
		scheduleFileName = HMGProperties.defaultReports + File.separator + "Schedule_Week";
		matchSchedule = new ArrayList<Schedule>();
		// Schedule.parseSchedule(scheduleFileName, matchSchedule);

		for (Player p : playerLst) {
			choicePlayer1.add(p.getName());
			choicePlayer2.add(p.getName());
		}

		// PLAYER1
		String val = getPreference(PREF_PLAYER1);
		if (val != null && val.isEmpty() == false) {
			try {
				int i = Integer.parseInt(val);
				if (i > playerLst.size()) {
					i = 0;
				}
				choicePlayer1.select(i);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		// PLAYER2
		val = getPreference(PREF_PLAYER2);
		if (val != null && val.isEmpty() == false) {
			try {
				int i = Integer.parseInt(val);
				if (i > playerLst.size()) {
					i = 0;
				}
				choicePlayer2.select(i);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		// MATCH
		val = getPreference(PREF_MATCH);
		if (val != null && val.isEmpty() == false) {
			try {
				int i = Integer.parseInt(val);
				if (i > playerLst.size()) {
					i = 0;
				}
				choiceMatchNumber.select(i);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		// WEEK
		val = getPreference(PREF_WEEK);
		if (val != null && val.isEmpty() == false) {
			try {
				int i = Integer.parseInt(val);
				choiceWeek.select(i);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		// START SYSTEM MONITOR
		MD = new SystemMonitor(txtrDebug);
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		MD.start();
		Cout.outString(MAIN + "Ready!");
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		// **CUSTOM**
		int x = 100;
		int y = 100;
		int dx = 1012;
		int dy = 437;

		String val = getPreference(PREF_X);
		try {
			if (val != null) {
				x = Integer.parseInt(val);
			}
			val = getPreference(PREF_DX);
			if (val != null) {
				dx = Integer.parseInt(val);
			}
			val = getPreference(PREF_Y);
			if (val != null) {
				y = Integer.parseInt(val);
			}
			val = getPreference(PREF_DY);
			if (val != null) {
				dy = Integer.parseInt(val);
			}
		} catch (Exception ex) {
		}
		// **CUSTOM**

		frmSchedulerGuiV = new JFrame();
		frmSchedulerGuiV.setFont(ToolCSS.getTitleFont());
		frmSchedulerGuiV.setTitle("Scheduler GUI v1.00");
		frmSchedulerGuiV.setBounds(x, y, dx, dy);
		frmSchedulerGuiV.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmSchedulerGuiV.getContentPane().setLayout(new MigLayout("", "[167px][287px,grow]",
		        "[22px][18px][22px][22px][22px][22px][22px][6px][31px][13px][31px,grow]"));
		// **CUSTOM***
		frmSchedulerGuiV.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				Rectangle rec = frmSchedulerGuiV.getBounds();
				try {
					setPreference(PREF_X, Integer.toString(rec.x));
					setPreference(PREF_DX, Integer.toString(rec.width));
					setPreference(PREF_Y, Integer.toString(rec.y));
					setPreference(PREF_DY, Integer.toString(rec.height));
				} catch (Exception ex) {
					ex.printStackTrace();
				}
				if (MD != null) {
					MD.stop();
				}
			}
		});

		choicePlayer1 = new Choice();
		choicePlayer1.setFont(ToolCSS.getFont());
		frmSchedulerGuiV.getContentPane().add(choicePlayer1, "cell 0 0,growx,aligny top");

		choicePlayer2 = new Choice();
		choicePlayer2.setFont(ToolCSS.getFont());
		frmSchedulerGuiV.getContentPane().add(choicePlayer2, "cell 0 2,growx,aligny top");

		lblMatch = new JLabel("Match");
		lblMatch.setFont(ToolCSS.getFont());
		frmSchedulerGuiV.getContentPane().add(lblMatch, "cell 0 3");

		choiceMatchNumber = new Choice();
		choiceMatchNumber.setFont(ToolCSS.getFont());
		frmSchedulerGuiV.getContentPane().add(choiceMatchNumber, "cell 0 4,growx,aligny top");

		lblWeek = new JLabel("Week");
		lblWeek.setFont(ToolCSS.getFont());
		frmSchedulerGuiV.getContentPane().add(lblWeek, "cell 0 5");

		choiceWeek = new Choice();
		choiceWeek.setFont(ToolCSS.getFont());
		frmSchedulerGuiV.getContentPane().add(choiceWeek, "cell 0 6,growx,aligny top");

		JButton btnAdd = new JButton("Add..");
		btnAdd.setFont(ToolCSS.getFont());
		btnAdd.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				savePref();
				updateSchedule();
			}
		});
		frmSchedulerGuiV.getContentPane().add(btnAdd, "cell 0 8,grow");

		JButton btnGenerateReport = new JButton("Generate Schedule...");
		btnGenerateReport.setFont(ToolCSS.getFont());
		btnGenerateReport.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				savePref();
				generateScheduleList(matchSchedule);
			}
		});
		frmSchedulerGuiV.getContentPane().add(btnGenerateReport, "cell 0 10,growx,aligny top");

		JScrollPane scrollPane = new JScrollPane();
		frmSchedulerGuiV.getContentPane().add(scrollPane, "cell 1 0 1 11,grow");

		txtrDebug = new JTextArea();
		txtrDebug.setFont(ToolCSS.getDebugFont());
		scrollPane.setViewportView(txtrDebug);
		txtrDebug.setText("DEBUG");
	}

	protected void updateSchedule() {
		Schedule s = new Schedule();
		s.ID1 = choicePlayer1.getSelectedIndex() + 1;
		s.ID2 = choicePlayer2.getSelectedIndex() + 1;
		s.Week = choiceWeek.getSelectedIndex() + 1;
		int No = choiceMatchNumber.getSelectedIndex() + 1;
		Player p1 = HMGReport.findPlayer(s.ID1, playerLst);
		Player p2 = HMGReport.findPlayer(s.ID2, playerLst);
		String msg = String.format(" Match %d %s vs %s", No, p1.getName(), p2.getName());

		for (Schedule match : matchSchedule) {
			if (match.ID1 == s.ID1 && match.ID2 == s.ID2 && match.Week == s.Week) {
				Cout.outString("ERROR match already in schedule..." + msg);
				return;
			}
		}

		matchSchedule.add(s);
		Cout.outString("OK Match added..." + msg);
		choiceMatchNumber.select(choiceMatchNumber.getSelectedIndex() + 1);
	}

	protected void generateScheduleList(List<Schedule> schLst) {
		final String header = "****WEEK,ID1,ID2";
		Timestamp todaytm = new Timestamp(new Date().getTime());
		String yyyymmdd = new SimpleDateFormat("MM-dd-yyyy").format(todaytm);
		List<String> rpt = new ArrayList<String>();
		rpt.add(header);
		for (Schedule s : schLst) {
			rpt.add(s.toString());
		}
		int Week = choiceWeek.getSelectedIndex() + 1;
		try {
			String theFileName = scheduleFileName + Integer.toString(Week) + ".csv";
			Cout.outString("Write " + theFileName);
			FileUtils.writeLines(new File(theFileName), rpt);
		} catch (IOException e) {
			e.printStackTrace();
		}

		String matchSchFile = "matchSchedule_" + yyyymmdd + ".txt";

		String schedReportFile = FilenameUtils.concat(HMGProperties.defaultReports, matchSchFile);
		Cout.outString("Write to " + schedReportFile);
		rpt = new ArrayList<String>();
		int week = -1;
		int match = 1;
		String msg = null;

		for (Schedule s : schLst) {
			if (week != s.Week) {
				week = s.Week;
				msg = String.format("\n*****Match Play Week %d****", week);
				rpt.add(msg);
				match = 1;
			}
			Player p1 = HMGReport.findPlayer(s.ID1, playerLst);
			String Name1 = "**BYE**";
			if (p1 != null) {
				Name1 = p1.getName();
			}
			Player p2 = HMGReport.findPlayer(s.ID2, playerLst);
			String Name2 = "**BYE**";
			if (p2 != null) {
				Name2 = p2.getName();
			}
			msg = String.format("Match %d - %s vs %s.", match, Name1, Name2);
			rpt.add(msg);
			match++;
		}
		try {
			FileUtils.writeLines(new File(schedReportFile), rpt);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void savePref() {
		int var = choicePlayer1.getSelectedIndex();
		setPreference(PREF_PLAYER1, Integer.toString(var));
		var = choicePlayer2.getSelectedIndex();
		setPreference(PREF_PLAYER2, Integer.toString(var));
		var = choiceMatchNumber.getSelectedIndex();
		setPreference(PREF_MATCH, Integer.toString(var));
		var = choiceWeek.getSelectedIndex();
		setPreference(PREF_WEEK, Integer.toString(var));
	}

	/**
	 * Set preference for tool.
	 * 
	 * @param key
	 * @param value
	 */
	private void setPreference(String key, String value) {
		Preferences pref = Preferences.systemNodeForPackage(getClass());
		pref.put(key, value);
	}

	/**
	 * Get preference for tool.
	 * 
	 * @param key
	 * @return
	 */
	private String getPreference(String key) {
		String value = null;
		Preferences pref = Preferences.systemNodeForPackage(getClass());
		value = pref.get(key, "");
		return value;
	}
}
