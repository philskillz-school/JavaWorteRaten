import javax.swing.*;
import java.util.Arrays;
import java.awt.*;
import java.awt.event.*;

public class WorteRaten extends Frame
{

    byte maxTries = 10;
    byte length;
    char[] originalWord;
    char[] guessedWord;
    byte guessedCharacters;
    byte fails;

    Label instructionLabel;
    Label infoLabel;
    Label guessLabel;
    JPasswordField wordInput;
    TextField input;
    Button submitWord;
    Button submitGuess;


    public WorteRaten()
    {
        super("-----  Worte Raten -----");
        this.addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent e){
                dispose();
                System.exit(0);
            }
        });
        setLayout(null);

        instructionLabel = new Label("Geben Sie ein Wort ein");
        instructionLabel.setFont(new Font("arial", Font.BOLD, 14));
        instructionLabel.setBounds(650, 50, 250, 20);
        add(instructionLabel);

        infoLabel = new Label("x/x versuche");
        infoLabel.setFont(new Font("arial", Font.BOLD, 12));
        infoLabel.setBounds(650, 75, 250, 20);

        guessLabel = new Label("___");
        guessLabel.setFont(new Font("arial", Font.BOLD, 20));
        guessLabel.setBounds(100, 20, 500, 50);

        wordInput = new JPasswordField();
        wordInput.setBounds(650, 100, 150, 20);

        add(wordInput);

        input = new TextField();
        input.setBounds(650, 100, 150, 20);
        input.addTextListener(e -> inputEdit());

        submitWord = new Button("Fertig");
        submitWord.setBounds(650, 150, 150, 50);
        submitWord.addActionListener(e -> submitWord());
        add(submitWord);

        submitGuess = new Button("Raten");
        submitGuess.setBounds(650, 150, 150, 50);
        submitGuess.addActionListener(e -> guessCharacter());
        submitGuess.setEnabled(false);

    }

    void submitWord() {
        char[] word = wordInput.getPassword();
        if (word.length > 255) {
            System.out.println("Wort zu lang!");
            System.exit(1);
        }

        originalWord = word;
        length = (byte) word.length;
        guessedWord = new char[length];
        Arrays.fill(guessedWord, '_');

        guessedCharacters = 0;

        instructionLabel.setText("Erraten Sie Buchstaben...");
        updateUi();
        add(guessLabel);
        add(infoLabel);
        remove(wordInput);
        add(input);
        remove(submitWord);
        add(submitGuess);
    }

    void updateUi() {
        infoLabel.setText(fails + "/" + maxTries + " versuche");
        guessLabel.setText(String.join(" ", convertToStringArray(guessedWord)));
        updateHangman();
    }

    void updateHangman() {

    }

    void guessCharacter() {
        char guess = input.getText().charAt(0);
        boolean contained = evalGuess(guess);

        if (fails >= maxTries) {
            System.out.println("You failed!");
            updateUi();
        } else if (!contained) {
            System.out.println("Char not contained in word. " + fails + " fails");
            if (fails >= maxTries) {
                // lost
                submitGuess.setEnabled(false);
                input.setEnabled(false);
                instructionLabel.setText("Verloren");
                infoLabel.setText("Zu viele Fehlversuche");
            } else {
                updateUi();
            }
        } else {
            System.out.println("Success! Guessed " + guessedCharacters + "/" + length + " chars");
            if (guessedCharacters >= length) {
                // won
                submitGuess.setEnabled(false);
                input.setEnabled(false);
                instructionLabel.setText("Gewonnen!");
                infoLabel.setText("Sie hast das Wort erraten");
            }
            updateUi();
        }

        input.setText("");
        input.requestFocus(FocusEvent.Cause.ACTIVATION);

    }

    void inputEdit() {
        int length = input.getText().length();
        if (length < 1) {
            submitGuess.setEnabled(false);
        } else if (length == 1) {
            submitGuess.setEnabled(true);
        } else {
            input.setText(input.getText().substring(0, 1));
            input.setCaretPosition(1);
        }
    }

    private boolean evalGuess(char g) {
        boolean charContained = false;
        for (int i=0; i<length; i++) {
            if (originalWord[i] == g && guessedWord[i] != g) {
                charContained = true;
                guessedWord[i] = g;
                guessedCharacters++;
            }
        }

        if (!charContained) {
            fails++;
        }

        return charContained;
    }

    /*public void paint(Graphics g){
        g.fillRect(0, 0, 100, 100);
    }*/

    private boolean hasGuessedWord() {
        return guessedCharacters >= length;
    }

    public static String[] convertToStringArray(char[] charArray) {
        String[] stringArray = new String[charArray.length];
        for (int i = 0; i < charArray.length; i++) {
            // Convert each character to a string
            stringArray[i] = String.valueOf(charArray[i]);
        }
        return stringArray;
    }

    public static void main(String[]args) {
        WorteRaten m = new WorteRaten();
        m.setSize(1000,800);
        m.setVisible(true);
    }
}