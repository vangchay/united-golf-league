package com.ugl.dptxninq;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
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
import com.ugl.common.BaseServiceElement;
import com.ugl.common.CodeGen;
import com.ugl.common.Cout;
import com.ugl.speadsheet.PlayersSpreedsheet;

public class UpdatePeggyDDTSheet {

    private static final String INIT = "INIT";

    private static final String INTRADAY = "Intra";

    private static final String NONINTRADAY = "NonIntra";

    private static final String END = "END";

    private static Timestamp todaytm = new Timestamp(new Date().getTime());

    private static String yyyymmdd = new SimpleDateFormat("MM-dd-yyyy").format(todaytm);

    private static String[] START_UP = { "UpdatePeggyDDTSheet v1.0.0 - Updates PEGGY'S CSV based on 3 input parameters",
	    "Args1> Peggy's spreadsheet *.xlsx", "Args2> CW tool intraday CSV file",
	    "Args3> CW tool nonIntraday CSV file" };

    public static void main(String[] args) {
	for (String line : START_UP) {
	    Cout.outString(line);
	}
	String fileName = args[0];
	String intraDayCsv = args[1];
	String nonIntraDayCsv = args[2];

	List<BaseServiceElement> listIntraday = CodeGen.parseGenFile(intraDayCsv);
	List<BaseServiceElement> listNonIntraDay = CodeGen.parseGenFile(nonIntraDayCsv);

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
		if (sheetName.contentEquals(INTRADAY)) {
		    UpdateDDT(sheet, listIntraday);
		} else if (sheetName.equalsIgnoreCase(NONINTRADAY)) {
		    UpdateDDT(sheet, listNonIntraDay);
		}
	    }

	    finput.close();

	    FileOutputStream outputStream = new FileOutputStream(fileName);
	    workbook.write(outputStream);
	    workbook.close();
	    outputStream.close();
	    Cout.outString("Bye!");
	} catch (EncryptedDocumentException e) {
	    e.printStackTrace();
	} catch (InvalidFormatException e) {
	    e.printStackTrace();
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }

    private static void UpdateDDT(Sheet sheet, List<BaseServiceElement> listFields) {
	int nRow = 1;
	boolean foundGoodRow = true;
	while (foundGoodRow) {
	    Row CurrentRow = sheet.getRow(nRow++);
	    if (CurrentRow == null) {
		break;
	    }
	    Iterator<Cell> CellIter = CurrentRow.cellIterator();
	    int nCell = 0;
	    int element = -1;
	    BaseServiceElement field = null;
	    boolean skip = false;

	    while (CellIter.hasNext()) {
		if (skip) {
		    break;
		}
		Cell currentCell = CellIter.next();
		String Val = GetCellValue(currentCell);
		if (Val == null) {
		    break;
		}
		Cout.outString(Val);
		switch (nCell) {
		// element ID
		case 0:
		    if (Val.toUpperCase().equals(END)) {
			foundGoodRow = false;
			break;
		    }
		    try {
			element = Integer.parseInt(Val);
			field = CodeGen.FindElement(element, listFields);
			if (field == null) {
			    Cout.outString("****ERROR - COULD NOT FIND ELEMENT " + Val);
			} else {
			    Cout.outString("****FOUND ELEMENT");

			    nCell++;
			}
		    } catch (Exception ext) {
			Cout.outString("****NOT INTEGER, SKIP ROW");
			skip = true;
		    }
		    break;
		case 8: // data type
		    if (Val.equals("") == true) {
			// find and update
			PlayersSpreedsheet.setCell(CurrentRow, nCell, field.typeDefinition);
		    }
		    nCell++;
		    break;
		case 9: // data len
		    if (Val.equals("") == true) {
			// find and update
			PlayersSpreedsheet.setCell(CurrentRow, nCell, Integer.toString(field.len));
		    }
		    nCell++;
		    break;
		case 10: // dec
		    if (Val.equals("") == true) {
			// find and update
			PlayersSpreedsheet.setCell(CurrentRow, nCell, Integer.toString(field.dec));
		    }
		    nCell++;
		    break;
		case 11: // description
		    if (Val.equals("") == true) {
			// find and update
			PlayersSpreedsheet.setCell(CurrentRow, nCell, field.shortDescription);
		    }
		    nCell++;
		    skip = true; // done!
		    break;
		default:
		    nCell++;
		    break;
		}
	    }
	}
    }

    private static String GetCellValue(Cell command) {
	try {
	    String Val = command.getStringCellValue();
	    return Val;
	} catch (Exception Ext1) {
	    try {
		double d = command.getNumericCellValue();
		return Double.toString(d);
	    } catch (Exception ext2) {
	    }
	}
	return null;
    }

}
