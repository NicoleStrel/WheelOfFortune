/* WheelOfFortuneGame
 * A program that stimulates the famous game "Wheel of Fortune" with some extra features!
 * By: Nicole, Angelina and Anika
 * November 2017
 */

//Import scanner class and io classes
import java.util.Scanner;
import java.io.*;
import java.util.Arrays;

public class WheelOfFortuneGame{
  public static void main(String [] args)throws IOException{
    
    Scanner keyboard= new Scanner (System.in);
    
    //Declare and initialize all needed variables
    final int TWO_PLAYERS=2;
    final int THREE_PLAYERS=3;
    final int TWO_ROUNDS=2;
    final int FIVE_ROUNDS=5;
    final int NEW_GAME=1;
    final int SAME_PLAYERS=3;
    final char NO_LETTER='\0';
    final int START_TURN=1;
    final int NO_REPEAT=0;
    int numGuesses=1;
    String[] phrasesArray= new String [11];
    char[] playerGuesses= new char[numGuesses];
    int numPlayers, numRounds, category;
    int rounds=1, money, endGame, start;
    int randomStart=-1;
    int index=0, searchLetter=0;
    String phrase, playerName, newPhrase;
    char playerGuess;
    boolean trueOrFalse=true;
    
    
    //Introduce the users:
    do{
      System.out.println ("WELCOME to the Wheel Of Fortune Game!!!");
      System.out.println ("In this game, you must guess the letters in a random phrase based on the category, ");
      System.out.println ("and spin the wheel of fortune! The player that gains the most money from the wheel of fortune from guessing phrases, wins the game!");
      System.out.println ("LETS BEGIN!!!");
      
      //Recieve input of number of player's playing:
      System.out.println ("How many players are playing? 2 or 3?");
      numPlayers=keyboard.nextInt();
      while (numPlayers!=TWO_PLAYERS && numPlayers!=THREE_PLAYERS){
        System.out.println ("Enter 2-3 players!");
        numPlayers=keyboard.nextInt();
      }
      
      //Recieve input of player's names:
      String [] players= new String [numPlayers];    
      System.out.println ("What are your player names?");
      for (int i=0; i<numPlayers; i++){
        System.out.print ("Player "+ (i+1)+" : "); 
        players[i]= keyboard.next();
      }
      
      do{
        //Recieve input of number of rounds to play:
        System.out.println ("How many rounds do you want to play? Enter 2-5");
        numRounds=keyboard.nextInt();
        while (numRounds<TWO_ROUNDS|| numRounds>FIVE_ROUNDS){
          System.out.println ("Enter 2-5 rounds!!");
          numRounds=keyboard.nextInt();
        }
        // ---------------------------------Every Round------------------------------------
        for (rounds=1; rounds<=numRounds; rounds++){
          System.out.println ("ROUND "+rounds+ " :");
          
          //Generate a phrase for the round
          System.out.println ("Please choose a category to play in: 1-phrase, 2-song titles, 3-slogans");
          category=keyboard.nextInt();
          phrasesArray=choseCategory(category);
          phrase=readPhrase(phrasesArray);
          newPhrase=phraseDisplay(phrase, playerGuesses);
          System.out.println ("Your new phrase is: "+newPhrase);
          
          //---------------------------------Every Turn -------------------------------------------
          do{
            //generate the order of the nextplayer
            randomStart= orderOfPlayers(players, randomStart, numPlayers);
            playerName=players[randomStart];
            
            do{
              System.out.println (playerName+", Enter 1 to start your turn: ");
              start=keyboard.nextInt();
              while (start!=START_TURN){
                System.out.println ("Enter 1 please!!");
                start=keyboard.nextInt();
              }
              
              //When the player chooses to start their turn
              if (start==START_TURN){
                System.out.println ("Enter a character to guess: ");
                playerGuess=playerTurn();
                
                //If the letter was already guessed
                do{
                  Arrays.sort(playerGuesses);
                  searchLetter=Arrays.binarySearch(playerGuesses, playerGuess);
                  if (searchLetter>NO_REPEAT){
                    System.out.println ("That letter was guessed. Enter another: ");
                    playerGuess=playerTurn();
                  }
                }while (searchLetter>NO_REPEAT);
                
                index=index+1;
                numGuesses=numGuesses+1;
                playerGuesses = Arrays.copyOf(playerGuesses, numGuesses);
                playerGuesses[index]=playerGuess;
         
                //If player has entered something 
                if (playerGuess!='\0'){
                  trueOrFalse=phrase.contains(playerGuess+"");
                  money=wheelOfFortuneSpin(playerName);
                  
                  if (trueOrFalse){
                    System.out.println ("You have guessed correctly");
                  }
                  else{
                    System.out.println ("You have guessed incorectly");
                  }
                  newPhrase=phraseDisplay(phrase, playerGuesses);
                  System.out.println ("The phrase is: "+newPhrase); 
                  System.out.println (playerName+"'s score: "+ playerResult(money, trueOrFalse,  players, numPlayers));             
                }
              }
            }while (trueOrFalse && !phrase.equals(newPhrase));
            newPhrase=phraseDisplay(phrase, playerGuesses);
          }while (!phrase.equals(newPhrase)); 
          
          //--------------------------------------------------------------------------------------
          System.out.println ("||| END OF ROUND: |||");
          displayScore(players,numPlayers);
          
          //refresh all needed values for new round
          phrase="";
          Arrays.fill(playerGuesses,NO_LETTER);
          randomStart=-1;
        }
        //------------------------------------------------------------------------------------------
        System.out.println ("The winner of the game is: "+bestPlayer(players, numPlayers)+" ! CONGRATULATION!! :)");
        System.out.println ("Do you want to play a new game(1), end the program(2) or play with the same players(3)?");
        endGame=keyboard.nextInt();
        
        while (endGame<NEW_GAME || endGame>SAME_PLAYERS){
          System.out.println ("Input Invalid. Please enter a number between 1 and 3: ");
          endGame=keyboard.nextInt();
        }
      }while (endGame==SAME_PLAYERS);
    }while (endGame==NEW_GAME);
    keyboard.close();
    
  }//main program
  //------------------------------------------------------------------------------------
  //                                                                                   |
  //                                   METHODS:                                        |
  //                                                                                   |
  //------------------------------------------------------------------------------------
   
//----------------------------wheelOfFortuneSpin()--Nicole-------------------------------
  public static int wheelOfFortuneSpin (String playerName)throws IOException{
    
    //Declare needed variables:
    final int SKIP_TURN=1;
    final int BANKRUPT=2;
    final int EARN_ZER0=0;
    int[] wheelOfFortuneSpin= {1, 2, 400, 500, 600, 700, 800, 900};
    int lengthWheel=wheelOfFortuneSpin.length;
    int moneyLose;
    
    //Stimulate the wheel of fortune spin
    int randomIndex= (int)(lengthWheel*Math.random());
    int spinValue= wheelOfFortuneSpin[randomIndex];
    
    //Return the amount of money the player wins/ looses
    if (spinValue==SKIP_TURN){
      System.out.println ("Wheel of Fortune Spin- SKIP TURN!");
      return EARN_ZER0;
    } 
    else if (spinValue==BANKRUPT){ 
      System.out.println ("Wheel of Fortune Spin- BANCRUPCY");
      moneyLose= -(readScore(playerName));      
      return moneyLose;
    }
    else{
      System.out.println ("Wheel of Fortune Spin- "+spinValue+" dollars");
      return spinValue;
    }          
  }     
//----------------------------playerTurn()--Nicole--------------------------------------     
  public static char playerTurn() {        
    final Scanner keyboard = new Scanner (System.in);
    
    //Declare needed variables
    final String [] result = new String[1];  
    char playerGuess='\0';
    
    //Create a new thread to check if there is something in the keyboard input 
    Thread t = new Thread(new Runnable(){
      public void run() {
        result[0] = keyboard.nextLine();
      }
    });
    t.start();
    
    //Set up a timer for a 10 second keyboard input       
    for (int i=10; i>=1 && result[0] == null; i--){        
      try {
        Thread.sleep(1000);
      } catch (InterruptedException ie){} 
      
      System.out.println (i+ " seconds left!");
    }
    keyboard.close();
    //Return the players guess or no guess back to main program
    if (result[0] == null){
      System.out.println ("Sorry, you ran out of time.");  
    } 
    else {
      playerGuess = result[0].charAt(0);
    }      
    return playerGuess; 
  }
//----------------------------readScore()-----Nicole----------------------------------------
  public static int readScore (String playerName)throws IOException{
    //Declare needed variables
    int score=0;
    
    //Open file "scores.txt"
    File fileIn = new File("scores.txt");
    Scanner textIn = new Scanner(fileIn); 
    
    //Read score from textfile corresponding to player's name
    while (textIn.hasNext()){   
      String playerLine = textIn.nextLine();
      if (playerLine.startsWith(playerName+"\t")){
        score=Integer.parseInt(playerLine.substring(playerLine.indexOf("\t")+1));
      }
    }
    textIn.close();
    return score;
  }   
//----------------------------displayScore()--Nicole-------------------------------------
  public static void displayScore(String[] players, int numPlayers)throws IOException{
    //Declare needed variables
    final int ONE_PLAYER=1;
    final int THREE_PLAYERS=3;
    
    //Display the score of each player in an organized manner
    System.out.println ("_____________________"+"\n"+"|     SCORES:       |");      
    String player1= players[0];
    System.out.println (player1+" --> "+readScore(player1));
    
    //Output result for more than 1 player (if there is)
    if (numPlayers>ONE_PLAYER){
      String player2=players[1];
      System.out.println (player2+" --> "+readScore(player2));
      
      //Output result for 3 players (if there is)
      if (numPlayers==THREE_PLAYERS){
        String player3=players[2];
        System.out.println (player3+" --> "+readScore(player3));
      }
    }
    System.out.println ("_____________________");
  }    
//----------------------------orderOfPlayers()--Nicole-----------------------------------
  public static int orderOfPlayers(String[] players, int randomStart, int numPlayers){
    //Declare needed variables
    final int FIRST_TURN=-1;
    
    //Pick a random player to start the begining of the round
    if (randomStart==FIRST_TURN){ 
      randomStart=(int)(numPlayers*Math.random());
    }
    //Continue the turns in the array order 
    else{
      if (randomStart+1>=numPlayers){
        randomStart=0;
      }
      else{
        randomStart=randomStart+1;
      }
    }
    return randomStart; 
  }     
//----------------------------chooseCategory()--Angelina----------------------------------
  //Method that reads the text file for the chosen category
  public static String[] choseCategory(int category)throws IOException{
    String[]guessPhrases = new String [11];
    if (category == 1){
      File phrase = new File("phrases.txt");
      Scanner textIn1 = new Scanner(phrase);  
      for (int i = 0;  i <11; i ++){
        guessPhrases[i] = textIn1.nextLine();
      }
      textIn1.close();
    }
    else if (category == 2){
      File songTitles = new File("songTitles.txt");
      Scanner textIn2 = new Scanner(songTitles);  
      for (int i = 0;  i <11; i ++){
        guessPhrases[i] = textIn2.nextLine();
      }
      textIn2.close();
    }
    else if (category == 3){
      File slogans = new File("slogans.txt");
      Scanner textIn3 = new Scanner(slogans); 
      for (int i = 0; i <11; i ++){
        guessPhrases[i] = textIn3.nextLine();
      }
      textIn3.close();
    }
    return guessPhrases; 
    
  }
//----------------------------readPhrase()--Angelina--------------------------------------
  //randomly takes one string from the selected category text file
  public static String readPhrase(String[] guessPhrases){
    String randomPhrase;
    int randomNumber = (int)(11*Math.random()); 
    randomPhrase = guessPhrases[randomNumber]; 
    return randomPhrase;
  }
  
//----------------------storeGuess()----Angelina--------------------------------------
  public static char [] storeGuess(char playerGuess){
    char [] guessedLetters = new char[96];
    for (int index= 0; index<= 96; index++){
      guessedLetters[index] = playerGuess;
    }
    return guessedLetters;
  }
//----------------------------phraseDisplay()--Angelina------------------------------------
  public static String phraseDisplay (String phrase, char[] playerGuesses){
    int length = phrase.length();
    String emptySlot="_";
    String emptySpace=" ";
    String newPhrase="";
    
    for (int i = 0; i < length; i++){
        String letter = Character.toString(phrase.charAt(i));
        String newLetter = emptySlot;
        for (int j=0; j<playerGuesses.length; j++){
            String guess=Character.toString(playerGuesses[j]);
            if (letter.equalsIgnoreCase(guess)){
                newLetter = letter;
                break;
            } else if (letter.equals(emptySpace)){
                newLetter = " ";
                break;
            } 
        }
        newPhrase=newPhrase+newLetter;
    }
    return newPhrase;
  }
  //old one 
//  public static String phraseDisplay (String phrase, char playerGuess, String newPhrase){
//    int length = phrase.length();
//    String playerGuessStr = playerGuess+"";
//    String emptySlot="_ ";
//    String emptySpace=" ";
//    for (int i = 0; i <= length-1; i++){
//      String letter = Character.toString(phrase.charAt(i)); 
//      if (letter.equalsIgnoreCase(playerGuessStr))
//        newPhrase=newPhrase+letter;
//      else if (letter.equals(emptySpace))
//        newPhrase = newPhrase+"  ";
//      else
//        newPhrase=newPhrase+emptySlot;
//      
//    }
//    return newPhrase;
//  }
  
  
  //----------------------------readName()--Anika---------------------------------
  public static String readName(String playerName)throws IOException{
    //Declare needed variables
    String name  = "";
    File fileIn = new File("scores.txt");
    Scanner textIn = new Scanner(fileIn); 
    //Read score from textfile corresponding to player's name
    while (textIn.hasNext()){   
      String playerLine = textIn.nextLine();
      if (playerLine.startsWith(playerName+"\t")){
        name = playerLine.substring(0, playerLine.indexOf("\t")); 
      }
    }
    textIn.close();
    return name;
  }
   
//----------------------------bestPlayer()--Angelina---------------------------------------
  public static String bestPlayer(String [] players, int numPlayers)throws IOException{
    int index=0;
    String playerName1=players[index];
    int score1 = readScore(playerName1);
    String playerName2=players[index+1];
    int score2 = readScore(playerName2);
    
    String playerName3="";
    int score3=0;
    if (numPlayers>2){
       playerName3=players[index+2];
       score3 = readScore(playerName3);
    }

    int bestScore = score1;
    if (score2 >= bestScore){
      bestScore = score2; 
    }else if (score3 >= score2){
      bestScore = score3;
    }
    String bestScoreStr = Integer.toString (bestScore);
    String bestPlayer = readBestPlayerName(bestScoreStr); 
    return bestPlayer; 
  }
  
