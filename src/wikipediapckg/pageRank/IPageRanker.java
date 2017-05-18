package wikipediapckg.pageRank;

import java.io.IOException;

import main.ResultDTO;

public interface IPageRanker {

	public ResultDTO createPageRank(final int nbIterations,final double damping) throws IOException;
	
}
