
/**
 * CMPE 250 Project 1 - Nightpass Survivor Card Game
 *
 * This skeleton provides file I/O infrastructure. Implement your game logic
 * as you wish. There are some import that is suggested to use written below.
 * You can use them freely and create as manys classes as you want. However,
 * you cannot import any other java.util packages with data structures, you
 * need to implement them yourself. For other imports, ask through Moodle before
 * using.
 *
 * TESTING YOUR SOLUTION:
 * ======================
 *
 * Use the Python test runner for automated testing:
 *
 * python test_runner.py              # Test all cases
 * python test_runner.py --type type1 # Test only type1
 * python test_runner.py --type type2 # Test only type2
 * python test_runner.py --verbose    # Show detailed diffs
 * python test_runner.py --benchmark  # Performance testing (no comparison)
 *
 * Flags can be combined, e.g.:
 * python test_runner.py -bv              # benchmark + verbose
 * python test_runner.py -bv --type type1 # benchmark + verbose + type1
 * python test_runner.py -b --type type2  # benchmark + type2
 *
 * MANUAL TESTING (For Individual Runs):
 * ======================================
 *
 * 1. Compile: cd src/ && javac *.java
 * 2. Run: java Main ../testcase_inputs/test.txt ../output/test.txt
 * 3. Compare output with expected results
 *
 * PROJECT STRUCTURE:
 * ==================
 *
 * project_root/
 * ├── src/                     # Your Java files (Main.java, etc.)
 * ├── testcase_inputs/         # Input test files
 * ├── testcase_outputs/        # Expected output files
 * ├── output/                  # Generated outputs (auto-created)
 * └── test_runner.py           # Automated test runner
 *
 * REQUIREMENTS:
 * =============
 * - Java SDK 8+ (javac, java commands)
 * - Python 3.6+ (for test runner)
 *
 * @author CMPE250 Student (replace with your name)
 */

import java.io.*;
import java.util.Scanner;
import java.util.ArrayList;
import java.math.*;

public class Main {
    public static void main(String[] args) {
        // Check command line arguments
        Tree deckTree=new Tree();
        Tree discardTree=new Tree();
        Score score = deckTree.score; // shared score
        if (args.length != 2) {
            System.out.println("Usage: java Main <input_file> <output_file>");
            System.out.println("Example: java Main ../testcase_inputs/test.txt ../output/test.txt");
            return;
        }

        String inFile = args[0];
        String outFile = args[1];

        // Initialize file reader
        Scanner reader = null;
        try {
            reader = new Scanner(new File(inFile));
        } catch (FileNotFoundException e) {
            System.out.println("Input file not found: " + inFile);
            e.printStackTrace();
            return;
        }

        // Initialize file writer
        FileWriter writer = null;
        try {
            writer = new FileWriter(outFile);
        } catch (IOException e) {
            System.out.println("Writing error: " + outFile);
            e.printStackTrace();
            if (reader != null)
                reader.close();
            return;
        }

        // Process commands line by line
        try {
            while (reader.hasNext()) {
                String line = reader.nextLine();
                Scanner scanner = new Scanner(line);
                String command = scanner.next();
                String out = "";

                switch (command) {
                    case "draw_card": {
                        String name = "";
                        int att = 0;
                        int hp = 0;
                        if (scanner.hasNext())
                            name = scanner.next();
                        if (scanner.hasNext())
                            att = scanner.nextInt();
                        if (scanner.hasNext())
                            hp = scanner.nextInt();
                        Card c = new Card(name, att, hp);
                        if (c.getHcur()!=0)
                        //adding to the deck
                        {deckTree.root = deckTree.nodeInsertingDeck(deckTree.root, c);}

                        out += "Added " + name + " to the deck\n";
                        break;
                    }
                    case "battle": {
                        int att = 0;
                        int hp = 0;
                        int heal = 0;
                        if (scanner.hasNext())
                            att = scanner.nextInt();
                        if (scanner.hasNext())
                            hp = scanner.nextInt();
                        if (scanner.hasNext())
                            heal = scanner.nextInt();

                        //calling battle function and saving a class in return
                        Tree.BattleResult result = deckTree.battle(deckTree.root, discardTree.root, att, hp);

                        deckTree.root = result.deckRoot;
                        discardTree.root = result.discardRoot;

                        int revivedCount = 0; //

                        if (!result.isPlayed) {
                            //if no card found
                            out += "No card to play, " + revivedCount + " cards revived\n";
                        }
                        else if (result.priority == 1 || result.priority == 2) {
                            //if the card survives
                            out += "Found with priority " + result.priority +
                                    ", Survivor plays " + result.playedCard.getId() +
                                    ", the played card returned to deck, " + revivedCount + " cards revived\n";
                        }
                        else if (result.priority == 3 || result.priority == 4) {
                            //if the card dies
                            out += "Found with priority " + result.priority +
                                    ", Survivor plays " + result.playedCard.getId() +
                                    ", the played card is discarded, " + revivedCount + " cards revived\n";
                        }
                        break;
                    }
                    case "find_winning": {
                        //it will compare the results of stranger and survivor
                        if (score.survivorScore >= score.strangerScore) {
                            //if survivor is currently winning
                            out += "The Survivor, Score: " + score.survivorScore + "\n";
                        } else {
                            //if stranger is currently winning
                            out += "The Stranger, Score: " + score.strangerScore + "\n";
                        }

                        break;
                    }
                    case "deck_count": {
                        //it will tell the current number of deck in the deck
                        out += "Number of cards in the deck: " + deckTree.getCount() + "\n";
                        break;
                    }

                    /*
                     * Comment this out if you are going to implement type-2 commands
                     * case "discard_pile_count": {
                     * out = discardPileCount(); // suggested method for discard_pile_count command
                     * break;
                     * }
                     */
                    case "steal_card": {
                        //it will find the optimal card to steal
                        int att = 0;
                        int hp = 0;
                        if (scanner.hasNext())
                            att = scanner.nextInt();
                        if (scanner.hasNext())
                            hp = scanner.nextInt();
                        Card stolen = deckTree.stealCard(att, hp, deckTree.root);
                        if (stolen == null) {
                            out += "No card to steal\n";
                        } else {
                            Tree.RemoveResult r = deckTree.removeCard(deckTree.root, stolen);
                            deckTree.root = r.newRoot;
                            out += "The Stranger stole the card: " + stolen.getId() + "\n";
                        }
                        // out = steal_card(att, hp); // suggested method for steal_card command
                        break;
                    }
                    default: {
                        scanner.close();
                        writer.close();
                        reader.close();
                        return;
                    }
                }

                scanner.close();

                try {

                    //System.out.println("[DEBUG] Writing to file: " + out.replace("\n", "\\n"));


                    writer.write(out);
                    // writer.write("\n"); // uncomment if each output needs to be in a new line and
                    // you did not implement that inside the functions.
                } catch (IOException e2) {
                    System.out.println("Writing error");
                    e2.printStackTrace();
                }
            }

        } catch (Exception e) {
            System.out.println("Error processing commands: " + e.getMessage());
            e.printStackTrace();
        }

        // Clean up resources
        try {
            writer.close();
        } catch (IOException e2) {
            System.out.println("Writing error");
            e2.printStackTrace();
        }

        if (reader != null) {
            reader.close();
        }

        System.out.println("end");
        return;
    }
}
