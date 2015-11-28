/*
 * LAN Battleship
 * Copyright (c) 2015, spiralhalo
 * You are free to modify and reuse this program code for commercial and non-commercial purpose
 * with the condition that you removed this license header or replaced it with your own.
 */
package lbs.mvcn.view;

import javax.swing.JFrame;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JButton;
import lbs.mvcn.controller.IViewController;
import lbs.mvcn.model.IModel;
import lbs.mvcn.controller.ButtonEnum;
/**
 *
 * @author nanashi95
 */
public class MainView extends JFrame implements IView, ActionListener, MouseListener{

    private IViewController controller;
    private IModel model;
    private JButton gridButton[][];
    private CardLayout cards;
    
    private final Color NavyBlue = new Color(40,44,77);
    private final Color SkyBlue = new Color(80,151,230);
    
    private final String placingHint =
              "<html>Hint: Click on a tile to place"
            + "<br/>your ship. Press 'R' button to"
            + "<br/>rotate your ship before placing.";
    
    private String currentCard;
    
    /**
     * Creates new form Main
     */
    public MainView() {
        initComponents();
        setLocationRelativeTo(null);
        
        left.setBackground(Color.lightGray);
        middle.setBackground(Color.lightGray);
        right.setBackground(Color.lightGray);
        self.setBackground(Color.lightGray);
        
        cards = (CardLayout)carder.getLayout();
    }
    
    private void showCard(String cardName) {
        currentCard = cardName;
        cards.show(carder, cardName);
    }
    
    @Override
    public void setModel(IModel model){
        this.model = model;
        
        model.createOrUpdateRooster();
        tableRooster.setModel(new FixedLengthTableModel( model.getRoosterData(), new String[]{"IP Address", "Player Name", "Ready"}));
        ((FixedLengthTableModel)tableRooster.getModel()).fireTableDataChanged();
    }
    
    @Override
    public void setController(IViewController controller) {
        this.controller =  controller;
        
        buttonAcceptName.addActionListener(this);
        buttonChangeName.addActionListener(this);
        buttonCreate.addActionListener(this);
        buttonJoin.addActionListener(this);
        buttonLeave.addActionListener(this);
        buttonReady.addActionListener(this);
        buttonStart.addActionListener(this);
        buttonServerNameAccept.addActionListener(this);
        buttonHostnameAccept.addActionListener(this);
        buttonX.addActionListener(this);
        buttonX1.addActionListener(this);
        textHostname.addMouseListener(this);
        textServerName.addMouseListener(this);
    }
    
