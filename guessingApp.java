import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

class GameConfig {
    private final int MIN = 1;
    private final int MAX = 100;
    private final int MAX_ATTEMPTS = 7;
    private final int MAX_HINTS = 3;
    int targetNumber;

    public GameConfig() {
        Random random = new Random();
        targetNumber = random.nextInt(MAX - MIN + 1) + MIN;
    }

    public int getTargetNumber() { return targetNumber; }
    public int getMaxAttempts() { return MAX_ATTEMPTS; }
    public int getMaxHints() { return MAX_HINTS; }

    public void showRules() {
        System.out.println("Guess a number between " + MIN + " and " + MAX);
        System.out.println("You have " + MAX_ATTEMPTS + " attempts.");
        System.out.println("Hints will be provided after wrong guess.\n");
    }
}

class GuessValidator {
    public static String validateGuess(int guess, int target) {
        if (guess == target) {
            return "CORRECT";
        } else if(guess < target){
            return "LOW";
        }
        return "HIGH";
    }
}

class HintService {
    public static String generateHint(int target, int hintCount) {
        if (hintCount == 1) {
            return (target % 2 == 0) ? "Hint: Number is EVEN" : "Hint: Number is ODD";
        } else if (hintCount == 2) {
            return (target > 50) ? "Hint: Number is greater than 50" : "Hint: Number is 50 or less";
        }

        return "No more hints available";
    }
}

class InvalidInputException extends Exception {
    public InvalidInputException(String message) {
        super(message);
    }
}

class ValidationService {
    public static int validateInput(String input) throws InvalidInputException {
        try {
            int value = Integer.parseInt(input);
            if (value < 1 || value > 100) {
                throw new InvalidInputException("Number must be between 1 and 100");
            }
            return value;
        } catch (NumberFormatException e) {
            throw new InvalidInputException("Invalid input. Please enter numbers only");
        }
    }
}

class StorageService {
    public static void saveResult(String player, int attempt, boolean win) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("game_results.txt", true))) {
            writer.write("Player: " + player + ", Attempts: " + attempt + ", Result: " + (win ? "WIN" : "LOSE"));
            writer.newLine();
        } catch (IOException e) {
            System.out.println("Unable to save game result.");
        }
    }
}

class GameControler {
    public static boolean restartGame(Scanner scanner) {
        System.out.println("Do you want to play once again? (Yes/No) :");
        return scanner.nextLine().equalsIgnoreCase("Yes");
    }
}

class GuessingApp {
    public static void main(String[] args) throws InvalidInputException {
        boolean restart;
        System.out.println("=======================================");
        System.out.println("Welcome to the Guessing App");
        System.out.println("=======================================");

        GameConfig gameConfig = new GameConfig();
        gameConfig.showRules();
        Scanner scanner = new Scanner(System.in);
        do {
            System.out.println("Enter Player Name: ");
            String player = scanner.nextLine();
            int attempts = 0;
            boolean win = false;

            while (attempts < gameConfig.getMaxAttempts()) {
                System.out.println("Enter your guess : ");
                int guess = ValidationService.validateInput(scanner.nextLine());
                attempts++;

                if (attempts <= 3) {
                    String hint = HintService.generateHint(gameConfig.getTargetNumber(), attempts);
                    System.out.println(hint);
                }

                String result = GuessValidator.validateGuess(guess, gameConfig.getTargetNumber());
                System.out.println(result);

                if ("CORRECT".equals(result)) {
                    win = true;
                    break;
                }
            }
            StorageService.saveResult(player, attempts, win);
            restart = GameControler.restartGame(scanner);
        } while(restart);
    }
}