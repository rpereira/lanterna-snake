import java.util.LinkedList;

import com.googlecode.lanterna.input.Key;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.terminal.Terminal;
import com.googlecode.lanterna.terminal.Terminal.Color;
import com.googlecode.lanterna.terminal.swing.SwingTerminal;

/**
 *
 * This class represents the game view, which depends on the GameState. It
 * holds menu interaction and some game play behavior.
 *
 * This class relies on a Lanterna's SwingTerminal and on a Screen object to
 * write to.
 */
public class GameView
{
	/*
	 * An empty string (or blank space) used to 'clear' other strings.
	 */
	private final static String EMPTY_STRING = " ";

	/*
	 * Wall representation.
	 */
	private final static String BORDER_STRING = "▒";

	/*
	 * Snake's representation.
	 */
	private final static String SNAKE_HEAD_STRING = "@";
	private final static String SNAKE_BODY_STRING = "O";

	/*
	 * Fruit's representation.
	 */
	private final static String FRUIT_STRING = "$";

	/*
	 * Obstacle representation.
	 */
	private final static String DYNAMITE_STRING = "#";

	/*
	 * Terminal size (does not represent the game play dimensions).
	 */
	private final static int LARGE_WIDTH  = 80;
	private final static int LARGE_HEIGHT = 23;

	/*
	 * The game speed. The higher, the slower.
	 */
	private final static int GAME_SPEED_1 = 90;
	private final static int GAME_SPEED_2 = 75;
	private final static int GAME_SPEED_3 = 60;
	private final static int GAME_SPEED_4 = 45;
	private final static int GAME_SPEED_5 = 35;

	/*
	 * The time rate at which a new object will be generated.
	 * The higher the slower.
	 */
	private final static int NEW_OBJECT_TIME_RATE = 6000;

	/*
	 * Minimum value for any coordinate (either x or y). This value is 1 because
	 * there is a border of width 1 around the board.
	 */
	private final static int X_COORDINATE_OFFSET = 1;
	private final static int Y_COORDINATE_OFFSET = 2;

	/*
	 * Main menu options' position.
	 */
	private final static int OPTION_SPEED_1 = 32;
	private final static int OPTION_SPEED_2 = 34;
	private final static int OPTION_SPEED_3 = 36;
	private final static int OPTION_SPEED_4 = 38;
	private final static int OPTION_SPEED_5 = 40;
	private final static int QUIT_GAME      = 17;

	/*
	 * Game over menu options' position.
	 */
	private final static int START_GAME   = 1;
	private final static int RESTART_GAME = 15;
	private final static int MAIN_MENU    = 16;

	/*
	 * Lanterna's terminal.
	 */
	private static SwingTerminal terminal;

	/*
	 * A layer to put on the top of the Terminal object, which is a kind of a
	 * screen buffer.
	 */
	private static Screen screen;

	/*
	 * A game state which contains the game model.
	 */
	private GameState state;

	/*
	 * Both hold the game play dimensions.
	 */
	private final int gameplay_height;
	private final int gameplay_width;

	/*
	 * The selected speed by the user.
	 */
	private int selected_speed;

	/**
	 * Instantiates a new GameView object.
	 */
	private GameView(int width, int height)
	{
		// Create a new terminal. See https://code.google.com/p/lanterna/wiki/UsingTerminal
		// for reference.
		terminal = new SwingTerminal(width, height);

		gameplay_width  = width  - X_COORDINATE_OFFSET;
		gameplay_height = height - Y_COORDINATE_OFFSET;

		screen = new Screen(terminal);
		screen.setCursorPosition(null);		// Hack to hide cursor.
		screen.startScreen();				// terminal enters in private mode,
											// clears the screen, and refreshes.
	}

	/**
	 * Creates the main menu and takes action on the selected option. It is
	 * possible to select the game speed, start the game and to exit.
	 */
	private void openMainMenu()
	{
		selected_speed = GAME_SPEED_1;

		renderMainMenu();

		// Make changes visible.
		refreshScreen();

		int selected_option = handleMainMenu();

		if(selected_option == START_GAME)
		{
			clearScreen();

			state = new GameState();
			startGame();
		}
		else
		{
			exitGame();
		}
	}

