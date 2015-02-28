
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CE1 {
	
	private static final String MESSAGE_EMPTY = "%1$s is empty";
	private static final String MESSAGE_INVALID = "Invalid Command: %1$s";
	private static final String MESSAGE_WELCOME = "Welcome to textbuddy, %1$s is ready for use";
	private static final String MESSAGE_PROMPT = "command:";
	private static final String MESSAGE_INVALID_FORMAT = "invalid command format :%1$s";
	
	static PrintWriter printWriter = null;
	static BufferedWriter bufferedWriter = null;
	static BufferedReader bufferedReader = null;
	static BufferedReader bufferedReaderDisplay = null;
	static Scanner scanner = new Scanner(System.in);
	
	static File textFile = null;
	static String fileName = null;
	static String line;
	static int displayCounter = 0;

	static List<String> itemList = new ArrayList<String>();

	// These are the possible command types
	enum COMMAND_TYPE {
		ADD, DISPLAY, DELETE, CLEAR, INVALID, EXIT
	};

	public static void main(String[] args) {
		fileName=args[0];
		textFile = new File(fileName);
		println(String.format(MESSAGE_WELCOME, fileName));
		loadFileToList();
		
		while (true) {
			print(MESSAGE_PROMPT); 
			executeCommand(nextCommand());
		}
	}

	public static void executeCommand(String userCommand) {
		if (isInvalidUserCommand(userCommand))
			println(String.format(MESSAGE_INVALID, userCommand));

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
			clear();
			break;
		case INVALID:
			System.out.println(String.format(MESSAGE_INVALID_FORMAT, userCommand));
			break;
		case EXIT:
			saveListToFile();
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
		displayCounter = 1;
		if (itemList.size() == 0) {
			println (String.format(MESSAGE_EMPTY, fileName));
		}
		else{
			for (String i:itemList){
				println("["+displayCounter+"]: "+i);
				displayCounter++;
			}
			displayCounter = 0; //Reset the counter once function is done
		}
	}

	private static void add(String userCommand) {

		userCommand = removeFirstWord(userCommand);
		itemList.add(userCommand);

	}

	private static void delete(String userCommand) {
			userCommand = removeFirstWord(userCommand);
			int no = Integer.parseInt(userCommand);
			no--;
			itemList.remove(no);
			println("deleted "+ userCommand +" from "+fileName);
		}
	
	private static void createFile (String fileName ) throws FileNotFoundException, UnsupportedEncodingException {
		printWriter = new PrintWriter(fileName);
	}
	private static void clear() {
		try { 
			textFile.delete();
			createFile(fileName);
			itemList = new ArrayList<String>();
			println("all content deleted from " + fileName);
		}
		catch(Exception e)
		{
			System.err.println("Error in clear()");
		}
	}
	
	
	private static void loadFileToList () {
		try {
			Scanner f = new Scanner(textFile);
			if(!f.hasNextLine()){
				//Don't do anything. Keep the list empty.
			}
			for(int i=1;f.hasNextLine();i++) {
				itemList.add(f.nextLine());
			}
		} 
		
		catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("File not found");
		}
	}
	
	private static void saveListToFile() {
		
		for(String i:itemList){			
			printToFile(i);
		}
	}

	private static void printToFile(String text) {
		try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(new File(fileName).getAbsoluteFile(),true)))) {
			out.println(text);
			
		} catch (IOException e) {
			println(e.getMessage());
		}
	}

	private static boolean isInvalidUserCommand(String userCommand) { //NEED
		return userCommand.trim().equals("");
	}

	private static void println(String text) { //NEED
		System.out.println(text);
	}
	
	private static void print(String text) { //NEED
		System.out.print(text);
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
