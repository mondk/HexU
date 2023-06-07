import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.ArrayList;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class GameState{
	
	//Size of game screen

	Online online = new OnlineImplementation();
	WaitingRoom waitingRoom = null;
	OnlineMove onlineMove = null;
	Integer onlineId = 0;

	//game grid
	ArrayList<Hexagon> grid = new ArrayList<>();
	ArrayList<BorderR> border = new ArrayList<>();
	Point hexCenter1;
	Point hexCenter2;
	Point hexCenter3;
	Point hexCenter4;
	
	//Hexagon constants
	int numberOfHexagons =3;

	// Variables for hexagon placement
	double radius=(0.5773502717*(600-150))/(numberOfHexagons+1);
	double shift = 2*radius*0.8660254;
	int xOffSet= 100- (int) (radius*2);
	
	//Size of game screen depending on number of hexagones and radius
	int widthScreen = (numberOfHexagons*(int)Math.round(radius))+(int)Math.round(shift)+400;
	int heightScreen = (numberOfHexagons*(int)Math.round(radius))+(int)Math.round(shift)+200;

	Dimension SCREEN_SIZE = new Dimension(Toolkit.getDefaultToolkit().getScreenSize());
	

	HashMap<Integer,Player> players = new HashMap<>();

	//Start point for grid
	Point startPoint = new Point((int) radius+50,(int) radius+50);

	// JPanel, which includes the different screens
	JPanel cards = new JPanel(new CardLayout());

	//Playerstate
	boolean singlePlayer = false;
	boolean host = true;
	State playerState = State.MULTIPLAYER;

	// list for moves when loading saved game
	String[] load = {};

	//Show which player turn it is
	Turn whosTurn = Turn.Player1;
	String paneTurnString;
	Color paneTColor;

	public GameState(){
		players.put(0, new Player("Player 1", Color.pink));
		players.put(1, new Player("Player 2", Color.green));
		paneTurnString = players.get(0).name;
		paneTColor = players.get(0).color;
		
	}

	public enum State{
		SINGLEPLAYER,
		MULTIPLAYER,
		ONLINE
	}

	public enum Turn{
		Player1,
		Player2,
		AI,
		ONLINE_PLAYER
	}

	//Lists containing start arrays for players
	List<Integer> startP1 = new ArrayList<>();
	List<Integer> startP2 = new ArrayList<>();
	List<Integer> winP1 = new ArrayList<>();
	List<Integer> winP2 = new ArrayList<>();

	// Linked list containing moves made
	LinkedList<Integer> q = new LinkedList<>();

	public void changeState(String state) {
		
		if(state.equals("single"))
			playerState=State.SINGLEPLAYER;
		else if(state.equals("multiplayer")) {
			playerState=State.MULTIPLAYER;
		}
		else if (state.equals("online")) {
			playerState=State.ONLINE;
		}
	}
	//change player turn
	public void nextTurn() {
		switch(playerState) {
			case SINGLEPLAYER:
				if(whosTurn.equals(Turn.Player1)) {
					whosTurn = Turn.AI;
					paneTurnString = "AI";
					paneTColor = players.get(1).color;
				}
				else {
					whosTurn = Turn.Player1;
					paneTurnString = players.get(0).name;
					paneTColor = players.get(0).color;
				}
				break;
			//Multiplayer
			case MULTIPLAYER:
				if(whosTurn.equals(Turn.Player1)) {
					whosTurn = Turn.Player2;
					paneTurnString = players.get(1).name;
					paneTColor = players.get(1).color;
				}
				else {
					whosTurn = Turn.Player1;
					paneTurnString = players.get(0).name;
					paneTColor = players.get(0).color;
				}
				break;
			case ONLINE:
				if(whosTurn.equals(Turn.Player1)){
					whosTurn = Turn.ONLINE_PLAYER;
					paneTurnString = players.get(1).name;
					paneTColor = players.get(1).color;
				} else if(whosTurn.equals(Turn.Player2)){
					whosTurn = Turn.ONLINE_PLAYER;
					paneTurnString = players.get(0).name;
					paneTColor = players.get(0).color;
				} else {
					whosTurn = host ? Turn.Player1 : Turn.Player2;
					paneTurnString = host ? players.get(0).name : players.get(1).name;
					paneTColor = host ? players.get(0).color : players.get(1).color;
				}
				break;
		}
	}

	public void fillWinStateArrays() {
		for (Hexagon hexes: grid.subList(0, numberOfHexagons)){
			startP1.add(hexes.id);
		}
		for (Hexagon hexes: grid.subList(numberOfHexagons*(numberOfHexagons-1), numberOfHexagons*numberOfHexagons)){
			winP1.add(hexes.id);
		}
		for(int i=0;i<numberOfHexagons;i++) {
			int s = i*numberOfHexagons;
			int a = i*numberOfHexagons+numberOfHexagons-1;
			startP2.add(grid.get(s).id);
			winP2.add(grid.get(a).id);
		}
	}

	public ArrayList<ArrayList<Integer>> winingState(List<Integer> s, Color p, List<Integer> win) {
		ArrayList<ArrayList<Integer>> result = new ArrayList<>(2);
		result.add(new ArrayList<>());
		ArrayList<ArrayList<Integer>> pCluster = new ArrayList<>();


		// Array der holder alle hexagon der er en del af en cluster
		ArrayList<Integer> seen = new ArrayList<>();
		//Loop over hele griddet, for at finde alle clusters
		for(Hexagon v : this.grid) {
			// Tjekker om den givne hexagon er den givne spillers farve
			// eller om den allerede er en del af en cluster, i så fald skip!
			if (v.color != p || seen.contains(v.id)) {
				continue;
			}

			ArrayList<Integer> cluster = new ArrayList<>();

			// Standard BFS ting...
			boolean visited[] = new boolean[numberOfHexagons*numberOfHexagons];
			LinkedList<Integer> queue = new LinkedList<Integer>();
			visited[v.id] = true;
			queue.add(v.id);
			// Tilføjer hexagon til cluster
			cluster.add(v.id);

			while (queue.size()!=0) {
				int inter = queue.poll();
				Iterator<Integer> i = this.grid.get(inter).adj.listIterator();
				while(i.hasNext()) {
					int n = i.next();
					if(visited[n] == false && this.grid.get(n).color == p) {
						visited[n] = true;
						queue.add(n);
						seen.add(n);
						cluster.add(n);
						// Ser om den funde cluster ud fra v, indeholde en hexagon fra start og slut enden
						// af den givne spillers plade
						if (!Collections.disjoint(cluster, s) && !Collections.disjoint(cluster,win)){
							result.get(0).add(1);
							return result;
						}
					}
				}
			}
			pCluster.add(cluster);
		}	
		result.get(0).add(0);
		result.addAll(pCluster);
		return result;
	}

	public void resetGame(int id) {
		whosTurn = Turn.Player1;
		paneTColor = players.get(0).color;
		paneTurnString = players.get(0).name;
		if (id==0){
			startP1.clear();
			winP1.clear();
			startP2.clear();
			winP2.clear();
		}
		q.clear();
		for (Hexagon h : grid) {
			h.color = Color.gray;
			h.clicked=false;
		}
	}

	public ArrayList<Integer> getValidMoves() {
		ArrayList<Integer> validmoves = new ArrayList<>();
		
		for(Hexagon h: grid) {
			if(!h.clicked)
				validmoves.add(h.id);
		}
		return validmoves;
	}

	public void updateNumberOfHexagons(int numberOfHexagons){
		this.numberOfHexagons = numberOfHexagons;
		this.radius=(0.5773502717*(600-150))/(numberOfHexagons+1);
		this.shift = 2*radius*0.8660254;
	}

	public void addPlayer(){
		int id = players.size();
		players.put(id, new Player("Player " + id, Color.orange));
	}

	public void removePlayer(){
		players.remove(players.size());
	}


	public void startGame(int numberOfHexagons, boolean singlePlayer){
		this.paneTColor = players.get(0).color;
		this.paneTurnString = players.get(0).name;
		this.singlePlayer = singlePlayer;
		updateNumberOfHexagons(numberOfHexagons);
		this.whosTurn=Turn.Player1;
		this.playerState = singlePlayer ? State.SINGLEPLAYER : State.MULTIPLAYER;
		border = new ArrayList<>();
		grid = new ArrayList<>();
		Panel panel = new Panel(this);
		
		cards.add(panel, "PANEL");
		CardLayout cl = (CardLayout)cards.getLayout();
		cl.next(cards);
		cards.remove(0);
	}

	public void returnToMenu(){
		Menu menu  = new Menu(this);
		cards.add(menu, "MENU2");
		CardLayout cl = (CardLayout)cards.getLayout();
		cl.next(cards);
		cards.remove(0);
	}

	public void fillLoadMoves(String[] moves){
		try{
			switch(playerState){
				case SINGLEPLAYER:
					for (int i = 0; i<moves.length; i++){
						grid.get(Integer.parseInt(moves[i])).color = players.get(i%2).color;
						grid.get(Integer.parseInt(moves[i])).clicked = true;
						q.add(Integer.parseInt(moves[i]));
					}
					break;
				case MULTIPLAYER:
					for (String move : moves) {
						switch(whosTurn){
							case Player1:
								grid.get(Integer.parseInt(move)).color = players.get(0).color;
								grid.get(Integer.parseInt(move)).clicked = true;
								nextTurn();
								break;
							case Player2:
								grid.get(Integer.parseInt(move)).color = players.get(1).color;
								grid.get(Integer.parseInt(move)).clicked = true;
								nextTurn();
								break;
							}
							q.add(Integer.parseInt(move));
					}
					break;
				case ONLINE:
					break;
			}
		} catch (Exception null_error){
			System.out.println("No moves loaded");
		}

	}

	public String randomBackground(){
		String[] files = new File("res/background").list();
		int x = 1+(int)(Math.random()*(files.length-1));
		return "res/background/" + files[x];
	}

	public void hostGame(String ip) {
		online.start(true, ip);
		playerState = GameState.State.ONLINE;
		host = true;
		WaitingRoomUI waitingRoomUI = new WaitingRoomUI(this);
	}

	public void setOnline(Online online) {
		this.online = online;
	}

	public void joinGame(String ip) throws IOException {
		online.start(false, ip);
		//gameSpace = new RemoteSpace("tcp://" + ip + ":9001/game?keep");
		this.host = false;
		this.playerState = GameState.State.ONLINE;
		WaitingRoomUI waitingRoomUI = new WaitingRoomUI(this);
	}


	public void startWaitingRoom() throws InterruptedException {
		waitingRoom = new WaitingRoom(this);
		Thread playersThread = new Thread(waitingRoom);
        playersThread.start();
	}
	public void startOnlineMove() throws InterruptedException {
		onlineMove = new OnlineMove(this);
		Thread moveThread = new Thread(onlineMove);
		moveThread.start();
	}
	public void startOnlineGame(int startPlayer) throws InterruptedException {
		players = online.getPlayers();
		CardLayout cl = (CardLayout)cards.getLayout();
		paneTurnString = players.get(startPlayer).name;
		paneTColor = players.get(startPlayer).color;
		updateNumberOfHexagons(numberOfHexagons);
		whosTurn = onlineId == startPlayer ? GameState.Turn.values()[startPlayer] : GameState.Turn.ONLINE_PLAYER;
		startOnlineMove();
		Panel panel = new Panel(this);
		onlineMove.subscribe(panel);
		cards.add(panel);
		cl.next(cards);
		cards.remove(0);
	}

	public String returnPS(){
		String state = "";
		switch(playerState){
			case SINGLEPLAYER:
				state = "single";
				break;
			case MULTIPLAYER:
				state = "multiplayer";
				break;
			case ONLINE:
				state = "online";
				break;
		}
		return state;
	}

	public void loadFile(){
		try {
			File Obj = new File("res/saves.txt");
			Scanner Reader = new Scanner(Obj);
			while (Reader.hasNextLine()) {
				String[] data = Reader.nextLine().split(": ");
				//System.out.println(data[0].toString()+ " " + data[1].toString());
				if (data[0].equals("mode")){
					if (data[1].equals("false"))
						singlePlayer = false;
					else 
						singlePlayer = true;
					changeState(data[2]); 
				}
				else if (data[0].equals("hexes")){
					updateNumberOfHexagons(Integer.parseInt(data[1]));
				}
				else if (data[0].equals("P0")){
					players.get(0).name = data[1];
				}
				else if (data[0].equals("CP0")){
					players.get(0).color = new Color(Integer.parseInt(data[1]));
				}
				else if (data[0].equals("P1")){
					players.get(1).name = data[1];
				}
				else if (data[0].equals("CP1")){
					players.get(1).color = new Color(Integer.parseInt(data[1]));
				}
				else if (data[0].equals("moves")){
					load = data[1].substring(1, data[1].length()-1).split(", ");
				}
			}
			Reader.close();	
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void createGrid() {
		if (!grid.isEmpty())
			return;
		int id=0;
		int scoreI =0;

		for(int i =0;i<numberOfHexagons;i++) {
			int scoreJ =0;
			if(i==numberOfHexagons/2) {

			}
			else if(i>numberOfHexagons/2) {
				scoreI--;
			}
			else {
				scoreI++;
			}
			for(int j =0;j<numberOfHexagons;j++) {
				Hexagon h1 = new Hexagon(new Point((int) (startPoint.x+shift*j+i*shift*Math.cos(60*(Math.PI/180))),(int) (startPoint.y+i*shift*Math.sin(60*(Math.PI/180)))),radius,id);
				if(j==numberOfHexagons/2) {

				}
				else if(j>numberOfHexagons/2) {
					scoreJ--;
				}
				else {
					scoreJ++;
				}
				h1.score=scoreI+scoreJ;

				grid.add( h1);
				id++;
				int hex = i*numberOfHexagons+j;

				if (i==0 && j==0) {														//First hexagon
					grid.get(hex).adj.add(1);
					grid.get(hex).adj.add(numberOfHexagons);
					hexCenter1 = grid.get(hex).getCenter();
				}
				else if (i==numberOfHexagons-1 && j ==numberOfHexagons-1) {		//Last Hexagon
					grid.get(hex).adj.add(hex-1);
					grid.get(hex).adj.add(hex-numberOfHexagons);
					hexCenter3 = grid.get(hex).getCenter();
				}
				else if(i==0 & j==numberOfHexagons-1) {								//Last hexagon first row
					grid.get(hex).adj.add(hex-1);
					grid.get(hex).adj.add(hex+numberOfHexagons);
					grid.get(hex).adj.add(hex+numberOfHexagons-1);
					hexCenter2 = grid.get(hex).getCenter();
				}
				else if (i == numberOfHexagons-1 && j ==0) {							//First hexagon last row
					grid.get(hex).adj.add(hex-numberOfHexagons);
					grid.get(hex).adj.add(hex-numberOfHexagons+1);
					grid.get(hex).adj.add(hex+1);
					hexCenter4 = grid.get(hex).getCenter();
				}
				else if (i==0) {														//Rest of first row
					grid.get(hex).adj.add(hex+numberOfHexagons);;
					grid.get(hex).adj.add(hex+numberOfHexagons-1);
					grid.get(hex).adj.add(hex+1);
					grid.get(hex).adj.add(hex-1);
				}
				else if (i==numberOfHexagons-1) {									//Rest of last row
					grid.get(hex).adj.add(hex-1);
					grid.get(hex).adj.add(hex-numberOfHexagons);
					grid.get(hex).adj.add(hex-numberOfHexagons+1);
					grid.get(hex).adj.add(hex+1);
				}
				else if (j==0) {														//Rest of first column
					grid.get(hex).adj.add(hex-numberOfHexagons);
					grid.get(hex).adj.add(hex-numberOfHexagons+1);
					grid.get(hex).adj.add(hex+1);
					grid.get(hex).adj.add(hex+numberOfHexagons);
				}
				else if(j==numberOfHexagons-1) {										//Rest of last column
					grid.get(hex).adj.add(hex-numberOfHexagons);
					grid.get(hex).adj.add(hex-1);
					grid.get(hex).adj.add(hex+numberOfHexagons-1);
					grid.get(hex).adj.add(hex+numberOfHexagons);
				}
				else {																	//Everything in between
					grid.get(hex).adj.add(hex-1);
					grid.get(hex).adj.add(hex+1);
					grid.get(hex).adj.add(hex-numberOfHexagons);
					grid.get(hex).adj.add(hex-numberOfHexagons+1);
					grid.get(hex).adj.add(hex+numberOfHexagons);
					grid.get(hex).adj.add(hex+numberOfHexagons-1);
				}
			}
		}

		//Colors for borders, indicating players sides
		int xp1 = (int) Math.round(hexCenter1.getX());
		int xp2 = (int) Math.round(hexCenter2.getX());
		int xp3 = (int) Math.round(hexCenter3.getX());
		int xp4 = (int) Math.round(hexCenter4.getX());
		int yp1 = (int) Math.round(hexCenter1.getY());
		int yp2 = (int) Math.round(hexCenter2.getY());
		int yp3 = (int) Math.round(hexCenter3.getY());
		int yp4 = (int) Math.round(hexCenter4.getY());
		//int rInt = (int) Math.round(gs.radius*1.5);
		int[] x1= {xp1-(int) Math.round(radius)+3,xp2-(int) Math.round(radius*0.75)-3,xp2-(int) Math.round(radius*0.75)-3,xp1-(int) Math.round(radius)+3};
		int[] x2= {xp3-(int) Math.round(radius*0.75)-3,xp4-(int) Math.round(radius)+3,xp4-(int) Math.round(radius)+3,xp3-(int) Math.round(radius*0.75)-3};
		int[] x3= {xp1-(int) Math.round(radius*1.75),xp1-(int) Math.round(radius),xp4-(int) Math.round(radius),xp4-(int) Math.round(radius*1.75)};
		int[] x4= {xp2-(int) Math.round(radius),xp2,xp3,xp3-(int) Math.round(radius)};
		int[] y1= {yp1-(int) Math.round(radius*1.5)+2,yp2-(int) Math.round(radius*1.5)+2,yp2,yp1};
		int[] y2= {yp3,yp4,yp4+(int) Math.round(radius*0.5),yp3+(int) Math.round(radius*0.5)};
		int[] y3= {yp1,yp1,yp4,yp4};
		int[] y4= {yp2-(int) Math.round(radius),yp2-(int) Math.round(radius),yp3-(int) Math.round(radius),yp3-(int) Math.round(radius)};

		border.add(new BorderR(players.get(0).color, x1 , y1)); //top
		border.add(new BorderR(players.get(0).color, x2 , y2)); //buttom
		border.add(new BorderR(players.get(1).color, x3 , y3)); //left
		border.add(new BorderR(players.get(1).color, x4 , y4)); //right

		fillLoadMoves(load);
		fillWinStateArrays();
	}

	public Color calcTint(Color c){
		int red = c.getRed();
		int blue = c.getBlue();
		int green = c.getGreen();
		double coef = 0.45;
		return new Color((int)(red+(255-red)*coef), (int)(green+(255-green)*coef), (int)(blue+(255-blue)*coef));
	}
}