	/**
	 * Returns the selected option on main menu and creates a navigate animation.
	 *
	 * @return The selected option.
	 */
	private int handleMainMenu()
	{
		Key k;

		while(true)
		{
			k = readKeyInput();

			if(k != null)
			{
				switch(k.getCharacter())
				{
					case '1':
						selected_speed = GAME_SPEED_1;
						highlighMainMenuSelectedOption(OPTION_SPEED_1);
						break;

					case '2':
						selected_speed = GAME_SPEED_2;
						highlighMainMenuSelectedOption(OPTION_SPEED_2);
						break;

					case '3':
						selected_speed = GAME_SPEED_3;
						highlighMainMenuSelectedOption(OPTION_SPEED_3);
						break;

					case '4':
						selected_speed = GAME_SPEED_4;
						highlighMainMenuSelectedOption(OPTION_SPEED_4);
						break;

					case '5':
						selected_speed = GAME_SPEED_5;
						highlighMainMenuSelectedOption(OPTION_SPEED_5);
						break;

					case 's':
						return START_GAME;

					case 'q':
						return QUIT_GAME;

					default:
						break;
				}
			}

			try
			{
				Thread.sleep(100);
			}
			catch (InterruptedException ie)
			{
				ie.printStackTrace();
			}
		}
	}

	/**
	 * Creates a menu navigation effect by highlighting the selected option.
	 *
	 * @param selected The option to highlight.
	 */
	private void highlighMainMenuSelectedOption(int selected)
	{
		int y = 15;

		drawString(OPTION_SPEED_1, y, "1", Color.BLUE);
		drawString(OPTION_SPEED_2, y, "2", Color.BLUE);
		drawString(OPTION_SPEED_3, y, "3", Color.BLUE);
		drawString(OPTION_SPEED_4, y, "4", Color.BLUE);
		drawString(OPTION_SPEED_5, y, "5", Color.BLUE);

		if(selected == OPTION_SPEED_1)
		{
			drawString(OPTION_SPEED_1, y, "1", Color.WHITE);
		}
		else if(selected == OPTION_SPEED_2)
		{
			drawString(OPTION_SPEED_2, y, "2", Color.WHITE);
		}
		else if(selected == OPTION_SPEED_3)
		{
			drawString(OPTION_SPEED_3, y, "3", Color.WHITE);
		}
		else if(selected == OPTION_SPEED_4)
		{
			drawString(OPTION_SPEED_4, y, "4", Color.WHITE);
		}
		else if(selected == OPTION_SPEED_5)
		{
			drawString(OPTION_SPEED_5, y, "5", Color.WHITE);
		}

		// Make changes visible.
		refreshScreen();
	}

	/**
	 * Creates the game over menu and takes action on the selected option. When
	 * the game ends, it's possible to restart it, go back to main menu or quit.
	 */
	private void openGameOverMenu()
	{
		// Clear all fruits and obstacles from the screen.
		clearGameObjects();

		renderGameOverMenu();

		// Make changes visible.
		refreshScreen();

		int selected_option = handleGameOverMenu();

		if(selected_option == RESTART_GAME)
		{
			clearScreen();

			state = new GameState();
			startGame();
		}
		else if(selected_option == MAIN_MENU)
		{
			clearScreen();

			openMainMenu();
		}
		else if(selected_option == QUIT_GAME)
		{
			exitGame();
		}
	}

	/**
	 * Returns the selected option on game over menu and creates a navigate animation.
	 *
	 * @return The selected option.
	 */
	private int handleGameOverMenu()
	{
		int selected  = RESTART_GAME;

		Key k;

		while(true)
		{
			k = readKeyInput();

			if(k != null)
			{
				switch(k.getKind())
				{
					case ArrowDown:
						if(selected < QUIT_GAME)
							selected++;

						break;

					case ArrowUp:
						if(selected > RESTART_GAME)
							selected--;

						break;

					case Enter:
						return selected;

					default:
						break;
				}

				highlighGameOverMenuSelectedOption(selected);
			}

			try
			{
				Thread.sleep(100);
			}
			catch (InterruptedException ie)
			{
				ie.printStackTrace();
			}
		}
	}

