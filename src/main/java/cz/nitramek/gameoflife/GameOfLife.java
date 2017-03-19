package cz.nitramek.gameoflife;


import lombok.Getter;

import static java.lang.Math.sqrt;

public class GameOfLife {

    @Getter
    private int[] generation;
    private int[] nextGenerationBuffer;
    private final int oneDimensionSize;

    public GameOfLife(int oneDimensionSize) {
        this.generation = new int[oneDimensionSize * oneDimensionSize];
        this.nextGenerationBuffer = new int[oneDimensionSize * oneDimensionSize];
        this.oneDimensionSize = oneDimensionSize;
    }
    public GameOfLife(int[] generation) {
        this.generation = generation;
        this.nextGenerationBuffer = new int[generation.length];
        this.oneDimensionSize = (int) sqrt(generation.length);
    }

    public void advance() {
        for (int y = 0; y < oneDimensionSize; y++) {
            for (int x = 0; x < oneDimensionSize; x++) {
                advanceSingleCell(x, y);
            }
        }
        System.arraycopy(nextGenerationBuffer, 0, generation, 0, generation.length);
    }


    private void advanceSingleCell(int x, int y) {
        int cellState = generation[pos(x, y)];
        int aliveAroundCount = countAliveAround(x, y);
        if (cellState == 1) {
            if (aliveAroundCount == 2 || aliveAroundCount == 3) {
                markNextGenerationAlive(x, y);
            } else {
                markNextGenerationDead(x, y);
            }
        } else {
            if (aliveAroundCount == 3) {
                markNextGenerationAlive(x, y);
            } else {
                markNextGenerationDead(x, y);
            }
        }
    }


    private int pos(int x, int y) {
        return y * oneDimensionSize + x;
    }

    public void switchState(int x, int y) {
        generation[pos(x, y)] ^= 1;
    }

    public int getState(int x, int y) {
        return generation[pos(x, y)];
    }

    public void markNextGenerationAlive(int x, int y) {
        nextGenerationBuffer[pos(x, y)] = 1;
    }

    public void markNextGenerationDead(int x, int y) {
        nextGenerationBuffer[pos(x, y)] = 0;
    }

    private int countAliveAround(int x, int y) {
        int aliveCount = 0;
        for (int xOffset = -1; xOffset <= 1; xOffset++) {
            for (int yOffset = -1; yOffset <= 1; yOffset++) {
                if (yOffset != 0 || xOffset != 0) {
                    int xOffseted = x + xOffset;
                    int yOffseted = y + yOffset;
                    if (xOffseted > 0 && yOffseted > 0 && xOffseted < oneDimensionSize && yOffseted <
                            oneDimensionSize) {
                        aliveCount += generation[pos(xOffseted, yOffseted)];
                    }
                }
            }
        }
        return aliveCount;
    }
}
