package com.ugl.handicap;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import com.ugl.common.Cout;
import com.ugl.common.Utils;

import org.apache.commons.io.FileUtils;

public class Course implements Comparable<Course> {
	/**
	 * The course name.
	 */
	private String Name;

	/**
	 * The tee description (example BLUE, WHITE, GREEN, BLACK, etc).
	 */
	private String Tee;

	/**
	 * The course rating.
	 */
	private double Rating;

	/**
	 * The course slope.
	 */
	private double Slope;

	/**
	 * The par score.
	 */
	private double Par;

	/**
	 * The front par.
	 */
	private double Front;

	/**
	 * The back par.
	 */
	private double Back;

	/**
	 * The course information.
	 */
	private List<Hole> courseInfo = null;

	/**
	 * Holes
	 */
	private int numHoles;

	/**
	 * To string.
	 */
	public String toString() {
		String msg = String.format("[%s-%s holes=%d rating=%f slope=%f par=%f front=%f back=%f]\n", Name, Tee, numHoles,
				Rating, Slope, Par, Front, Back);
		String msgHoles = "";
		for (Hole var : courseInfo) {
			msgHoles = msgHoles + ", " + var.toString();
		}
		return msg + msgHoles;
	}

	public Course() {
		courseInfo = new ArrayList<Hole>();
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return Name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		Name = name;
	}

	/**
	 * @return the rating
	 */
	public double getRating() {
		return Rating;
	}

	/**
	 * @param rating the rating to set
	 */
	public void setRating(double rating) {
		Rating = rating;
	}

	/**
	 * @return the slope
	 */
	public double getSlope() {
		return Slope;
	}

	/**
	 * @param slope the slope to set
	 */
	public void setSlope(double slope) {
		Slope = slope;
	}

	/**
	 * @return the tee
	 */
	public String getTee() {
		return Tee;
	}

	/**
	 * @param tee the tee to set
	 */
	public void setTee(String tee) {
		Tee = tee;
	}

	/**
	 * @return the par
	 */
	public double getPar() {
		return Par;
	}

	/**
	 * @param par the par to set
	 */
	public void setPar(double par) {
		Par = par;
	}

	/**
	 * @return the front
	 */
	public double getFront() {
		return Front;
	}

	/**
	 * @param front the front to set
	 */
	public void setFront(double front) {
		Front = front;
	}

	/**
	 * @return the back
	 */
	public double getBack() {
		return Back;
	}

	/**
	 * @param back the back to set
	 */
	public void setBack(double back) {
		Back = back;
	}

	/**
	 * @return the courseInfo
	 */
	public List<Hole> getCourseInfo() {
		return courseInfo;
	}

	public void setHoles(int i) {
		numHoles = i;
	}

	public int getHoles() {
		return numHoles;
	}

	@Override
	public int compareTo(Course o) {
		return Name.compareToIgnoreCase(o.getName());
	}

	/**
	 * Parse the course.
	 * 
	 * @param courseFile2
	 */
	public static void parseCourse(String courseFile2, List<Course> courseList) {
		@SuppressWarnings("unchecked")
		List<String> lines = null;
		try {
			lines = FileUtils.readLines(new File(courseFile2), "UTF-8");
		} catch (IOException e) {
			e.printStackTrace();
		}

		int state = 0;
		int index = 0;
		Course currentCourse = null;
		for (String s : lines) {
			if (s.isEmpty() == false) {
				String comment = "";
				try {
					comment = s.substring(0, 4);
				} catch (Exception ex) {
					ex.printStackTrace();
					break;
				}
				if (comment.contains("***")) {
					Vector<String> rec = Utils.parseRec(s);
					if (rec.size() == 0) {
						continue;
					}
					String funct = rec.get(0).toUpperCase();
					if (funct.contains("****COURSE")) {
						state = 1;
					} else if (funct.contains("****HOLE")) {
						state = 2;
					}
					continue;
				}

				Vector<String> rec = Utils.parseRec(s);
				if (rec.size() == 0) {
					continue;
				} else {
					switch (state) {
					case 0:
						break;
					case 1: // create course
						if (rec.size() < 8) {
							break;
						}
						Course courseObj = new Course();
						index = 0;
						try {
							courseObj.setName(rec.get(index++));
							courseObj.setTee(rec.get(index++));
							courseObj.setRating(Double.parseDouble(rec.get(index++)));
							courseObj.setSlope(Double.parseDouble(rec.get(index++)));
							courseObj.setPar(Double.parseDouble(rec.get(index++)));
							courseObj.setFront(Double.parseDouble(rec.get(index++)));
							courseObj.setBack(Double.parseDouble(rec.get(index++)));
							courseObj.setHoles(Integer.parseInt(rec.get(index++)));
						} catch (Exception ex) {
							Cout.outString(ex.toString());
							break;
						}
						courseList.add(courseObj);
						currentCourse = courseObj;
						state++;
						break;
					case 2: // Hole
						if (rec.size() < 4) {
							break;
						}
						if (currentCourse == null) {
							break;
						}
						index = 0;
						try {
							String hole = rec.get(index++);
							String par = rec.get(index++);
							String strokeindex = rec.get(index++);
							String yards = rec.get(index++);
							Hole h = new Hole(Integer.parseInt(hole), Integer.parseInt(par), Integer.parseInt(yards),
									Integer.parseInt(strokeindex));
							currentCourse.getCourseInfo().add(h);
						} catch (Exception ex) {
							Cout.outString(ex.toString());
						}
						break;
					default:
						break;
					}
				}
			}
		}
	}

	public static Course findCourse(String courseName, String tee2, List<Course> courseList) {
		for (Course c : courseList) {
			if (c.getName().equalsIgnoreCase(courseName) && c.getTee().equalsIgnoreCase(tee2)) {
				return c;
			}
		}
		return null;
	}

}