	/**
	 * Creates a menu navigation effect by highlighting the selected option.
	 *
	 * @param selected The option to highlight.
	 */
	private void highlighGameOverMenuSelectedOption(int selected)
	{
		drawString(28, RESTART_GAME, "Restart", Color.BLUE);
		drawString(28, MAIN_MENU, "Back to main menu", Color.BLUE);
		drawString(28, QUIT_GAME, "Quit", Color.BLUE);

		if(selected == RESTART_GAME)
		{
			drawString(28, RESTART_GAME, "Restart", Color.WHITE);
		}
		else if(selected == MAIN_MENU)
		{
			drawString(28, MAIN_MENU, "Back to main menu", Color.WHITE);
		}
		else if(selected == QUIT_GAME)
		{
			drawString(28, QUIT_GAME, "Quit", Color.WHITE);
		}

		// Make changes visible.
		refreshScreen();
	}

	/**
	 * Start a new game.
	 */
	private void startGame()
	{
		// A counter will be used to determine how frequently the game should be
		// updated. This increases at the end of every game play execution cycle.
		int counter = 0;

		drawWall();
		drawSnake();

		drawString(4, gameplay_height + 1, "SCORE: ", Color.CYAN);
		drawScore(); // initial score.

		// Game play execution: this updates and renders the game, and it will
		// run while the snake is alive.
		while(state.isSnakeAlive())
		{
			// Generate a random fruit and dynamite at a specific time rate.
			if(counter % NEW_OBJECT_TIME_RATE == 0)
			{
				generateNewFruit();
				generateNewDynamite();
			}

			readKeyboard();

			// The higher the mod value, the slower the snake will move. This
			// little trick guarantees that the keyboard input will be read
			// at a higher rate than the snake will move.
			if(counter % selected_speed == 0)
				updateGame();

			try
			{
				Thread.sleep(1);
			}
			catch (InterruptedException ie)
			{
				ie.printStackTrace();
			}

			counter++;
		}
	}

	/**
	 * Updates the game. Move the snake, draw it, and then check if it has eaten
	 * a fruit or has collided. The later will cause a game over.
	 */
	private void updateGame()
	{
		// Get the snake's tail before it moves, in order to erase it from
		// the terminal.
		Position tail = state.getSnakeTail();

		// Hack to erase the string representation of the snake's tail.
		// Just draw an empty string in the previous position of the
		// snake's tail.
		clearStringAt(tail.getX(), tail.getY());

		// Move and re-draw the snake.
		state.moveSnake();
		drawSnake();

		// Get the snake's head position after it has moved.
		Position head = state.getSnakeHead();

		if(checkCollision())
		{
			// Mark the collision spot.
			highlightCrashPosition(head.getX(), head.getY());

			// By killing the snake, the game play loop will end.
			state.killSnake();

			openGameOverMenu();
		}
		else if(state.snakeAteFruit())
		{
			drawScore();

			generateNewFruit();
		}
		else if(state.snakeSteppedDynamite())
		{
			drawScore();
		}

		// Make changes visible.
		refreshScreen();
	}

	/**
	 * Reads input from the keyboard in order to control the snake's next
	 * direction.
	 */
	private void readKeyboard()
	{
		Key k = readKeyInput();

		if(k != null)
		{
			switch(k.getKind())
			{
				case ArrowUp:
					state.setDirection(Direction.UP);
					break;

				case ArrowDown:
					state.setDirection(Direction.DOWN);
					break;

				case ArrowLeft:
					state.setDirection(Direction.LEFT);
					break;

				case ArrowRight:
					state.setDirection(Direction.RIGHT);
					break;

				default:
					break;
			}
		}
	}

