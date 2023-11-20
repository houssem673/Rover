package de.hhu.propra.rover.domain;

import java.util.function.UnaryOperator;

record Position(int x, int y) {

    public Position up() {
        return new Position(x, y - 1);
    }

    public Position down() {
        return new Position(x, y + 1);
    }

    public Position left() {
        return new Position(x - 1, y);
    }

    public Position right() {
        return new Position(x + 1, y);
    }
}
