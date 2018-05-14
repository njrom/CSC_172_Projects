public class Minimax {
    private final int CUTOFF;
    public Minimax(int cf){
        this.CUTOFF = cf;
    }

    public Vertex miniMaxAction(State s){
        // For Max Player
        // n = maxValue(s)
        // Want to return the possible move with which n was the value
       Vertex maxMove = null;
       int maxVal = Integer.MIN_VALUE;
       Vertex current = s.graph.robber.vertexOn;
       for(Vertex v: current.edges){
           int vVal = minValue(s.result(v), 0);
           if(vVal >= maxVal){
               maxVal = vVal;
               maxMove = v;
           }
       }
       return maxMove;
    }

    private int maxValue(State s, int depth){
        if(s.isTerminal()){ // If Game ends with Cop catching robber
            return s.utility;
        }
        if(depth > CUTOFF){ //If we go down the game tree to a height greater than the cutoff
            return s.utility+1;
        }
        int minUtil = Integer.MIN_VALUE;
        for(Vertex v : s.possibleMoves){
            int currentMin = minValue(s.result(v), depth + 1);
            if(minUtil < currentMin)
                minUtil = currentMin;
        }
        return minUtil;
    }

    private int minValue(State s, int depth){
        if(s.isTerminal()){ // If Game ends with Cop catching robber
            return s.utility;
        }
        if(depth > CUTOFF){ //If we go down the game tree to a height greater than the cutoff
            return s.utility+1;
        }
        int maxUtil = Integer.MAX_VALUE;
        for(Vertex v : s.possibleMoves){
            int currentMax = maxValue(s.result(v), depth + 1);
            if(maxUtil > currentMax)
                maxUtil = currentMax;
        }
        return maxUtil;
    }

}
