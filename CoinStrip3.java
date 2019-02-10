import java.util.*;
public class CoinStrip3 {

	public static void main(String[] args) {

		CoinStrip3 game = new CoinStrip3(7, 4);
		game.play();
		
	}

	static boolean[] realGame;
	static int size;
	static int numPieces;

	public CoinStrip3(int length, int coins) {
		size = length;
		numPieces = coins;
		realGame = new boolean[size];
		fillGame(numPieces);
	}



	public void play() {

		int iteration = 0;

		while(!gameIsFinished(realGame)) {

			iteration++;
			System.out.print("\n" + this);

			if(iteration%2 == 1) {
				this.playerMove();
			}

			else {
				int[] aiMove = goodAI();
				if(aiMove[0] == 0 && aiMove[1] == 0) {
					System.out.println("I resign!");
					break;
				}
				else {
					System.out.println("AI's move...");
					realGame = movePiece(realGame, aiMove[0], aiMove[1]);
				}
			}
		}

		System.out.print("\n" + this);

		if(iteration%2 == 1) {
			System.out.println("Congratulations! You win!");
		}
		else {
			System.out.println("Damn, too bad! AI wins.");
		}

	}
	
	private int[] goodAI() {
		ArrayList<int[]> allMoves = getAllMoves(realGame);
		boolean[] test = realGame;
		for(int i = 0; i < allMoves.size(); i++) {
			if(branch(test, allMoves.get(i), 0)) {
				return allMoves.get(i);
			}
		}
		return badAI(test);
	}

	private boolean branch(boolean[] gameState, int[] move, int turn) {
		if(turn%2 == 0 && (wins(gameState, move) || wins2(gameState, move))) {
			return true;
		}
		if(!loses(gameState, move) && !loses2(gameState, move)) {
			gameState = movePiece(gameState, move[0], move[1]);
			ArrayList<int[]> allMoves = getAllMoves(gameState);
			for(int i = 0; i < allMoves.size(); i++) {
				if(branch(gameState, allMoves.get(i), turn + 1)) {
					return true;
				}
			}
		}
		return false;
	}

	private int[] badAI(boolean[] gameState) {

		ArrayList<int[]> allMoves = getAllMoves(gameState);
		for(int i = 0; i < allMoves.size(); i++) {
			if(!loses(gameState, allMoves.get(i)) && !loses2(gameState, allMoves.get(i))) {
				return allMoves.get(i);
			}
		}
		int[] array = {0, 0};
		return array;
		
	}

	private ArrayList<int[]> getAllMoves(boolean[] gameState){

		ArrayList<int[]> list = new ArrayList<int[]>();
		int[] temp = new int[2];

		for(int i = 1; i < size; i++) {
			if(validTile(gameState, i)) {
				for(int j = 1; j <= i; j++) {
					if(validMove(gameState, i, j)) {
						temp[0] = i;
						temp[1] = j;
						list.add(temp);
					}
				}
			}
		}
		return list;
	}

	private boolean wins(boolean[] gameState, int[] move) {
		gameState = movePiece(gameState, move[0], move[1]);
		return gameIsFinished(gameState);
	}

	private boolean loses(boolean[] gameState, int[] move) {
		gameState = movePiece(gameState, move[0], move[1]);
		ArrayList<int[]> allMoves = getAllMoves(gameState);
		for(int i = 0; i < allMoves.size(); i++) {
			if(wins(gameState, allMoves.get(i))) {
				return true;
			}
		}
		return false;
	}

	private boolean wins2(boolean[] gameState, int[] move) {
		gameState = movePiece(gameState, move[0], move[1]);
		ArrayList<int[]> allMoves = getAllMoves(gameState);
		for(int i = 0; i < allMoves.size(); i++) {
			if(loses(gameState, allMoves.get(i))) {
				return false;
			}
		}
		return true;
	}

	private boolean loses2(boolean[] gameState, int[] move) {
		gameState = movePiece(gameState, move[0], move[1]);
		ArrayList<int[]> allMoves = getAllMoves(gameState);
		for(int i = 0; i < allMoves.size(); i++) {
			if(wins2(gameState, allMoves.get(i))) {
				return true;
			}
		}
		return false;
	}

	private void fillGame(int coins) {
		Random rand = new Random(); 
		int randIndex; 

		while(coins > 0) { 

			randIndex = rand.nextInt(size); 

			if(!realGame[randIndex]) { 

				realGame[randIndex] = true; 
				if(coins == 1 && gameIsFinished(realGame)) {
					realGame[randIndex] = false;
				}

				else{
					coins--;
				}
			}
		}
	}

	private boolean gameIsFinished(boolean[] gameState) {
		int index = 0; 
		while(gameState[index]) { 
			index++; 
		}
		return index == numPieces;
	}

	private boolean validTile(boolean[] gameState, int index) {
		return (index > 0 && index < size && gameState[index] && !gameState[index - 1]);
	}

	private boolean validMove(boolean[] gameState, int index, int movement) {
		if(!validTile(gameState, index)) {
			return false;
		}

		if(movement <= 0 || (index - movement) < 0){
			return false;
		}

		for(int i = 1; i <= movement; i++) {
			if(gameState[index - i]) {
				return false;
			}
		}

		return true;
	}

	private boolean[] movePiece(boolean[] gameState, int index, int movement) {
		gameState[index] = false;
		gameState[index - movement] = true;
		return gameState;
	}

	public String toString() {
		String board = "[";
		for(int i = 0; i < size - 1; i++) {
			if(realGame[i]) {
				board += " O |";
			}
			else {
				board += "   |";
			}
		}
		if(realGame[size-1]) {
			board += " O ]\n";
		}
		else {
			board += "  ]\n";
		}
		board += "  ";
		for(int i = 0; i < size; i++) {
			if(i > 99) {
				board += i + " ";
			}
			else if(i > 9) {
				board += i + "  ";
			}
			else {
				board += i + "   ";
			}
		}
		return board + "\n";
	}

	private void playerMove() {

		Scanner reader = new Scanner(System.in);
		int index;
		int movement;

		System.out.print("Your move. ");

		System.out.print("Please type the index of the piece you would like to move: ");
		index = reader.nextInt();

		while(!validTile(realGame, index)) {
			System.out.println("");
			System.out.print(this);
			System.out.print("That is not a legal index. Please type the index of the piece you would like to move: ");
			index = reader.nextInt();
		}

		System.out.print("Please type how far you would like to move that piece: ");
		movement = reader.nextInt();

		while(!validMove(realGame, index, movement)) {
			System.out.println("");
			System.out.print(this);
			System.out.print("That is not a legal move. Please type how far you would like to move that piece: ");
			movement = reader.nextInt();
		}

		movePiece(realGame, index, movement);
	}

}
