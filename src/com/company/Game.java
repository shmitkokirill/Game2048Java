package com.company;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import java.io.*;
import java.util.Random;

import javax.swing.*;

public class Game {

    public Game(int rows)
    {
        this.rows = rows;
        File gameFile = new File(path);
        if (!gameFile.exists()) {
            arr = new Integer[rows][rows];
            reset();
            showPoint(2);
        } else {
            arr = loadStateFromFile();
        }
        initUI();
        updateScreen();
    }

    private void initUI() // interface
    {
        JFrame jframe = new JFrame(MES_TITLE);
        // show in point (200, 200) with width 500 and height 550
        jframe.setBounds(200, 200, 500, 550);
        JPanel jp = new JPanel();

        jframe.setContentPane(jp);
        jp.setLayout(new BorderLayout());

        showScore.setText(MES_CUR_SCORE + 0);
        showScore.setFont(new Font(fontName, 0, 30));

        showArr.setLayout(new GridLayout(rows, rows));
        showArr.setBackground(Color.WHITE);

        newGame = new JButton("New Game");
        newGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                reset();
                showPoint(2);
            }
        });
        newGame.setFocusable(false);

        jp.add(newGame, BorderLayout.SOUTH);
        jp.add(showScore, BorderLayout.NORTH);
        jp.add(showArr, BorderLayout.CENTER);

        jframe.setVisible(true);
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jframe.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (false == moveArray(e.getKeyCode())) {
                    if (true == isEnd()) {
                        int option = JOptionPane.showConfirmDialog(
                                null,
                                MES_END
                        );
                        if (JOptionPane.OK_OPTION == option) {
                            reset();
                            showPoint(2);
                        }
                    }
                } else
                    showPoint(1);
            }
        });
        jframe.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
               saveStateToFile(arr);
            }
        });

    }
    // clear the array
    private void reset() {
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.rows; j++) {
                arr[i][j] = 0;
            }
        }
        score = 0;
        showScore.setText(MES_CUR_SCORE + 0);
    }

    /* show new squares */
    private void showPoint(int number) {
        Random  rand = new Random();
        for (int i = 0; i < number; ) {
            int x = rand.nextInt(rows);
            int y = rand.nextInt(rows);
            if (0 == arr[x][y]) {
                arr[x][y] = 2;
                i++;
            }
        }
        updateScreen();
    }

    /* Обновить отображение showArr */
    private void updateScreen() {
        showArr.removeAll();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < rows; j++) {
                Integer temp = arr[i][j];
                JLabel jl = new JLabel(
                        0 == temp ? "" : temp.toString(),
                        SwingConstants.CENTER
                );
                jl.setFont(new Font(fontName, 0, 33));
                jl.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                showArr.add(jl);
            }
        }
        showArr.updateUI();
        showScore.setText(MES_CUR_SCORE + score);
    }

    /* move elements */
    private boolean moveArray(int direction) {
        boolean moveFlag = false;
        switch (direction)
        {
            case LEFT:
                for (int y = 1; y < rows; y++) {
                    for (int x = 0; x < rows; x++) {
                        if (0 != arr[x][y]) {
                            for (int k = y - 1; k >= 0; k--) {
                                if (0 != arr[x][k]) {
                                    if (arr[x][k].equals(arr[x][k + 1])) {
                                        arr[x][k] = arr[x][k] * 2;
                                        score = score + arr[x][k]; // contain
                                        System.out.println(arr[x][k]);
                                        arr[x][k + 1] = 0;
                                        moveFlag = true;
                                        break;
                                    }
                                } else {
                                    arr[x][k] = arr[x][k + 1];
                                    arr[x][k + 1] = 0;
                                    moveFlag = true;
                                }
                            }
                        } else
                            continue;
                    }
                }
                break;
            case UP:
                for (int x = 1; x < rows; x++) {
                    for (int y = 0; y < rows; y++) {
                        if (0 != arr[x][y]) {
                            for (int k = x - 1; k >= 0; k--) {
                                if (0 != arr[k][y]) {
                                    if (arr[k][y].equals(arr[k + 1][y])) {
                                        arr[k][y] = arr[k][y] * 2;
                                        score = score + arr[k][y]; // contain
                                        arr[k + 1][y] = 0;
                                        moveFlag = true;
                                        break;
                                    }
                                } else {
                                    arr[k][y] = arr[k + 1][y];
                                    arr[k + 1][y] = 0;
                                    moveFlag = true;
                                }
                            }
                        } else
                            continue;
                    }
                }
                break;
            case RIGHT:
                for (int y = 2; y >= 0; y--) {
                    for (int x = 0; x < rows; x++) {
                        if (0 != arr[x][y]) {
                            for (int k = y + 1; k < rows; k++) {
                                if (0 != arr[x][k]) {
                                    if (arr[x][k].equals(arr[x][k - 1])) {
                                        arr[x][k] = arr[x][k] * 2;
                                        score = score + arr[x][k]; // contain
                                        arr[x][k - 1] = 0;
                                        moveFlag = true;
                                        break;
                                    }
                                } else {
                                    arr[x][k] = arr[x][k - 1];
                                    arr[x][k - 1] = 0;
                                    moveFlag = true;
                                }
                            }
                        } else
                            continue;
                    }
                }
                break;
            case DOWN:
                for (int x = 2; x >= 0; x--) {
                    for (int y = 0; y < rows; y++) {
                        if (0 != arr[x][y]) {
                            for (int k = x + 1; k < rows; k++) {
                                if (0 != arr[k][y]) {
                                    if (arr[k][y].equals(arr[k - 1][y])) {
                                        arr[k][y] = arr[k][y] * 2;
                                        score = score + arr[k][y]; // contain
                                        arr[k - 1][y] = 0;
                                        moveFlag = true;
                                        break;
                                    }
                                } else {
                                    arr[k][y] = arr[k - 1][y];
                                    arr[k - 1][y] = 0;
                                    moveFlag = true;
                                }
                            }
                        } else
                            continue;
                    }
                }
                break;
        }
        updateScreen();
        return moveFlag;
    }

    /* can array move? */
    private boolean isMove() {
        boolean moveFlag = false;

        for (int y = 1; y < rows; y++) {
            for (int x = 0; x < rows; x++) {
                if (0 != arr[x][y]) {
                    for (int k = y - 1; k >= 0; k--) {
                        if (0 != arr[x][k]) {
                            if (arr[x][k].equals(arr[x][k + 1]))
                                return true;
                        } else
                            return true;
                    }
                } else
                    continue;
            }
        }

        for (int x = 1; x < rows; x++) {
            for (int y = 0; y < rows; y++) {
                if (0 != arr[x][y]) {
                    for (int k = x - 1; k >= 0; k--) {
                        if (0 != arr[k][y]) {
                            if (arr[k][y].equals(arr[k + 1][y]))
                                return true;
                        } else
                            return true;
                    }
                } else
                    continue;
            }
        }

        for (int y = 2; y >= 0; y--) {
            for (int x = 0; x < rows; x++) {
                if (0 != arr[x][y]) {
                    for (int k = y + 1; k < rows; k++) {
                        if (0 != arr[x][k]) {
                            if (arr[x][k].equals(arr[x][k - 1]))
                                return true;
                        } else
                            return true;
                    }
                } else
                    continue;
            }
        }

        for (int x = 2; x >= 0; x--) {
            for (int y = 0; y < rows; y++) {
                if (0 != arr[x][y]) {
                    for (int k = x + 1; k < rows; k++) {
                        if (0 != arr[k][y]) {
                            if (arr[k][y].equals(arr[k - 1][y])) {
                                return true;
                            }
                        } else
                            return true;
                    }
                } else
                    continue;
            }
        }

        return moveFlag;
    }

    /* if array is full - finish the game */
    private boolean isEnd() {

        boolean fullFlag = true;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < rows; j++) {
                if (0 == arr[i][j]) {
                    fullFlag = false;
                    break;
                }
            }
            if (false == fullFlag) {
                break;
            }
        }
        if (fullFlag)
        {
            if (isMove())
                return false;
            else
                return true;
        } else
            return false;
    }
    private void saveStateToFile(Integer[][] arr) {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(path));
            bw.write(String.valueOf(score));
            bw.newLine();
            bw.write(String.valueOf(arr.length));
            bw.newLine();
            bw.write(String.valueOf(arr[0].length));
            bw.newLine();
            for (int i = 0; i < arr.length; i++) {
                for (int j = 0; j < arr[0].length; j++) {
                    bw.write(String.valueOf(arr[i][j]));
                    bw.newLine();
                }
            }
            bw.flush();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private Integer[][] loadStateFromFile() {
        Integer[][] arr = null;
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            score = Integer.parseInt(br.readLine());
            int rows = Integer.parseInt(br.readLine());
            int cols = Integer.parseInt(br.readLine());
            arr = new Integer[rows][cols];
            for (int i = 0; i < arr.length; i++) {
                for (int j = 0; j < arr[0].length; j++) {
                    arr[i][j] = Integer.parseInt(br.readLine());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return arr;
    }

    private Integer[][] arr = null; // array of interface
    private JPanel showArr = new JPanel();
    private JLabel showScore = new JLabel();
    private int score = 0;
    private int rows; // matrix size
    private String fontName = "Microsoft Yahei";

    private String path = "savedGameState.txt";

    // MESSAGES
    private static String MES_END = "Игра окончена. Начать сначала?";
    private static String MES_TITLE = "2048";
    private static String MES_CUR_SCORE = "Текущий счет: ";
    // ---

    // Directions
    private static final int LEFT = 37;
    private static final int UP = 38;
    private static final int RIGHT = 39;
    private static final int DOWN = 40;
    // ---

    JButton newGame;
}