	/**
	 * Draw game play walls.
	 */
	private void drawWall()
	{
		// Draw top and bottom borders.
		for(int i = 0; i < gameplay_width; i++)
		{
			drawString(i, 0, BORDER_STRING, null);
			drawString(i, gameplay_height, BORDER_STRING, null);
		}

		// Draw left and right borders.
		for(int i = 0; i <= gameplay_height; i++)
		{
			drawString(0, i, BORDER_STRING, null);
			drawString(gameplay_width, i, BORDER_STRING, null);
		}
	}

	/**
	 * Checks if the given position matches a board's wall.
	 *
	 * @param p the position to check.
	 * @return true if the position matches a board's wall; false otherwise.
	 * @see rand
	 */
	private boolean isWall(Position p)
	{
		if(p.getX() == 0 || p.getX() == gameplay_width ||
		   p.getY() == 0 || p.getY() == gameplay_height)
		{
			return true;
		}

		return false;
	}

	/**
	 * Checks whether the snake's head has collided with the wall or with itself.
	 *
	 * @param head the snake's head.
	 * @return true if snake's head has collided, or false otherwise.
	 */
	private boolean checkCollision()
	{
		Position head = state.getSnakeHead();

		// Collision with borders
		if(isWall(head))
			return true;

		LinkedList<Position> body = state.getSnakeBody();

		// Collision with itself. Must check all positions except the head,
		// which is at the end of the list.
		if(body.subList(0, body.size()-1).contains(head))
			return true;

		return false;
	}

	/**
	 * Draw a string on the screen at a given position of coordinates (x, y), and
	 * with a specified foreground color.
	 *
	 * @param x			the x coordinate.
	 * @param y			the y coordinate.
	 * @param string	the string do draw.
	 * @param fg_color	the foreground color to paint the string.
	 */
	private void drawString(int x, int y, String string, Terminal.Color fg_color)
	{
		screen.putString(x, y, string, fg_color, null);
	}

	/**
	 * Erases the string at a given position (x, y).
	 *
	 * @param x the x coordinate.
	 * @param y the y coordinate.
	 */
	private void clearStringAt(int x, int y)
	{
		drawString(x, y, EMPTY_STRING, null);
	}

	/**
	 * Draws the snake body on the terminal.
	 */
	private void drawSnake()
	{
		Position head = state.getSnakeHead();

		for(Position p : state.getSnakeBody())
		{
			if(!p.equals(head))
			{
				drawString(p.getX(), p.getY(), SNAKE_BODY_STRING, Color.GREEN);
			}
			else
			{
				drawString(p.getX(), p.getY(), SNAKE_HEAD_STRING, Color.GREEN);
			}
		}
	}

	/**
	 * Draws the current score on the screen.
	 *
	 * @param s the score to draw.
	 */
	private void drawScore()
	{
		int s = state.getScore();
		drawString(10, gameplay_height + 1, Integer.toString(s), null);
	}

	/**
	 * Clears the string representation of all fruits and obstacles.
	 */
	private void clearGameObjects()
	{
		for(Position p : state.getFruits())
		{
			clearStringAt(p.getX(), p.getY());
		}

		for(Position p : state.getDynamites())
		{
			clearStringAt(p.getX(), p.getY());
		}
	}

	/**
	 * Generates and draws a new fruit.
	 */
	private void generateNewFruit()
	{
		Position p = state.generateRandomObject(gameplay_width, gameplay_height);

		state.getFruits().add(new Position(p.getX(), p.getY()));
		drawString(p.getX(), p.getY(), FRUIT_STRING, Color.RED);
	}

	/**
	 * Generates and draws a new obstacle.
	 */
	private void generateNewDynamite()
	{
		Position p = state.generateRandomObject(gameplay_width, gameplay_height);

		state.getDynamites().add(new Position(p.getX(), p.getY()));
		drawString(p.getX(), p.getY(), DYNAMITE_STRING, Color.YELLOW);
	}

	/**
	 * Highlights the position where the collision happened.
	 *
	 * @param x the x coordinate.
	 * @param y the y coordinate.
	 */
	private void highlightCrashPosition(int x, int y)
	{
		drawString(x, y, "X", Color.RED);
	}

