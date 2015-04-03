import java.util.LinkedList;

/**
 *
 * A generic class to represent a Snake object. The Snake has a "body" and the
 * direction in which it's moving.
 */
public class Snake
{
	/*
	 * Define snake's initial length.
	 */
	private final static int SNAKE_INITIAL_SIZE = 4;

	/*
	 * Snake's body position. It's head is in the last position of the list.
	 */
	private static LinkedList<Position> body;

	/*
	 * The direction in which the snake is facing.
	 */
	private Direction direction;

	/*
	 * Whether or not the snake is alive.
	 */
	private boolean alive;

	/**
	 * Instantiate a new snake object facing the given direction.
	 *
	 * @param starting_direction the direction the snake is facing
	 */
	public Snake(Direction starting_direction)
	{
		body = new LinkedList<Position>();

		// Set snake's body. Note that x = 0 represents the wall.
		for(int i = 0; i < SNAKE_INITIAL_SIZE; i++)
		{
			body.add(new Position(i + 3, 15));
		}

		direction = starting_direction;
		alive     = true;
	}

	/**
	 * @return the snake's body
	 */
	public LinkedList<Position> getBody()
	{
		return body;
	}

	/**
	 * @return Snake's head.
	 */
	public Position getHead()
	{
		return body.getLast();
	}

	/**
	 * @return Snake's tail.
	 */
	public Position getTail()
	{
		return body.getFirst();
	}

	/**
	 * @param direction the new direction.
	 */
	public void setDirection(Direction direction)
	{
		this.direction = direction;
	}

	/**
	 * @return the direction in which the snake is moving.
	 */
	public Direction getDirection()
	{
		return direction;
	}

	/**
	 * @return true if the snake is alive; false otherwise.
	 */
	public boolean isAlive()
	{
		return alive;
	}

	/**
	 * Kill the snake.
	 */
	public void kill()
	{
		alive = false;
	}

	/**
	 * Move the snake in one of the four possible directions: UP, DOWN,
	 * LEFT, or RIGHT.
	 * The snake "moves" by bringing the first block (tail) up to the front,
	 * which is quiet elegant.
	 */
	public void move()
	{
		// Get current head position
		Position head = getHead();

		// Remove tail from body
		body.removeFirst();

		// Determine head's new position based on snake's direction
		switch(direction)
		{
			case UP:

				head = new Position(head.getX(), head.getY() - 1);
				break;

			case DOWN:

				head = new Position(head.getX(), head.getY() + 1);
				break;

			case LEFT:

				head = new Position(head.getX() - 1, head.getY());
				break;

			case RIGHT:

				head = new Position(head.getX() + 1, head.getY());
				break;

			default:
				throw new IllegalArgumentException("There is no such direction");
		}

		// Insert the new head into the snake's body
		body.addLast(head);
	}

	/**
	 * Returns true if this snake contains the specified position.
	 *
	 * @param p The position to check.
	 * @return true if the snake contains the specified position.
	 */
	public boolean isBody(Position p)
	{
		return body.contains(p);
	}

	/**
	 * Checks if the snake's head position is the same as a fruit's position.
	 * If it is true, then the fruit is removed from the list.
	 *
	 * @param fruits The list of fruits.
	 * @return true if the snake's head is over a fruit's position.
	 */
	public boolean ateFruit(LinkedList<Position> fruits)
	{
		Position head = getHead();

		for(Position p : fruits)
		{
			if(head.equals(p))
			{
				fruits.remove(p);

				return true;
			}
		}

		return false;
	}

	/**
	 * Checks if the snake's head position is the same as an obstacle's position.
	 * If it is true, then the dynamite is removed from the list.
	 *
	 * @param dynamites The list of obstacles.
	 * @return true if the snake's head is over an obstacle's position.
	 */
	public boolean steppedOverDynamite(LinkedList<Position> dynamites)
	{
		Position head = getHead();

		for(Position p : dynamites)
		{
			if(head.equals(p))
			{
				dynamites.remove(p);

				return true;
			}
		}

		return false;
	}

	/**
	 * Increases by one the size of the snake.
	 */
	public void increaseSize()
	{
		Position tail = getTail();

		body.addFirst(new Position(tail.getX(), tail.getY()));
	}
}
