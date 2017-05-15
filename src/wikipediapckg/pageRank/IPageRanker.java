package wikipediapckg.pageRank;

import java.io.IOException;

public interface IPageRanker {

	public void createPageRank(final int nbIterations,final double damping) throws IOException;
	
}