	/**
	 * Draws the main menu.
	 */
	private void renderMainMenu()
	{
		int x = 10;
		int y = 2;

		drawString(x, y, "########   #######  ###   ###########   ###    ##    #######", Color.CYAN);
		drawString(x,++y,"##         ###  ##  ###   ###     ###   ###   ##     ###    ", Color.CYAN);
		drawString(x,++y,"##         ###  ##  ###   ###     ###   ###  ##      ###    ", Color.CYAN);
		drawString(x,++y,"##         ###  ##  ###   ###########   #######      #######", Color.CYAN);
		drawString(x,++y,"########   ###  ##  ###   ###     ###   ###  ##      ###    ", Color.CYAN);
		drawString(x,++y,"      ##   ###  ##  ###   ###     ###   ###   ##     ###    ", Color.CYAN);
		drawString(x,++y,"      ##   ###  ######    ###     ###   ###    ##    ###    ", Color.CYAN);
		drawString(x,++y,"########   ###  ######    ###     ###   ###     ##   #######", Color.CYAN);

		y += 2;	// 2 blank lines
		x = 25;

		drawString(x, y,   "###########################", Color.BLUE);
		drawString(x, ++y, "Press 'S' to start and", Color.BLUE);
		drawString(x, ++y, "'Q' to quit the game.", Color.BLUE);

		y++;	// blank line

		drawString(x, ++y,  "Speed:", Color.BLUE);
		drawString(OPTION_SPEED_1, y,  "1", Color.WHITE);
		drawString(OPTION_SPEED_2, y,  "2", Color.BLUE);
		drawString(OPTION_SPEED_3, y, "3", Color.BLUE);
		drawString(OPTION_SPEED_4, y, "4", Color.BLUE);
		drawString(OPTION_SPEED_5, y, "5", Color.BLUE);
		drawString(x, ++y, "###########################", Color.BLUE);
	}

	/**
	 * Draws a game over message.
	 */
	private void renderGameOverMenu()
	{
		int x = 20;
		int y = 2;

		drawString(x, y,  "#####    #######  ##### #####  ######", Color.CYAN);
		drawString(x, ++y,"##       ##   ##  ## ## ## ##  ##    ", Color.CYAN);
		drawString(x, ++y,"## ####  #######  ## ##### ##  ######", Color.CYAN);
		drawString(x, ++y,"##   ##  ##   ##  ##  ###  ##  ##    ", Color.CYAN);
		drawString(x, ++y,"#######  ##   ##  ##       ##  ######", Color.CYAN);

		y++;	// blank line

		drawString(x, ++y,"########  ###  ###  ######  ######### ", Color.CYAN);
		drawString(x, ++y,"##    ##  ###  ###  ###     ###  ###  ", Color.CYAN);
		drawString(x, ++y,"##    ##   ######   ######  ########  ", Color.CYAN);
		drawString(x, ++y,"##    ##    ####    ###     ###   ### ", Color.CYAN);
		drawString(x, ++y,"########     ##     ######  ###     ##", Color.CYAN);

		y++;	// blank line
		x = 28;

		drawString(x, ++y, "####################", Color.BLUE);
		drawString(x, ++y, "Restart", Color.WHITE);
		drawString(x, ++y, "Back to main menu", Color.BLUE);
		drawString(x, ++y, "Quit", Color.BLUE);
		drawString(x, ++y, "####################", Color.BLUE);
	}

	/**
	 * @return Next Key off the input queue or null if there is none.
	 */
	private Key readKeyInput()
	{
		return terminal.readInput();
	}

	/**
	 * Make the changes visible on the terminal.
	 */
	private void refreshScreen()
	{
		screen.refresh();
	}

	/**
	 * Erases all the characters on the screen, giving a blank area.
	 */
	private void clearScreen()
	{
		screen.clear();
	}

	/**
	 * Exits the previously entered private mode.
	 */
	private void exitGame()
	{
		terminal.exitPrivateMode();
	}

	public static void main(String[] args)
	{
		GameView snake_game = new GameView(LARGE_WIDTH, LARGE_HEIGHT);
		snake_game.openMainMenu();
	}
}
