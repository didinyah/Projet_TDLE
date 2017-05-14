package wikipediapckg.pageRank;

import java.io.IOException;

public interface IPageRanker {

	public void createPageRank(int nbIterations) throws IOException;
	
}
