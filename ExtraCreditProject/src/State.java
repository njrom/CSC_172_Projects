import java.util.ArrayList;

public class State {
    Graph graph;
    ArrayList<Vertex> possibleMoves = new ArrayList<>();
    private int whosTurn; // 0 cop1, 1 Robber, 2 cop2
    public int utility;

    public State(Graph g, int t){
        this.graph = (Graph) g.clone();
        this.whosTurn = t;
        this.loadPossibleMoves();
    }
    private void loadPossibleMoves(){
        if(whosTurn == 0){
            Cop  cop = graph.cop1;
            possibleMoves = cop.vertexOn.edges;
        } else if(whosTurn == 1){
            Robber rob = graph.robber;
            possibleMoves = rob.vertexOn.edges;
        } else if(whosTurn == 2){
            Cop  cop = graph.cop2;
            possibleMoves = cop.vertexOn.edges;
        }
    }
    public State result(Vertex move){
        if(whosTurn == 0){ // Cop1
            graph.cop1.vertexOn = move;
            return new State(graph.clone(), 1);

        }
        if(whosTurn == 1){ // Robber
            graph.robber.vertexOn = move;
            return new State(graph.clone(), 2);
        }
        if(whosTurn == 2){ //Cop2
            graph.cop2.vertexOn = move;
            return new State(graph.clone(), 0);
        }
        return null;
    }

    public boolean isTerminal(){
        if(graph.isGameOver()){
            return true;
        }
        return false;
    }

    public boolean isMaxPlayerTurn(){
        return this.whosTurn == 1;
    }
    public boolean isMinPlayerTurn(){
        return this.whosTurn == 0 || this.whosTurn == 2;
    }
}
