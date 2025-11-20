/**
 * Point.java
 *
 * Student Name: Stanislav Kril
 * Student Number: 3133810
 */

public final class Point{
	private final int x, y;
	
	public Point(int x0, int y0){
		x = x0; y = y0;
	}
	
 	public int getX(){
 		return x;
 	}
 	
	public int getY(){
		return y;
	}
	
	public String toString(){
		return "(" + x + "," + y + ")";
	}

	public boolean equals(Object ob){
		if(!(ob instanceof Point)) return false;
		Point p = (Point)ob;
		return x == p.x && y == p.y;
	}
}

