package ui;

import java.util.Scanner;

import model.BullDozerSimulator;
import model.Command;
import model.CostManager;

public class StartSimulation {

	static Scanner in = new Scanner(System.in);

	/**
	 * show welcome info
	 */
	private static void showWelcomeInfo() {
		System.out.println("Welcome to the Aconex site clearing simulator. This is a map of the site:");
		System.out.println();
	}

	/**
	 * display the site map
	 * 
	 * @param bull
	 */
	private static void showMap(BullDozerSimulator bull) {
		// print the map, between each tile a space is added to make it look better
		for (int i = 0; i < bull.getSiteManager().getSite().length; i++) {
			StringBuffer line = new StringBuffer("    ");
			for (int j = 0; j < bull.getSiteManager().getSite()[0].length; j++) {
				line.append(bull.getSiteManager().getSite()[i][j]);
				line.append(" ");
			}
			System.out.println(line.toString());
		}
		System.out.println();
	}

	/**
	 * Display the welcome information when simulation starts
	 */
	private static void showBullDozerStartInfo() {
		System.out.println(
				"The bulldozer is currently located at the Northern edge of the site, immediately to the West of the site, and facing East.\r\n");
		System.out.println();
	}

	private static void showCommandHint() {
		System.out.print("(l)eft, (r)ight, (a)dvance <n>, (q)uit: ");
	}

	/**
	 * read and validate command
	 * 
	 * @return a size of 2 array of string if the command input is valid, otherwise
	 *         return null
	 */
	private static String[] readCommand() {
		String cmd = in.nextLine();
		// validate command
		String[] cmdWithVar = cmd.split(" ");
		boolean validCommand = false;
		if (cmdWithVar.length == 1 && (cmdWithVar[0].equals(Command.TURN_LEFT_COMMAND_CODE)
				|| cmdWithVar[0].equals(Command.TURN_RIGHT_COMMAND_CODE)
				|| cmdWithVar[0].equals(Command.QUIT_COMMAND_CODE))) {
			validCommand = true;
		} else if (cmdWithVar.length == 2 && cmdWithVar[0].equals(Command.ADVANCE_COMMAND_CODE)) {
			try {
				Integer.parseInt(cmdWithVar[1]);
			} catch (NumberFormatException e) {
				System.out.println("Error: invalid advance distance");
				return null;
			}
			validCommand = true;
		}
		if (!validCommand) {
			System.out.println("Error: invalid command");
		}
		return validCommand ? cmdWithVar : null;
	}

	/**
	 * display the result of the simulation after it ends
	 * 
	 * @param bull
	 */
	private static void showResult(BullDozerSimulator bull) {
		System.out.println();
		System.out.println("The simulation has ended at your request. These are the commands you issued:");
		StringBuffer buffer = new StringBuffer();
		for (String command : bull.getCommands()) {
			if (buffer.length() > 0) {
				buffer.append(", ");
			}
			buffer.append(command);
		}
		System.out.println();
		System.out.println(buffer.toString());
		System.out.println();
		System.out.println("The costs for this land clearing operation were:");
		System.out.println();
		// the format may be a little from the requirement doc
		int totalCost = 0;
		System.out.println(appendToThrity("Item") + appendToNine("Quantity") + appendToNine("Cost"));
		bull.getCostManager();
		System.out.println(appendToThrity(CostManager.COMM_COST)
				+ appendToNine(Integer.toString(bull.getCostManager().getCostCount(CostManager.COMM_COST)))
				+ appendToNine(Integer.toString(bull.getCostManager().getCostCredit(CostManager.COMM_COST))));
		totalCost += bull.getCostManager().getCostCredit(CostManager.COMM_COST);
		bull.getCostManager();
		System.out.println(appendToThrity(CostManager.FUEL_COST)
				+ appendToNine(Integer.toString(bull.getCostManager().getCostCount(CostManager.FUEL_COST)))
				+ appendToNine(Integer.toString(bull.getCostManager().getCostCredit(CostManager.FUEL_COST))));
		totalCost += bull.getCostManager().getCostCredit(CostManager.FUEL_COST);
		bull.getCostManager();
		System.out.println(appendToThrity(CostManager.UNCLEARED_COST)
				+ appendToNine(Integer.toString(bull.getCostManager().getCostCount(CostManager.UNCLEARED_COST)))
				+ appendToNine(Integer.toString(bull.getCostManager().getCostCredit(CostManager.UNCLEARED_COST))));
		totalCost += bull.getCostManager().getCostCredit(CostManager.UNCLEARED_COST);
		bull.getCostManager();
		System.out.println(appendToThrity(CostManager.DESTROY_TREE_COST)
				+ appendToNine(Integer.toString(bull.getCostManager().getCostCount(CostManager.DESTROY_TREE_COST)))
				+ appendToNine(Integer.toString(bull.getCostManager().getCostCredit(CostManager.DESTROY_TREE_COST))));
		totalCost += bull.getCostManager().getCostCredit(CostManager.DESTROY_TREE_COST);
		bull.getCostManager();
		System.out.println(appendToThrity(CostManager.PAINT_DAMAGE_COST)
				+ appendToNine(Integer.toString(bull.getCostManager().getCostCount(CostManager.PAINT_DAMAGE_COST)))
				+ appendToNine(Integer.toString(bull.getCostManager().getCostCredit(CostManager.PAINT_DAMAGE_COST))));
		totalCost += bull.getCostManager().getCostCredit(CostManager.PAINT_DAMAGE_COST);
		System.out.println("----");
		System.out.println(appendToThrity("Total") + appendToNine("") + appendToNine(Integer.toString(totalCost)));
		System.out.println();
		System.out.println("Thankyou for using the Aconex site clearing simulator.");
	}

	/**
	 * append a give string to the length of 30 with space
	 * 
	 * @param str
	 * @return an appended string with length of 30
	 */
	private static String appendToThrity(String str) {
		StringBuffer sb = new StringBuffer(str);
		while (sb.length() < 30) {
			sb.append(" ");
		}
		return sb.toString();
	}

	/**
	 * append a give string to the length of 9 with space
	 * 
	 * @param str
	 * @return an appended string with length of 9
	 */
	private static String appendToNine(String str) {
		StringBuffer sb = new StringBuffer(str);
		while (sb.length() < 9) {
			sb.append(" ");
		}
		return sb.toString();
	}

	/**
	 * main method to start the simulation
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		BullDozerSimulator bull = new BullDozerSimulator();
		showWelcomeInfo();
		showMap(bull);
		showBullDozerStartInfo();
		while (true) {
			showCommandHint();
			String[] cmd = readCommand();
			// when the command is a final command, leave the cycle and close the input
			// stream
			if (cmd != null && bull.execute(cmd)) {
				in.close();
				break;
			}
		}
		showResult(bull);
	}
}
