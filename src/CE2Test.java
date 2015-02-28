import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.io.File;

import org.junit.Test;

public class CE2Test {

	@Test
	public void reset() {
		
		CE2.fileName= "mytextfile.txt";
		CE2.textFile = new File(CE2.fileName);
		CE2.loadFileToList();
		CE2.clear();
		System.out.println(CE2.itemList.size());
		assertEquals(CE2.itemList.size(), 0);
		
		CE2.add("add t");
		CE2.add("add at");
		CE2.add("add a t");
		List<String> x = new ArrayList<String>();
		x.add("a t");
		x.add("at");
		x.add("t");
		CE2.sort();
		assertEquals(CE2.itemList, x);
		
		CE2.search("search t"); //Should output all 3 elements
		CE2.search("search a"); //Should output only first 2 elements
		CE2.search("search at");//Should output only 2
		CE2.search("search AT");//Should output only 2
		CE2.search("search At");//Should output only 2
		CE2.search("search aT");//Should output only 2

		CE2.search("search ATT");//Should output "Sorry,no items matched in the search term."
		
		//CE2.display();
	}
	
	

}
