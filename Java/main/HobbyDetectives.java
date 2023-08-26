package main;

//import GUI.Game;

import java.util.*;

public class HobbyDetectives {
	private static Guess solution;
	private int playerCount;
	private PlayerName currentTurn;
	public static GameState state = GameState.MENU;
	private Board board;
	private static ArrayList<Player> allPlayers = new ArrayList<>();
	private static ArrayList<Character> allCharacters = new ArrayList<>();
	private static ArrayList<Card> allCards = new ArrayList<>();
	private static ArrayList<Estate> allEstates = new ArrayList<>();
	private static ArrayList<Weapon> allWeapons = new ArrayList<>();
	private ArrayList<Card> tempDeck = new ArrayList<>();

	public static final Map<String, CharacterCard> characterMap = new HashMap<>();
	public static final Map<String, WeaponCard> weaponMap = new HashMap<>();
	public static final Map<String, EstateCard> estateMap = new HashMap<>();

	// COLOURS
	public static final String RESET = "\033[0m"; // Text Reset
	public static final String RED_BOLD = "\033[1;31m"; // RED
	public static final String GREEN_BOLD = "\033[1;32m"; // GREEN
	public static final String YELLOW_BOLD = "\033[1;33m"; // YELLOW
	public static final String BLUE_BOLD = "\033[1;34m"; // BLUE
	public static final String CYAN_BOLD = "\033[1;36m"; // CYAN

	public HobbyDetectives() {
		// initialize fields and perform necessary setup
		playerCount = 4;
	}

	/**
	 * Enum representing player names.
	 */
	public enum PlayerName {
		Lucilla, Bert, Malina, Percy;

		/**
		 * A map to convert lowercase player names to the corresponding enum value.
		 */
		public static Map<String, PlayerName> playerNameMap = Map.of("lucilla", Lucilla, "bert", Bert, "malina", Malina,
				"percy", Percy);
	}

	/**
	 * Enum representing weapon names.
	 */
	public enum WeaponName {
		Broom, Scissors, Knife, Shovel, iPad;

		/**
		 * A map to convert lowercase weapon names to the corresponding enum value.
		 */
		public static Map<String, WeaponName> weaponNameMap = Map.of("broom", Broom, "scissors", Scissors, "knife",
				Knife, "shovel", Shovel, "ipad", iPad);
	}

	/**
	 * Enum representing estate names.
	 */
	public enum EstateName {
		HauntedHouse, ManicManor, VisitationVilla, PerilPalace, CalamityCastle;

		/**
		 * A map to convert lowercase estate names to the corresponding enum value.
		 */
		public static Map<String, EstateName> estateNameMap = Map.of("haunted house", HauntedHouse, "manic manor",
				ManicManor, "visitation villa", VisitationVilla, "peril palace", PerilPalace, "calamity castle",
				CalamityCastle);

		/**
		 * Overrides the default toString method to provide a formatted version of the
		 * enum values.
		 *
		 * @return The formatted string representation of the enum value.
		 */
		@Override
		public String toString() {
			return switch (this) {
			case HauntedHouse -> "Haunted House";
			case ManicManor -> "Manic Manor";
			case VisitationVilla -> "Visitation Villa";
			case PerilPalace -> "Peril Palace";
			case CalamityCastle -> "Calamity Castle";
			};
		}
	}

	/**
	 * Enum representing the game state.
	 */
	public enum GameState {
		ONGOING, WON, LOST, MENU, QUIT;

		public static GameState state = MENU;
	}

	/**
	 * Main method initializes the game object setups up the game runs game loop
	 *
	 * @param args - Matt
	 */
	public static void main(String[] args) {
		//new Game();
		HobbyDetectives game = new HobbyDetectives();
		game.displayWelcomeMessage();
		game.displayGameRules();
		game.setup();
		game.loop();
		
	}

	/**
	 * Displays a welcome message to the players.
	 */
	public void displayWelcomeMessage() {
		System.out.println("Welcome to Hobby Detectives!");
		System.out.println("Get ready to solve the mystery...");
		System.out.println();
	}

