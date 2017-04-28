package data;

public class Stat {

    private int depth;
    private int searchCost;
    private int nodesGenerated;
    private double completionTime;

    public Stat(int depth, int searchCost, int nodesGenerated, double completionTime) {
        this.depth = depth;
        this.searchCost = searchCost;
        this.nodesGenerated = nodesGenerated;
        this.completionTime = completionTime;
    }

    public int getDepth() {
        return depth;
    }

    public int getSearchCost() {
        return searchCost;
    }

    public int getNodesGenerated() {
        return nodesGenerated;
    }

    public double getCompletionTime() {
        return completionTime;
    }

}
