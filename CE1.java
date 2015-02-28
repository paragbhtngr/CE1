
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class CE1 {

	private static final String MESSAGE_PROMPT = "command:";
	private static final String MESSAGE_INVALID_FORMAT = "invalid command format :%1$s";

	private static String filename=null;

	// These are the possible command types
	enum COMMAND_TYPE {
		ADD, DISPLAY, DELETE, CLEAR, INVALID, EXIT
	};

	private static Scanner scanner = new Scanner(System.in);

	public static void main(String[] args) {
		showToUser("Welcome to textbuddy, "+args[0]+" is ready for use");

		filename=args[0];

		while (true) {
			System.out.print(MESSAGE_PROMPT); 
			executeCommand(nextCommand());
		}
	}

	public static void executeCommand(String userCommand) {
		if (isInvalidUserCommand(userCommand))
			System.out.println ("Invalid Command: "+userCommand);

		String commandTypeString = getFirstWord(userCommand);

		COMMAND_TYPE commandType = determineCommandType(commandTypeString);

		switch (commandType) {
		case ADD:
			add(userCommand);
			break;
		case DISPLAY:
			display();
			break;
		case DELETE:
			delete(userCommand);
			break;
		case CLEAR:
			clear(userCommand);
			break;
		case INVALID:
			System.out.println(String.format(MESSAGE_INVALID_FORMAT, userCommand));
			break;
		case EXIT:
			System.exit(0);
		default:
			//throw an error if the command is not recognized
			System.out.println("Unrecognized Command. Try add, display, delete, clear, or exit.");
		}

	}


	private static COMMAND_TYPE determineCommandType(String commandTypeString) {
		if (commandTypeString == null)
			throw new Error("command type string cannot be null!");

		if (isAdd(commandTypeString)) {
			return COMMAND_TYPE.ADD;
		} else if (isDisplay(commandTypeString)) {
			return COMMAND_TYPE.DISPLAY;
		} else if (isDelete(commandTypeString)) {
			return COMMAND_TYPE.DELETE;
		} else if (isClear(commandTypeString)) {
			return COMMAND_TYPE.CLEAR;
		} else if (isExit(commandTypeString)) {
			return COMMAND_TYPE.EXIT;
		} else {
			return COMMAND_TYPE.INVALID;
		}
	}


	private static boolean isExit(String commandTypeString) {
		return commandTypeString.equalsIgnoreCase("exit");
	}

	private static boolean isDisplay(String commandTypeString) {
		return commandTypeString.equalsIgnoreCase("display");
	}

	private static boolean isDelete(String commandTypeString) {
		return commandTypeString.equalsIgnoreCase("delete");
	}

	private static boolean isClear(String commandTypeString) {
		return commandTypeString.equalsIgnoreCase("clear");
	}

	private static boolean isAdd(String commandTypeString) {
		return commandTypeString.equalsIgnoreCase("add");
	}

	private static void display() {

		try {
			Scanner f = new Scanner(new File(filename));
			if(!f.hasNextLine()){
				System.out.println(filename+" is empty");
				return;
			}
			for(int i=1;f.hasNextLine();i++)
				System.out.println(i+": "+f.nextLine());
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("File not found");
		}
	}

	private static void add(String userCommand) {

		userCommand = removeFirstWord(userCommand);

		try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(new File(filename).getAbsoluteFile(),true)))) {
			out.println(userCommand);

			System.out.println("added to "+filename+": \""+userCommand+"\"");

		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}

	private static void delete(String userCommand) {
		Scanner f;
		try {
			f = new Scanner(new File(filename));

			int i=0;
			for(i=0;f.hasNextLine();i++,f.nextLine());
			if(i<1) return;
			String[] lines = new String[i];
			f = new Scanner(new File(filename));
			for(i=0;f.hasNextLine();i++)
			{
				lines[i]=f.nextLine();
			}
			userCommand = removeFirstWord(userCommand);
			int no = Integer.parseInt(userCommand);
			no--;
			System.out.println("Deleted from "+filename+": "+lines[no]);

			try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(new File(filename).getAbsoluteFile())))) {
				for(int j=0;j<lines.length;j++)
				{
					if(j!=no)
					{
						out.println(lines[j]);
					}
				}

			} catch (IOException e) {
				System.out.println(e.getMessage());
			}			


		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	private static void clear(String userCommand) {
		try {
			PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(new File(filename).getAbsoluteFile())));
			out.println("\n");
		} catch (IOException e) {
			System.out.println("File not found to delete");
		}
	}

	private static boolean isInvalidUserCommand(String userCommand) { //NEED
		return userCommand.trim().equals("");
	}

	private static void showToUser(String text) { //NEED
		System.out.println(text);
	}

	private static String nextCommand() { //NEED
		return scanner.nextLine();
	}

	private static String removeFirstWord(String userCommand) { //NEED
		return userCommand.replace(getFirstWord(userCommand), "").trim();
	}

	private static String getFirstWord(String userCommand) {
		String commandTypeString = userCommand.trim().split("\\s+")[0];
		return commandTypeString;
	}

	private static String[] splitParameters(String commandParametersString) {
		String[] parameters = commandParametersString.trim().split("\\s+");
		return parameters;
	}
}
