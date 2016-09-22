import java.util.ArrayList;
import java.util.Scanner;

/**
 * A menu that is displayed on a console.
 */
class Menu {
    private String title;
    private ArrayList<String> options;
    private Scanner in;

    /**
     * Constructs an empty menu (i.e., a menu with no options).
     * @param title The title of the menu.
     * @param in The scanner object for reading the menu input from (probably based on System.in).
     */
    Menu(String title, Scanner in) {
        this.title = title;
        this.options = new ArrayList<String>();
        this.in = in;
    }

    /**
     * Adds an option to the end of this menu.
     * @param option the option to add.
     */
    void addOption(String option) {
        options.add(option);
    }

    /**
     * Display the menu, with options numbered starting with 1,
     * and prompts the user for input. Repeat the prompt until a valid input
     * is supplied. The prompt is the string "Choice: ".
     * @return the option number that the user chose.
     */
    int executeMenu() {
        return executeMenu("Choice: ");
    }

    /**
     * Display the menu, with options numbered starting with 1,
     * and prompts the user for input. Repeat the prompt until a valid input
     * is supplied.
     * @param prompt the prompt the user is shown.
     * @return the option number that the user chose.
     */
    private int executeMenu(String prompt) {
        printTitle();
        printOptions();

        // Get the user's choice
        boolean validChoiceEntered = false;
        String input = null;
        int inputAsInt = 0;
        do {
            System.out.print(prompt);
            input = in.nextLine();

            try {
                inputAsInt = Integer.parseInt(input);
                // We now definitely have an integer as a choice.
                if (inputAsInt >= 1 && inputAsInt <= options.size()) {
                    validChoiceEntered = true;
                } else {
                    // Out of range integer has been input.
                    printInputError(input);
                }
            } catch (NumberFormatException e) {
                // A non-integer has been input.
                printInputError(input);
            }
        } while (!validChoiceEntered);

        return inputAsInt;
    }

    /**
     * Print the title of the menu
     */
    private void printTitle() {
        for (int i = 0; i < title.length(); i++) {
            System.out.print("-");
        }
        System.out.print("\n");
        System.out.println(title);
        for (int i = 0; i < title.length(); i++) {
            System.out.print("-");
        }
        System.out.println();
        System.out.print("Please choose from one of the following options:\n\n");
    }

    /**
     * Print the options of the menu.
     */
    private void printOptions() {
        for (int i = 0; i < options.size(); i++) {
            // We want the choices to be numbered from 1 (not 0).
            int choice = i + 1;
            System.out.println(choice + ") " + options.get(i));
        }
    }

    /**
     * Print a error message for invalid input.
     * @param input The input that is invalid.
     */
    private void printInputError(String input) {
        System.out.println();
        System.out.println("Error: " + input + " is an invalid choice.");
        System.out.println("You must input a number between 1 and " + options.size() + ".");
        System.out.println();
    }
    /**
     * Method to return the users Array List .
     * @return
     */
    public int getMenuList() {
        return options.size();
    }
}