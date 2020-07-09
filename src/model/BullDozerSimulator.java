package model;

import java.util.ArrayList;
import java.util.List;

import model.DirectionManager.Direction;
import model.DirectionManager.Turn;

public class BullDozerSimulator {

	private Site site;

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
	 * a list of command history
	 */
	private ArrayList<String> commandHistory;

	private CostManager costManager;

	private int[] initialLocation = { 0, -1 };

	private Direction initialDirection = Direction.EAST;

	class FuelConsumption {
		public static final int PLAIN_GROUND = 1;
		public static final int TREE_GROUND = 2;
		public static final int ROCKY_GROUND = 2;
		public static final int PROTECTED_TREE_GROUND = 2;
	}

	/**
	 * initializer of the simulator
	 * 
	 * @param site
	 */
	public BullDozerSimulator() {
		site = new Site();
		loc = initialLocation;
		facing = initialDirection;
		commandHistory = new ArrayList<>();
		costManager = new CostManager();
	}

	public Site getSiteManager() {
		return site;
	}

	public void setSiteManager(Site siteManager) {
		this.site = siteManager;
	}

	public ArrayList<String> getCommands() {
		return commandHistory;
	}

	public void setCommands(ArrayList<String> commands) {
		this.commandHistory = commands;
	}

	public CostManager getCostManager() {
		return costManager;
	}

	public void setCostManager(CostManager costManager) {
		this.costManager = costManager;
	}

	/**
	 * execute the operation of turning left
	 */
	private void turn(Turn turn) {
		facing = DirectionManager.updateDirection(facing, turn);
	}

	/**
	 * execute the command received from console
	 * 
	 * @param cmd the command received
	 * @return true if this command turns out to be a final command
	 */
	public boolean execute(String[] cmd) {
		boolean finalCommand = false;
		Command command = new Command(cmd[0]);
		List<int[]> path = null;
		if (command.isTurnRight()) {
			turn(Turn.RIGHT);
		} else if (command.isTurnLeft()) {
			turn(Turn.LEFT);
		} else if (command.isAdvance()) {
			int advanceLength = Integer.parseInt(cmd[1]);
			path = advance(advanceLength);
		} else if (command.isQuit()) {
			finalCommand = true;
		}
		// add command to command history
		commandHistory.add(command.getCommandName());
		// manage cost incurred by the command
		if (!finalCommand) {
			finalCommand = manageCost(command, path);
		}
		return finalCommand;
	}

	/**
	 * 
	 * @param dist the distance that the bull dozer advances
	 * @return a list of nodes that the bull dozer has passed
	 */
	private List<int[]> advance(int dist) {
		ArrayList<int[]> path = new ArrayList<>();
		if (facing == Direction.EAST) {
			for (int i = 1; i <= dist; i++) {
				path.add(new int[] { loc[0], loc[1] + i });
			}
			loc[1] = loc[1] + dist;
		}
		if (facing == Direction.SOUTH) {
			for (int i = 1; i <= dist; i++) {
				path.add(new int[] { loc[0] + i, loc[1] });
			}
			loc[0] = loc[0] + dist;
		}
		if (facing == Direction.WEST) {
			for (int i = 1; i <= dist; i++) {
				path.add(new int[] { loc[0], loc[1] - i });
			}
			loc[1] = loc[1] - dist;
		}
		if (facing == Direction.NORTH) {
			for (int i = 1; i <= dist; i++) {
				path.add(new int[] { loc[0] - i, loc[1] });
			}
			loc[0] = loc[0] - dist;
		}
		return path;
	}

	private boolean manageCost(Command command, List<int[]> path) {
		boolean finalCommand = false;
		if (command.isTurnLeft() || command.isTurnRight() || command.isAdvance()) {
			costManager.addCost(CostManager.COMM_COST, 1);
		}
		if (command.isAdvance() && path != null) {
			for (int i = 0; i < path.size(); i++) {
				int xcoord = path.get(i)[0];
				int ycoord = path.get(i)[1];
				if (site.isInsideSite(xcoord, ycoord)) {
					if (site.isPlainGround(xcoord, ycoord) || site.isClearedGround(xcoord, ycoord)) {
						costManager.addCost(CostManager.FUEL_COST, FuelConsumption.PLAIN_GROUND);
					} else if (site.isRockyGround(xcoord, ycoord)) {
						costManager.addCost(CostManager.FUEL_COST, FuelConsumption.ROCKY_GROUND);
					} else if (site.isTreeGround(xcoord, ycoord)) {
						costManager.addCost(CostManager.FUEL_COST, FuelConsumption.TREE_GROUND);
						if (i < path.size() - 1) {
							costManager.addCost(CostManager.PAINT_DAMAGE_COST, 1);
						}
					} else if (site.isProtectedtreeGround(xcoord, ycoord)) {
						costManager.addCost(CostManager.FUEL_COST, FuelConsumption.PROTECTED_TREE_GROUND);
						costManager.addCost(CostManager.DESTROY_TREE_COST, 1);
						finalCommand = true;
					}
					site.clear(xcoord, ycoord);
				} else {
					finalCommand = true;
				}
			}
		}
		if (finalCommand) {
			costManager.addCost(CostManager.UNCLEARED_COST, site.calculateUnclearedSquareCost());
		}
		return finalCommand;
	}

}
