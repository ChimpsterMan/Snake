package snake;

import processing.core.PApplet;


public class Snake extends PApplet {

	public static void main(String[] args) {
		PApplet.main("snake.Snake");
	}
	
	Snek snake;
	Apple apple;
	int dir, num, score, menu;
	boolean pause, difficulty;
	public void settings()
	{
		size(600, 600);
	}
	
	public void setup()
	{
	  //all of the variables are initialized above and are set to their proper values.
	  pause = false;
	  snake = new Snek();
	  apple = new Apple();
	  play();
	  background(51);
	  snake.draw();
	  textSize(20);
	  text("Score: " + score, 20, 20);
	  menu = 1;
	  difficulty = false;
	}

	//controls what is being drawn according to what menu the player is interacting with in the game
	public void draw()
	{
	  //If the person is playing the game
	  if (pause == false && menu == 0)
	  {
	    background(51);
	    apple.draw();
	    snake.collision();
	    snake.draw();
	    textSize(20);
	    text("Score: " + score, 20, 20);
	  }
	  //if they are on the main menu
	  if (menu == 1)
	  {
	    mainmenu();
	  }
	  //the screen when you die
	  if (menu == 2)
	  {
	    die();
	  }
	  //the instructions page
	  if (menu == 3)
	  {
	    instructions();
	  }
	  //if the player want to use the AI easter egg
	  if (menu == 4)
	  {
	    background(51);
	    snake.AI();
	    apple.draw();
	    snake.collision();
	    snake.draw();
	    textSize(20);
	    text("Score: " + score, 20, 20);
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
	  
	  void setup()
	  {
	    //sets all of the values properly for the start of the snake playing the game
	    tailLength = 0;
	    tailX[0] = 40;
	    tailY[0] = 300;
	  }
	  
	  //This is a method to draw the snake
	  void draw()
	  {
	    //colors in the snake green
	    fill(snakeColor);
	    //calculates the parts of the tail as it follows the snake
	    for (int i = tailLength; i >= 0; i--)
	    {
	      if (i > 0)
	        {
	          tailX[i] = tailX[i-1];
	          tailY[i] = tailY[i-1];
	          rect(tailX[i],tailY[i],20,20);
	        }
	    }
	    //controls the head.
	    //dir 1 = UP
	    //dir 2 = right
	    //dir 3 = down
	    //dir 4 = left
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
	  
	  //controls the movements of the snake in the AI easter egg
	  void AI()
	  {
	    if (tailX[0] == apple.appleX || tailY[0] == apple.appleY)
	    {
	      if (tailY[0] == apple.appleY)
	      {
	        if (tailX[0] > apple.appleX)
	        {
	          dir = 4;
	        }
	        if (tailX[0] < apple.appleX)
	        {
	          dir = 2;
	        }
	      }
	      if (tailX[0] == apple.appleX)
	      {
	        if (tailY[0] > apple.appleY)
	        {
	          dir = 1;
	        }
	        if (tailY[0] < apple.appleY)
	        {
	          dir = 3;
	        }
	      }
	    } else {
	      if ((tailX[0] == 0 && tailY[0] == 0) || (tailX[0] == 0 && tailY[0] == 580))
	      {
	        if (tailX[0] == 0 && tailY[0] == 0)
	        {
	          dir = 3;
	        }
	        if (tailX[0] == 0 && tailY[0] == 580)
	        {
	          dir = 1;
	        }
	      } else {
	    if (tailX[0] == 0)
	    {
	      if (tailY[0] > 0)
	      {
	        dir = 1;
	      } else {
	        if (tailY[0] < 580)
	        {
	        dir = 3;
	        }
	      }
	    }
	    if (tailX[0] == 580)
	    {
	      if (tailY[0] > 0)
	      {
	        dir = 1;
	      } else {
	        if (tailY[0] < 580)
	        {
	        dir = 3;
	        }
	      }
	    }
	    if (tailY[0] == 0)
	    {
	      if (tailX[0] > 0)
	      {
	        dir = 4;
	      } else {
	        if (tailX[0] < 580)
	        {
	          dir = 2;
	        }
	      }
	    }
	    if (tailY[0] == 580)
	    {
	      if (tailX[0] > 0)
	      {
	        dir = 4;
	      } else {
	        if (tailX[0] < 580)
	        {
	          dir = 2;
	        }
	      }
	    }
	    }
	    }
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
	        apple.eaten();
	      }
	    }
	    
	    //If collides with wall then it will reset
	    
	    if (tailY[0] == -20 || tailY[0] == 600 || tailX[0] == -20 || tailX[0] == 600)
	    {
	      menu = 2;
	    }
	    
	    //If it runs into its own tail it will reset
	    
	    for (int i = tailLength; i >= 1; i--)
	    {
	      if (tailX[i] == tailX[0] && tailY[i] == tailY[0])
	        {
	          menu = 2;
	        }
	    }
	  }
	}

	class Apple
	{
	  //initiallizes all of the variables
	  int appleX, appleY;
	  int appleColor = color(237, 14, 14);
	  
	  //draws the apple
	  void draw()
	  {
	    fill(appleColor);
	    rect(appleX, appleY, 20, 20);
	  }
	  
