package com.ugl.speadsheet;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.google.common.collect.Multimap;
import com.ugl.common.Cout;
import com.ugl.common.Utils;
import com.ugl.handicap.Course;
import com.ugl.handicap.HMGReport;
import com.ugl.handicap.Player;
import com.ugl.handicap.Schedule;
import com.ugl.handicap.ScoreCards;
import com.ugl.handicap.UnitedCupStandings;
import com.unitedgolfleague.HMGProperties;

/**
 * @author e1042631
 *
 */
public class PlayersSpreedsheet {
	private final static String CLASS_NAME = "PlayersSpreedsheet.";

	private final static String INIT = CLASS_NAME + "Init().";

	private final static String MAIN = CLASS_NAME + "main().";

	private final static String PLAYERS = "players";

	private final static String SCHEDULE = "schedule";

	private final static String COURSE = "course";

	private final static String WEEK = "week";

	private final static String UNITED_CUP_STANDINGS = "United Cup Standings";

	public static List<Player> ListPlayers = null;

	public static List<ScoreCards> ListScoreCards = new ArrayList<ScoreCards>();

	public static List<Course> ListCourses = null;

	public static List<Schedule> ListSchedules = null;

	public static List<UnitedCupStandings> ListUCS = null;

	public static Course uglCourse = null;

	public static int nWeeksInLeague = 0;

	private static String yyyymmdd;

	private static int nSheetWeek = 0;

	private static int CELL_POINTS;

	private static int CELL_ID;

	private static int CELL_NAME;

	private static int CELL_PHONE;

	private static int CELL_FIRST_HPI;

	private static int CELL_HPI;

	private static int CELL_18_HP;

	private static int CELL_9_HP;

	private static int CELL_AVE;

	private static int CELL_STD;

	private static int CELL_EMAIL;

	private static Integer[] CELL_WEEKLY_TABLE;

