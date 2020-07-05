package model;

import java.util.ArrayList;
import java.util.HashMap;

public class BullDozerSimulator {

	/**
	 * a 2d array of char to represent the map
	 */
	private char[][] site;

	/**
	 * the int array for the current coordinate of the bull dozer
	 */
	private int[] loc;

	/**
	 * the current facing of the bull dozer
	 * 
	 * my 1st design is to use an int to represent facing, when turn right + 1 and
	 * when turn left -1. We can use the absolute value of it to find its reminder
	 * when divided by 4 if need to decide the facing, but this is hard to
	 * understand by others so I abandoned this design
	 */
	private Direction facing;

	/**
	 * enums for directions
	 * 
	 * @author Peizheng Shang
	 *
	 */
	enum Direction {
		EAST, WEST, SOUTH, NORTH
	}

	/**
	 * a list of command history
	 */
	private ArrayList<String> commands;

	/**
	 * a hashmap to persist all the cost items and times
	 */
	private HashMap<String, Integer> costEvent;

	/**
	 * String for the name of cost items
	 */
	public String COMM_COST = "communication overhead";

	public String FUEL_COST = "fuel usage";

	public String UNCLEARED_COST = "uncleared squares";

	public String DESTROY_TREE_COST = "destruction of protected tree";

	public String PAINT_DAMAGE_COST = "paint damage to bulldozer";

	/**
	 * initializer of the simulator
	 * 
	 * @param site
	 */
	public BullDozerSimulator(char[][] site) {
		this.site = site;
		loc = new int[] { 0, -1 };
		facing = Direction.EAST;
		commands = new ArrayList<>();
		costEvent = new HashMap<>();
	}

	public char[][] getSite() {
		return site;
	}

	public void setSite(char[][] site) {
		this.site = site;
	}

	/**
	 * add a new cost to the map of cost
	 * 
	 * @param item the name of the cost item
	 * @param time the time that the new cost occurred
	 */
	private void addCost(String item, int time) {
		if (costEvent.get(item) == null) {
			costEvent.put(item, time);
		} else {
			costEvent.put(item, costEvent.get(item) + time);
		}
	}

	private int getCost(String item) {
		if (costEvent.get(item) == null) {
			return 0;
		} else {
			return costEvent.get(item);
		}
	}

	public int getCommCost() {
		return getCost(COMM_COST);
	}

	public int getFuelCost() {
		return getCost(FUEL_COST);
	}

	public int getUnclearedSquareCost() {
		return getCost(UNCLEARED_COST);
	}

	public int getDestroyTreeCost() {
		return getCost(DESTROY_TREE_COST);
	}

	public int getPaintDamageCost() {
		return getCost(PAINT_DAMAGE_COST);
	}

	/**
	 * execute the operation of turning left
	 */
	private void turnLeft() {
		// add command into list
		commands.add("turn left");
		// update facing
		if (facing == Direction.EAST) {
			facing = Direction.NORTH;
		} else if (facing == Direction.NORTH) {
			facing = Direction.WEST;
		} else if (facing == Direction.WEST) {
			facing = Direction.SOUTH;
		} else if (facing == Direction.SOUTH) {
			facing = Direction.EAST;
		}
		// add comm cost
		addCost(COMM_COST, 1);
	}

	/**
	 * execute the operation of turning right
	 */
	private void turnRight() {
		// add command into list
		commands.add("turn right");
		// update facing
		if (facing == Direction.EAST) {
			facing = Direction.SOUTH;
		} else if (facing == Direction.SOUTH) {
			facing = Direction.WEST;
		} else if (facing == Direction.WEST) {
			facing = Direction.NORTH;
		} else if (facing == Direction.NORTH) {
			facing = Direction.EAST;
		}
		// add comm cost
		addCost(COMM_COST, 1);
	}

