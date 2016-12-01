/*
 *  File name: Location.java
 *  File purpose: This class describes the location of an object based on the X and Y axes.
 */

package game.movement;

public class Location {

	private double axisXlocation;
	private double axisYlocation;

	/** Creates a new instance of Location */
	public Location(double x, double y) {
		this.axisXlocation = x;
		this.axisYlocation = y;

	}

	public Location(int x, int y) {
		this.axisXlocation = (double) x;
		this.axisYlocation = (double) y;

	}

	@Override
	public String toString() {
		return (String) ("axisX:" + String.valueOf(axisXlocation) + " axisY:" + String.valueOf(axisYlocation));
	}

	public double getX() {
		return axisXlocation;
	}

	public void setX(double x) {
		this.axisXlocation = x;
	}

	public double getY() {
		return axisYlocation;
	}

	public void setY(double y) {
		this.axisYlocation = y;
	}

	public void setLocation(double x, double y) {
		this.axisXlocation = x;
		this.axisYlocation = y;

	}

}
