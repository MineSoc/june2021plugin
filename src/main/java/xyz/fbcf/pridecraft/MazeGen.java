package xyz.fbcf.pridecraft;

import java.util.ArrayList;
import java.util.Random;
import java.awt.Point;

public class MazeGen{
	private static final int WALL = 0;
	private static final int PATH = 1;
	//private static final int FRONT = 3;
	//generates maze, exit is the first path square to be made
	public static int[][] generateMaze(){
		Random rand = new Random();
		int num = App.getRandomNumberInRange(15, 30);
		int[][] maze = new int[num][num];
		//addEdges(maze);
		int position = 1 + rand.nextInt(num-2);
		ArrayList<Point> walls;
		if(rand.nextInt(2) == 0){
			maze[maze.length-1][position] = PATH;
			maze[maze.length-2][position] = PATH;
			walls = getSurroundingWalls(maze, position, maze.length-2);
		}
		else{
			maze[position][maze.length-1] = PATH;
			maze[position][maze.length-2] = PATH;
			walls = getSurroundingWalls(maze, maze.length-2, position);
		}
		int index;
		Point choice;
		ArrayList<Point> caught;
		while(!walls.isEmpty()){
			index = rand.nextInt(walls.size());
			choice = walls.get(index);
			caught = getSurroundingWalls(maze, (int) choice.getX(), (int) choice.getY());
			if(caught.size()>=3){
				maze[(int) choice.getY()][(int) choice.getX()] = PATH;
				addWalls(maze, walls, choice);
			}
			walls.remove(choice);
		}

		return maze;

	}

	private static ArrayList<Point> getSurroundingWalls(int[][] maze, int x, int y){
		ArrayList<Point> walls = new ArrayList<>();
		for(int i = -1; i<=1; i+=2){
			try{
				if(maze[y+i][x] == WALL) walls.add(new Point(x, y+i));
				if(maze[y][x+i] == WALL) walls.add(new Point(x+i, y));
			}catch(IndexOutOfBoundsException e){
				continue;
			}
		}
		return walls;

	}

	private static void addWalls(int[][] maze, ArrayList<Point> walls, Point point){
		ArrayList<Point> surrounding = getSurroundingWalls(maze, (int) point.getX(), (int) point.getY());
		for(int i = 0; i< surrounding.size(); i++){
			walls.add(surrounding.get(i));
		}
	}

}