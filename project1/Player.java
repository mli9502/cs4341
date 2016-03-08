/**
 * @author Yihong Zhou, Mengwen Li
 */
package referee;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * This is our player. The player runs iterative deepening minimax with alpha-beta pruning to detemine the best move.
 * @author yzhou8, mli2
 */
public class Player {
	Board b;
	int width, height, numToWin, playerNumber, timeLimit, move;
	// Keep track of the opponent's number.
	int oppNum;
	// Keep track of how many pop opportunity is left.
	int popTimeLeft;
	// Keep track of how many pop opportunity opponent has left.
	int oppPopLeft;
	// Flg used to control debug printing.
	boolean printFlg = false;
	// Define a Move class to encapsulate column and operation.
	static class Move{
		public int col;
		// 1 for put, 0 for pop.
		public int opt = 1;

		public Move(int val){
			this.col = val;
		}
		public Move(int val, int opt){
			this.col = val;
			this.opt = opt;
		}
		public void setMove(int col, int opt){
			this.col = col;
			this.opt = opt;
		}
		public void setMove(Move m){
			this.col = m.col;
			this.opt = m.opt;
		}
		
		public String toString(){
			return "" + col + " " + opt;
		}
	}
	// Define the player's name.
	String playerName="yzhou8mli2";
	// Open an input stream.
	BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
	boolean turn = false;
	// Method to process the input stream.
	public void processInput() throws IOException{
		if(this.printFlg){
			System.out.println("ENTER YOUR MOVE!!!");
		}
		// Read in input line.
		String s=input.readLine();
		// Parse the input line by space.
		List<String> ls=Arrays.asList(s.split(" "));
		// If input is an opponent's move.
		if(ls.size()==2){
			// Update board with opponent's move to keep track of current board.
			int oppMov = Integer.parseInt(ls.get(0));
			if(Integer.parseInt(ls.get(1)) == 1){
				b.dropADiscFromTop(oppMov, this.oppNum);
			}else{
				b.removeADiscFromBottom(oppMov);
				this.oppPopLeft --;
			}
			// Construct a new move.
			Move m = new Move(0);
			// Try to find the best move in the given time limit.
        	ExecutorService service = Executors.newSingleThreadExecutor();
        	try{
        		Runnable r = new Runnable(){
        			public void run(){
        				try {
							getMove(m);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
        			}
        		};
        		Future<?> f = service.submit(r);
        		// Set the time limit to be 50ms less than the given time.
        		f.get(this.timeLimit * 1000 - 50, TimeUnit.MILLISECONDS);
        	}catch(final InterruptedException e){
        		System.out.println("Thread was interrupted.");
        	}catch(final TimeoutException e){
        	}catch(final ExecutionException e){
        		System.out.println("Runnable task exception.");
        	}finally{
        		// When the getMove finish or time runs out, output the calculated move.
        		System.out.println(m.toString());
        		System.out.flush();
        		// Update the player's board to keep track.
        		if(m.opt == 1){
        			b.dropADiscFromTop(m.col, this.playerNumber);
        		}else{
        			b.removeADiscFromBottom(m.col);
        			this.popTimeLeft --;
        		}
        		// Debug printing.
        		if(this.printFlg){
        			System.out.println("Current board: ");
        			b.printBoard();
        		}
        	}
		}
		// If player gets a game over message.
		else if(ls.size()==1){
			System.out.println("game over!!!");
			System.out.flush();
			System.exit(0);
		}
		// If player gets the board setting.
		else if(ls.size()==5){
			// Set up a board to keep record.
			this.height = Integer.parseInt(ls.get(0));
			this.width = Integer.parseInt(ls.get(1));
			this.numToWin = Integer.parseInt(ls.get(2));
			this.timeLimit = Integer.parseInt(ls.get(4));
			this.oppNum = 3 - this.playerNumber;
			this.popTimeLeft = 1;
			this.oppPopLeft = 1;
			b = new Board(this.height, this.width, this.numToWin);
			// Check whether player is the first to move.
			if(this.playerNumber == Integer.parseInt(ls.get(3))){
				// First move always go to the center of the board.
				int firstTar = (int)this.width/2;
				System.out.println(firstTar + " 1"); //first move
				System.out.flush();
				// Update board after doing the move.
				b.dropADiscFromTop(firstTar, this.playerNumber);
			}
		}
		// If the referee passes in both players names, check the names to determine who goes first.
		else if(ls.size()==4){
			if(this.playerName.equals(ls.get(1))){
				this.playerNumber = 1;
			}else if(this.playerName.equals(ls.get(3))){
				this.playerNumber = 2;
			}else{
				System.out.println("Can't find this player in game!");
			}
		}
		else
			System.out.println("not what I want");
	}

	public static void main(String[] args) throws IOException {
		Player rp=new Player();
		/**
		 * This part is used to test the heuristic function.
		 */

//		rp.playerNumber = 1;
//		rp.height = 4;
//		rp.width = 5;
//		rp.numToWin = 3;
//		rp.oppNum = 2;
//		rp.b = new Board(4, 5, 4);
//		rp.b.setBoard(0, 0, rp.playerNumber);
//		rp.b.setBoard(1, 0, rp.oppNum);
//		rp.b.setBoard(2, 0, rp.playerNumber);
//		rp.b.setBoard(3, 0, rp.playerNumber);
//		rp.b.setBoard(0, 1, rp.oppNum);
//		rp.b.setBoard(1, 1, rp.playerNumber);
//		rp.b.setBoard(2, 1, rp.playerNumber);
//		rp.b.setBoard(3, 1, rp.oppNum);
//		rp.b.setBoard(0, 2, rp.oppNum);
//		rp.b.setBoard(1, 2, rp.oppNum);
//		rp.b.setBoard(2, 2, rp.oppNum);
//		rp.b.setBoard(3, 2, rp.playerNumber);
//		rp.b.setBoard(0, 3, rp.oppNum);
//		rp.b.setBoard(1, 3, rp.oppNum);
//		rp.b.setBoard(2, 3, rp.playerNumber);
//		rp.b.setBoard(3, 3, rp.oppNum);
//		rp.b.setBoard(0, 4, rp.oppNum);
//		rp.b.setBoard(1, 4, rp.oppNum);
//		rp.b.setBoard(2, 4, rp.playerNumber);
//		rp.b.setBoard(3, 4, rp.oppNum);			
//		System.out.println("Test result!!!");
//		rp.b.printBoard();
//		System.out.println(rp.eval(rp.b));

		// Start the game.		
		System.out.println(rp.playerName);
		System.out.flush();
		while (true){
			rp.processInput();
		}
	}

	private void getMove(Move m) throws Exception{
		// Apply iterative deepening on minimax algorithm until time out. 
		
		for(int i=1; ; i++){
			m.setMove(this.minimaxDecision(b, i));
		}
		
		// This is used when a given depth is desired.
		// m.setMove(this.minimaxDecision(b, 2));
	}
	// Minimax algorithm with alpha-beta pruning.
	public Move minimaxDecision(Board b, int depth){
		ArrayList<Integer> actionVal = new ArrayList<Integer>();
		ArrayList<Move> actions = new ArrayList<Move>();
		/**
		 * alpah-beta pruning.
		 */
		int alpha = Integer.MIN_VALUE;
		int beta = Integer.MAX_VALUE;
		
		for(int i=0; i<this.width; i++){
			// Check wheter the move is valid.
			boolean dFlg = b.canDropADiscFromTop(i, this.playerNumber);
			boolean pFlg = b.canRemoveADiscFromBottom(i, this.playerNumber);
			if(this.popTimeLeft != 1){
				pFlg = false;
			}
			if(dFlg){
				Board dropBoard = new Board(b);
				dropBoard.dropADiscFromTop(i, this.playerNumber);
				// Check whether the player can win the game after the move.
				// If it can, return this move immediately.
				if(dropBoard.isConnectN() == this.playerNumber){
					return new Move(i, 1);
				}
				else{
					if(this.printFlg){
						System.out.println("dropBoard is: ");
						dropBoard.printBoard();
					}
					// Update alpha and keep track of action-actionVal pair.
					// action-actionVal pair is used to determine the final move.
					int tmp = this.minVal(dropBoard, depth - 1, this.popTimeLeft, this.oppPopLeft, alpha, beta);
					alpha = tmp > alpha? tmp: alpha;
					actions.add(new Move(i, 1));
					actionVal.add(new Integer(tmp));
				}	
			}
			if(pFlg){
				Board popBoard = new Board(b);
				popBoard.removeADiscFromBottom(i);
				// Check whether the player can win the game after the move.
				// If it can, return this move immediately.
				if(popBoard.isConnectN() == this.playerNumber){
					return new Move(i, 0);
				}else{
					if(this.printFlg){
						System.out.println("popBoard is: ");
						popBoard.printBoard();
					}
					// Update alpha and keep track of action-actionVal pair.
					// action-actionVal pair is used to determine the final move.
					int tmp = this.minVal(popBoard, depth - 1, this.popTimeLeft, this.oppPopLeft, alpha, beta);
					alpha = tmp > alpha? tmp: alpha;
					actions.add(new Move(i, 0));
					actionVal.add(new Integer(tmp));
				}	
			}
			if(!dFlg && !pFlg){
				actions.add(new Move(i, 1));
				actionVal.add(new Integer(Integer.MIN_VALUE));
			}
		}
		// loop through actionVal to find the best action.
		Move rtnMv = actions.get(0);
		int max = Integer.MIN_VALUE;
		for(int i=0; i<actionVal.size(); i++){
			if(actionVal.get(i) > max){
				max = actionVal.get(i);
				rtnMv = actions.get(i);
			}
		}
		return rtnMv;
	}
	
	// Get's the max value of the subtree.
	public int maxVal(Board b, int depth, int popTimeLeft, int oppPopLeft, int alpha, int beta){
		// If reaches the bottom or the board is full, return evaluation.
		if(depth == 0 || b.isFull()){
			int rtn = this.eval(b);
			
			if(this.printFlg){
				System.out.println("At max bottom level: ");
				b.printBoard();
				System.out.println("rtn is " + rtn);
			}
			
			return rtn;
		}
		int v = Integer.MIN_VALUE;
		// Update value of v when going through all the choices.
		for(int i=0; i<this.width; i++){
			// Check whether the drop move is valid.
			if(b.canDropADiscFromTop(i, this.playerNumber)){
				Board dropBoard = new Board(b);
				dropBoard.dropADiscFromTop(i, this.playerNumber);
				
				if(this.printFlg){
					System.out.println("dropBoard is: ");
					dropBoard.printBoard();
				}	
				// Get value from the next level.
				int tmp = this.minVal(dropBoard, depth - 1, popTimeLeft, oppPopLeft, alpha, beta);
				// Update v.
				if(tmp > v){
					v = tmp;
				}
				// Pruning.
				if(v >= beta){
					if(this.printFlg){
						System.out.println("PRUNE!!!");
					}
					return v;
				}
				// update alpha.
				alpha = v > alpha? v: alpha;
			}
			// Check whether the pop move is valid.
			if(b.canRemoveADiscFromBottom(i, this.playerNumber) &&
					popTimeLeft == 1){
				Board popBoard = new Board(b);
				popBoard.removeADiscFromBottom(i);
				if(this.printFlg){
					System.out.println("popBoard is: ");
					popBoard.printBoard();
				}
				// Get value from the next level.
				int tmp = this.minVal(popBoard, depth - 1, 0, oppPopLeft, alpha, beta);
				// Update v.
				if(tmp > v){
					v = tmp;
				}
				// Prune.
				if(v >= beta){
					if(this.printFlg){
						System.out.println("PRUNE!!!");
					}
					return v;
				}
				// Update alpha value.
				alpha = v > alpha? v: alpha;
			}
		}
		if(this.printFlg){
			System.out.println("max result is " + v);
		}
		return v;
	}
	// Get's the min value of the subtree.
	public int minVal(Board b, int depth, int popTimeLeft, int oppPopLeft, int alpha, int beta){
		// If it reaches the bottom or the board is full.
		if(depth == 0 || b.isFull()){
			int rtn = this.eval(b);
			if(this.printFlg){
				System.out.println("At min bottom level: ");
				b.printBoard();
				System.out.println("rtn is " + rtn);
			}
			return rtn;
		}
		int v = Integer.MAX_VALUE;
		// Update value of v when going through all the choices.
		for(int i=0; i<this.width; i++){
			// Check whether the drop move is valid.
			if(b.canDropADiscFromTop(i, this.oppNum)){
				Board dropBoard = new Board(b);
				dropBoard.dropADiscFromTop(i, this.oppNum);
				
				if(this.printFlg){
					System.out.println("dropBoard is: ");
					dropBoard.printBoard();
				}
				// Get value from the next level.
				int tmp = this.maxVal(dropBoard, depth - 1, popTimeLeft, oppPopLeft, alpha, beta);
				// Update v.
				if(tmp < v){
					v = tmp;
				}
				// Prune.
				if(v <= alpha){
					if(this.printFlg){
						System.out.println("PRUNE!!!");
					}
					return v;
				}
				// Update beta.
				beta = v < beta? v: beta;
			}
			// Check whether the pop move is valid.
			if(b.canRemoveADiscFromBottom(i, this.oppNum) && 
					oppPopLeft == 1){
				Board popBoard = new Board(b);
				popBoard.removeADiscFromBottom(i);
				if(this.printFlg){
					System.out.println("popBoard is: ");
					popBoard.printBoard();
				}
				// Get value from the next level.
				int tmp = this.maxVal(popBoard, depth - 1, popTimeLeft, 0, alpha, beta);
				// Update v.
				if(tmp < v){
					v = tmp;
				}
				// Prune.
				if(v <= alpha){
					if(this.printFlg){
						System.out.println("PRUNE!!!");
					}
					return v;
				}
				// Update beta.
				beta = v < beta? v: beta;
			}
		}
		if(this.printFlg){
			System.out.println("min result is " + v);
		}
		
		return v;
	}
	
	// Heuristic function.
	private int eval(Board b){
		ArrayList<Integer> weights = new ArrayList<Integer>();
		for(int i=2; i<=this.numToWin; i++){
			if(i != this.numToWin){
				weights.add(new Integer(i * 100));
			}else{
				weights.add(new Integer(Integer.MAX_VALUE / 100));
			}
		}
		// System.out.println(weights.toString());
		int rtn = 0;
		for(int i=2; i<=this.numToWin; i++){
//			System.out.println("horiConti: " + this.horiContinuous(b, i));
//			System.out.println("vertical: " + this.vertical(b, i));
			rtn += this.horiContinuous(b, i)*weights.get(i - 2) + this.vertical(b, i)*weights.get(i - 2) + this.diagonally1(b, i)*weights.get(i - 2) + this.diagonally2(b, i)*weights.get(i - 2);
//			System.out.println("rtn " + i + " is " + rtn);
		}
		// numToWin-2 is too big
		rtn += this.horiDiscreet(b) * weights.get(this.numToWin - 2);
		if(this.printFlg){
//			System.out.println("eval is " + rtn);
		}
		return rtn;
	}
	
	//check the vertical aligned pieces
	private int vertical(Board b, int n){
		int max1 = 0;
		int max2 = 0;
		int num = 0;
		boolean firstFlag1 = true;
		boolean firstFlag2 = true;
		boolean noUseFlag1 = false;
		boolean noUseFlag2 = false;
		for(int j = 0;j<this.width;j++){
			max1 = 0;
			max2 = 0;
			noUseFlag1 = false;
			firstFlag1 = true;
			noUseFlag2 = false;
			firstFlag2 = true;
			for(int i = 0;i<this.height;i++){
				if(b.board[i][j] == this.playerNumber){
					if(firstFlag1){
						if(i > 0){
							if(b.board[i-1][j] == this.oppNum){
								noUseFlag1 = true;
							}
							else{
//								System.out.println("i:"+i+"j"+j);
								max1 ++;
								max2 = 0;
//								System.out.println("max1:"+max1);
							}
						}
						//check if one side is blocked
						else if(i == 0){
							if(i+n < this.height && j < this.width){
								if(b.board[i+n][j] == this.oppNum && n != this.numToWin){
									noUseFlag2 = true;
								}
								else{
									max1 = 0;
									max2++;
								}
							}
							else{
								max1 ++;
								max2 = 0;
							}
						}
						else{
							max1 ++;
							max2 = 0;
						}
						firstFlag1 = false;
						firstFlag2 = true;
					}
					else{
						if(noUseFlag1){
							continue;
						}
						else{
							max1 ++;
							max2 = 0;
						}					
					}
					//if reaches the numTowin
					if(max1 == n){
						firstFlag1 = true;
//						System.out.println("height-2:"+(height-1));
						if(i < this.height - 1){
//							System.out.println("i:"+i+"j:"+j+"n:"+n);
							if(b.board[i+1][j] == this.playerNumber){
//								System.out.println("im in");
								//System.out.println("i:"+i+"j:"+j);
								continue;
							}
							else{
								num ++;
							}
						}
						else{
							num ++;
						}
					}
				}
				else if(b.board[i][j] == this.oppNum){
					if(firstFlag2){
						if(i > 0){
							if(b.board[i-1][j] == this.playerNumber){
								noUseFlag2 = true;
							}
							else{
//								System.out.println("hi i am here");
								max1 = 0;
								max2++;
							}
						}
						//check if one side is blocked
						else if(i == 0){
							// System.out.println("i+n+1:"+(i+n+1));
							if(i+n < this.height && j < this.width){
								if(b.board[i+n][j] == this.playerNumber && n != this.numToWin){
									noUseFlag2 = true;
								}
								else{
									max1 = 0;
									max2++;
								}
							}
							else{
								max1 = 0;
								max2++;
							}
						}
						else{
							max1 = 0;
							max2++;
						}
						firstFlag2 = false;
						firstFlag1 = true;
					}
					else{
						if(noUseFlag2){
							continue;
						}
						else{
							max1 = 0;
							max2++;
						}					
					}
					//if reaches the numTowin
					if(max2 == n){
						firstFlag2 = true;
//						System.out.println("height-2:"+(height-2));
						if(i < this.height - 1){
//							System.out.println("i:"+i+"j:"+j+"n:"+n);
							if(b.board[i+1][j] == this.oppNum){
//								System.out.println("im in");
//								System.out.println("i:"+i+"j:"+j);
								continue;
							}
							else{
								num --;
							}
						}
						else{
							num --;
						}
					}					
				}
				else{
					max1=0;
					max2=0;
				}
			}
		} 
//		System.out.println("num for vertical:"+num);
		return num;
	}
	
	//check horizontal aligned piece with continuity
	private int horiContinuous(Board b, int n){
		//check continuous number of n-1,n-2,n-3, if any empty block on the left or right and if empty under it
		// b.printBoard();
		int num = 0;
		int max1_con = 0;
		int leftCheckCol1 = 0;
		boolean firstFlag1 = true;
		int max2_con = 0;
		int leftCheckCol2 = 0;
		boolean firstFlag2 = true;
//		System.out.println("CHECK NUM");
//		System.out.println(this.playerNumber);
		for(int i = 0; i < this.height; i++){
			max1_con = 0;
			max2_con = 0;
			for(int j = 0; j < this.width; j++){
				if(b.board[i][j] == this.playerNumber){
					//remember left column num of continuous piece
					if(firstFlag1){
						leftCheckCol1 = j-1;
						firstFlag1 = false;
					}
					max1_con ++;
					max2_con = 0;
					//there is a continuous piece, check left and right
					if(max1_con == n){
//						System.out.println("I have been here n:"+n);
						if(n == this.numToWin){
							num ++;
							num *= 10;
						}
						//check if one side is free
						else if(leftCheckCol1 >= 0 && b.board[i][leftCheckCol1] == 9){
//							System.out.println("into left");
//							System.out.println("i"+i+" leftCheckCol"+leftCheckCol1);
							if(((j+1) < this.width && b.board[i][j+1] != this.playerNumber)|| (j+1) >= this.width){
								if(!isUnderEmpty(b, i,leftCheckCol1)){
//									System.out.println("left ++");
									num ++;
//									System.out.println("num:"+num);
								}								
							}
						}
						//check if one side is free
						else if((j+1) < this.width && b.board[i][j+1] == 9){
//							System.out.println("in right i:"+i+" j+1,"+(j+1));
//							System.out.println("leftCheckCol1:"+leftCheckCol1);
							if((leftCheckCol1 >= 0 && b.board[i][leftCheckCol1] != this.playerNumber)||(leftCheckCol1 < 0)){
								if(!isUnderEmpty(b, i,j+1)){
//									System.out.println("right ++");
									num ++;
//									System.out.println("num:"+num);
								}
							}					
						}
						else if(((leftCheckCol1 >= 0 && b.board[i][leftCheckCol1] == this.oppNum)|| leftCheckCol1 < 0) && (((j+1) < this.width && b.board[i][j+1] != this.playerNumber)|| (j+1) >= this.width)){
							continue;
						}
						else{
							num ++;
						}
					}
				}
				else if(b.board[i][j] == this.oppNum){
					//remember left column num of continuous piece
					if(firstFlag2){
						leftCheckCol2 = j-1;
						firstFlag2 = false;
					}
					max2_con ++;
					max1_con = 0;
					//there is a continuous piece, check left and right
					if(max2_con == n){
						if(n == this.numToWin){
							num ++;
							num *= 10;
						}
						//check if one side is free
						else if(leftCheckCol2 >= 0 && b.board[i][leftCheckCol2] == 9){
//							System.out.println("in left i:"+i+" j+1,"+(j+1));
							if(((j+1) < this.width && b.board[i][j+1] != this.oppNum)|| (j+1) >= this.width){
								if(!isUnderEmpty(b, i,leftCheckCol2)){
//									System.out.println("left ++");
									num --;
//									System.out.println("num:"+num);
								}								
							}
						}
						//check if one side is free
						else if((j+1) < this.width && b.board[i][j+1] == 9){
//							System.out.println("in right i:"+i+" j+1,"+(j+1));
							if((leftCheckCol2 >= 0 && b.board[i][leftCheckCol2] != this.oppNum)||(leftCheckCol2 < 0)){
								if(!isUnderEmpty(b, i,j+1)){
//									System.out.println("right ++");
									num --;
//									System.out.println("num:"+num);
								}
							}					
						}
						else if(((leftCheckCol2 >= 0 && b.board[i][leftCheckCol2] == this.playerNumber)|| leftCheckCol2 < 0) && (((j+1) < this.width && b.board[i][j+1] != this.oppNum)|| (j+1) >= this.width)){
							continue;
						}
						else{
							num --;
						}
					}
				}
				else{
					max1_con = 0;
					max2_con = 0;
					firstFlag1 = true;
					firstFlag2 = true;
				}			
			}
		}
//		System.out.println("horiC num is " + num);
		return num;
	}
	
	//return not empty numbers in a discreet piece
	private int horiDiscreet(Board b){
		//check discreet piece
		int num = 0;
		int emptyNum = 0;
		int limit = 0;
		for(int i = 0; i < this.height; i++){
			for(int j = 0; j < this.width; j++){
				//check the player side
				if(b.board[i][j] == this.playerNumber){
					limit = j + numToWin - 1;
					if(limit < this.width){
//						System.out.println("omg");
						for(int m = 0; m < numToWin; m++){
							//discreet in middle, count available empty number
//							System.out.println("discreet");
							if((b.board[i][j+m] == this.oppNum)){
								emptyNum = 0;
								break;
							}
							if((b.board[i][j+m] == 9) && !isUnderEmpty(b,i,j+m)){
//								System.out.println("i:"+i+"j+m:"+(j+m)+"j:"+j);
								emptyNum++;
//								System.out.println("emptyNum:"+emptyNum);
							}
						}
						if(emptyNum == 1 && b.board[i][limit] != 9){
//							System.out.println(b.board[i][limit]);
//							System.out.println("i:"+i+"j+m:"+(j+m)+"j:"+j+"m:"+limit);
//							System.out.println("number befor:"+num);
							num++;
//							System.out.println("number here is:"+num);
						}
						emptyNum = 0;
					}
					limit = 0;
				}
				//check if one the opponent side
				else if(b.board[i][j] == this.oppNum){
					limit = j + numToWin - 1;
					if(limit < this.width){
						for(int m = 0; m < numToWin; m++){
							//discreet in middle, count available empty number
							if((b.board[i][j+m] == this.playerNumber)){
								emptyNum = 0;
								break;
							}
							if((b.board[i][j+m] == 9) && !isUnderEmpty(b,i,j+m)){
								//System.out.println("i:"+i+"j+m:"+(j+m)+"j:"+j);
								emptyNum++;
//								System.out.println("empry:"+emptyNum);
							}
						}
						if(emptyNum == 1 && b.board[i][limit] != 9){
//							System.out.println(b.board[i][limit]);
//							System.out.println("i:"+i+"j+m:"+(j+m)+"j:"+j+"m:"+limit);
//							System.out.println("number befor:"+num);
							num--;
//							System.out.println("number here is:"+num);
						}
						emptyNum = 0;
					}
					limit = 0;
				}
			}
		}
//		System.out.println("discreet number:"+num);
		return num;
	}
	
	//check diagonally
	private int diagonally1(Board b, int n){
		//check diagonally y=-x+k
		int num = 0;
		int max1 = 0;
		int max2 = 0;
		int rightCheckCol1 = 0;
		int rightCheckRow1 = 0;
		boolean firstFlag1 = true;
		int rightCheckCol2 = 0;
		int rightCheckRow2 = 0;
		boolean firstFlag2 = true;
		int upper_bound = height-1+width-1-(n-1);

		for(int k = n-1;k <= upper_bound;k++){			
			max1 = 0;
			max2 = 0;
			int x,y;
			if(k < width) 
				x = k;
			else
				x = width-1;
			y = -x+k;
			while(x >= 0  && y < height){
				if(b.board[height-1-y][x] == this.playerNumber){
//					System.out.println("at beginning x:"+x+" y:"+(height-y-1));
					//remember left column num of continuous piece
					if(firstFlag1){
						rightCheckCol1 = x+1;
						rightCheckRow1 = height-y;
						firstFlag1 = false;
					}
					max1 ++;
					max2 = 0;
					//there is a continuous piece, check upperleft and downright
					if(max1 == n){
//						System.out.println("i am here with n:"+n);
//						System.out.println("x:"+x+" y:"+(height-y-1));
						if(rightCheckCol1 < this.width && rightCheckRow1 < this.height && b.board[rightCheckRow1][rightCheckCol1] == 9){
							if(!isUnderEmpty(b,rightCheckRow1,rightCheckCol1)){
//								System.out.println("hahhahaha");
								num ++;
							}
						}
						else if((x-1) >= 0 && (height-2-y) >= 0 && b.board[height-2-y][x-1] == 9){
//							System.out.println("I finally get here");
							if(!isUnderEmpty(b,height-2-y,x-1)){
//								System.out.println("yayayyayaya");
								num ++;
							}
			
						}
					}				
				}
				else if(b.board[height-1-y][x] == this.oppNum){
					if(firstFlag2){
						rightCheckCol2 = x+1;
						rightCheckRow2 = height-y;
						firstFlag2 = false;
					}
					max1 = 0;
					max2 ++;
					//there is a continuous piece, check upperleft and downright
					if(max2 == n){
						if(rightCheckCol2 < this.width && rightCheckRow2 < this.height && b.board[rightCheckRow2][rightCheckCol2] == 9){
							if(!isUnderEmpty(b,rightCheckRow2,rightCheckCol2)){
//								System.out.println("let's see height:"+(height-1-y)+" col:"+x);
								num --;
							}
						}
						if((x-1) >= 0 && (height-2-y) >= 0 && b.board[height-2-y][x-1] == 9){
							if(!isUnderEmpty(b,height-2-y,x-1)){
//								System.out.println("here it is height :"+(height-1-y)+" col:"+x);
								num --;
							}
						}
					}

				}
				else{
					max1 = 0;
					max2 = 0;
					firstFlag1 = true;
					firstFlag2 = true;
				}
//				System.out.println("before x:"+x+" y:"+(height-y-1));
				x--;
				y++;
//				System.out.println("after x:"+x+" y:"+(height-y-1));
			}	 
		}
//		System.out.println("num from diagonally1:"+num);
		return num;
	}
	
	public int diagonally2(Board b,int n){
		//check diagonally y=x-k
		int num = 0;
		int max1 = 0;
		int max2 = 0;
		int rightCheckCol1 = 0;
		int rightCheckRow1 = 0;
		boolean firstFlag1 = true;
		int rightCheckCol2 = 0;
		int rightCheckRow2 = 0;
		boolean firstFlag2 = true;
		int upper_bound=width-1-(n-1);
		int  lower_bound=-(height-1-(n-1));
		// System.out.println("lower: "+lower_bound+", upper_bound: "+upper_bound);
		for(int k=lower_bound;k<=upper_bound;k++){			
			max1=0;
			max2=0;
			int x,y;
			if(k>=0) 
				x=k;
			else
				x=0;
			y=x-k;
			while(x>=0 && x<width && y<height){
				// System.out.println("k: "+k+", x: "+x+", y: "+y);
				if(b.board[height-1-y][x] == this.playerNumber){
					//remember left column num of continuous piece
					if(firstFlag1){
						rightCheckCol1 = x-1;
						rightCheckRow1 = height-y;
						firstFlag1 = false;
					}
					max1 ++;
					max2 = 0;
					//there is a continuous piece, check upperright and downleft
					if(max1 == n){
//						System.out.println("i am here 1");
//						System.out.println("rightCheckCol1:"+rightCheckCol1+"rightCheckRow1:"+rightCheckRow1);
						if(rightCheckCol1 >= 0 && rightCheckCol1 < this.width &&rightCheckRow1 <this.height && rightCheckRow2 >= 0 && b.board[rightCheckRow1][rightCheckCol1] == 9){
//							System.out.println("i am here 2");
							if(!isUnderEmpty(b,rightCheckRow1,rightCheckCol1)){
//								System.out.println("i am here 3");
								num ++;
							}
						}
//						System.out.println("row:"+(height-y-2)+"x+1:"+(x+1)+" y :"+y);
						if((x+1) < this.width && (x+1) >= 0 && (height-y-2) >= 0 && (height-y-2) <this.height && b.board[height-y-2][x+1] == 9 ){
//							System.out.println("i am here 4");
							if(!isUnderEmpty(b,height-y-2,x+1)){
//								System.out.println("i am here 5");
								num ++;
							}
						}
					}				
				}
				else if(b.board[height-1-y][x] == this.oppNum){
					if(firstFlag2){
						rightCheckCol2 = x+1;
						rightCheckRow2 = height-2-y;
						firstFlag2 = false;
					}
					max1 = 0;
					max2 ++;
					//there is a continuous piece, check upperright and downleft
					if(max2 == n){
//						System.out.println("rightCheckRow2:"+rightCheckRow2+"rightCheckCol2:"+rightCheckCol2+" y:"+y);
						if(rightCheckCol2 >= 0 && rightCheckCol2 < this.width && rightCheckRow2 <this.height && rightCheckRow2 >= 0 && b.board[rightCheckRow2][rightCheckCol2] == 9){
							
							if(!isUnderEmpty(b,rightCheckRow2,rightCheckCol2)){
								num --;
							}
						}
//						System.out.println("row:"+(height-y-2)+"x+1:"+(x+1));
						if((x+1) < this.width && (x+1) >= 0 && (height-y-2) >= 0 && (height-y-2) <this.height && b.board[height-y-2][x+1] == 9 ){
							if(!isUnderEmpty(b,height-y-2,x+1)){
								num --;
							}
						}
					}
				}
				else{
					max1 = 0;
					max2 = 0;
					firstFlag1 = true;
					firstFlag2 = true;
				}
				x++;
				y++;
			}	 

		}	 //end for y=x-k
//		System.out.println("num from diagonally2:"+num);
		return num;
	}

	//check if it is empty under the block, if empty return true
	private boolean isUnderEmpty(Board b, int row, int col){
//		System.out.println("under empty");
		if(row == this.height -1){
			return false;
		}
		if(row < this.height -1){
			row ++;
		}
//		System.out.println("row:"+row);
//		System.out.println("col"+col);
		if(b.board[row][col] == 9){
			return true;
		}
		return false;
	}
}
