import java.io.File;
import java.io.StreamTokenizer;

public class Game {

	public static void main(String[] args)
	{
		//declare variables
		
		//tracks number of incorrect answers
		int failures=0;
		//number of allowed failures
		int numOfFailures=7;
		
		//temp word, will come from a file or database in the end
		String word="Java";
		String visibleWord="****";
//		String[] word={"J","a","v","a"};
//		String[] visibleWord={"*","*","*","*"};
		
		//game loop controller
		boolean runGame = true;
		

		
		word = word.toUpperCase();
		//StreamTokenizer
        StreamTokenizer Input=new StreamTokenizer(System.in); 
		
		//Run the input/output portion
		while(runGame){
			try{

			System.out.println("Failures: " + failures + "\n");
			System.out.println("Clue: " + visibleWord + "\n");
			
            System.out.print("Enter your guess?" + "\n");
            Input.nextToken();
            
            //local variables
            String guess = Input.sval;
            guess = guess.toUpperCase();
            guess = guess.substring(0,1);
            
            System.out.print("Your guess is:" + guess + "\n");

    		//keeps track of most recent answers state
    		boolean guessChecker = word.contains(guess);
    		int index = word.indexOf(guess);
    		if(index>=0)
//    			replace
            System.out.print("Is it in the word: " + guessChecker + "\n");
            System.out.print("Index: " + index + "\n");
            if(guessChecker==false)
            	failures++;
            
            	//win state
	            if(word == visibleWord)
	            {
	                System.out.print("You win\n");
	                runGame=false;	            	
	            }            
            	//win state
	            else if(failures == numOfFailures)
	            {
	                System.out.print("You lose\n");
	                runGame=false;
	            }
			}
            catch (Exception e) {
                e.printStackTrace();
             } 
		}
	}//end main

}
