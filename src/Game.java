//Mine sweeper back end class

public class Game 
{
	private Block[][] blocks;			//All the blocks objects in 2D array
	
	public Game()
	{
		blocks = new Block[10][10];		//Initialize all blocks
		
		for(int i=0;i<10;i++)
		{
			for(int j=0;j<10;j++)
			{
				blocks[i][j] = new Block(i,j);
			}
		}
		
		int minesToDo = 10;				//To add 10 land mines
		
		while(minesToDo>0)
		//Randomly pick 10 blocks, mine them. If already mined, skip and redo.
		{
			int x = (int)(Math.random()*10);
			int y = (int)(Math.random()*10);
			
			if(!blocks[x][y].checkMined())
			{
				blocks[x][y].mine();
				
				minesToDo -=1;
			}
			
		}

	}
	
	public Block getBlock(int x, int y)
	{
		return blocks[x][y];
	}
	
	public boolean leftClick(int x, int y)
	//Called when front end block (x, y) left clicked
	{
		if(x>=0 && x<=9 && y>=0 && y<=9)
		//Avoid borders
		{
			if(blocks[x][y].getStatus()==0 && !blocks[x][y].checkCleared())
			//If marked or already cleared, do nothing
			{
				if(blocks[x][y].step())
				//Step on the block, if explode, return true for the front end to do sth
					return true;
				else if(calculateAround(x,y)==0)
				//Recursive clear around
				{		
					leftClick(x+1,y);
					leftClick(x-1,y);
					leftClick(x+1,y+1);
					leftClick(x+1,y-1);
					leftClick(x,y+1);
					leftClick(x,y-1);
					leftClick(x-1,y+1);
					leftClick(x-1,y-1);
				}
			}
		}

		return false;
	}
	
	public void rightClick(int x, int y)
	//Called when front end block (x, y) right clicked
	{
		blocks[x][y].mark();
	}
	
	public int remainingMines()
	//Return the "remaining" mines for display, can go to negative
	{
		int mineCount = 10;
		
		for(int i=0;i<10;i++)
		{
			for(int j=0;j<10;j++)
			{
				if(blocks[i][j].getStatus()==1)
					mineCount -= 1;
			}
		}
		
		return mineCount;
	}
	
	public int calculateAround(int x, int y)
	//Calculate the mines around block (x, y)
	{
		int around = 0;
		
		if(blocks[x][y].checkMined())
			return -1;
		
		if(x>0 && x<9 && y>0 && y<9)
		//Center
		{
			if(blocks[x+1][y].checkMined())
				around +=1;
			if(blocks[x-1][y].checkMined())
				around +=1;
			if(blocks[x+1][y+1].checkMined())
				around +=1;
			if(blocks[x+1][y-1].checkMined())
				around +=1;
			
			if(blocks[x][y+1].checkMined())
				around +=1;
			if(blocks[x][y-1].checkMined())
				around +=1;
			if(blocks[x-1][y+1].checkMined())
				around +=1;
			if(blocks[x-1][y-1].checkMined())
				around +=1;
		}
		else if(x==0 && y>0 && y<9)
		//Top edge
		{
			if(blocks[x+1][y].checkMined())
				around +=1;
			
			if(blocks[x+1][y+1].checkMined())
				around +=1;
			if(blocks[x+1][y-1].checkMined())
				around +=1;
			
			if(blocks[x][y+1].checkMined())
				around +=1;
			if(blocks[x][y-1].checkMined())
				around +=1;
			
		}
		else if(x==9 && y>0 && y<9)
		//Bottom edge
		{
			
			if(blocks[x-1][y].checkMined())
				around +=1;
			
			if(blocks[x][y+1].checkMined())
				around +=1;
			if(blocks[x][y-1].checkMined())
				around +=1;
			if(blocks[x-1][y+1].checkMined())
				around +=1;
			if(blocks[x-1][y-1].checkMined())
				around +=1;
		}
		else if(x>0 && x<9 && y==0)
		//Left edge
		{
			if(blocks[x+1][y].checkMined())
				around +=1;
			if(blocks[x-1][y].checkMined())
				around +=1;
			if(blocks[x+1][y+1].checkMined())
				around +=1;
			
			
			if(blocks[x][y+1].checkMined())
				around +=1;
			
			if(blocks[x-1][y+1].checkMined())
				around +=1;
			
		}
		else if(x>0 && x<9 && y==9)
		//Right edge
		{
			if(blocks[x+1][y].checkMined())
				around +=1;
			if(blocks[x-1][y].checkMined())
				around +=1;
			
			if(blocks[x+1][y-1].checkMined())
				around +=1;
			
			
			if(blocks[x][y-1].checkMined())
				around +=1;
			
			if(blocks[x-1][y-1].checkMined())
				around +=1;
		}
		else if(x==0 && y==0)
		//Top left corner
		{
			if(blocks[x+1][y].checkMined())
				around +=1;
			
			if(blocks[x+1][y+1].checkMined())
				around +=1;

			if(blocks[x][y+1].checkMined())
				around +=1;
				
		}
		else if(x==9 && y==0)
		//Bottom left corner
		{
			if(blocks[x-1][y].checkMined())
				around +=1;
			
			if(blocks[x-1][y+1].checkMined())
				around +=1;

			if(blocks[x][y+1].checkMined())
				around +=1;
		}
		else if(x==9 && y==9)
		//Bottom right corner
		{
			if(blocks[x-1][y].checkMined())
				around +=1;
			
			if(blocks[x-1][y-1].checkMined())
				around +=1;
			
			if(blocks[x][y-1].checkMined())
				around +=1;
		}
		else if(x==0 && y==9)
		//Bottom left corner
		{
			if(blocks[x+1][y].checkMined())
				around +=1;
			
			if(blocks[x+1][y-1].checkMined())
				around +=1;

			if(blocks[x][y-1].checkMined())
				around +=1;
		}
		
		return around;
	}
	
	public String getDisplay(int x, int y)
	//Get what this block should appear at the front end. Return one char string.
	{
		String display="";
		
		Block block = blocks[x][y];
		
		if(getUncleared()==10)
		//Game already win, uncleared blocks are all shown "M"
		{
			if(block.checkCleared())
			{
				display = "" + calculateAround(x, y);
			}
			else
			{
				display = "M";
			}
		}
		else
		//Normal gaming
		{
			if(block.checkCleared())
			//Cleared blocks show number
			{
				display = "" + calculateAround(x, y);
			}
			else
			//Uncleared blocks depend on mark
			{
				if(block.getStatus()==0)
					display = "";
				else if(block.getStatus()==1)
					display = "M";
				else
					display = "?";
			}
		}
		return display;
		
	}
	
	public int getUncleared()
	//Calculate uncleared blocks for checking win condition.
	{
		int uncleared = 100;
		
		for(int i=0;i<10;i++)
		{
			for(int j=0;j<10;j++)
			{
				if(blocks[i][j].checkCleared())
					uncleared -=1;
			}
		}
		
		return uncleared;
	}
}