	/**
	 * execute the command received from console
	 * 
	 * @param cmd the command received
	 * @return true if this command turns out to be a final command
	 */
	public boolean execute(String[] cmd) {
		boolean finalCommand = false;
		if (cmd[0].equals("r")) {
			turnRight();
		} else if (cmd[0].equals("l")) {
			turnLeft();
		} else if (cmd[0].equals("a")) {
			finalCommand = advance(Integer.parseInt(cmd[1]));
		} else if (cmd[0].equals("q")) {
			commands.add("quit");
			finalCommand = true;
		}
		if (finalCommand) {
			calculateUnclearedSquareCost();
		}
		return finalCommand;
	}

	/**
	 * calculate the cost of uncleared squares and add the item to cost map
	 */
	private void calculateUnclearedSquareCost() {
		int cost = 0;
		for (int i = 0; i < site.length; i++) {
			for (int j = 0; j < site[0].length; j++) {
				if (site[i][j] != 'c' && site[i][j] != 'T') {
					cost++;
				}
			}
		}
		addCost(UNCLEARED_COST, cost);
	}

	public ArrayList<String> getCommands() {
		return commands;
	}

	public void setCommands(ArrayList<String> commands) {
		this.commands = commands;
	}

	public HashMap<String, Integer> getCostEvent() {
		return costEvent;
	}

	public void setCostEvent(HashMap<String, Integer> costEvent) {
		this.costEvent = costEvent;
	}

	/**
	 * check if a given coordinate is in the map
	 * 
	 * @param xcoord
	 * @param ycoord
	 * @return true if the given coordinate is inside the map
	 */
	private boolean isInsideSite(int xcoord, int ycoord) {
		if (xcoord < 0 || ycoord < 0 || xcoord >= site.length || ycoord >= site[0].length) {
			return false;
		}
		return true;
	}

	/**
	 * execute the advance operation
	 * 
	 * @param dist
	 * @return true if the bull dozer is outside of map or removed a protected tree
	 */
	private boolean advance(int dist) {
		// add command to the list
		commands.add("advance " + dist);
		// record the squares that this advance has passed
		// if any square is outside of the map then set this command to final command
		boolean finalCommand = false;
		ArrayList<int[]> path = new ArrayList<>();
		if (facing == Direction.EAST) {
			for (int i = 1; i <= dist; i++) {
				if (isInsideSite(loc[0], loc[1] + i)) {
					path.add(new int[] { loc[0], loc[1] + i });
				} else {
					finalCommand = true;
					break;
				}
			}
			loc[1] = loc[1] + dist;
		}
		if (facing == Direction.SOUTH) {
			for (int i = 1; i <= dist; i++) {
				if (isInsideSite(loc[0] + i, loc[1])) {
					path.add(new int[] { loc[0] + i, loc[1] });
				} else {
					finalCommand = true;
					break;
				}
			}
			loc[0] = loc[0] + dist;
		}
		if (facing == Direction.WEST) {
			for (int i = 1; i <= dist; i++) {
				if (isInsideSite(loc[0], loc[1] - i)) {
					path.add(new int[] { loc[0], loc[1] - i });
				} else {
					finalCommand = true;
					break;
				}
			}
			loc[1] = loc[1] - dist;
		}
		if (facing == Direction.NORTH) {
			for (int i = 1; i <= dist; i++) {
				if (isInsideSite(loc[0] - i, loc[1])) {
					path.add(new int[] { loc[0] - i, loc[1] });
				} else {
					finalCommand = true;
					break;
				}
			}
			loc[0] = loc[0] - dist;
		}
		// check the path and calculate the cost
		addCost(COMM_COST, 1);
		for (int i = 0; i < path.size(); i++) {
			char ground = site[path.get(i)[0]][path.get(i)[1]];
			if (ground == 'o' || ground == 'c') {
				addCost(FUEL_COST, 1);
			} else if (ground == 'r') {
				addCost(FUEL_COST, 2);
			} else if (ground == 't') {
				addCost(FUEL_COST, 2);
				if (i < path.size() - 1) {
					addCost(PAINT_DAMAGE_COST, 1);
				}
			} else if (ground == 'T') {
				addCost(FUEL_COST, 2);
				addCost(DESTROY_TREE_COST, 1);
				finalCommand = true;
			}
			// mark the square cleared as c
			site[path.get(i)[0]][path.get(i)[1]] = 'c';
		}
		return finalCommand;
	}

}
