import com.formdev.flatlaf.FlatDarkLaf;
import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.List;

public class GameGUI extends JFrame {
    private JTextPane outputPane;
    private JTextField inputField;
    private JButton sendButton;
    private JButton continueButton;

    // GUI używa tego żeby wysyłać rzeczy do System.in
    private PipedOutputStream pos;

    public GameGUI() {
        super("She culling my game till i GUI");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 1000);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);

        // Tutaj wyświetla się to co się dzieje
        outputPane = new JTextPane();
        outputPane.setEditable(false);

        StyledDocument doc = outputPane.getStyledDocument();
        SimpleAttributeSet centerAttr = new SimpleAttributeSet();
        StyleConstants.setAlignment(centerAttr, StyleConstants.ALIGN_CENTER);
        doc.setParagraphAttributes(0, doc.getLength(), centerAttr, false);

        Font font = new Font("Helvetica", Font.PLAIN, 17);
        outputPane.setFont(font);

        JScrollPane scrollPane = new JScrollPane(outputPane);
        add(scrollPane, BorderLayout.CENTER);

        // z tąd idzie na konsole
        JPanel inputPanel = new JPanel(new BorderLayout());
        inputField = new JTextField();
        inputPanel.add(inputField, BorderLayout.CENTER);

        // guziki na wysyłanie do konsoli i na wysyłanie "c"
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        sendButton = new JButton("Send");
        continueButton = new JButton("Continue");
        buttonPanel.add(sendButton);
        buttonPanel.add(continueButton);
        inputPanel.add(buttonPanel, BorderLayout.EAST);

        add(inputPanel, BorderLayout.SOUTH);

        // iniclalizacja pos
        pos = new PipedOutputStream();

        ActionListener inputListener = e -> sendInput();
        inputField.addActionListener(inputListener);
        sendButton.addActionListener(inputListener);


        continueButton.addActionListener(e -> {
            try {
                typeText("c");
                pos.write("c\n".getBytes());
                pos.flush();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
    }

    private void sendInput() {
        String text = inputField.getText();
        if (text != null) {
            try {
                typeText(text);
                // wysyła tekst i newline jakby by ł enterem
                pos.write((text + "\n").getBytes());
                pos.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
            inputField.setText("");  // Czyści po wysłaniu
        }
    }

    private void typeText(String text) {
        StyledDocument doc = outputPane.getStyledDocument();
        SimpleAttributeSet centerAttr = new SimpleAttributeSet();
        StyleConstants.setAlignment(centerAttr, StyleConstants.ALIGN_CENTER);
        try {
            doc.insertString(doc.getLength(), text + "\n", centerAttr);
            doc.setParagraphAttributes(doc.getLength() - (text.length() + 1), text.length() + 1, centerAttr, false);
            outputPane.setCaretPosition(doc.getLength());
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    // nie użyty ale nie usuwaj może się przyda potem
    public PipedOutputStream getPipedOutputStream() {
        return pos;
    }

    // Getter żeby można było zmieniać źródło dla outputarea
    public JTextPane getOutputPane() {
        return outputPane;
    }

    public static void main(String[] args) {

        try {
            UIManager.setLookAndFeel( new FlatDarkLaf() );
        } catch( Exception ex ) {
            System.err.println( "Failed to initialize LaF" );
        }


        try {
            // Tworzymy pipedstreamy, do pos pisze GUI, a pis zastąpuje System.in
            PipedOutputStream pos = new PipedOutputStream();
            PipedInputStream pis = new PipedInputStream(pos);
            System.setIn(pis);

            // GUI na EDT bo inaczej gra czekając na input blokuje GUI który może dac jej input XD
            SwingUtilities.invokeLater(() -> {
                GameGUI gui = new GameGUI();
                // GUI używa naszego pos a nie swojego
                gui.pos = pos;
                gui.setVisible(true);

                try {
                    pos.write("\n".getBytes());
                    pos.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                // dzięki temu output idzie na konsole I do GUI
                PrintStream guiOut = new PrintStream(new TextPaneOutputStream(gui.getOutputPane(), System.out));
                System.setOut(guiOut);
                System.setErr(guiOut);
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        // gra na innym wątku bo już mówiłem co
        new Thread(() -> {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            String line;
            try {
                // gra czyta z naszeg in
                while ((line = reader.readLine()) != null) {
                    List<Action> actionList = ActionLoader.loadActions("simple_actions.txt", "advanced_actions.txt", "modded_actions.txt");
                    List<TeamAction> teamActionList = ActionLoader.loadActions("team_actions.txt");

                    Game gameManager = new Game(actionList, teamActionList);

                    gameManager.setupCharacters();
                    gameManager.runGame();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }
}

// ten output stream po prosyu pisze na dwie rzeczy (nasze GUI + orginaln ą konsolę)
class TextPaneOutputStream extends OutputStream {
    private JTextPane textPane;
    private OutputStream console;
    private SimpleAttributeSet centerAttr;

    public TextPaneOutputStream(JTextPane textPane, OutputStream console) {
        this.textPane = textPane;
        this.console = console;
        centerAttr = new SimpleAttributeSet();
        StyleConstants.setAlignment(centerAttr, StyleConstants.ALIGN_CENTER);
    }

    // fundamentalna metoda outputstream, nic nie zmieniamy tylko dodajemy wypisywanie do GUI i autoscroll na dół
    @Override
    public void write(int b) throws IOException {
        console.write(b);
        String s = String.valueOf((char) b);
        try {
            StyledDocument doc = textPane.getStyledDocument();
            doc.insertString(doc.getLength(), s, centerAttr);
            doc.setParagraphAttributes(doc.getLength() - s.length(), s.length(), centerAttr, false);
        } catch (BadLocationException e) {
            throw new IOException(e);
        }
        SwingUtilities.invokeLater(() -> textPane.setCaretPosition(textPane.getDocument().getLength()));
    }

    // to samo tylko dla tablic byteów
    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        console.write(b, off, len);
        String text = new String(b, off, len);
        try {
            StyledDocument doc = textPane.getStyledDocument();
            doc.insertString(doc.getLength(), text, centerAttr);
            doc.setParagraphAttributes(doc.getLength() - text.length(), text.length(), centerAttr, false);
        } catch (BadLocationException e) {
            throw new IOException(e);
        }
        SwingUtilities.invokeLater(() -> textPane.setCaretPosition(textPane.getDocument().getLength()));
    }

    @Override
    public void flush() throws IOException {
        console.flush();
    }

    @Override
    public void close() throws IOException {
        console.close();
    }
}

