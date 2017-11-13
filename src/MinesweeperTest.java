import junit.framework.*;

import static org.junit.Assert.*;
 
import org.junit.Test;

public class MinesweeperTest{
	
	
	Game test_Game = new Game();
	
	Block test_block = new Block(4, 5);
	
	Block test_block2 = new Block(1, 2);
	
	
	@Test
	public void test_checkMine()
	{
		assertEquals("check mine at the start: ", test_block.checkMined(), false); 
	}

	@Test
	public void test_checkCleared()
	{
		assertEquals("check cleared at the start: ", test_block.checkCleared(), false); 	
	}
	
	@Test
	public void test_checkStatus()
	{
		assertEquals("check status  at the start: ", test_block.getStatus(), 0); 	
	}
	
	@Test
	public void test_checkMined()
	{
		test_block2.mine();
		assertEquals("check mine after mined: ", test_block2.checkMined(), true); 	
	}
	@Test
	public void test_checkStepnoMined()
	{
		assertEquals("check step no mined: ", test_block2.step(), false); 	
	}
	@Test
	public void test_checkSteponMined()
	{
		test_block2.mine();
		assertEquals("check step after mined: ", test_block2.step(), true); 	
	}
	@Test
	public void test_checkStepNotMinedCleared()
	{
		test_block2.mine();
		assertEquals("check cleared after mine: ", test_block2.checkCleared(), false); 	
	}
	@Test
	public void test_checkMarkStatus1()
	{
		test_block.mark();
		assertEquals("status after a marks: ", test_block.getStatus(), 1); 	
	}
	@Test
	public void test_checkMarkStatus2()
	{
		test_block.mark();
		test_block.mark();
		assertEquals("status after 2 marks: ", test_block.getStatus(), 2); 	
	}
	@Test
	public void test_checkMarkStatus0After3marks()
	{
		test_block.mark();
		test_block.mark();
		test_block.mark();
		assertEquals("status after 3 marks: ", test_block.getStatus(), 0); 	
	}
	@Test
	public void test_GameBlockStatus12()
	{
		assertEquals("Total mines at start: ", test_Game.remainingMines(), 10);
	}
	@Test
	public void check_around()
	{
		int around = test_Game.calculateAround(1, 1);
		assertEquals("checking around: ", test_Game.calculateAround(1, 1), around);
	}
	@Test
	public void check_uncleared()
	{
		assertEquals("Checking the number of uncleared boxes: ", test_Game.getUncleared(), 100);
	}
	@Test
	public void check_display1rightclick()
	{
		test_Game.rightClick(1, 2);
		assertEquals("Checking what displays after one right click: ", test_Game.getDisplay(1, 2), "M");
	}
	@Test
	public void check_display2rightclicks()
	{
		test_Game.rightClick(1, 2);
		test_Game.rightClick(1, 2);
		assertEquals("Checking what displays after two right click: ", test_Game.getDisplay(1, 2), "?");
	}
	@Test
	public void check_display3rightclicks()
	{
		test_Game.rightClick(1, 2);
		test_Game.rightClick(1, 2);
		test_Game.rightClick(1, 2);
		assertEquals("Checking what displays after two right click: ", test_Game.getDisplay(1, 2), "");
	}
	
	
}