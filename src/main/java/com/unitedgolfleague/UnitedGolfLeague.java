
package com.unitedgolfleague;

import java.awt.Button;
import java.awt.Choice;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Rectangle;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import com.ugl.common.Cout;
import com.ugl.common.INIFile;
import com.ugl.common.SystemMonitor;
import com.ugl.common.ToolCSS;
import com.ugl.handicap.Course;
import com.ugl.handicap.HMGReport;
import com.ugl.handicap.Player;
import com.ugl.handicap.SchedUtil;
import com.ugl.handicap.Schedule;
import com.ugl.handicap.ScoreCards;

import net.miginfocom.swing.MigLayout;

public class UnitedGolfLeague {
	private final static String CLASS_NAME = "UnitedGolfLeague.";

	private final static String VERSION = CLASS_NAME + ToolCSS.VERSION;

	private final static String MAIN = CLASS_NAME + "main()";

	private final static String PREF_X = CLASS_NAME + "X";

	private final static String PREF_DX = CLASS_NAME + "DX";

	private final static String PREF_Y = CLASS_NAME + "Y";

	private final static String PREF_DY = CLASS_NAME + "DY";

	// private final static String PREF_SCORECARD = CLASS_NAME + "PREF_SCORECARD";
	private final static String PREF_PLAYERSELECT = CLASS_NAME + "PREF_PLAYERSELECT";

	private final static String PREF_WEEKSELECT = CLASS_NAME + "WEEK_SELECT";

	private final static String DELIMITER = ", ";

	private final static String SCORE_HEADER1 = "****template for scorecard****";

	private final static String SCORE_HEADER2 = "****ID,NAME,WEEK,H1,H2,H3,H4,H5,H6,H7,H8,H9,H10,H11,H12,H13,H14,H15,H16,H17,H18,TOTAL";

	private final static int WEEKS = 12;

	static INIFile myIniFile = null;

	private SystemMonitor MD = null;

	private JFrame frame;

	private Choice choicePlayer;

	private Choice choiceWeek;

	private JTextArea txtrDebug;

	private String test_directory;

	private String courseFile;

	private String playersFile;

	private String scoreCardFile;

	private String scheduleFile;

	private List<Course> courseList = new ArrayList<Course>();

	private List<ScoreCards> scoresList = new ArrayList<ScoreCards>();

	private List<Schedule> matchSchedule = new ArrayList<Schedule>();

	private List<Player> playerList = new ArrayList<Player>();

	private JTable table_Scores;

	private JPanel panel;

	private JTextField textScoreCard;

	private JButton btnAdd;

	private String reportFile;

	private String playersHTMLFile;

	private JButton btnGenSchedule;

	private JTextField txtWeeks;

	private JTextField txtHpisample;

	private Button btnGenerateReport;

	private JButton btnClearDebug;

	private JButton btnBrowseFile;

	private JLabel lblWeek;

	private JLabel lblSelectPlayer;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UnitedGolfLeague window = new UnitedGolfLeague();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public UnitedGolfLeague() {
		// testcode(vs)
		myIniFile = new INIFile(HMGProperties.HMG_INI_FILE);
		// testcode(vs)
		initialize();
		// **CUSTOM**
		initSettings();
	}

