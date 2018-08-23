package snakeai2;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Scanner;

import processing.core.PApplet;

public class SnakeAI2 extends PApplet{

	public static void main(String[] args) {
		PApplet.main("snakeai2.SnakeAI2");
	}

	Snek[] snake;
	Apple apple;
	float[][][] list;
	int num, score, menu;
	boolean pause, difficulty, replaying;
	Scanner playerInput;
	int input;
	String fileInput;
	@Override

	public void settings()
	{
		size(600, 600);
	}

	public void setup()
	{
		//all of the variables are initialized above and are set to their proper values
		pause = false;
		replaying = false;
		input = 0;
		snake = new Snek[100];
		apple = new Apple();
		playerInput = new Scanner(System.in);
		for (int i = 0; i < 100; i++)
		{
			snake[i] = new Snek();
			apple.eaten(snake[i]);
		}
		list = new float[10][3][964];
		textSize(20);
		text("Score: " + score, 20, 20);
		frameRate(20);
		println("started program");
	}

	//controls what is being drawn according to what menu the player is interacting with in the game
	@Override
	public void draw()
	{
		if (input == 0)
		{
			println("How many generations would you like to test?(numbers only). Type -1 if you want to access past algorithm");
			input = playerInput.nextInt();

			if(input == -1)
			{
				playerInput.nextLine();
				println("input the file name of the save");
				fileInput = playerInput.nextLine();
				try (Scanner scan = new Scanner(new File(fileInput))) {
					snake[0].load(scan);
				}
				catch (IOException e) {
					e.printStackTrace();
				}
				input = 1;

			}  else {

				for (int i = 0; i < input; i++)
				{
					RunAI();
					if(i%100 == 0)
					{
						println(i);
					}
				}
				for (Snek s: snake)
				{
					println("Cost: " + s.cost);
				}

			}
			println("Would you like to see the best of the generation? 1 for yes, 0 for no");
			input = playerInput.nextInt();
		}
		if (input == 1 || replaying)
		{
			if (input == 1)
			{
				snake[0].setup();
				apple.eaten(snake[0]);
				replaying = true;
				input = 3;
				score = 0;
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (snake[0].dead == false)
			{
				snake[0].AIsetup();
				background(51);
				text("Score: " + score, 20, 20);
				text("Cost: " + snake[0].cost, 200, 20);
				apple.draw();
				snake[0].AICalculate();
				snake[0].CalculateMove();
				snake[0].draw();
				snake[0].collision();
				snake[0].rewardSnake();
			}
			if (snake[0].dead == true)
			{
				println("Would you like to see again? 1 for yes, 0 for no or 2 for save snake algorithm");
				input = playerInput.nextInt();
				if (input == 2)
				{
					playerInput.nextLine();
					println("Enter a file name to save to");
					fileInput = playerInput.nextLine();
					try (PrintWriter pr = new PrintWriter(new File(fileInput))) {
						snake[0].save(pr);
					}
					catch (IOException e) {
						e.printStackTrace();
					}

					println("Would you like to see again? 1 for yes, 0 for no or 2 for save snake algorithm");
					input = playerInput.nextInt();
				}
				replaying = false;

			}
		}
	}

	void RunAI ()
	{
		int current = 0;
		background(51);
		for (int i = 0; i < 100; i++)
		{
			snake[i].setup();
			snake[i].mutate();
			apple.eaten(snake[i]);
			score = 0;
			while(snake[i].dead == false)
			{
				snake[i].AIsetup();
				snake[i].AICalculate();
				snake[i].CalculateMove();
				//println(snake[i].behaviour[1][1]);
				snake[i].move();
				snake[i].collision();
				snake[i].rewardSnake();
			}
			//println(i);
			//println("calculated snake: " + i);
		}
		//println("calculated all snakes");

		//println("Initialized Top Ten");

		Arrays.sort(snake, Comparator.comparing((s) -> s.cost));
		/*for (Snek s: snake)
		{
			println("Cost: "+s.cost);
		}*/

		//println("saving top ten");
		for (int i = 0; i < 10; i++)
		{
			for (int j = 0; j < 3; j++)
			{
				for (int g = 0; g < 964; g++)
				{
					//println(snake[99].behaviour[j][g]);
					list[i][j][g] = snake[99 - i].behaviour[j][g];
					//println(list[i][j][g]);
				}
			}
		}
		//println("saved top ten algorithems");
		//println("cost: " + snake[99].cost);
		current = 0;
		for (int i = 0; i < 10; i++)
		{
			for (int j = 0; j < 10; j++)
			{
				for (int g = 0; g < 3; g++)
				{
					for (int f = 0; f < 964; f++)
					{
						//println(snake[current].behaviour[g][f]);
						snake[current].behaviour[g][f] = list[i][g][f];
						//println(snake[current].behaviour[g][f]);
					}
				}
				current += 1;
			}
		}
	}
	class Snek
	{
		//initializes all the variables for the snake class
		//the array for all of the parts of the snake.
		int[] tailX = new int[10000];
		int[] tailY = new int[10000];
		int tailLength;
		int snakeColor = color(51, 232, 27);
		int dir;
		boolean dead;
		boolean firstSnake = true;
		float[][] behaviour = new float[3][964];
		float[] inputs = new float[964];
		float[] outputs = new float[3];
		//initialize inputs
		float cost = 0;
		int isFoodAhead = 0;
		int isFoodLeft = 0;
		int isFoodRight = 0;
		int moveFoward = 0;
		int moveLeft = 0;
		int moveRight = 0;
		int pastscore = 0;
		int moveCount = 0;

		void setup()
		{
			//sets all of the values properly for the start of the snake playing the game
			tailLength = 1;
			dir = 2;
			tailX[0] = 40;
			tailY[0] = 300;
			dead = false;
			//firstSnake = true;
			cost = 0;
		}

		void AIsetup()
		{
			isFoodAhead = 0;
			isFoodLeft = 0;
			isFoodRight = 0;
			moveFoward = 0;
			moveLeft = 0;
			moveRight = 0;
			for (int i = 0; i < 964; i++)
			{
				inputs[i] = 0;
			}
		}

		//This is a method to draw the snake
		void draw()
		{
			//colors in the snake green
			fill(snakeColor);
			if (outputs[0] > outputs[1] && outputs[0] > outputs[2])
			{
				moveFoward = 1;
			}
			if (outputs[1] > outputs[0] && outputs[1] > outputs[2])
			{
				moveLeft = 1;
			}
			if (outputs[2] > outputs[0] && outputs[2] > outputs[1])
			{
				moveRight = 1;
			}
			//println("Cost: " + cost + " output1: " + outputs[0] + " output2: " + outputs[1] + " output3: " + outputs[2]);
			if (moveFoward == 1)
			{

			}
			if (moveLeft == 1)
			{
				if (dir == 1)
				{
					dir = 4;
				}
				else
				{
					if (dir == 2)
					{
						dir = 1;
					}
					else
					{
						if (dir == 3)
						{
							dir = 2;
						}
						else
						{
							if (dir == 4)
							{
								dir = 3;
							}
						}
					}
				}
			}
			if (moveRight == 1)
			{
				if (dir == 1)
				{
					dir = 2;
				}
				else
				{
					if (dir == 2)
					{
						dir = 3;
					}
					else
					{
						if (dir == 3)
						{
							dir = 4;
						}
						else
						{
							if (dir == 4)
							{
								dir = 1;
							}
						}
					}
				}
			}
			for (int i = tailLength; i >= 0; i--)
			{
				if (i > 0)
				{
					tailX[i] = tailX[i-1];
					tailY[i] = tailY[i-1];
					rect(tailX[i], tailY[i], 20, 20);
				}
			}
			if (dir == 1)
			{
				tailY[0] -= 20;
				rect(tailX[0],tailY[0],20,20);
			}
			if (dir == 2)
			{
				tailX[0] += 20;
				rect(tailX[0],tailY[0],20,20);
			}
			if (dir == 3)
			{
				tailY[0] += 20;
				rect(tailX[0],tailY[0],20,20);
			}
			if (dir == 4)
			{
				tailX[0] -= 20;
				rect(tailX[0],tailY[0],20,20);
			}
		}
		
		void CalculateMove()
		{
			for (int i = 0; i < 3; i++)
			{
				outputs[i] = 0;
				for(int j = 0; j < 964; j++)
				{
					outputs[i] += behaviour[i][j] * inputs[j];
					//println(behaviour[i][j]);
				}
				//println(i + ": " + outputs[i]);
			}
		}

		void mutate()
		{
			if (firstSnake)
			{
				//println("first mutation");
				for (int i = 0; i < 3; i++)
				{
					for (int j = 0; j < 964; j++)
					{
						behaviour[i][j] = random(0,2);
						//println(behaviour[i][j]);
					}
				}
				firstSnake = false;
			}
			else
			{
				int three;
				int ninehundred;
				float rand = random(0,1);
				if (rand < 0.9f)
				{
					for (int i = 0; i < 320; i++)
					{
						three = (int) random(0,3);
						ninehundred = (int) random(0,964);
						behaviour[three][ninehundred] += random(0,2);
					}
				}
				//println(behaviour[three][ninehundred]);
			}
		}

		//controls the movements of the snake in the AI easter egg
		void AICalculate()
		{
			if (dir == 1)
			{
				if (tailY[0] > apple.appleY)
				{
					isFoodAhead = 1;
				}
				if (tailX[0] > apple.appleX)
				{
					isFoodLeft = 1;
				}
				if (tailX[0] < apple.appleX)
				{
					isFoodRight = 1;
				}
			}
			if (dir == 2)
			{
				if (tailX[0] < apple.appleX)
				{
					isFoodAhead = 1;
				}
				if (tailY[0] > apple.appleY)
				{
					isFoodLeft = 1;
				}
				if (tailY[0] < apple.appleY)
				{
					isFoodRight = 1;
				}
			}
			if (dir == 3)
			{
				if (tailY[0] < apple.appleY)
				{
					isFoodAhead = 1;
				}
				if (tailX[0] < apple.appleX)
				{
					isFoodLeft = 1;
				}
				if (tailX[0] > apple.appleX)
				{
					isFoodRight = 1;
				}
			}
			if (dir == 4)
			{
				if (tailX[0] > apple.appleX)
				{
					isFoodAhead = 1;
				}
				if (tailY[0] < apple.appleY)
				{
					isFoodLeft = 1;
				}
				if (tailY[0] > apple.appleY)
				{
					isFoodRight = 1;
				}
			}
			
			int inp = 0;
			//calculate info for inputs
			for (int i = 0; i < 31; i++)
			{
				for (int j = 0; j < 31; j++)
				{
					//apple
					if (apple.appleX/20 == i-1 && apple.appleY/20 == j-1)
					{
						inputs[inp] = 0;
					}
					else
					{
						//Walls/border
						if (i-1 == 30 || i-1 == 0 || j-1 == 30 || j-1 == 0)
						{
							inputs[inp] = 1;
							//println("Cost: " + cost + " something is at: " + inp);
						}
						else
						{
							//own tail
							for (int g = 0; g < tailLength; g++)
							{
								//head
								if (i-1 == tailX[0]/20 && j-1 == tailY[0]/20)
								{
									inputs[inp] = 0;
									//break;
								}
								else
								{
									if (i-1 == tailX[g]/20 && j-1 == tailY[g]/20)
									{
										inputs[inp] = 1;
										//println("Cost: " + cost + " something is at: " + inp);
										//break;
									}
									//open space
									else
									{
										inputs[inp] = 0;
										break;
									}
								}
							}
						}
					}
					inp += 1;
				}
			}
			inputs[961] = isFoodAhead;
			inputs[962] = isFoodLeft;
			inputs[963] = isFoodRight;
			//println("Cost: " + cost + " ahead: " + inputs[961] + " left: " + inputs[962] + " right: " + inputs[963]);
		}
		void move()
		{
			if (outputs[0] > outputs[1] && outputs[0] > outputs[2])
			{
				moveFoward = 1;
			}
			if (outputs[1] > outputs[0] && outputs[1] > outputs[2])
			{
				moveLeft = 1;
			}
			if (outputs[2] > outputs[0] && outputs[2] > outputs[1])
			{
				moveRight = 1;
			}
			//println("Cost: " + cost + " output1: " + outputs[0] + " output2: " + outputs[1] + " output3: " + outputs[2]);
			if (moveFoward == 1)
			{

			}
			if (moveLeft == 1)
			{
				if (dir == 1)
				{
					dir = 4;
				}
				else
				{
					if (dir == 2)
					{
						dir = 1;
					}
					else
					{
						if (dir == 3)
						{
							dir = 2;
						}
						else
						{
							if (dir == 4)
							{
								dir = 3;
							}
						}
					}
				}
			}
			if (moveRight == 1)
			{
				if (dir == 1)
				{
					dir = 2;
				}
				else
				{
					if (dir == 2)
					{
						dir = 3;
					}
					else
					{
						if (dir == 3)
						{
							dir = 4;
						}
						else
						{
							if (dir == 4)
							{
								dir = 1;
							}
						}
					}
				}
			}
			for (int i = tailLength; i >= 0; i--)
			{
				if (i > 0)
				{
					tailX[i] = tailX[i-1];
					tailY[i] = tailY[i-1];
				}
			}
			if (dir == 1)
			{
				tailY[0] -= 20;
			}
			if (dir == 2)
			{
				tailX[0] += 20;
			}
			if (dir == 3)
			{
				tailY[0] += 20;
			}
			if (dir == 4)
			{
				tailX[0] -= 20;
			}
		}

		void rewardSnake()
		{
			//reward system of the AI
			//moveCount += 1;
			//cost = (float) ((moveCount/4) + (tailLength * 100000.0));
			if ((moveRight == 1 && isFoodLeft == 1) || (moveLeft == 1 && isFoodRight == 1) || (moveFoward == 1 && isFoodAhead == 0) || (moveFoward == 0 && moveLeft == 0 && moveRight == 0))
			{
				cost -= 3f;
			}
			if ((moveRight == 1 && isFoodRight == 1) || (moveLeft == 1 && isFoodLeft == 1) || (moveFoward == 1 && isFoodAhead == 1))
			{
				cost += 1;
			}
			if (cost < -100 || cost > 10000)
			{
				die();
			}
			//println(cost);
			//println("Cost: " + cost + " move forward: " + moveFoward + " move left: " + moveLeft + " move right: " + moveRight);
		}

		//a method to lengthen the tail
		void addtail()
		{
			tailLength += 1;
		}

		//controls what happens when the snake runs into certain objects
		void collision()
		{

			//If eats the apple
			if (tailY[0] == apple.appleY)
			{
				if (tailX[0] == apple.appleX)
				{
					addtail();
					score += 1;
					apple.eaten(this);
					cost += 10;
				}
			}

			//If collides with wall then it will reset

			if (tailY[0] == -20 || tailY[0] == 600 || tailX[0] == -20 || tailX[0] == 600)
			{
				die();
			}

			//If it runs into its own tail it will reset

			for (int i = tailLength; i >= 1; i--)
			{
				if (tailX[i] == tailX[0] && tailY[i] == tailY[0])
				{
					die();
					cost -= 300;
				}
			}
		}

		void die()
		{
			dead = true;
		}

		void save(PrintWriter pr) {

			for (int i = 0; i < 3; i++)
			{
				for (int j = 0; j < 964; j++)
				{
					pr.print(behaviour[i][j] + " ");
				}
				pr.println("");
			}
			pr.println("");

		}

		void load(Scanner file) {
			for (int i = 0; i < 3; i++)
			{
				for (int j = 0; j < 964; j++)
				{
					this.behaviour[i][j] = file.nextFloat();
				}
			}
		}

	}

	class Apple
	{
		//initiallizes all of the variables
		int appleX, appleY;
		int appleColor = color(237, 14, 14);

		void setup()
		{
			appleY = 300;
			appleX = 300;
		}

		//draws the apple
		void draw()
		{
			fill(appleColor);
			rect(appleX, appleY, 20, 20);
		}

		//controls what happens when the snake eats the apple
		void eaten(Snek snake)
		{
			randomspot(20);
			appleX = num;
			randomspot(20);
			appleY = num;
			for (int i = 0; i < snake.tailLength; i++)
			{
				if (appleX == snake.tailX[i] && appleY == snake.tailY[i])
				{
					randomspot(20);
					appleX = num;
					randomspot(20);
					appleY = num;
				}
			}
		}
	}

	//generates a random position according to the size of each square of the grid
	void randomspot(int grid)
	{
		num = round(random(0, 580));
		if (num % grid >= grid/2)
		{
			num += grid-(num % grid);
		}
		if (num % grid < grid/2)
		{
			num -= (num % grid);
		}
		return;
	}
}