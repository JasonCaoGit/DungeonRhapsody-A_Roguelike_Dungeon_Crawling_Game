package byow.lab13;

import byow.Core.RandomUtils;
import edu.princeton.cs.algs4.StdDraw;

import java.awt.Color;
import java.awt.Font;
import java.util.Random;

public class MemoryGame {
    /** The width of the window of this game. */
    private int width;
    /** The height of the window of this game. */
    private int height;
    /** The current round the user is on. */
    private int round;
    /** The Random object used to randomly generate Strings. */
    private Random rand;
    /** Whether or not the game is over. */
    private boolean gameOver;
    /** Whether or not it is the player's turn. Used in the last section of the
     * spec, 'Helpful UI'. */
    private boolean playerTurn;
    /** The characters we generate random Strings from. */
    private static final char[] CHARACTERS = "abcdefghijklmnopqrstuvwxyz".toCharArray();
    /** Encouraging phrases. Used in the last section of the spec, 'Helpful UI'. */
    private static final String[] ENCOURAGEMENT = {"You can do this!", "I believe in you!",
                                                   "You got this!", "You're a star!", "Go Bears!",
                                                   "Too easy for you!", "Wow, so impressive!"};

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Please enter a seed");
            return;
        }

        long seed = Long.parseLong(args[0]);
        MemoryGame game = new MemoryGame(40, 40, seed);


        game.startGame();

    }

    public MemoryGame(int width, int height, long seed) {
        /* Sets up StdDraw so that it has a width by height grid of 16 by 16 squares as its canvas
         * Also sets up the scale so the top left is (0,0) and the bottom right is (width, height)
         */
        this.width = width;
        this.height = height;
        rand=   new Random(seed);
        StdDraw.setCanvasSize(this.width * 16, this.height * 16);
        Font font = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(font);
        StdDraw.setXscale(0, this.width);
        StdDraw.setYscale(0, this.height);
        StdDraw.clear(Color.BLACK);
        StdDraw.enableDoubleBuffering();

        //TODO: Initialize random number generator
    }

    public String generateRandomString(int n) {
        //TODO: Generate random string of letters of length n
        String ret = "";
        for(int i = 0 ; i < n ; i++) {
            int randomInt = rand.nextInt(CHARACTERS.length);
            char randomChat = CHARACTERS[randomInt];
            ret += randomChat;
        }
        return ret;
    }
    //show the string 2 seconds
    public void drawFrame(String s, boolean isWatch) {
        //TODO: Take the string and display it in the center of the screen
        StdDraw.clear(Color.BLACK);
        Font myFont = new Font("Serif", Font.BOLD, 30);
        StdDraw.setFont(myFont);
         StdDraw.setPenColor(StdDraw.WHITE);
        StdDraw.text(width / 2, height / 2, s);
        StdDraw.text(4, height-2, "Round :" + round);
        if (isWatch) {
            StdDraw.text(width / 2, height - 2, "Watch!");
        } else {
            StdDraw.text(width / 2, height - 2, "Type!");

        }








        StdDraw.show(1000);
        StdDraw.clear(Color.BLACK);

        //TODO: If game is not over, display relevant game information at the top of the screen
    }


    public void drawFrame(boolean isWatch) {
        //TODO: Take the string and display it in the center of the screen

        Font myFont = new Font("Serif", Font.BOLD, 30);
        StdDraw.setFont(myFont);
        StdDraw.setPenColor(StdDraw.WHITE);

        StdDraw.text(4, height - 2, "Round :" + round);
        if (isWatch) {
            StdDraw.text(width / 2, height - 2, "Watch!");
        } else {
            StdDraw.text(width / 2, height - 2, "Type!");

        }
    }

    /*
    *
    * so that it takes the input string and displays one character at a
time centered on the screen. Each character should be visible on the screen for 1 second and there
should be a brief 0.5 second break between characters where the screen is blank.
    * */

    public void flashSequence(String letters) {
        //TODO: Display each character in letters, making sure to blank the screen between letters
        StdDraw.clear(Color.BLACK);
        char[] chars = letters.toCharArray();
        for(char c : chars) {
            StdDraw.setPenColor(StdDraw.WHITE);
            StdDraw.text(width / 2, height / 2, Character.toString(c));
            drawFrame(true);

            StdDraw.show(1000);
            StdDraw.clear(Color.BLACK);
            drawFrame(true);

            StdDraw.show(500);


        }
    }

    /*
    *
    * Once you’ve familiarized yourself with how StdDraw handles inputs from the keyboard, write
solicitNCharsInput which reads n keystrokes using StdDraw and returns the string corresponding to
those keystrokes. Also, the string built up so far should appear centered on the screen as keys are being
typed by the user so that they can see what they’ve hit so far.*/

    public String solicitNCharsInput(int n) {
        //TODO: Read n letters of player input
        StdDraw.clear(Color.BLACK);
        String answer = "";
        while (true) {
            StdDraw.clear(Color.BLACK);
            if (StdDraw.hasNextKeyTyped()) {
            char c = StdDraw.nextKeyTyped();
            answer += c;
            StdDraw.setPenColor(StdDraw.WHITE);
            StdDraw.text(width / 2, height / 2, answer);

            StdDraw.show();




        }
             if (answer.length() == n) {
                  StdDraw.setPenColor(StdDraw.WHITE);
            StdDraw.text(width / 2, height / 2, answer);
            StdDraw.show(500);


                 StdDraw.clear(Color.BLACK);
                 StdDraw.show();
                break;
            }

        }




        return answer;
    }

    public void startGame() {
        //TODO: Set any relevant variables before the game starts
        round = 1;
        while (true) {
            gameOver = true;
            StdDraw.clear(Color.BLACK);
            drawFrame("Round " + round, true);

            String randomString = generateRandomString(round);
            flashSequence(randomString);
            StdDraw.clear(Color.BLACK);
            drawFrame(false);
            StdDraw.show(500);
            String answer = solicitNCharsInput(round);
            if (answer.equals(randomString)) {
                gameOver = false;
                round++;
                continue;
            } else {
                gameOver = true;
                drawFrame("Game Over! You made it to round " + round, true);

                break;
            }

        }

        //TODO: Establish Engine loop
    }

}