	/**
	 * Init customer settings.
	 */
	private void initSettings() {
		// Load the default properties
		HMGProperties.load(null);
		// Set weeks in league
		int totalWeeks = HMGProperties.numberOfWeeks;
		txtWeeks.setText(Integer.toString(totalWeeks));
		for (int i = 1; i <= totalWeeks; i++) {
			choiceWeek.add(Integer.toString(i));
		}
		HMGReport.setTotalWeeks(totalWeeks);

		// Set number of HPI samples
		int sample = HMGProperties.HPISample;
		txtHpisample.setText(Integer.toString(sample));
		HMGReport.setHPIScores(sample);
		// Get NINE holes?
		int nineHole = HMGProperties.numberHoles;
		if (nineHole == 9) {
			HMGReport.setNineHoles(true);
		} else {
			HMGReport.setNineHoles(false);
		}

		test_directory = HMGProperties.defaultDir;
		String report_dir = HMGProperties.defaultReports;
		/**
		 * CUSTOM Set preference
		 */
		Timestamp todaytm = new Timestamp(new Date().getTime());
		String yyyymmdd = new SimpleDateFormat("MM-dd-yyyy").format(todaytm);

		courseFile = FilenameUtils.concat(test_directory, "course.csv");
		playersFile = FilenameUtils.concat(test_directory, "players.csv");
		scoreCardFile = FilenameUtils.concat(test_directory, "scorecards.csv");
		scheduleFile = FilenameUtils.concat(test_directory, "schedule.csv");
		reportFile = FilenameUtils.concat(report_dir, "matchPlay_" + yyyymmdd + ".html");
		playersHTMLFile = FilenameUtils.concat(report_dir, "players_" + yyyymmdd + ".html");

		textScoreCard.setText(scoreCardFile);
		Course.parseCourse(courseFile, courseList);
		Player.parsePlayers(playersFile, playerList);
		for (Player p : playerList) {
			choicePlayer.add(p.getName());
		}
		ScoreCards.parseScoreCard(scoreCardFile, scoresList);
		Schedule.parseSchedule(scheduleFile, matchSchedule);

		String x = getPreference(PREF_PLAYERSELECT);
		int i = 0;
		if (x.isEmpty() == false) {
			i = Integer.parseInt(x);
			// choicePlayer.select(i);
		}

		x = getPreference(PREF_WEEKSELECT);
		if (x.isEmpty() == false) {
			i = Integer.parseInt(x);
			choiceWeek.select(i);
		}

		// updateScoreCard();

		txtrDebug.setText("");
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

		frame = new JFrame();
		frame.setBounds(x, y, dx, dy);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle(VERSION);
		// **CUSTOM***
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				Rectangle rec = frame.getBounds();
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
				// testcode(vs)
				myIniFile.update();
			}
		});
		frame.setFont(ToolCSS.getTitleFont());

		choicePlayer = new Choice();
		choicePlayer.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				updateScoreCard();
				txtrDebug.setText("Player change!");
			}
		});
		choicePlayer.setFont(ToolCSS.getFont());

		frame.getContentPane().setLayout(new MigLayout("", "[250px][25px][129px][14px][130px][12px][200px][82px][grow]",
		        "[26px][16px][22px][6px][16px][6px][26px][500px,grow]"));

		btnGenerateReport = new Button("Gen Report");
		btnGenerateReport.setFont(ToolCSS.getFont());
		btnGenerateReport.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				txtrDebug.setText("GenReport!");
				genReport();
			}
		});
		btnGenerateReport.setFont(ToolCSS.getFont());

		btnGenerateReport.setActionCommand("");
		frame.getContentPane().add(btnGenerateReport, "cell 6 0,growx,aligny top");

		txtWeeks = new JTextField();
		txtWeeks.setEnabled(false);
		txtWeeks.setToolTipText("Number of weeks in league.");
		txtWeeks.setText("12");
		frame.getContentPane().add(txtWeeks, "cell 7 0,growx");
		txtWeeks.setColumns(10);
		txtWeeks.setFont(ToolCSS.getFont());

		btnClearDebug = new JButton("Clear Debug");
		btnClearDebug.setFont(ToolCSS.getFont());
		btnClearDebug.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				txtrDebug.setText("");
			}
		});
		btnClearDebug.setFont(ToolCSS.getFont());

		frame.getContentPane().add(btnClearDebug, "cell 8 0");

		btnGenSchedule = new JButton("Gen Schedule");
		btnGenSchedule.setToolTipText("Generate the schedule.");
		btnGenSchedule.setFont(ToolCSS.getFont());
		btnGenSchedule.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				Cout.outString("Gen Scheduled");
				genSchedule();
			}
		});

		btnAdd = new JButton("Add Score");
		frame.getContentPane().add(btnAdd, "cell 2 1,growx,aligny top");
		btnAdd.setToolTipText("Add new score to score card.");
		btnAdd.setFont(ToolCSS.getFont());
		btnAdd.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				txtrDebug.setText("Add Score!");
				int playerSelect = choiceWeek.getSelectedIndex();
				Cout.outString(String.format("Player %d", playerSelect));
				String x = Integer.toString(choicePlayer.getSelectedIndex());
				setPreference(PREF_PLAYERSELECT, x);
				x = Integer.toString(choiceWeek.getSelectedIndex());
				setPreference(PREF_WEEKSELECT, x);
				addPlayerScores();
				genScoreCard();
			}
		});
		btnAdd.setFont(ToolCSS.getFont());
		btnGenSchedule.setFont(ToolCSS.getFont());
		frame.getContentPane().add(btnGenSchedule, "cell 6 1,growx");

		txtHpisample = new JTextField();
		txtHpisample.setEnabled(false);
		txtHpisample.setToolTipText("Number of scores to sample for HPI.");
		txtHpisample.setText("4");
		frame.getContentPane().add(txtHpisample, "cell 7 1,growx");
		txtHpisample.setColumns(10);
		txtHpisample.setFont(ToolCSS.getFont());
		frame.getContentPane().add(choicePlayer, "cell 0 2,growx,aligny top");

		choiceWeek = new Choice();
		choiceWeek.setFont(ToolCSS.getFont());
		choiceWeek.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				updateScoreCard();
				txtrDebug.setText("Week change!");
			}
		});
		choiceWeek.setFont(ToolCSS.getFont());
		frame.getContentPane().add(choiceWeek, "cell 0 6,growx,aligny top");

		JScrollPane scrollPane = new JScrollPane();
		frame.getContentPane().add(scrollPane, "cell 0 7 9 1,grow");

		txtrDebug = new JTextArea();
		txtrDebug.setFont(ToolCSS.getDebugFont());
		txtrDebug.setToolTipText("Debug output.");
		// txtrDebug.setFont(new Font("Courier New", Font.PLAIN, 15));
		txtrDebug.setLineWrap(true);
		txtrDebug.setText("Debug");
		txtrDebug.setFont(ToolCSS.getDebugFont());
		scrollPane.setViewportView(txtrDebug);

		panel = new JPanel();
		panel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "ScoreCard", TitledBorder.LEADING,
		        TitledBorder.TOP, null, new Color(0, 0, 0)));
		frame.getContentPane().add(panel, "cell 2 2 7 5,grow");
		panel.setLayout(null);

		table_Scores = new JTable();
		table_Scores.setFont(ToolCSS.getFont());
		table_Scores.setSurrendersFocusOnKeystroke(true);
		table_Scores.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table_Scores.setBounds(6, 18, 588, 34);
		table_Scores.setFont(ToolCSS.getFont());
		panel.add(table_Scores);
		table_Scores.setModel(new DefaultTableModel(new Object[][] {
		        { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18" },
		        { null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
		                null }, },
		        new String[] { "New column", "New column", "New column", "New column", "New column", "New column",
		                "New column", "New column", "New column", "New column", "New column", "New column",
		                "New column", "New column", "New column", "New column", "New column", "New column" }));

		lblSelectPlayer = new JLabel("Select Player");
		lblSelectPlayer.setFont(ToolCSS.getFont());
		frame.getContentPane().add(lblSelectPlayer, "cell 0 1,alignx left,aligny top");

		lblWeek = new JLabel("Select week.");
		lblWeek.setFont(ToolCSS.getFont());
		frame.getContentPane().add(lblWeek, "cell 0 4,alignx left,aligny top");

		textScoreCard = new JTextField();
		textScoreCard.setEnabled(false);
		textScoreCard.setToolTipText(
		        "Link to the score card file. All the files (course.csv, players.csv, and schedule.csv) should be contained in this directory.");
		textScoreCard.setFont(ToolCSS.getFont());
		frame.getContentPane().add(textScoreCard, "cell 0 0 3 1,growx,aligny center");
		textScoreCard.setColumns(10);

		btnBrowseFile = new JButton("Browse File...");
		btnBrowseFile.setEnabled(false);
		btnBrowseFile.setFont(ToolCSS.getFont());
		btnBrowseFile.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// TestClass.BrowseFile(textScoreCard);
				// setPreference(PREF_SCORECARD, textScoreCard.getText());
			}
		});
		btnBrowseFile.setFont(ToolCSS.getFont());
		frame.getContentPane().add(btnBrowseFile, "cell 4 0,growx,aligny bottom");
	}

	/**
	 * Generate report...
	 */
	private void genReport() {
		Course golf = courseList.get(0);
		int weekNum = choiceWeek.getSelectedIndex() + 1;
		List<String> var = HMGReport.generateMatchReport(golf, matchSchedule, scoresList, playerList, weekNum);

		// DEBUG...
		// for (String s: var) {
		// cout.outString(s);
		// }

		try {
			FileUtils.writeLines(new File(reportFile), var);
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Calculate HPI
		for (Player p : playerList) {
			p.calculateScores(true, golf);

		}

		var = HMGReport.generatePlayerCSV(golf, playerList, true);
		try {
			FileUtils.writeLines(new File(playersFile), var);
		} catch (IOException e) {
			e.printStackTrace();
		}

		var = HMGReport.generatePlayerHTML(golf, playerList, true);
		try {
			FileUtils.writeLines(new File(playersHTMLFile), var);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Generate the schedule...
	 */
	private void genSchedule() {
		List<Schedule> sch = SchedUtil.generateSchedule(1, HMGProperties.numberOfWeeks, playerList, matchSchedule);
		final String header = "****WEEK,ID1,ID2";
		List<String> rpt = new ArrayList<String>();
		rpt.add(header);
		for (Schedule s : sch) {
			rpt.add(s.toString());
		}
		try {
			FileUtils.writeLines(new File(scheduleFile), rpt);
		} catch (IOException e) {
			e.printStackTrace();
		}
		String schedReportFile = FilenameUtils.concat(test_directory, "matchSchedule.txt");
		rpt = new ArrayList<String>();
		int week = -1;
		int match = 1;
		String msg = null;

		for (Schedule s : sch) {
			if (week != s.Week) {
				week = s.Week;
				msg = String.format("\n*****Match Play Week %d****", week);
				rpt.add(msg);
				match = 1;
			}
			Player p1 = HMGReport.findPlayer(s.ID1, playerList);
			String Name1 = "**BYE**";
			if (p1 != null) {
				Name1 = p1.getName();
			}
			Player p2 = HMGReport.findPlayer(s.ID2, playerList);
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

	/**
	 * Update the score card...
	 */
	private void updateScoreCard() {
		for (int x = 0; x < 18; x++) {
			table_Scores.setValueAt("", 1, x);
		}
		String name = choicePlayer.getSelectedItem().trim();
		int week = Integer.parseInt(choiceWeek.getSelectedItem());
		int i = 0;
		for (ScoreCards item : scoresList) {
			if (item.Name.equalsIgnoreCase(name) && item.Week == week) {
				for (; i < 18; i++) {
					Integer val = item.Scores[i];
					if (val != null) {
						table_Scores.setValueAt(val, 1, i);
					}
				}
				break;
			}
		}
		for (; i < 18; i++) {
			table_Scores.setValueAt("", 1, i);
		}
	}

	/**
	 * Generate and save the score card...
	 */
	private void genScoreCard() {
		List<String> lines = new ArrayList<String>();
		lines.add(SCORE_HEADER1);
		lines.add(SCORE_HEADER2);

		Collections.sort(scoresList);

		for (ScoreCards item : scoresList) {
			item.updateTotal();
			String msg = String.format("%d, %s, %d", item.ID, item.Name, item.Week);
			for (int i = 0; i < 18; i++) {
				Integer Val = item.Scores[i];
				if (Val != null) {
					msg += DELIMITER + Val.toString();
				} else {
					msg += DELIMITER;
				}
			}
			msg += DELIMITER + item.Total.toString();
			lines.add(msg);
		}
		try {
			FileUtils.writeLines(new File(scoreCardFile), lines);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Add player scores.
	 */
	protected void addPlayerScores() {
		ScoreCards card = new ScoreCards();
		String player = choicePlayer.getSelectedItem().trim();
		card.ID = getPlayerID(player);
		if (card.ID == -1) {
			Cout.outString("Player ID error!");
			return;
		}
		card.Name = player;
		card.Week = choiceWeek.getSelectedIndex() + 1;

		// Get the 18 hole score...
		for (int i = 0; i < 18; i++) {
			String val = null;
			try {
				val = table_Scores.getValueAt(1, i).toString();
				if (val.isEmpty() == false) {
					card.Scores[i] = new Integer(Integer.parseInt(val));
				}
			} catch (Exception ex) {
				continue;
			}
		}
		addScoreCard(card);
	}

	/**
	 * Add the score card.
	 * 
	 * @param srd
	 */
	private void addScoreCard(ScoreCards srd) {
		int i = 0;
		for (ScoreCards score : scoresList) {
			if (score.ID == srd.ID && score.Week == srd.Week) {
				scoresList.remove(i);
				break;
			}
			i++;
		}
		scoresList.add(srd);
	}

	/**
	 * Find the player ID for giving player.
	 * 
	 * @param player
	 * @return
	 */
	private int getPlayerID(String player) {
		for (Player currentPlayer : playerList) {
			if (currentPlayer.getName().compareToIgnoreCase(player) == 0) {
				return currentPlayer.getID();
			}
		}
		return -1;
	}

	/**
	 * Set preference for tool.
	 * 
	 * @param key
	 * @param value
	 */
	private void setPreference(String key, String value) {
		// Preferences pref = Preferences.systemNodeForPackage(getClass());
		// pref.put(key, value);
		// testcode(vs)
		myIniFile.setSettings(key, value);
	}

	/**
	 * Get preference for tool.
	 * 
	 * @param key
	 * @return
	 */
	private String getPreference(String key) {
		String value = null;
		// Preferences pref = Preferences.systemNodeForPackage(getClass());
		// value = pref.get(key, "");
		value = myIniFile.getSetting(key);
		return value;
	}
}