    @Override
    public void diplay(){
        this.setVisible(true);
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource().equals(buttonAcceptName)){
            controller.onButtonClick(ButtonEnum.NAME_OK);
        } else if(e.getSource().equals(buttonChangeName)){
            controller.onButtonClick(ButtonEnum.CHANGE_NAME);
        } else if(e.getSource().equals(buttonCreate)){
            controller.onButtonClick(ButtonEnum.CREATE_SERVER);
        } else if(e.getSource().equals(buttonJoin)){
            controller.onButtonClick(ButtonEnum.JOIN_SERVER);
        } else if(e.getSource().equals(buttonServerNameAccept)){
            buttonServerNameAccept.setEnabled(false);
            controller.onButtonClick(ButtonEnum.SERVERNAME_OK);
        } else if(e.getSource().equals(buttonHostnameAccept)){
            buttonHostnameAccept.setEnabled(false);
            controller.onButtonClick(ButtonEnum.HOSTNAME_OK);
        } else if(e.getSource().equals(buttonReady)){
            if(buttonReady.getText().equals("Ready")){
                buttonReady.setText("Not Ready");
                controller.onButtonClick(ButtonEnum.CLIENT_READY);
            } else {
                buttonReady.setText("Ready");
                controller.onButtonClick(ButtonEnum.CLIENT_UNREADY);
            }
        } else if(e.getSource().equals(buttonLeave)){
            controller.onButtonClick(ButtonEnum.CLIENT_LEAVE);
        } else if(e.getSource().equals(buttonStart)){
            controller.onButtonClick(ButtonEnum.SERVER_START);
        } else if(e.getSource().equals(buttonX) || e.getSource().equals(buttonX1)){
            controller.onButtonClick(ButtonEnum.TO_TITLE);
        }
    }
    
    Timer loader;
    TimerTask loadTask;
    int load_increase, load_milestone, load_halt_counter;
    String[] hints = new String[]{"Cleaning the Decks...",
                                    "Printing Reports...",
                                  "Training Marineers...",
                              "Raising the Fog of War...",
                                "Preparing for Sortie...",
                             "Strengthening Protocols...",
                              "Paying Retirement Fees...",
                            "Raising Electricity Bill...",
                                   "Firing Up Engines...",
                                     "PREPARE FOR BATTLE","!"};
    @Override
    public void showLoadingScreen() {
        loadingBar.setValue(0);
        labelLoading.setText(hints[0]);
        load_halt_counter = 0;
        load_milestone = 20+Math.round((float)Math.random()*(20));
        load_increase = Math.round((float)Math.random()*load_milestone);
        loadTask = new TimerTask() {
            @Override
            public void run() {
                if(loadingBar.getValue()>load_milestone && load_halt_counter<load_increase){
                    load_halt_counter ++;
                } else if(load_halt_counter>=load_increase){
                    labelLoading.setText(hints[loadingBar.getValue()/10]);
                    load_halt_counter = 0;
                    load_increase = 20+Math.round((float)Math.random()*(20));
                    load_milestone += Math.round((float)Math.random()*load_increase);
                    load_increase = Math.round((float)Math.random()*load_increase);
                } else {
                    loadingBar.setValue(loadingBar.getValue()+1);
                }
                if(loadingBar.getValue() == loadingBar.getMaximum())
                    controller.onLoadingEnd();
            }
        };
        loader = new Timer();
        loader.scheduleAtFixedRate(loadTask, 20, 20);
        showCard("loading");
    }

    @Override
    public void showInputNameScreen() {
        loader.cancel();
        showCard("start");
    }

    @Override
    public void showMainMenuScreen(String playerName) {
        labelNameShow.setText("Your name is : "+playerName);
        textMenuName.setText(playerName);
        showCard("menu");
    }

    @Override
    public void showCreateServerScreen(String playerName) {
        textServerName.setText(playerName + "'s server");
        buttonServerNameAccept.setEnabled(true);
        showCard("create");
    }
    
    @Override
    public String getNameInput() {
        switch(currentCard) {
            case "start" : return textStartName.getText();
            default : return textMenuName.getText();
        }
    }

    @Override
    public String getServerNameInput() {
        return textServerName.getText();
    }

    @Override
    public String getDestHostNameInput() {
        return textHostname.getText();
    }
    
    @Override
    public void showJoinScreen() {
        textHostname.setText("localhost");
        buttonHostnameAccept.setEnabled(true);
        showCard("join");
    }

    @Override
    public void showLobbyScreen(String serverName, String ipAddress) {
        labelServerName.setText(serverName);
        labelIp.setText("Your IP Address is : "+ipAddress);
        buttonReady.setText("Ready");
        updateRoosterTable();
        showCard("lobby");
    }
    
    @Override
    public void updateRoosterTable() {
        model.createOrUpdateRooster();
        ((FixedLengthTableModel)tableRooster.getModel()).fireTableDataChanged();
        if(model.getReadyCount()>1)
            buttonStart.setEnabled(true);
        else buttonStart.setEnabled(false);
    }

    @Override
    public void showClientButtonsOnly() {
        buttonLeave.setVisible(true);
        buttonReady.setVisible(true);
        buttonStart.setVisible(false);
    }

    @Override
    public void showServerButtonsOnly() {
        buttonReady.setVisible(false);
        buttonLeave.setVisible(false);
        buttonStart.setVisible(true);
        buttonStart.setEnabled(false);
    }

    @Override
    public void showGameScreen() {
        createGridButtons();
        showCard("game");
    }

    @Override
    public void modePlacing() {
        labelHint.setText(placingHint);
        labelHint.setVisible(true);
        disableAiming();
        //enable placing
    }

    @Override
    public void modeBegin() {
        //TODO Implement this.
        labelHint.setVisible(false);
        enableAiming();
    }

    @Override
    public void modeWaiting() {
        //TODO Implement this.
        disableAiming();
    }

    @Override
    public void modeExecute() {
        //TODO Implement this.
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void cleanGameScreen() {
        //TODO Implement this.
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void showResultScreen() {
        //TODO Implement this.
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public int getMaxPlayer() {
        return (Integer)spinnerMaxPlayer.getValue();
    }
    
    public void createGridButtons() {
        
        int playerCount = model.getPlayerCount();
        
        gridButton = new JButton[playerCount][100];
        System.out.println(playerCount);
        //create the buttons
        for(int i=0; i<playerCount; i++){
            for(int j=0; j<100; j++){
                System.out.println(j);
                JButton temp = gridButton[i][j] = new JButton();
                
                temp.setBackground(new java.awt.Color(255, 255, 255));
                if(i!=playerCount-1)
                   temp.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
                else temp.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(88, 88, 88)));
                temp.setContentAreaFilled(false);
                temp.setText("");
                temp.setMargin(new java.awt.Insets(0, 0, 0, 0));
                temp.setMaximumSize(new java.awt.Dimension(24, 24));
                temp.setMinimumSize(new java.awt.Dimension(24, 24));
                temp.setPreferredSize(new java.awt.Dimension(24, 24));
                temp.setEnabled(false);
                
                switch(playerCount){
                    case 2:
                        switch(i){
                            case 0: middle.add(temp); break;
                            case 1: self.add(temp); break; }
                        break;
                    case 3:
                        switch(i){
                            case 0: left.add(temp); break;
                            case 1: right.add(temp); break;
                            case 2: self.add(temp); break; }
                        break;
                    case 4:
                        switch(i){
                            case 0: left.add(temp); break;
                            case 1: middle.add(temp); break;
                            case 2: right.add(temp); break;
                            case 3: self.add(temp); break; }
                        break; }
            }
        }
        
        switch(playerCount){
            case 2:
                middle.setBackground(NavyBlue);
                self.setBackground(SkyBlue);
                break;
            case 3:
                left.setBackground(NavyBlue);
                right.setBackground(NavyBlue);
                self.setBackground(SkyBlue);
                break; 
            case 4:
                left.setBackground(NavyBlue);
                middle.setBackground(NavyBlue);
                right.setBackground(NavyBlue);
                self.setBackground(SkyBlue);
                break;
        }
        
    }
    
    public void destroyGridButtons() {
        
        int playerCount = model.getPlayerCount();
        
        for(int i=0; i<playerCount; i++){
            for(int j=0; j<100; j++){
                JButton temp = gridButton[i][j];
                
                switch(playerCount){
                    case 2:
                        switch(i){
                            case 0: middle.remove(temp); break;
                            case 1: self.remove(temp); break; }
                        break;
                    case 3:
                        switch(i){
                            case 0: left.remove(temp); break;
                            case 1: right.remove(temp); break;
                            case 2: self.remove(temp); break; }
                        break;
                    case 4:
                        switch(i){
                            case 0: left.remove(temp); break;
                            case 1: middle.remove(temp); break;
                            case 2: right.remove(temp); break;
                            case 3: self.remove(temp); break; }
                        break; }
            }
        }
        
        left.setBackground(Color.lightGray);
        middle.setBackground(Color.lightGray);
        right.setBackground(Color.lightGray);
        self.setBackground(Color.lightGray);
        
        gridButton = null;
        
    }
    
    public void enableAiming() {
        //enable all except self
        for (int i=0;i<gridButton.length-1; i++)
            for (JButton tile : gridButton[i])
                tile.setEnabled(true);
    }

    public void disableAiming() {
        //disable all enemy grid buttons
        for (int i=0;i<gridButton.length-1; i++)
            for (JButton tile : gridButton[i])
                tile.setEnabled(true);
    }
    
    //self is the last player
    private void renderGrid(int player) {
        
    }
    
    private void renderGhostGrid() {
        
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        title = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        carder = new javax.swing.JPanel();
        startPanel = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        textStartName = new javax.swing.JTextField();
        buttonAcceptName = new javax.swing.JButton();
        menuPanel = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        buttonJoin = new javax.swing.JButton();
        buttonCreate = new javax.swing.JButton();
        buttonChangeName = new javax.swing.JButton();
        textMenuName = new javax.swing.JTextField();
        labelNameShow = new javax.swing.JLabel();
        createPanel = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        textServerName = new javax.swing.JTextField();
        buttonServerNameAccept = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        spinnerMaxPlayer = new javax.swing.JSpinner();
        buttonX = new javax.swing.JButton();
        joinPanel = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        textHostname = new javax.swing.JTextField();
        buttonHostnameAccept = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        buttonX1 = new javax.swing.JButton();
        lobbyPanel = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tableRooster = new javax.swing.JTable();
        buttonStart = new javax.swing.JButton();
        labelIp = new javax.swing.JLabel();
        buttonReady = new javax.swing.JButton();
        buttonLeave = new javax.swing.JButton();
        JLabal = new javax.swing.JLabel();
        labelServerName = new javax.swing.JLabel();
        gamePanel = new javax.swing.JPanel();
        left = new javax.swing.JPanel();
        middle = new javax.swing.JPanel();
        right = new javax.swing.JPanel();
        controlL = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        standingsTable = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        self = new javax.swing.JPanel();
        controlR = new javax.swing.JPanel();
        labelHint = new javax.swing.JLabel();
        loadingPanel = new javax.swing.JPanel();
        loadingBar = new javax.swing.JProgressBar();
        labelLoading = new javax.swing.JLabel();
        resultPanel = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(800, 600));
        setResizable(false);

        title.setBackground(new java.awt.Color(0, 51, 102));

        jLabel1.setFont(jLabel1.getFont().deriveFont(jLabel1.getFont().getSize()+13f));
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("LAN BAttleship");

        javax.swing.GroupLayout titleLayout = new javax.swing.GroupLayout(title);
        title.setLayout(titleLayout);
        titleLayout.setHorizontalGroup(
            titleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(titleLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        titleLayout.setVerticalGroup(
            titleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(titleLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        carder.setLayout(new java.awt.CardLayout(6, 6));

        jLabel4.setFont(jLabel4.getFont().deriveFont((float)24));
        jLabel4.setText("Your name:");

        textStartName.setFont(textStartName.getFont().deriveFont((float)24));
        textStartName.setText("Rookie");

        buttonAcceptName.setFont(buttonAcceptName.getFont().deriveFont((float)24));
        buttonAcceptName.setText("OK");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(textStartName, javax.swing.GroupLayout.PREFERRED_SIZE, 337, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(185, 185, 185)
                        .addComponent(buttonAcceptName, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(textStartName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(buttonAcceptName)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout startPanelLayout = new javax.swing.GroupLayout(startPanel);
        startPanel.setLayout(startPanelLayout);
        startPanelLayout.setHorizontalGroup(
            startPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(startPanelLayout.createSequentialGroup()
                .addGap(109, 109, 109)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(126, 126, 126))
        );
        startPanelLayout.setVerticalGroup(
            startPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(startPanelLayout.createSequentialGroup()
                .addGap(162, 162, 162)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(208, Short.MAX_VALUE))
        );

        carder.add(startPanel, "start");

        buttonJoin.setFont(buttonJoin.getFont().deriveFont((float)24));
        buttonJoin.setText("Join Room");

        buttonCreate.setFont(buttonCreate.getFont().deriveFont((float)24));
        buttonCreate.setText("Create Room");

        buttonChangeName.setText("Change Name");

        textMenuName.setText("Rookie");

        labelNameShow.setText("Your name is: Rookie");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(buttonCreate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(buttonJoin, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                            .addComponent(textMenuName, javax.swing.GroupLayout.PREFERRED_SIZE, 282, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(buttonChangeName)))
                    .addComponent(labelNameShow))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(labelNameShow)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(textMenuName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(buttonChangeName))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(buttonCreate, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(buttonJoin, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout menuPanelLayout = new javax.swing.GroupLayout(menuPanel);
        menuPanel.setLayout(menuPanelLayout);
        menuPanelLayout.setHorizontalGroup(
            menuPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(menuPanelLayout.createSequentialGroup()
                .addGap(159, 159, 159)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(160, Short.MAX_VALUE))
        );
        menuPanelLayout.setVerticalGroup(
            menuPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(menuPanelLayout.createSequentialGroup()
                .addGap(132, 132, 132)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(173, Short.MAX_VALUE))
        );

        carder.add(menuPanel, "menu");

        jLabel6.setFont(jLabel6.getFont().deriveFont((float)24));
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("Enter server name:");

        textServerName.setFont(textServerName.getFont().deriveFont((float)24));
        textServerName.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        buttonServerNameAccept.setFont(buttonServerNameAccept.getFont().deriveFont((float)24));
        buttonServerNameAccept.setText("OK");

        jLabel7.setFont(jLabel7.getFont().deriveFont(jLabel7.getFont().getStyle() | java.awt.Font.BOLD, 24));
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel7.setText("Create Server");

        jLabel8.setFont(jLabel8.getFont().deriveFont((float)24));
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel8.setText("Enter max player (2-4):");

        spinnerMaxPlayer.setFont(spinnerMaxPlayer.getFont().deriveFont((float)24));
        spinnerMaxPlayer.setModel(new javax.swing.SpinnerNumberModel(2, 2, 4, 1));

        buttonX.setText("X");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(textServerName)
                    .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(105, 105, 105)
                        .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 252, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 58, Short.MAX_VALUE)
                        .addComponent(buttonX, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(186, 186, 186)
                .addComponent(spinnerMaxPlayer, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(buttonServerNameAccept, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(166, 166, 166))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(buttonX))
                .addGap(36, 36, 36)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(textServerName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(spinnerMaxPlayer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(40, 40, 40)
                .addComponent(buttonServerNameAccept)
                .addContainerGap())
        );

        javax.swing.GroupLayout createPanelLayout = new javax.swing.GroupLayout(createPanel);
        createPanel.setLayout(createPanelLayout);
        createPanelLayout.setHorizontalGroup(
            createPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(createPanelLayout.createSequentialGroup()
                .addGap(121, 121, 121)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(119, Short.MAX_VALUE))
        );
        createPanelLayout.setVerticalGroup(
            createPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(createPanelLayout.createSequentialGroup()
                .addGap(65, 65, 65)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(83, Short.MAX_VALUE))
        );

        carder.add(createPanel, "create");

        jLabel3.setFont(jLabel3.getFont().deriveFont((float)24));
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("Enter server ip address:");

        textHostname.setFont(textHostname.getFont().deriveFont((float)24));
        textHostname.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        buttonHostnameAccept.setFont(buttonHostnameAccept.getFont().deriveFont((float)24));
        buttonHostnameAccept.setText("OK");

        jLabel5.setFont(jLabel5.getFont().deriveFont(jLabel5.getFont().getStyle() | java.awt.Font.BOLD, 24));
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("Joining Server");

        buttonX1.setText("X");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(textHostname)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 265, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(59, 59, 59)
                        .addComponent(buttonX1, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(165, Short.MAX_VALUE)
                .addComponent(buttonHostnameAccept, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(171, 171, 171))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(buttonX1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 31, Short.MAX_VALUE)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(textHostname, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(buttonHostnameAccept)
                .addContainerGap())
        );

        javax.swing.GroupLayout joinPanelLayout = new javax.swing.GroupLayout(joinPanel);
        joinPanel.setLayout(joinPanelLayout);
        joinPanelLayout.setHorizontalGroup(
            joinPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, joinPanelLayout.createSequentialGroup()
                .addContainerGap(123, Short.MAX_VALUE)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(120, 120, 120))
        );
        joinPanelLayout.setVerticalGroup(
            joinPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(joinPanelLayout.createSequentialGroup()
                .addGap(126, 126, 126)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(141, Short.MAX_VALUE))
        );

        carder.add(joinPanel, "join");

        tableRooster.setFont(tableRooster.getFont().deriveFont((float)20));
        tableRooster.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane2.setViewportView(tableRooster);

        buttonStart.setFont(buttonStart.getFont().deriveFont((float)20));
        buttonStart.setText("Start");

        labelIp.setFont(labelIp.getFont().deriveFont((float)20));
        labelIp.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        labelIp.setText("Your ip address is:");

        buttonReady.setFont(buttonReady.getFont().deriveFont((float)20));
        buttonReady.setText("Ready");

        buttonLeave.setFont(buttonLeave.getFont().deriveFont((float)20));
        buttonLeave.setText("Leave");

        JLabal.setFont(JLabal.getFont());
        JLabal.setText("Server");
        JLabal.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 15, 0, 0));

        labelServerName.setFont(labelServerName.getFont().deriveFont(labelServerName.getFont().getStyle() | java.awt.Font.BOLD, 20));
        labelServerName.setText("The Anumans");
        labelServerName.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 15, 0, 0));

        javax.swing.GroupLayout lobbyPanelLayout = new javax.swing.GroupLayout(lobbyPanel);
        lobbyPanel.setLayout(lobbyPanelLayout);
        lobbyPanelLayout.setHorizontalGroup(
            lobbyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(lobbyPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(lobbyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2)
                    .addGroup(lobbyPanelLayout.createSequentialGroup()
                        .addGap(350, 350, 350)
                        .addComponent(buttonLeave, javax.swing.GroupLayout.DEFAULT_SIZE, 110, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(buttonReady, javax.swing.GroupLayout.DEFAULT_SIZE, 109, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(buttonStart, javax.swing.GroupLayout.DEFAULT_SIZE, 109, Short.MAX_VALUE))
                    .addComponent(labelIp, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
            .addGroup(lobbyPanelLayout.createSequentialGroup()
                .addComponent(JLabal)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(labelServerName, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        lobbyPanelLayout.setVerticalGroup(
            lobbyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, lobbyPanelLayout.createSequentialGroup()
                .addGroup(lobbyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(JLabal, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelServerName, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 254, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(labelIp)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 82, Short.MAX_VALUE)
                .addGroup(lobbyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(buttonStart)
                    .addComponent(buttonReady)
                    .addComponent(buttonLeave))
                .addContainerGap())
        );

        carder.add(lobbyPanel, "lobby");

        gamePanel.setLayout(new java.awt.GridLayout(2, 3, 2, 2));

        left.setFocusable(false);
        left.setMinimumSize(new java.awt.Dimension(260, 260));
        left.setPreferredSize(new java.awt.Dimension(260, 260));
        left.setRequestFocusEnabled(false);
        left.setLayout(new java.awt.GridLayout(10, 10));
        gamePanel.add(left);

        middle.setFocusable(false);
        middle.setMinimumSize(new java.awt.Dimension(260, 260));
        middle.setPreferredSize(new java.awt.Dimension(260, 260));
        middle.setRequestFocusEnabled(false);
        middle.setLayout(new java.awt.GridLayout(10, 10));
        gamePanel.add(middle);

        right.setFocusable(false);
        right.setMinimumSize(new java.awt.Dimension(260, 260));
        right.setPreferredSize(new java.awt.Dimension(260, 260));
        right.setRequestFocusEnabled(false);
        right.setLayout(new java.awt.GridLayout(10, 10));
        gamePanel.add(right);

        controlL.setFocusable(false);
        controlL.setRequestFocusEnabled(false);

        standingsTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane1.setViewportView(standingsTable);

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("STANDINGS");

        javax.swing.GroupLayout controlLLayout = new javax.swing.GroupLayout(controlL);
        controlL.setLayout(controlLLayout);
        controlLLayout.setHorizontalGroup(
            controlLLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(controlLLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(controlLLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 210, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap())
        );
        controlLLayout.setVerticalGroup(
            controlLLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, controlLLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        gamePanel.add(controlL);

        self.setFocusable(false);
        self.setMinimumSize(new java.awt.Dimension(260, 260));
        self.setPreferredSize(new java.awt.Dimension(260, 260));
        self.setRequestFocusEnabled(false);
        self.setLayout(new java.awt.GridLayout(10, 10));
        gamePanel.add(self);

        controlR.setFocusable(false);
        controlR.setRequestFocusEnabled(false);

        javax.swing.GroupLayout controlRLayout = new javax.swing.GroupLayout(controlR);
        controlR.setLayout(controlRLayout);
        controlRLayout.setHorizontalGroup(
            controlRLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(controlRLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(labelHint, javax.swing.GroupLayout.DEFAULT_SIZE, 210, Short.MAX_VALUE)
                .addContainerGap())
        );
        controlRLayout.setVerticalGroup(
            controlRLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(controlRLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(labelHint, javax.swing.GroupLayout.DEFAULT_SIZE, 208, Short.MAX_VALUE)
                .addContainerGap())
        );

        gamePanel.add(controlR);

        carder.add(gamePanel, "game");

        loadingBar.setValue(20);

        labelLoading.setFont(labelLoading.getFont().deriveFont((float)18));
        labelLoading.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelLoading.setText("Calculating Probability Matrix...");

        javax.swing.GroupLayout loadingPanelLayout = new javax.swing.GroupLayout(loadingPanel);
        loadingPanel.setLayout(loadingPanelLayout);
        loadingPanelLayout.setHorizontalGroup(
            loadingPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, loadingPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(loadingPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(loadingBar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(labelLoading, javax.swing.GroupLayout.DEFAULT_SIZE, 696, Short.MAX_VALUE))
                .addContainerGap())
        );
        loadingPanelLayout.setVerticalGroup(
            loadingPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(loadingPanelLayout.createSequentialGroup()
                .addGap(207, 207, 207)
                .addComponent(loadingBar, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(labelLoading)
                .addContainerGap(207, Short.MAX_VALUE))
        );

        carder.add(loadingPanel, "loading");

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane3.setViewportView(jTable1);

        javax.swing.GroupLayout resultPanelLayout = new javax.swing.GroupLayout(resultPanel);
        resultPanel.setLayout(resultPanelLayout);
        resultPanelLayout.setHorizontalGroup(
            resultPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(resultPanelLayout.createSequentialGroup()
                .addGap(133, 133, 133)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(141, Short.MAX_VALUE))
        );
        resultPanelLayout.setVerticalGroup(
            resultPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(resultPanelLayout.createSequentialGroup()
                .addGap(36, 36, 36)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 304, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(142, Short.MAX_VALUE))
        );

        carder.add(resultPanel, "result");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(title, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(carder, javax.swing.GroupLayout.PREFERRED_SIZE, 738, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(title, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(carder, javax.swing.GroupLayout.PREFERRED_SIZE, 494, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel JLabal;
    private javax.swing.JButton buttonAcceptName;
    private javax.swing.JButton buttonChangeName;
    private javax.swing.JButton buttonCreate;
    private javax.swing.JButton buttonHostnameAccept;
    private javax.swing.JButton buttonJoin;
    private javax.swing.JButton buttonLeave;
    private javax.swing.JButton buttonReady;
    private javax.swing.JButton buttonServerNameAccept;
    private javax.swing.JButton buttonStart;
    private javax.swing.JButton buttonX;
    private javax.swing.JButton buttonX1;
    private javax.swing.JPanel carder;
    private javax.swing.JPanel controlL;
    private javax.swing.JPanel controlR;
    private javax.swing.JPanel createPanel;
    private javax.swing.JPanel gamePanel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTable jTable1;
    private javax.swing.JPanel joinPanel;
    private javax.swing.JLabel labelHint;
    private javax.swing.JLabel labelIp;
    private javax.swing.JLabel labelLoading;
    private javax.swing.JLabel labelNameShow;
    private javax.swing.JLabel labelServerName;
    private javax.swing.JPanel left;
    private javax.swing.JProgressBar loadingBar;
    private javax.swing.JPanel loadingPanel;
    private javax.swing.JPanel lobbyPanel;
    private javax.swing.JPanel menuPanel;
    private javax.swing.JPanel middle;
    private javax.swing.JPanel resultPanel;
    private javax.swing.JPanel right;
    private javax.swing.JPanel self;
    private javax.swing.JSpinner spinnerMaxPlayer;
    private javax.swing.JTable standingsTable;
    private javax.swing.JPanel startPanel;
    private javax.swing.JTable tableRooster;
    private javax.swing.JTextField textHostname;
    private javax.swing.JTextField textMenuName;
    private javax.swing.JTextField textServerName;
    private javax.swing.JTextField textStartName;
    private javax.swing.JPanel title;
    // End of variables declaration//GEN-END:variables

    @Override
    public void mouseClicked(MouseEvent e) {
        if(e.getSource().equals(textServerName)){
            textServerName.selectAll();
        } else if(e.getSource().equals(textHostname)){
            textHostname.selectAll();
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }


}
