package com.sokolovskyi.jasm.ide;

import com.sokolovskyi.jasm.ide.components.NoteCompile;
import com.sokolovskyi.jasm.ide.components.fileMenu.NoteOpenFile;
import com.sokolovskyi.jasm.ide.components.helpMenu.NoteAbout;
import com.sokolovskyi.jasm.ide.components.ideErrors.NoteError;
import com.sokolovskyi.jasm.ide.components.panelComponents.NoteLineNumbering;
import com.sokolovskyi.jasm.ide.components.settingsMenu.NoteColors;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

class NoteFrame extends JFrame {
    //menu bar
    private JMenuBar menuBar = new JMenuBar();
    //text area
    private JTextArea theText;
    private NoteLineNumbering noteLineNumbering;
    //fonts
    private Font font;

    private JMenuItem menuOpen;
    private JMenuItem menuSave;
    private JMenuItem menuExit;

    private JMenuItem menuAbout;

    private JMenuItem menuCompile;

    private JMenuItem menuFont;

    //components of IDE
    private NoteAbout noteAbout;
    private NoteOpenFile noteOpenFile;
    private NoteColors noteColors;

    //errors of IDE
    private NoteError errorEmptyText;
    private NoteError errorFileCanNotOpen;


     NoteFrame() {
        super("JASM");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //init ide
        initTextArea();
        initFonts();
        initMenu();
        initMainPanel();
        initListeners();

        //init components of ide
        initComponents();

        //set last file
        noteOpenFile.initLastFile();
        //set last color theme
        noteColors.initLastColors();

        setVisible(true);
    }

    private void initFonts(){
        font = new Font("Courier New", Font.PLAIN, 14);
    }

    private void initTextArea(){
        theText = new JTextArea();

        noteLineNumbering = new NoteLineNumbering(theText);
        noteLineNumbering.updateLineNumbers();

        //set tab size
        theText.setTabSize(2);
        //set cursor width
        theText.putClientProperty("caretWidth", 3);

        theText.setFont( new Font("Courier New", Font.PLAIN, 14));
    }

    private void initComponents(){
        //components of IDE
        noteAbout = new NoteAbout(this);
        noteOpenFile = new NoteOpenFile(theText, errorFileCanNotOpen);

        //errors of IDE
        errorEmptyText = new NoteError(this, "Error! Empty file!");
        errorFileCanNotOpen = new NoteError(this, "Can't open file");
    }

    private void finalAction(){
         //save last colors
        noteColors.saveLastColors();
    }

    @SuppressWarnings("Duplicates")
    private void initMenu() {
        //init file menu
        //file menu
        JMenu fileMenu = new JMenu("File");
        fileMenu.setFont(font);


        menuOpen = new JMenuItem("open");
        menuOpen.setFont(font);
        fileMenu.add(menuOpen);
        menuSave = new JMenuItem("save");
        menuSave.setFont(font);
        fileMenu.add(menuSave);

        fileMenu.addSeparator();
        menuExit = new JMenuItem("exit");
        menuExit.setFont(font);
        fileMenu.add(menuExit);


        //init help menu
        //help menu
        JMenu helpMenu = new JMenu("Help");
        helpMenu.setFont(font);

        menuAbout = new JMenuItem("about");
        menuAbout.setFont(font);
        helpMenu.add(menuAbout);


        //init tools menu
        //tools menu
        JMenu toolsMenu = new JMenu("Tools");
        toolsMenu.setFont(font);

        menuCompile = new JMenuItem("run >>\t");
        menuCompile.setFont(font);
        toolsMenu.add(menuCompile);


        //init setting menu
        //settings menu
        JMenu settingsMenu = new JMenu("Settings");
        settingsMenu.setFont(font);

        menuFont = new JMenuItem("font");
        menuFont.setFont(font);
        settingsMenu.add(menuFont);

        //menu of color scheme
        JMenu menuColorScheme = new JMenu("color scheme");
        menuColorScheme.setFont(font);

        //init colors
        noteColors = new NoteColors(theText, noteLineNumbering, font, menuColorScheme);


        settingsMenu.add(menuColorScheme);

        menuBar.add(fileMenu);
        menuBar.add(helpMenu);
        menuBar.add(toolsMenu);
        menuBar.add(settingsMenu);


        setJMenuBar(menuBar);

        //FIXME trouble with icon, resizing
        //setIconImage(new ImageIcon("./src/asserts/logo.png").getImage());

        setPreferredSize(new Dimension(270, 225));
        pack();
        setLocationRelativeTo(null);

        setBounds( IDEParams.CORD_X_MAIN_WIN, IDEParams.CORD_Y_MAIN_WIN, 700, 700);
    }

    private void initMainPanel() {
        JScrollPane spane = new JScrollPane(theText);
        spane.setRowHeaderView(noteLineNumbering);

        JPanel panel = new JPanel(new BorderLayout());

        panel.add(spane, BorderLayout.CENTER);
        getContentPane().add(panel);
    }

    private void initListeners() {
        //Listener of main window
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                noteColors.saveLastColors();
            }
        });

        //Listeners of textAre
        theText.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent documentEvent) {
                noteLineNumbering.updateLineNumbers();
            }

            @Override
            public void removeUpdate(DocumentEvent documentEvent) {
                noteLineNumbering.updateLineNumbers();
            }

            @Override
            public void changedUpdate(DocumentEvent documentEvent) {
                noteLineNumbering.updateLineNumbers();
            }
        });

        //Listeners of file menu
        menuExit.addActionListener(actionEvent -> {
            finalAction();
            System.exit(0);
        });

        menuSave.addActionListener(actionEvent -> {

        });

        menuOpen.addActionListener(actionEvent -> noteOpenFile.setChoosenFile());


        //Listeners of help menu
        menuAbout.addActionListener(actionEvent -> noteAbout.setVisible(true));


        //Listeners of tools menu
        menuCompile.addActionListener(actionEvent -> {
            //FIXME create new algorithm
            if(theText.getText().isEmpty()){
                errorEmptyText.setVisible(true);
            }
            else{
               new NoteCompile(font, theText.getText());
            }
        });


        //Listeners of settings menu
        menuFont.addActionListener(actionEvent -> {

        });

    }
}
