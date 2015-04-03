import java.util.LinkedList;
import java.util.Random;

/**
 *
 * A generic class that holds the state of a snake game.
 */
public class GameState
{
	/*
	 * The value used to decrement the score when the snake steps over
	 * an obstacle.
	 */
	private final static int SCORE_PENALTY = 25;

	/*
	 * Snake object.
	 */
	private Snake snake;

	/*
	 * A list of fruits to catch.
	 */
	private LinkedList<Position> fruits;

	/*
	 * A list of obstacles to avoid.
	 */
	private LinkedList<Position> dynamites;

	/*
	 * The score regarding this game.
	 */
	private int score;

	/*
	 * A Random number generator.
	 */
	private final Random rand;

	/**
	 * Instantiate a new GameState object.
	 */
	public GameState()
	{
		rand = new Random();

		// Create a snake facing the RIGHT direction.
		snake  = new Snake(Direction.RIGHT);

		fruits    = new LinkedList<Position>();
		dynamites = new LinkedList<Position>();

		score  = 0;
	}

	/**
	 * @return The list of fruits.
	 */
	public LinkedList<Position> getFruits()
	{
		return fruits;
	}

	/**
	 * @return The list of dynamites.
	 */
	public LinkedList<Position> getDynamites()
	{
		return dynamites;
	}

	/**
	 * @return The score.
	 */
	public int getScore()
	{
		return score;
	}

	/**
	 * @return True if the snake is alive.
	 */
	public boolean isSnakeAlive()
	{
		return snake.isAlive();
	}

	/**
	 * Kills the snake.
	 */
	public void killSnake()
	{
		snake.kill();
	}

	/**
	 * Moves the snake in its current direction.
	 */
	public void moveSnake()
	{
		snake.move();
	}

	/**
	 * @return The snake's body.
	 */
	public LinkedList<Position> getSnakeBody()
	{
		return snake.getBody();
	}

	/**
	 * @return The snake's head.
	 */
	public Position getSnakeHead()
	{
		return snake.getHead();
	}

	/**
	 * @return The snake's tail.
	 */
	public Position getSnakeTail()
	{
		return snake.getTail();
	}

	/**
	 * If the snake has eaten the fruit, then the score will be increased and
	 * then returns true.
	 *
	 * @return True if the snake ate a fruit.
	 */
	public boolean snakeAteFruit()
	{
		if(snake.ateFruit(fruits))
		{
			updateScore(true);
			snake.increaseSize();

			return true;
		}

		return false;
	}

	/**
	 * If the snake has stepped over the obstacle, then the score will be
	 * decreased and then returns true.
	 *
	 * @return True if the snake stepped over an obstacle.
	 */
	public boolean snakeSteppedDynamite()
	{
		if(snake.steppedOverDynamite(dynamites))
		{
			updateScore(false);

			return true;
		}

		return false;
	}

	/**
	 * Sets a new snake direction, preventing it from turning to its opposite
	 * direction, otherwise the player will instantly lose the game.
	 *
	 * @param dir The direction so set.
	 */
	public void setDirection(Direction dir)
	{
		switch(dir)
		{
			case UP:

				if(snake.getDirection() != Direction.DOWN)
					snake.setDirection(Direction.UP);

				break;

			case DOWN:

				if(snake.getDirection() != Direction.UP)
					snake.setDirection(Direction.DOWN);

				break;

			case LEFT:

				if(snake.getDirection() != Direction.RIGHT)
					snake.setDirection(Direction.LEFT);

				break;

			case RIGHT:

				if(snake.getDirection() != Direction.LEFT)
					snake.setDirection(Direction.RIGHT);

				break;

			default:
				throw new IllegalArgumentException("No such direction");
		}
	}

	/**
	 * Updates the players' score.
	 *
	 * @param increase Whether or not the score should be increased. For instance,
	 * if the snake steps over a dynamite, the score should decrease.
	 */
	private void updateScore(boolean increase)
	{
		if(increase)
			score += (snake.getBody().size() * 2 + dynamites.size());
		else
			score -= SCORE_PENALTY;
	}

	/**
	 * Returns a pseudo-random number between min (inclusive) and max (exclusive).
	 * Such number represents a Position of coordinates (x, y) on the Terminal.
	 *
	 * The difference between min and max can be at most width-2 or
	 * height-2.
	 *
	 * @param min The minimum value
	 * @param max The maximum value. Must be greater than min.
	 * @return Integer between min (inclusive) and max (exclusive).
	 * @see java.util.Random#nextInt(int)
	 */
	private int randomNumber(int min, int max)
	{
	    // nextInt is normally exclusive of the top value
	    return rand.nextInt(max - min) + min;
	}

	/**
	 * Generates a random position inside the game play area.
	 *
	 * @param max_x The maximum value that X can take.
	 * @param max_y The maximum value that Y can take.
	 * @return A new Position of coordinates (x, y).
	 */
	private Position generateRandomPosition(int max_x, int max_y)
	{
		int x = randomNumber(1, max_x);
		int y = randomNumber(1, max_y);

		return new Position(x, y);
	}

	/**
	 * Returns true if the specified position is empty, meaning that it is not
	 * occupied by another object, which can be this snake, a fruit, or an
	 * dynamite.
	 *
	 * @param p The position to check.
	 * @return True if the specified position is empty.
	 */
	private boolean isEmptyPosition(Position p)
	{
		return !(snake.isBody(p) || fruits.contains(p) || dynamites.contains(p));
	}

	/**
	 * Generates a random position for a new object, ensuring that it is not
	 * generated in a position already occupied by another object.
	 *
	 * Please note that we already ensured that the new fruit is not generated
	 * over the board's wall by exclusively generating a random number.
	 *
	 * @param x_max The maximum value that X can take.
	 * @param y_max The maximum value that Y can take.
	 * @return The generated position.
	 */
	public Position generateRandomObject(int x_max, int y_max)
	{
		Position p = null;

		do
		{
			p = generateRandomPosition(x_max, y_max);

		} while(!isEmptyPosition(p));

		return p;
	}
}
