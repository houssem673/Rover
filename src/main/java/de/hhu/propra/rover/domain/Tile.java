package de.hhu.propra.rover.domain;

public class Tile {

    final boolean isGoal;
    final boolean isObstacle;
    Position myPosition;

    public Tile(boolean isGoal, boolean isObstacle) {
        this.isGoal = isGoal;
        this.isObstacle = isObstacle;
    }

    public boolean isGoal() {
        return isGoal;
    }

    public boolean isObstacle() {
        return isObstacle;
    }

    void setMyPosition(Position myPosition) {
        this.myPosition = myPosition;
    }

    public String htmlPrint(Position playerPosition){
        if(isObstacle){
            return "🍔";
        } else if(isGoal){
            return "🏁";
        } else if(playerPosition.equals(myPosition)){
            return "🛒";
        } else {
            return "🏻";
        }
    }
}
