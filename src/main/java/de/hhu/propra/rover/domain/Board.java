package de.hhu.propra.rover.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Board {

    final List<List<Tile>> tiles;
    final int width;
    final int height;
    Position playerPosition;
    boolean gameWon;

    public Board() {
        this(10, 10);
    }

    public Board(int width, int height) {
        this.tiles = new ArrayList<>(height);
        this.width = width;
        this.height = height;
        reset();
    }

    public void moveUp() {
        move(Position::up);
    }

    public void moveDown() {
        move(Position::down);
    }

    public void moveRight() {
        move(Position::right);
    }

    public void moveLeft() {
        move(Position::left);
    }

    private void move(UnaryOperator<Position> dir) {
        Position newPosition = dir.apply(playerPosition);
        Optional<Tile> target = getTile(newPosition);
        boolean obstacle = target
                .map(Tile::isObstacle)
                .orElse(true);
        if (!obstacle) {
            playerPosition = newPosition;
            if (target.get().isGoal()) {
                gameWon = true;
            }
        }
    }

    public Optional<Tile> getTile(Position position) {
        if (position.x() < 0 || position.x() >= width || position.y() < 0 || position.y() >= height) {
            return Optional.empty();
        }

        return Optional.of(tiles.get(position.y()).get(position.x()));
    }

    public void setTile(Position position, Tile tile) {
        if (position.x() < 0 || position.x() >= width || position.y() < 0 || position.y() >= height) {
            throw new IllegalArgumentException();
        }

        tiles.get(position.y()).set(position.x(), tile);
    }

    public List<List<Tile>> getTiles() {
        return tiles;
    }

    public Position getPlayerPosition() {
        return playerPosition;
    }

    private Position initBoard() {
        for (int y = 0; y < height; y++) {
            int finalY = y;
            tiles.add(y,
                    IntStream.range(0, width)
                            .mapToObj(x -> {
                                Tile tile = new Tile(false, false);
                                tile.setMyPosition(new Position(x, finalY));
                                return tile;
                            })
                            .collect(Collectors.toList())
            );
        }

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (Math.random() < 0.2) {
                    setTile(new Position(x, y), new Tile(false, true));
                }
            }
        }

        Random random = new Random();

        setTile(new Position(random.nextInt(width), random.nextInt(height)), new Tile(true, false));

        Position playerPos;
        do {
            playerPos = new Position(random.nextInt(width), random.nextInt(height));
        } while (getTile(playerPos).map(t -> t.isGoal() || t.isObstacle()).orElse(true));
        return playerPos;
    }

    public boolean isGameWon() {
        return gameWon;
    }

    public void reset() {
        tiles.clear();
        gameWon = false;
        playerPosition = initBoard();
    }
}
