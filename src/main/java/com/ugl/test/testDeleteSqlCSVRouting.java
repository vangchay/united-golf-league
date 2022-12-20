package com.ugl.test;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.ugl.common.Cout;
import com.ugl.common.Utils;

import org.apache.commons.io.FileUtils;

/**
 * @author e1042631
 *
 */
public class testDeleteSqlCSVRouting {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String fileName = args[0];
		Cout.outString(fileName);
		try {
			List<String> lines = FileUtils.readLines(new File(fileName), "UTF-8");
			for (String line : lines) {
				line = line.trim();
				if (line.isEmpty() == true) {
					continue;
				}
				String[] tbl = Utils.parseRecString(line);
				if (tbl.length > 0) {
					try {
						int Cat = Integer.parseInt(tbl[0]);
						if (Cat == 51) {
							String Routing = String.format(
							        "delete from IHXNG232.cfg_data WHERE CAT_ID = 51 AND CFG_KEY = '%s';",
							        tbl[1].trim());
							Cout.outString(Routing);
						}
					} catch (Exception ex) {

					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		Cout.outString("bye!");

	}

}
