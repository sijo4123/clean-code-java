package victor.training.trivia;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class GameRefactored implements IGame {
   private final Writer writer;

   List<Player> players = new ArrayList<>();
   int[] purses = new int[6];
   boolean[] inPenaltyBox = new boolean[6];

   private List<String> popQuestions = new LinkedList<>();
   private List<String> scienceQuestions = new LinkedList<>();
   private List<String> sportsQuestions = new LinkedList<>();
   private List<String> rockQuestions = new LinkedList<>();

   int currentPlayer = 0;
   boolean isGettingOutOfPenaltyBox;

   public GameRefactored(Writer writer) {
      this.writer = writer;
      for (int i = 0; i < 50; i++) {
         popQuestions.add("Pop Question " + i);
         scienceQuestions.add("Science Question " + i);
         sportsQuestions.add("Sports Question " + i);
         rockQuestions.add("Rock Question " + i);
      }
   }

   private void writeText(Object text) {
      try {
         writer.write(text + "\n");
         writer.flush();
      } catch (IOException e) {
         throw new RuntimeException(e);
      }
   }

   public boolean isPlayable() {
      return (howManyPlayers() >= 2);
   }

   @Override
   public boolean add(String playerName) {
      players.add(new Player(playerName));
      purses[howManyPlayers()] = 0;
      inPenaltyBox[howManyPlayers()] = false;

      writeText(playerName + " was added");
      writeText("They are player number " + players.size());
      return true;
   }

   public int howManyPlayers() {
      return players.size();
   }

   @Override
   public void roll(int roll) {
      writeText(getCurrentPlayerName() + " is the current player");
      writeText("They have rolled a " + roll);

      if (inPenaltyBox[currentPlayer]) {
         if (roll % 2 != 0) {
            isGettingOutOfPenaltyBox = true;

            writeText(getCurrentPlayerName() + " is getting out of the penalty box");
            movePlayer(roll);

            writeText(getCurrentPlayerName()
                + "'s new location is "
                + players.get(currentPlayer).getPlace());
            writeText("The category is " + currentCategory().label);
            askQuestion();
         } else {
            writeText(getCurrentPlayerName() + " is not getting out of the penalty box");
            isGettingOutOfPenaltyBox = false;
         }

      } else {

         movePlayer(roll);

         writeText(getCurrentPlayerName()
             + "'s new location is "
             + players.get(currentPlayer).getPlace());
         writeText("The category is " + currentCategory().label);
         askQuestion();
      }

   }

   private void movePlayer(int roll) {
      players.get(currentPlayer).move(roll);
   }

   private String getCurrentPlayerName() {
      return players.get(currentPlayer).getName();
   }

   private void askQuestion() {
      writeText(extractNextQuestion());
   }

   private String extractNextQuestion() {
      switch (currentCategory()) {
         case POP: // Map<Category,List<String>>
            return popQuestions.remove(0);
         case SCIENCE:
            return scienceQuestions.remove(0);
         case SPORTS:
            return sportsQuestions.remove(0);
         case ROCK:
            return rockQuestions.remove(0);
         default:
            throw new IllegalStateException("Unexpected value: " + currentCategory());
      }
   }


   private Category currentCategory() {
      switch (players.get(currentPlayer).getPlace()) {
         case 0:
            return Category.POP;
         case 4:
            return Category.POP;
         case 8:
            return Category.POP;

         case 1:
            return Category.SCIENCE;
         case 5:
            return Category.SCIENCE;
         case 9:
            return Category.SCIENCE;
         case 2:
            return Category.SPORTS;
         case 6:
            return Category.SPORTS;
         case 10:
            return Category.SPORTS;
         default:
            return Category.ROCK;
      }
   }

   @Override
   public boolean wasCorrectlyAnswered() {
      if (inPenaltyBox[currentPlayer]) {
         if (isGettingOutOfPenaltyBox) {
            writeText("Answer was correct!!!!");
            purses[currentPlayer]++;
            writeText(getCurrentPlayerName()
                + " now has "
                + purses[currentPlayer]
                + " Gold Coins.");

            boolean winner = didPlayerWin();
            currentPlayer++;
            if (currentPlayer == howManyPlayers()) currentPlayer = 0;

            return winner;
         } else {
            currentPlayer++;
            if (currentPlayer == howManyPlayers()) currentPlayer = 0;
            return true;
         }


      } else {

         writeText("Answer was corrent!!!!");
         purses[currentPlayer]++;
         writeText(getCurrentPlayerName()
             + " now has "
             + purses[currentPlayer]
             + " Gold Coins.");

         boolean winner = didPlayerWin();
         currentPlayer++;
         if (currentPlayer == howManyPlayers()) currentPlayer = 0;

         return winner;
      }
   }

   @Override
   public boolean wrongAnswer() {
      writeText("Question was incorrectly answered");
      writeText(getCurrentPlayerName() + " was sent to the penalty box");
      inPenaltyBox[currentPlayer] = true;

      currentPlayer++;
      if (currentPlayer == howManyPlayers()) currentPlayer = 0;
      return true;
   }


   private boolean didPlayerWin() {
      return !(purses[currentPlayer] == 6);
   }
}
