import java.io.StreamTokenizer;


// dummy line to test git

public class Game extends Main_GUI {

	@SuppressWarnings("deprecation")
	public static void main(String[] args)
	{
		//declare variables
		
		//tracks number of incorrect answers
		int failures=0;
		//number of allowed failures
		int numOfFailures=7;
		
		//temp word, will come from a file or database in the end
		String word="Love";
		StringBuilder visibleWord = new StringBuilder("----");

				
		//game loop controller
		boolean runGame = true;
		
		Main_GUI.main(null);
		
		word = word.toUpperCase();
		
		//StreamTokenizer
        StreamTokenizer Input=new StreamTokenizer(System.in); 
		
		//Run the input/output portion
		while(runGame){
			try{
					System.out.println("Failures: " + failures + "\n");
					System.out.println("Clue: " + visibleWord.toString() + "\n");
					
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
		    			for (int i = word.indexOf(guess);
		    				     i >= 0;
		    				     i = word.indexOf(guess, i + 1))
		    				{
		    					visibleWord.setCharAt(i, guess.charAt(0));
		    				}
		            System.out.print("Is it in the word: " + guessChecker + "\n");
		            
//		            System.out.print(word.matches(visibleWord.toString()));
		            if(guessChecker==false)
		            	failures++;
		            	int temp = Main_GUI.getImgPointer();
		    			Main_GUI.setImgPointer(temp++);
		            
		            //win state
			        if(word.matches(visibleWord.toString()))
			        {
			             System.out.print("You win\n");
			             runGame=false;	            	
			        }            
		            //lose state
			        if(failures == numOfFailures)
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
