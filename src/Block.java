//Basic mine sweeper block

public class Block 
{
	private int x;				//horizontal coordinate in UI
	private int y;				//vertical coordinate in UI
	
	private boolean mined;		//whether there is a mine in this block
	private boolean cleared;	//whether this block has been clicked or cleared
	
	private int status;			//iteration status of right click. 0 for not clicked, 1 for marked, 2 for questioned
	
	public Block(int coordx, int coordy)
	//Initialize the block with coordinate x and y, other status by default are false.
	{
		x = coordx;
		y = coordy;
		
		mined = false;
		cleared = false;
		
		status = 0;
	}
	
	public void mine()
	//Mine a land mine here
	{
		mined = true;
	}
	
	public boolean checkMined()
	//Check if a block is mined, used to calculate surrounding mines
	{
		return mined;
	}
	
	public boolean step()
	//When left click a block, return true for the game to be over, return false to change status
	{
		if(mined)
			return true;
		else
		{
			cleared = true;
			return false;
		}
	}
	
	public void mark()
	//When right click the block, if not already cleared, change display status
	{
		if(!cleared)
		{
			if(status == 0 || status == 1)
				status +=1;
			else
				status = 0;
		}
	}
	
	public int getStatus()
	//return the marked status of this block
	{
		return status;
	}
	
	public boolean checkCleared()
	//get the cleared or not
	{
		return cleared;
	}
}