	/**
	 * Display the game rules and instructions on how to play
	 */
	public void displayGameRules() {
		System.out.println("Game Rules:");
		System.out.println("1. Your goal is to solve the mystery of the crime!");
		System.out.println("2. You will take turns moving around the board, making guesses, and collecting clues.");
		System.out.println("3. Each player represents a character on the board.");
		System.out.println("4. The game includes characters, weapons, and estates.");
		System.out.println(
				"5. You'll need to deduce the correct combination of character, weapon, and estate to solve the mystery.");
		System.out.println("6. Be the first to make an accurate accusation to win!");
		System.out.println();
		System.out.println("How to Play:");
		System.out.println("- Roll the dice to move your character on the board.");
		System.out.println("- Enter the direction you want to move (U for Up, R for Right, D for Down, L for Left).");
		System.out.println("- Explore estates, make guesses about suspects and weapons.");
		System.out.println("- Use your detective skills to deduce the solution.");
		System.out.println("- Collect clues from other players by making guesses.");
		System.out.println("- When you're ready, make an accusation to solve the mystery.");
		System.out.println("- But be careful, a wrong accusation can lead to defeat!");
		System.out.println();
	}

	/**
	 * The setup for the game. Shuffles the cards. Distributes each card to each
	 * player Allows player selection Initializes all elements - Matt
	 */
	public void setup() {
		initializeEstates();
		initializeBoard();
		initializeDoors();
		initializeUnreachableSquares();
		initializeCards();
		initializeCharacters();
		initializePlayers();
		distributeCards();
		initializeWeapons();
		state = GameState.ONGOING;
	}

	/**
	 * Create estates Hardcoded values
	 */
	private void initializeEstates() {
		allEstates.add(new Estate("Haunted House", 2, 2, 5, 5));
		allEstates.add(new Estate("Manic Manor", 17, 2, 5, 5));
		allEstates.add(new Estate("Visitation Villa", 9, 10, 6, 4));
		allEstates.add(new Estate("Calamity Castle", 2, 17, 5, 5));
		allEstates.add(new Estate("Peril Palace", 17, 17, 5, 5));
	}

	/**
	 * Creates the unreachable squares on the map
	 *
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 */
	private void createUnreachableSquares(int x, int y, int width, int height) {
		x = x - 1;
		y = y - 1;
		for (int row = x; row < x + width; row++) {
			for (int col = y; col < y + height; col++) {
				Square square = new Square(row, col);
				board.addSquare(square, row, col);

				square.setBlocked(true);
			}
		}
	}

	/**
	 * The ranges the unreachable squares start and end between
	 */
	private void initializeUnreachableSquares() {
		createUnreachableSquares(12, 6, 2, 2);
		createUnreachableSquares(6, 12, 2, 2);
		createUnreachableSquares(18, 12, 2, 2);
		createUnreachableSquares(12, 18, 2, 2);
	}

	/**
	 * creates the squares and estates on the board
	 */
	public void initializeBoard() {
		for (int x = 0; x < board.getLength(); x++) {
			for (int y = 0; y < board.getLength(); y++) {
				Square square = new Square(x, y);
				board.addSquare(square, x, y);
				for (Estate e : allEstates) {
					if (e.squarePartOfEstate(square) != null) {
						square.setBlocked(true);
						square.setEstate(e);
					}
				}
			}
		}
	}

	/**
	 * Adds the doors to each estate Hardcoded for each estate
	 */
	private void initializeDoors() {
		// Haunted House doors
		allEstates.get(0).addDoor(6, 3, "Right");
		allEstates.get(0).addDoor(5, 6, "Bottom");

		allEstates.get(1).addDoor(17, 5, "Left");
		allEstates.get(1).addDoor(20, 6, "Bottom");

		// Visitation Villa doors
		allEstates.get(2).addDoor(12, 10, "Top");
		allEstates.get(2).addDoor(14, 11, "Right");
		allEstates.get(2).addDoor(11, 13, "Left");
		allEstates.get(2).addDoor(9, 12, "Bottom");

		// Calamity Castle doors
		allEstates.get(3).addDoor(3, 17, "Top");
		allEstates.get(3).addDoor(6, 18, "Right");

		// Peril Palace doors
		allEstates.get(4).addDoor(18, 17, "Top");
		allEstates.get(4).addDoor(17, 20, "Left");

	}