	/**
	 * Update the players and united cup standings...
	 * 
	 * @param sheetName
	 */
	public static void Update(String fileName) {
		if (ListPlayers == null) {
			Cout.outString("ERROR - Cannot update, the spreedsheet has not be initilized.");
		} else {
			Player.nSortBy = Player.SORT_UNITED_CUP;
			Collections.sort(ListPlayers);
			if (ListUCS.isEmpty()) {
				int Rank = 1;
				for (Player p : ListPlayers) {
					UnitedCupStandings u = new UnitedCupStandings();
					u.thePlayer = p;
					u.rank = Rank;
					u.SetRanking(Rank++);
					ListUCS.add(u);
				}
			} else {
				// set the initial rankings...
				int Rank = 1;
				for (Player p : ListPlayers) {
					UnitedCupStandings u = findUCS(p);
					u.SetRanking(Rank++);

				}

				// ...calculate tournament...
				for (UnitedCupStandings usc : ListUCS) {
					double slope = (double) HMGProperties.tournamentSlope;
					usc.TourneyHCP = (int) Math.round((usc.hpi * slope) / 113.00);
					usc.EffectiveScore = usc.TourneyScore - usc.TourneyHCP;
				}

				// tally the points
				List<UnitedCupStandings> tally = new ArrayList<UnitedCupStandings>();
				for (UnitedCupStandings usc : ListUCS) {
					if (usc.TourneyScore > 0) {
						tally.add(usc);
					}
				}

				// FIGURE OUT THE ADDITONAL POINTS..
				if (tally.isEmpty() == false) {
					// sort by scores...
					UnitedCupStandings.Sort = 1;
					Collections.sort(tally);
					// collect additional points...
					int total = HMGProperties.UnitedCupPointsTable.length;
					int index = 0;
					for (UnitedCupStandings usc : tally) {
						usc.TourneyPoints = HMGProperties.UnitedCupPointsTable[index++];
						usc.TotalPoints = usc.points + usc.TourneyPoints;
						if (index == total) {
							break;
						}
					}
					UnitedCupStandings.Sort = 2;
				}

				Collections.sort(ListUCS);

			}
			Workbook workbook;
			try {
				FileInputStream finput = new FileInputStream(fileName);
				workbook = WorkbookFactory.create(finput);
				int sheets = workbook.getNumberOfSheets();
				Cout.outString(INIT + yyyymmdd + " Workbook has " + Integer.toString(sheets) + " Sheets :");
				Iterator<Sheet> sheetIterator = workbook.sheetIterator();
				while (sheetIterator.hasNext()) {
					Sheet sheet = sheetIterator.next();
					String sheetName = sheet.getSheetName();
					Cout.outString("Process> " + sheet.getSheetName());
					if (sheetName.contentEquals(PLAYERS)) {
						UpdatePlayerSheet(sheet);
					} else if (sheetName.equalsIgnoreCase(UNITED_CUP_STANDINGS)) {
						UpdateUnitedCup(sheet);
					}
				}

				finput.close();

				FileOutputStream outputStream = new FileOutputStream(fileName);
				workbook.write(outputStream);
				workbook.close();
				outputStream.close();

			} catch (EncryptedDocumentException e) {
				e.printStackTrace();
			} catch (InvalidFormatException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}

	public static UnitedCupStandings findUCS(Player p) {
		for (UnitedCupStandings u : ListUCS) {
			if (u.thePlayer.getID() == p.getID()) {
				return u;
			}
		}
		return null;
	}

	/**
	 * Update united cup...
	 * 
	 * @param sheet
	 */
	public static void UpdateUnitedCup(Sheet sheet) {
		int nRow = 1;
		for (UnitedCupStandings u : ListUCS) {
			String[] csv = com.ugl.common.Utils.parseRecString(u.toString());
			Row CurrentRow = sheet.getRow(nRow);
			if (CurrentRow == null) {
				CurrentRow = sheet.createRow(nRow);
			}

			int nCell = 0;
			int index = 0;
			setCell(CurrentRow, nCell++, csv[index++]); // name
			setCell(CurrentRow, nCell++, getDouble(csv[index++]));
			setCell(CurrentRow, nCell++, getDouble(csv[index++]));
			setCell(CurrentRow, nCell++, getDouble(csv[index++]));
			setCell(CurrentRow, nCell++, getDouble(csv[index++]));
			// added 8/13/2018...
			setCell(CurrentRow, nCell++, getDouble(csv[index++]));
			setCell(CurrentRow, nCell++, getDouble(csv[index++]));
			setCell(CurrentRow, nCell++, getDouble(csv[index++]));
			setCell(CurrentRow, nCell++, getDouble(csv[index++]));
			setCell(CurrentRow, nCell++, getDouble(csv[index++]));

			nRow++;
		}
	}

	/**
	 * Update the players sheet...
	 * 
	 * @param sheet
	 */
	public static void UpdatePlayerSheet(Sheet sheet) {
		int nRow = 1;
		for (Player p : ListPlayers) {
			Row row = sheet.getRow(nRow++);
			String[] csv = p.generateCSVLine(uglCourse);
			int index = 0;
			int nCell = 0;

			setCell(row, nCell++, getDouble(csv[index++]));
			setCell(row, nCell++, csv[index++]); // name
			setCell(row, nCell++, csv[index++]); // email
			setCell(row, nCell++, csv[index++]); // phone
			setCell(row, nCell++, getDouble(csv[index++]));
			setCell(row, nCell++, getDouble(csv[index++]));
			setCell(row, nCell++, getDouble(csv[index++]));
			setCell(row, nCell++, getDouble(csv[index++]));
			setCell(row, nCell++, getDouble(csv[index++]));
			for (int n = 0; n < CELL_WEEKLY_TABLE.length; n++) {
				if (CELL_WEEKLY_TABLE[n] == null) {
					break;
				}
				setCell(row, nCell++, getDouble(csv[index++]));
			}
			setCell(row, nCell++, getDouble(csv[index++]));
			setCell(row, nCell++, getDouble(csv[index++]));
		}
	}

	/**
	 * Get double of the string or return 0.
	 * 
	 * @param str
	 * @return
	 */
	public static double getDouble(String str) {
		try {
			return Double.parseDouble(str.trim());
		} catch (Exception ex) {
		}
		return 0;
	}

	/**
	 * Set the cell of a spreadsheet to double value.
	 * 
	 * @param row
	 * @param nCell
	 * @param Val
	 */
	public static void setCell(Row row, int nCell, double Val) {
		Cell cell = row.getCell(nCell);
		if (cell == null) {
			cell = row.createCell(nCell);
			cell.setCellValue(Val);
		} else {
			cell.setCellValue(Val);
		}
	}

	/**
	 * Set cell to string value.
	 * 
	 * @param row
	 * @param nCell
	 * @param Val
	 */
	public static void setCell(Row row, int nCell, String Val) {
		Cell cell = row.getCell(nCell);
		if (cell == null) {
			cell = row.createCell(nCell);
			cell.setCellValue(Val);
		} else {
			cell.setCellValue(Val);
		}
	}

	/**
	 * Init this class and read the players...
	 * 
	 * @param SpreadsheetName
	 */
	public static void Init(String SpreadsheetName) {
		try {
			Timestamp todaytm = new Timestamp(new Date().getTime());
			yyyymmdd = new SimpleDateFormat("MM-dd-yyyy").format(todaytm);

			Workbook workbook = WorkbookFactory.create(new File(SpreadsheetName));
			int sheets = workbook.getNumberOfSheets();
			Cout.outString(INIT + yyyymmdd + " Workbook has " + Integer.toString(sheets) + " Sheets :");
			Iterator<Sheet> sheetIterator = workbook.sheetIterator();
			// cout.outString(INIT + "Retrieving Sheets using Iterator");
			while (sheetIterator.hasNext()) {
				Sheet sheet = sheetIterator.next();
				String sheetName = sheet.getSheetName();
				Cout.outString("Process> " + sheet.getSheetName());
				if (sheetName.contentEquals(PLAYERS)) {
					ParsePlayerSheet(sheet);
				} else if (sheetName.contentEquals(SCHEDULE)) {
					ParseScheduleSheet(sheet);
				} else if (sheetName.contentEquals(COURSE)) {
					// ParseCourseSheet(sheet);
				} else if (sheetName.contains(WEEK)) {
					ParseWeekSheet(sheet, sheetName);
				} else if (sheetName.equalsIgnoreCase(UNITED_CUP_STANDINGS)) {
					ParseUnitedCup(sheet);
				}

			}
			workbook.close();
		} catch (EncryptedDocumentException | IOException e) {
			e.printStackTrace();
		}
	}

	private static void ParseUnitedCup(Sheet sheet) {
		Cout.outString(MAIN + "ParseUnitedCup()");
		if (ListPlayers == null) {
			return;
		}
		ListUCS = new ArrayList<UnitedCupStandings>();
		int nFirst = sheet.getFirstRowNum();
		int nLastRow = sheet.getLastRowNum();
		for (int i = nFirst; i <= nLastRow; i++) {
			Row CurrentRow = sheet.getRow(i);
			Iterator<Cell> CellIter = CurrentRow.cellIterator();
			int nCell = 0;
			UnitedCupStandings upc = null;
			while (CellIter.hasNext()) {
				Cell cell = CellIter.next();
				String cmd = cell.toString().trim().toUpperCase();
				if (nCell == 0 && cmd.startsWith("****")) {
					break;
				}
				nCell = cell.getColumnIndex();
				switch (nCell) {
				case 0: // name
					String playerName = cell.getStringCellValue();
					Player p = null;// todo: HMGReport.findPlayer(playerName, ListPlayers);
					upc = new UnitedCupStandings();
					upc.thePlayer = p;
					ListUCS.add(upc);
					break;
				case 1: // points
					upc.points = (int) cell.getNumericCellValue();
					break;
				case 2: // hpi
					upc.hpi = cell.getNumericCellValue();
					break;
				case 3: // rank
					upc.rank = (int) cell.getNumericCellValue();
					break;
				case 4:
					upc.lastWeekRank = (int) cell.getNumericCellValue();
					break;
				case 5:
					upc.TourneyHCP = getCellInt(cell);
					break;
				case 6:
					upc.TourneyScore = getCellInt(cell);
					break;
				case 7:
					upc.EffectiveScore = getCellInt(cell);
					break;
				case 8:
					upc.TourneyPoints = getCellInt(cell);
					break;
				case 9:
					upc.TotalPoints = getCellInt(cell);
					Cout.outString(upc.toString());
					break;
				default:
					break;
				}
			}
		}

	}

	private static int getCellInt(Cell cell) {
		try {
			String score = cell.getStringCellValue().trim();
			int i = Integer.parseInt(score);
			return i;
		} catch (Exception ex) {
			int i = (int) cell.getNumericCellValue();
			return i;
		}
	}

	/**
	 * Parse the week sheet...
	 * 
	 * @param sheet
	 * @param sheetName
	 */
	private static void ParseWeekSheet(Sheet sheet, String sheetName) {
		Cout.outString(MAIN + "ParseWeekSheet() Parse Weekly Scores...");
		nSheetWeek = 0;
		String str = sheetName.substring(4);
		try {
			nSheetWeek = new Integer(Integer.parseInt(str));
		} catch (Exception ex) {
			Cout.outString(MAIN + "ParseWeekSheet() ERROR - " + ex.toString());
			return;
		}

		int nFirst = sheet.getFirstRowNum();
		int nLastRow = sheet.getLastRowNum();
		for (int i = nFirst; i <= nLastRow; i++) {
			Row CurrentRow = sheet.getRow(i);
			Iterator<Cell> CellIter = CurrentRow.cellIterator();
			int nCell = 0;
			ScoreCards crd = new ScoreCards();
			crd.Week = nSheetWeek; // set the current week...
			boolean isCard = false;
			while (CellIter.hasNext()) {
				Cell cell = CellIter.next();
				String cmd = cell.toString().trim().toUpperCase();
				nCell = cell.getColumnIndex();
				if (nCell == 0 && cmd.startsWith("****")) {
					break; // skip comment row...
				}
				isCard = parseScoreCardCol(cell, crd);
			}
			if (isCard) {
				crd.updateTotal();
				if (crd.Total == 0) {
					Cout.outString("bad scorecard" + crd.toString());
				} else {
					ListScoreCards.add(crd);
				}
			}
		}
	}

	/**
	 * Parse the score card...
	 * 
	 * @param cell
	 * @param crd
	 * @return true
	 */
	private static boolean parseScoreCardCol(Cell cell, ScoreCards crd) {

		int nCol = cell.getColumnIndex();
		switch (nCol) {
		case 0: // ID
			crd.ID = (int) cell.getNumericCellValue();
			break;
		case 1: // NAME
			crd.Name = cell.getStringCellValue();
			break;
		case 2: // WEEK
			crd.Week = nSheetWeek;
			cell.setCellValue(Integer.toString(nSheetWeek));
			break;
		default:
			// Only 18 holes allowed....
			if (nCol >= 3 && nCol <= 20) {
				int index = nCol - 3;
				crd.Scores[index] = (int) cell.getNumericCellValue();
			} else if (nCol == 21) {
				crd.Total = (int) cell.getNumericCellValue();
			}
			break;
		}
		return true;
	}

	// private static void ParseCourseSheet(Sheet sheet) {
	// //TODO:...
	// }

	private static void ParseScheduleSheet(Sheet sheet) {
		Cout.outString(MAIN + "ParseScheduleSheet() Parse Schedule...");
		ListSchedules = new ArrayList<Schedule>();
		int nFirst = sheet.getFirstRowNum();
		int nLastRow = sheet.getLastRowNum();
		for (int i = nFirst; i <= nLastRow; i++) {
			Row CurrentRow = sheet.getRow(i);
			Iterator<Cell> CellIter = CurrentRow.cellIterator();
			Schedule theSched = new Schedule();
			boolean bAdd = false;
			while (CellIter.hasNext()) {
				Cell cell = CellIter.next();
				String cmd = cell.toString().trim().toUpperCase();
				int colIndex = cell.getColumnIndex();
				if (cmd.startsWith("****WEEK")) {
					break;
				} else if (colIndex == 0) {
					theSched.Week = (int) cell.getNumericCellValue();
					bAdd = true;
				} else if (colIndex == 1) {
					theSched.ID1 = (int) cell.getNumericCellValue();
					bAdd = true;
				} else if (colIndex == 2) {
					theSched.ID2 = (int) cell.getNumericCellValue();
					bAdd = true;
				}
			}
			if (bAdd) {
				ListSchedules.add(theSched);
				Cout.outString(theSched.toString());
			}
		}
	}

	private static void ParsePlayerSheet(Sheet sheet) {
		Cout.outString(MAIN + "ParsePlayerSheet() Create the player's list...");
		ListPlayers = new ArrayList<Player>();
		int nFirst = sheet.getFirstRowNum();
		int nLastRow = sheet.getLastRowNum();
		Row CurrentRow = sheet.getRow(nFirst);
		Iterator<Cell> CellIter = CurrentRow.cellIterator();

		int CellIndex = 0;
		/**
		 * These are the offsets...
		 */
		CELL_POINTS = -1;
		CELL_ID = -1;
		CELL_NAME = -1;
		CELL_PHONE = -1;
		CELL_FIRST_HPI = -1;
		CELL_HPI = -1;
		CELL_18_HP = -1;
		CELL_9_HP = -1;
		CELL_AVE = -1;
		CELL_STD = -1;
		CELL_EMAIL = -1;
		CELL_WEEKLY_TABLE = new Integer[52];
		int WeekIndex = 0;
		while (CellIter.hasNext()) {
			Cell Command = CellIter.next();
			String cmd = Command.getStringCellValue().trim().toUpperCase();
			if (cmd.isEmpty()) {
				continue;
			}
			if (cmd.contentEquals("****ID")) {
				CELL_ID = CellIndex;
			} else if (cmd.contentEquals("NAME")) {
				CELL_NAME = CellIndex;
			} else if (cmd.contentEquals("PHONE")) {
				CELL_PHONE = CellIndex;
			} else if (cmd.contentEquals("1ST HPI")) {
				CELL_FIRST_HPI = CellIndex;
			} else if (cmd.contentEquals("HPI")) {
				CELL_HPI = CellIndex;
			} else if (cmd.contentEquals("18 HP")) {
				CELL_18_HP = CellIndex;
			} else if (cmd.contentEquals("9 HP")) {
				CELL_9_HP = CellIndex;
			} else if (cmd.contentEquals("AVE")) {
				CELL_AVE = CellIndex;
			} else if (cmd.contentEquals("STD")) {
				CELL_STD = CellIndex;
			} else if (cmd.contentEquals("POINTS")) {
				CELL_POINTS = CellIndex;
			} else if (cmd.contentEquals("EMAIL")) {
				CELL_EMAIL = CellIndex;
			} else if (cmd.startsWith("WK")) {
				CELL_WEEKLY_TABLE[WeekIndex++] = CellIndex;
				nWeeksInLeague++;
			}
			CellIndex++;
		}

		// Set the number of weeks in the league
		HMGReport.setTotalWeeks(nWeeksInLeague);

		for (int i = nFirst + 1; i <= nLastRow; i++) {
			boolean bAdd = false;
			Player PlayerPlayer = new Player();
			CurrentRow = sheet.getRow(i);
			CellIter = CurrentRow.cellIterator();
			int WeeklyIndex = 0;
			while (CellIter.hasNext()) {
				Cell cell = CellIter.next();
				String CellValue = cell.toString().trim();
				// cout.outString(CellValue);
				CellIndex = cell.getColumnIndex();

				if (CellIndex == CELL_ID) {
					double d = cell.getNumericCellValue();
					PlayerPlayer.setID((int) d);
					bAdd = true;
				} else if (CellIndex == CELL_NAME) {
					PlayerPlayer.setName(CellValue);
					bAdd = true;
				} else if (CellIndex == CELL_PHONE) {
					PlayerPlayer.setPhoneNumber(CellValue);
					bAdd = true;
				} else if (CellIndex == CELL_FIRST_HPI) {
					PlayerPlayer.setHPIndexInitial(cell.getNumericCellValue());
					bAdd = true;
				} else if (CellIndex == CELL_HPI) {
					PlayerPlayer.setHPIndex(cell.getNumericCellValue());
					bAdd = true;
				} else if (CellIndex == CELL_18_HP) {
					Cout.outString("18 HP " + cell.getNumericCellValue());
				} else if (CellIndex == CELL_9_HP) {
					Cout.outString("9 HP " + cell.getNumericCellValue());
				} else if (CellIndex == CELL_AVE) {
					// cout.outString("AVE " + cell.getNumericCellValue());
				} else if (CellIndex == CELL_STD) {
					// cout.outString("STD " + cell.getNumericCellValue());
				} else if (CellIndex == CELL_POINTS) {
					PlayerPlayer.setTotalPoints((int) cell.getNumericCellValue());
					bAdd = true;
				} else if (CellIndex == CELL_EMAIL) {
					PlayerPlayer.setEmail(CellValue);
				} else {
					for (int wk = 0; wk < nWeeksInLeague; wk++) {
						if (CELL_WEEKLY_TABLE[wk] == null) {
							break;
						}
						if (CellIndex == CELL_WEEKLY_TABLE[wk]) {
							Integer[] tbl = PlayerPlayer.getWeeklyScores();
							tbl[WeeklyIndex++] = (int) cell.getNumericCellValue();
							bAdd = true;
							break;
						}
					}
				}
			}
			if (bAdd) {
				ListPlayers.add(PlayerPlayer);
			}
		}

		for (Player tmpP : ListPlayers) {
			Cout.outString(tmpP.toString());
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Cout.outString(MAIN);
		Multimap<String, String> mapArgs = Utils.ScanArguments(args);
		String spreadsheet = mapArgs.get("f").iterator().next();
		Init(spreadsheet);
		Cout.outString("Bye!");
	}

}
