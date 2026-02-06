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

class GuessingApp {
    public static void main(String[] args) {
        System.out.println("Welcome to the Guessing App");
        GameConfig gameConfig = new GameConfig();
        gameConfig.showRules();
        Scanner scanner = new Scanner(System.in);
        int attempts = 0;

        while (attempts < gameConfig.getMaxAttempts()) {
            System.out.println("Enter your guess : ");
            int guess = scanner.nextInt();
            attempts++;

            if (attempts <= 3) {
                String hint = HintService.generateHint(gameConfig.getTargetNumber(), attempts);
                System.out.println(hint);
            }

            String result = GuessValidator.validateGuess(guess, gameConfig.getTargetNumber());
            System.out.println(result);

            if ("CORRECT".equals(result)) {
                break;
            }
        }
    }
}