	/**
	 * Create each card Add cards to the solution
	 */
	private void initializeCards() {
		for (PlayerName p : PlayerName.values()) {
			CharacterCard c = new CharacterCard(p.toString());
			characterMap.put(p.toString(), c);
			allCards.add(c);
		}

		for (WeaponName w : WeaponName.values()) {
			WeaponCard c = new WeaponCard(w.toString());
			weaponMap.put(w.toString(), c);
			allCards.add(c);
		}

		for (EstateName e : EstateName.values()) {
			EstateCard c = new EstateCard(e.toString());
			estateMap.put(e.toString(), c);
			allCards.add(c);
		}
	}

	/**
	 * Create character and place them at starting positions
	 */
	private void initializeCharacters() {
		allCharacters.add(new Character(board.getSquare(11, 1), PlayerName.Lucilla, GREEN_BOLD));
		allCharacters.add(new Character(board.getSquare(1, 9), PlayerName.Bert, YELLOW_BOLD));
		allCharacters.add(new Character(board.getSquare(9, 22), PlayerName.Malina, BLUE_BOLD));
		allCharacters.add(new Character(board.getSquare(22, 14), PlayerName.Percy, RED_BOLD));
	}

	/**
	 * Create players Asks the user how many players to play.
	 */
	private void initializePlayers() {
		while (true) {
			System.out.println("How many players are playing? (Enter 3 or 4):");
			Scanner s = new Scanner(System.in);
			if (s.hasNextInt()) {
				int ans = s.nextInt();
				if (ans == (3)) {
					playerCount = 3;
					break;
				} else if (ans == 4) {
					playerCount = 4;
					break;
				}
			} else {
				System.out.println("Please enter 3 or 4 players");
			}
		}

		for (int i = 0; i < playerCount; i++) {
			allPlayers.add(new Player(allCharacters.get(i), board));
		}

	}

	/**
	 * Distributes cards evenly between players
	 */
	private void distributeCards() {
		Collections.shuffle(allCards);
		tempDeck = new ArrayList<>(allCards);
		solution = generateSolution();
		int playerIndex = 0;
		while (!tempDeck.isEmpty()) {
			Player currentPlayer = allPlayers.get(playerIndex);
			Card c = tempDeck.get((int) (Math.random() * tempDeck.size()));
			currentPlayer.addCard(c);
			tempDeck.remove(c);
			playerIndex = (playerIndex + 1) % playerCount;
		}

	}

	/**
	 * For debugging purposes, display the solution. Also display each character,
	 * and their decks.
	 */
	private void printCheatAnswers() {
		System.out.println("\n--------------- CHEAT ANSWERS ---------------");
		System.out.println("Solution: " + solution.getCharacter().getCardName() + " with the "
				+ solution.getWeapon().getCardName() + " in " + solution.getEstate().getCardName());
		for (Player p : getPlayers()) {
			System.out.print(p.getName() + ": ");
			System.out.println(p.getHand().size());
			for (Card card : p.getHand()) {
				System.out.println(card.getCardName());
			}
		}
		System.out.println("--------------- CHEAT ANSWERS ---------------\n");

	}

	/**
	 * Create weapons and randomly add to estates
	 *
	 * @return
	 */
	private void initializeWeapons() {
		allWeapons.add(new Weapon("Broom"));
		allWeapons.add(new Weapon("Scissors"));
		allWeapons.add(new Weapon("Knife"));
		allWeapons.add(new Weapon("Shovel"));
		allWeapons.add(new Weapon("iPad"));

		Collections.shuffle(allEstates);

		for (int i = 0; i < allEstates.size(); i++) {
			allEstates.get(i).addWeapon(allWeapons.get(i));
		}
	}