  //------------------------readBestPlayerName()--Angelina---------------------------
  public static String readBestPlayerName (String bestScoreStr)throws IOException{ 
    String bestPlayer = "";
    //Open file Scores
    File fileIn = new File("scores.txt");
    Scanner textIn = new Scanner(fileIn); 
    
    //Read player's name corresponding to player's name 
    while (textIn.hasNext()){
      String playerLine = textIn.nextLine(); 
      if(playerLine.contains(bestScoreStr)){
        bestPlayer = playerLine.substring(0,playerLine.indexOf("\t"));
      }
    }
    textIn.close();
    return bestPlayer;
  }
  
//----------------------------playerResult()--Anika----------------------------------------
    public static int playerResult(int money, boolean lossOrGain, String[] players, int numPlayers)throws IOException{
    int index=0;

    String playerName1=players[index];
    int score1 = readScore(playerName1);
    String playerName2=players[index+1];
    int score2 = readScore(playerName2);
    
    String playerName3="";
    int score3=0;
    if (numPlayers>2){
       playerName3=players[index+2];
       score3 = readScore(playerName3);
    }
    
    int newScore;
    //parameter entered as player1 will be the score we change
    if (lossOrGain == true){
      newScore = score1 + money;
    }
    else{
      newScore = score1-money;
    }
    
    FileWriter fileOut = new FileWriter("scores.txt");
    PrintWriter textOut = new PrintWriter(fileOut); 
    textOut.println(playerName1+"\t"+newScore);
    if (numPlayers>1){
      textOut.println(playerName2+"\t"+score2);
      if (numPlayers==3){
        textOut.println(playerName3+"\t"+score3);
      }
    }
    textOut.close();
    return newScore;  
  }
//--------------------------------------------------------------------------------
}// WheelOfFortuneGame class