	  //controls what happens when the snake eats the apple
	  void eaten()
	  {
	    randomspot(20);
	    appleX = num;
	    randomspot(20);
	    appleY = num;
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

	//draws the screen that appears when you die
	void die()
	{ 
	  fill(40);
	  rect(100,100,400,400);
	  fill(249, 57, 57);
	  textSize(40);
	  text("You done died!", 160, 200);
	  text("Score: " + score, 200, 300);
	  fill(51);
	  rect(140, 400, 150, 50);
	  rect(320, 400, 150, 50);
	  textSize(25);
	  if (mouseX >= 140 && mouseX <= 290 && mouseY >= 400 && mouseY <= 450)
	  {
	    fill(44, 204, 26);
	  } else {
	    fill(249, 57, 57);
	  }
	  text("Main Menu", 150, 435);
	  if (mouseX >= 320 && mouseX <= 470 && mouseY >= 400 && mouseY <= 450)
	  {
	    fill(44, 204, 26);
	  } else {
	  fill(249, 57, 57);
	  }
	  text("Try Again", 335, 435);
	}

	//draws teh mainmenu
	void mainmenu()
	{
	  fill(40);
	  rect(100,100,400,400);
	  fill(249, 57, 57);
	  textSize(60);
	  text("Snake", 210, 180);
	  fill(51);
	  rect(220,240, 150, 50);
	  rect(220,300, 150, 50);
	  rect(220,360, 150, 50);
	  rect(220,420, 150, 50);
	  fill(249, 57, 57);
	  textSize(25);
	  if (mouseX >= 220 && mouseX <= 370 && mouseY >= 240 && mouseY <= 290)
	  {
	    fill(44, 204, 26);
	  } else {
	    fill(249, 57, 57);
	  }
	  text("Play", 270, 275);
	  if (mouseX >= 220 && mouseX <= 370 && mouseY >= 300 && mouseY <= 350)
	  {
	    fill(44, 204, 26);
	  } else {
	    fill(249, 57, 57);
	  }
	  text("Instructions", 225, 335);
	  if (mouseX >= 220 && mouseX <= 370 && mouseY >= 360 && mouseY <= 410)
	  {
	    fill(44, 204, 26);
	  } else {
	    fill(249, 57, 57);
	  }
	  if (difficulty)
	  {
	    text("Hard", 265, 395);
	    frameRate(20);
	  } else {
	    text("Easy", 265, 395);
	    frameRate(10);
	  }
	  if (mouseX >= 220 && mouseX <= 370 && mouseY >= 420 && mouseY <= 470)
	  {
	    fill(44, 204, 26);
	  } else {
	    fill(249, 57, 57);
	  }
	  text("Exit", 270, 455);
	}

	//draws the instructions menu
	void instructions()
	{
	  fill(40);
	  rect(100,100,400,400);
	  fill(249, 57, 57);
	  textSize(60);
	  text("Instructions", 130, 180);
	  textSize(20);
	  text("You play the game with WASD controls.",120,250);
	  text("The goal of the game is to get as long", 120, 280);
	  text("a snake as possible. You grow your", 120, 310);
	  text("snake's tail by eating the red apples.", 120,340);
	  textSize(25);
	  fill(51);
	  rect(220,420, 150, 50);
	  if (mouseX >= 220 && mouseX <= 370 && mouseY >= 420 && mouseY <= 470)
	  {
	    fill(44, 204, 26);
	  } else {
	    fill(249, 57, 57);
	  }
	  text("Main Menu", 230, 455);
	}

	//reads all of the keypresses for each page of the game you are on
	public void keyPressed()
	{
	  //menu 0 is when you are playing the game
	  //menu 1 is the main menu
	  //menu 2 is the menu when you die
	  //menu 3 is the instructions menu
	  //menu 4 is an easter egg AI Mode
	  if (menu == 0)
	  {
	    if (key == 'w')
	    {
	      dir = 1;
	    }
	    if (key == 'd')
	    {
	      dir = 2;
	    }
	    if (key == 's')
	    {
	      dir = 3;
	    }
	    if (key == 'a')
	    {
	      dir = 4;
	    }
	    if (key == ' ')
	    {
	      pause = !pause;
	    }
	    if (key == 'q')
	    {
	      menu = 2;
	    }
	  }
	}

	//re-initializes all of the settings for the snake game when you start playing again
	void play()
	{
	    menu = 0;
	    dir = 2;
	    score = 0;
	    pause = false;
	    apple.eaten();
	    snake.setup();
	}

	//initializes the values for the snake when using the AI easter egg
	void playAI()
	{
	    menu = 4;
	    dir = 2;
	    score = 0;
	    pause = false;
	    apple.eaten();
	    snake.setup();
	}

	//controls all of the mouse clicks on the various inputs
	public void mousePressed()
	{
	  if (menu == 0)
	  {
	    
	  }
	  if (menu == 1)
	  {
	    if (mouseX >= 100 && mouseX <= 120 && mouseY >= 100 && mouseY <= 120)
	    {
	      playAI();
	    }
	    if (mouseX >= 220 && mouseX <= 370 && mouseY >= 240 && mouseY <= 290)
	    {
	      play();
	    }
	    if (mouseX >= 220 && mouseX <= 370 && mouseY >= 300 && mouseY <= 350)
	    {
	      menu = 3;
	    }
	    if (mouseX >= 220 && mouseX <= 370 && mouseY >= 360 && mouseY <= 410)
	    {
	      difficulty = !difficulty;
	    }
	    if (mouseX >= 220 && mouseX <= 370 && mouseY >= 420 && mouseY <= 470)
	    {
	      exit();
	    }
	  }
	  if (menu == 2)
	  {
	    if (mouseX >= 140 && mouseX <= 290 && mouseY >= 400 && mouseY <= 450)
	    {
	      menu = 1;
	    }
	    if (mouseX >= 320 && mouseX <= 470 && mouseY >= 400 && mouseY <= 450)
	    {
	      play();
	    }
	  }
	  if (menu == 3)
	  {
	    if (mouseX >= 220 && mouseX <= 370 && mouseY >= 420 && mouseY <= 470)
	    {
	      menu = 1;
	    }
	  }
	}
}