	/**
	 * Create a guess object that is the solution for the game
	 *
	 * @return
	 */
	private Guess generateSolution() {
		WeaponCard weapon;
		EstateCard estate;
		CharacterCard murderer;

		int i = (int) (Math.random() * tempDeck.size());
		while (!(tempDeck.get(i) instanceof WeaponCard)) {
			i = (int) (Math.random() * tempDeck.size());
		}

		weapon = (WeaponCard) tempDeck.get(i);
		tempDeck.remove(weapon);

		i = (int) (Math.random() * tempDeck.size());
		while (!(tempDeck.get(i) instanceof EstateCard)) {
			i = (int) (Math.random() * tempDeck.size());

		}

		estate = (EstateCard) tempDeck.get(i);
		tempDeck.remove(estate);

		i = (int) (Math.random() * tempDeck.size());
		while (!(tempDeck.get(i) instanceof CharacterCard)) {
			i = (int) (Math.random() * tempDeck.size());

		}

		murderer = (CharacterCard) tempDeck.get(i);
		tempDeck.remove(murderer);

		return new Guess(murderer, weapon, estate);
	}

	/**
	 * The game loop, runs the game logic, and allows each player to play during
	 * their turn
	 * <p>
	 * if state is won, print out the solution, and who it was solved by
	 */
	public void loop() {
		int currentPlayerIndex = (int) (Math.random() * 4 + 1);

		// for debugging
//        printCheatAnswers();

		while (state == GameState.ONGOING) { // condition for game loop to run
			Player currentPlayer = allPlayers.get(currentPlayerIndex);
			currentTurn = currentPlayer.getName();
			// Display the current game state
			board.drawToScreen();

			currentPlayer.getCharacter().startNewRound();
			currentPlayer.doTurn();

			if (currentPlayer.checkGamestate()) {
				state = GameState.WON;
			}
			if (allPlayers.stream().allMatch(Player::checkEliminated)) {
				state = GameState.LOST;
			}
			currentPlayerIndex = (currentPlayerIndex + 1) % playerCount;
		}

		// prints winning statement
		if (state == GameState.WON) {
			System.out.println("\n------------------------------------------------");
			System.out.println("\nThe murder mystery has been solved.");
			Guess solution = getSolution();
			System.out.println("Solution: " + solution.getCharacter().getCardName() + " with the "
					+ solution.getWeapon().getCardName() + " in " + solution.getEstate().getCardName());
			System.out.println("It was solved by: " + currentTurn);
			System.out.println("\n------------------------------------------------");
		}

		// prints losing statement
		if (state == GameState.LOST) {
			System.out.println("\n------------------------------------------------");
			System.out.println("\nThe murder mystery has failed.");
			Guess solution = getSolution();
			System.out.println("Solution: " + solution.getCharacter().getCardName() + " with the "
					+ solution.getWeapon().getCardName() + " in " + solution.getEstate().getCardName());
			System.out.println("No player correctly solved the murder");
			System.out.println("\n------------------------------------------------");
		}
	}

	/**
	 * Returns the murder solution
	 *
	 * @return
	 */

	public static Guess getSolution() {
		return solution;
	}

	/**
	 * Returns all the players in the game
	 *
	 * @return
	 */
	public static List<Player> getPlayers() {
		return allPlayers;
	}

	/**
	 * Returns a list of the players, in order from the current player.
	 *
	 * @param playerOrder
	 * @param currentPlayer
	 * @return
	 */
	public static List<Player> getOrderedPlayers(List<Player> playerOrder, Player currentPlayer) {
		List<Player> orderedPlayerList = new ArrayList<>();

		int currentPlayerIndex = playerOrder.indexOf(currentPlayer);
		for (int i = 1; i <= playerOrder.size(); i++) {
			orderedPlayerList.add(playerOrder.get((currentPlayerIndex + i) % playerOrder.size()));
		}
		return orderedPlayerList;
	}

	public static GameState getGamestate(){
		return state;
	}

	public void setGamestate(GameState state){
		this.state = state;
	}
	
	public ArrayList<Character> getAllCharacters(){
		return allCharacters;
	}

	public ArrayList<Estate> getAllEstates(){
		return allEstates;
	}
}
