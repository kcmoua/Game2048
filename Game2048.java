package com.codegym.games.game2048;

import com.codegym.engine.cell.*;
import java.util.Arrays;

public class Game2048 extends Game {
    
    private static final int SIDE = 4;
    private int score = 0;
    private int[][] gameField = new int[SIDE][SIDE];
    private boolean isGameStopped = false;
    
    private void createGame() {
        gameField = new int[SIDE][SIDE];
        createNewNumber();
        createNewNumber();
        score = 0;
        setScore(score);
    }
    
    private void drawScene() {
        for(int i=0;i<SIDE;i++) {
            for(int j=0;j<SIDE;j++) {
                setCellColoredNumber(i, j, gameField[j][i]);
            }
        }
    }
    
    private void createNewNumber() {
        int x = getRandomNumber(SIDE);
        int y = getRandomNumber(SIDE);
        while(gameField[x][y]!=0) {
            x = getRandomNumber(SIDE);
            y = getRandomNumber(SIDE);
        }
        
        int assignValue = getRandomNumber(10);
        if(assignValue==9) {
            gameField[x][y] = 4;
        } else {
            gameField[x][y] = 2;
        }
        
        if(getMaxTileValue() == 2048) {
            win();
        }
    }
    
    private Color getColorByValue(int value) {
        if (value == 0){
            return Color.WHITE;
        } else if (value == 2){
            return Color.BLUE;
        } else if (value == 4){
            return Color.GRAY;
        } else if (value == 8){
            return Color.AQUA;
        } else if (value == 16){
            return Color.AQUAMARINE;
        } else if (value == 32){
            return Color.GREEN;
        } else if (value == 64){
            return Color.BEIGE;
        } else if (value == 128){
            return Color.YELLOW;
        } else if (value == 256){
            return Color.BLACK;
        } else if (value == 512){
            return Color.PINK;
        } else if (value == 1024){
            return Color.ORANGE;
        } else if (value == 2048) {
            return Color.PURPLE;
        } else {
            return Color.WHITE;
        }
    }
    
    private void setCellColoredNumber(int x, int y, int value) {
        if(value==0) {
            setCellValueEx(x, y, getColorByValue(value), "");
        } else {
            setCellValueEx(x, y, getColorByValue(value), Integer.toString(value));
        }
    }
    
    private boolean compressRow(int[] row) {
        int temp = 0;
        boolean didChange = false;
        int[] rowTemp = row.clone();
        for(int i=0;i<SIDE-1;i++) {
            for(int j=0;j<SIDE-i-1;j++) {
                if(row[j]==0) {
                    temp = row[j+1];
                    row[j+1] = row[j];
                    row[j] = temp;
                }
            }
        }
        if(Arrays.equals(row, rowTemp)) {
            didChange = false;
        } else {
            didChange = true;
        }
        return didChange;
    }
    
    private boolean mergeRow(int[] row) {
        boolean didChange = false;
        for(int i=0;i<SIDE-1;i++) {
            if(row[i]>0 && row[i]==row[i+1]) {
                row[i] = row[i] * 2;
                row[i+1] = 0;
                didChange = true;
                score = score + row[i];
                setScore(score);
            }
        }
        return didChange;
    }
    
    private void moveLeft() {
        boolean didRowCompress = false;
        boolean didRowMerge = false;
        boolean moveOccurred = false;
        for(int i=0;i<SIDE;i++) {
            didRowCompress = compressRow(gameField[i]);
            didRowMerge = mergeRow(gameField[i]);
            compressRow(gameField[i]);
            if(didRowCompress || didRowMerge) {
                moveOccurred=true;
            }
        }
        if(moveOccurred) {
            createNewNumber();
        }
    }
    
    private void moveRight() {
        rotateClockwise();
        rotateClockwise();
        moveLeft();
        rotateClockwise();
        rotateClockwise();
    }
    
    private void moveUp() {
        rotateClockwise();
        rotateClockwise();
        rotateClockwise();
        moveLeft();
        rotateClockwise();
    }
    
    private void moveDown() {
        rotateClockwise();
        moveLeft();
        rotateClockwise();
        rotateClockwise();
        rotateClockwise();
    }
    
    private void rotateClockwise() {
        for(int i=0;i<SIDE/2;i++) { 
            for(int j=i;j<SIDE-i-1;j++) { 
                int temp = gameField[i][j]; 
                gameField[i][j] = gameField[SIDE-1-j][i]; 
                gameField[SIDE-1-j][i] = gameField[SIDE-1-i][SIDE-1-j]; 
                gameField[SIDE-1-i][SIDE-1-j] = gameField[j][SIDE-1-i]; 
                gameField[j][SIDE-1-i] = temp; 
            } 
        }
    }
    
    private int getMaxTileValue() {
        int max = 0;
        for(int i=0;i<SIDE;i++) {
            for(int j=0;j<SIDE;j++) {
                if(gameField[i][j]>max) {
                    max = gameField[i][j];
                }
            }
        }
        return max;
    }
    
    private void win() {
        isGameStopped = true;
        showMessageDialog(Color.PURPLE, "2048", Color.PURPLE, 2048);
    }
    
    private boolean canUserMove() {
        boolean canMove = false;
        for(int i=0;i<SIDE;i++) {
            for(int j=0;j<SIDE;j++) {
                if(gameField[i][j] == 0) {
                    canMove = true;
                }
                if((i-1)>0 && (gameField[i][j] == gameField[i-1][j])) {
                    canMove = true;
                }
                if((i+1)<SIDE && (gameField[i][j] == gameField[i+1][j])) {
                    canMove = true;
                }
                if((j+1)<SIDE && (gameField[i][j] == gameField[i][j+1])) {
                    canMove = true;
                }
                if((j-1)>0 && (gameField[i][j] == gameField[i][j-1])) {
                    canMove = true;
                }
            }
        }
        return canMove;
    }
    
    private void gameOver() {
        isGameStopped = true;
        int max = getMaxTileValue();
        showMessageDialog(Color.PURPLE, "2048", Color.PURPLE, 2048);
    }
    
    @Override
    public void initialize() {
        setScreenSize(SIDE, SIDE);
        createGame();
        drawScene();
    }
    
    @Override
    public void onKeyPress(Key key) {
        if(isGameStopped==true) {
            if(key == Key.SPACE) {
                isGameStopped = false;
                createGame();
                drawScene();
            }
        } else if(canUserMove()==false) {
            gameOver();
        } else if(key == Key.LEFT) {
            moveLeft();
            drawScene();
        } else if(key == Key.RIGHT) {
            moveRight();
            drawScene();
        } else if(key == Key.UP) {
            moveUp();
            drawScene();
        } else if(key == Key.DOWN) {
            moveDown();
            drawScene();
        }
    }
    
}