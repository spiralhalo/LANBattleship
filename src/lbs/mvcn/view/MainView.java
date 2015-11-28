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
import javax.swing.JButton;
import javax.swing.table.DefaultTableModel;
import lbs.mvcn.controller.IViewController;
import lbs.mvcn.model.IModel;
import lbs.mvcn.controller.ButtonEnum;
/**
 *
 * @author nanashi95
 */
public class MainView extends JFrame implements IView, ActionListener{

    private IViewController controller;
    private IModel model;
    private JButton gridButton[][];
    private CardLayout cards;
    
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
        
        buttonAcceptName.addActionListener(this);
        buttonChangeName.addActionListener(this);
        buttonCreate.addActionListener(this);
        buttonJoin.addActionListener(this);
        buttonLeave.addActionListener(this);
        buttonReady.addActionListener(this);
        buttonStart.addActionListener(this);
    }
    
    private void showCard(String cardName) {
        currentCard = cardName;
        cards.show(this, cardName);
    }
    
    @Override
    public void setModel(IModel model){
        this.model = model;
    }
    
    @Override
    public void setController(IViewController controller) {
        this.controller =  controller;
    }
    
    @Override
    public void diplay(){
        this.setVisible(true);
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource().equals(buttonAcceptName)){
            controller.onButtonClick(ButtonEnum.NAME_OK);
        } else if(e.getSource().equals(buttonCreate)){
            controller.onButtonClick(ButtonEnum.CREATE_SERVER);
        } else if(e.getSource().equals(buttonJoin)){
            controller.onButtonClick(ButtonEnum.JOIN_SERVER);
        } 
    }
    
    @Override
    public void showLoadingScreen() {
        showCard("loading");
    }

    @Override
    public void showInputNameScreen() {
        showCard("start");
    }

    @Override
    public void showMainMenuScreen(String playerName) {
        labelNameShow.setText("Your name is : "+playerName);
        textMenuName.setText(playerName);
        showCard("menu");
    }

    @Override
    public void showCreateServerScreen() {
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
    public String serverNameInput() {
        //TODO Implement this.
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String destHostNameInput() {
        //TODO Implement this.
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public void showJoinScreen() {
        
    }

    @Override
    public void showLobbyScreen(String serverName, String ipAddress) {
        labelServerName.setText(serverName);
        labelIp.setText(ipAddress);
        showCard("lobby");
    }

    @Override
    public void refreshReadyList() {
        ((FixedLengthTableModel)tableRooster.getModel()).fireTableDataChanged();
    }

    @Override
    public void showClientButtonsOnly() {
        //TODO Implement this.
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void showServerButtonsOnly() {
        //TODO Implement this.
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void prepareGameScreen() {
        //TODO Implement this.
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void showGameScreen() {
        //TODO Implement this.
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void prepareGameScreenForShipArrangement() {
        //TODO Implement this.
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void updateGameScreenForStartingRound() {
        //TODO Implement this.
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void updateGameScreenForWaiting() {
        //TODO Implement this.
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void updateGameScreenForExecution() {
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
    
    public void createGridButtons() {
        
        int playerCount = model.getPlayerCount();
        
        gridButton = new JButton[playerCount][100];
        
        //create the buttons
        for(int i=0; i<playerCount; i++){
            for(int j=0; j<100; j++){
                JButton temp = gridButton[i][j] = new JButton();
                
                temp.setBackground(new java.awt.Color(255, 255, 255));
                temp.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
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
                middle.setBackground(Color.blue);
                self.setBackground(Color.blue);
                break;
            case 3:
                left.setBackground(Color.blue);
                right.setBackground(Color.blue);
                self.setBackground(Color.blue);
                break; 
            case 4:
                left.setBackground(Color.blue);
                middle.setBackground(Color.blue);
                right.setBackground(Color.blue);
                self.setBackground(Color.blue);
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
        loadingPanel = new javax.swing.JPanel();
        jProgressBar1 = new javax.swing.JProgressBar();
        jLabel5 = new javax.swing.JLabel();
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
        joinPanel = new javax.swing.JPanel();
        lobbyPanel = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tableRooster = new javax.swing.JTable();
        buttonStart = new javax.swing.JButton();
        labelIp = new javax.swing.JLabel();
        buttonReady = new javax.swing.JButton();
        buttonLeave = new javax.swing.JButton();
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

        jProgressBar1.setValue(20);

        jLabel5.setFont(jLabel5.getFont().deriveFont((float)18));
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("Calculating Probability Matrix...");

        javax.swing.GroupLayout loadingPanelLayout = new javax.swing.GroupLayout(loadingPanel);
        loadingPanel.setLayout(loadingPanelLayout);
        loadingPanelLayout.setHorizontalGroup(
            loadingPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, loadingPanelLayout.createSequentialGroup()
                .addContainerGap(53, Short.MAX_VALUE)
                .addGroup(loadingPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, 620, Short.MAX_VALUE)
                    .addComponent(jProgressBar1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(53, 53, 53))
        );
        loadingPanelLayout.setVerticalGroup(
            loadingPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(loadingPanelLayout.createSequentialGroup()
                .addGap(207, 207, 207)
                .addComponent(jProgressBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel5)
                .addContainerGap(210, Short.MAX_VALUE))
        );

        carder.add(loadingPanel, "loading");

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

        javax.swing.GroupLayout createPanelLayout = new javax.swing.GroupLayout(createPanel);
        createPanel.setLayout(createPanelLayout);
        createPanelLayout.setHorizontalGroup(
            createPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 726, Short.MAX_VALUE)
        );
        createPanelLayout.setVerticalGroup(
            createPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 482, Short.MAX_VALUE)
        );

        carder.add(createPanel, "create");

        javax.swing.GroupLayout joinPanelLayout = new javax.swing.GroupLayout(joinPanel);
        joinPanel.setLayout(joinPanelLayout);
        joinPanelLayout.setHorizontalGroup(
            joinPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 726, Short.MAX_VALUE)
        );
        joinPanelLayout.setVerticalGroup(
            joinPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 482, Short.MAX_VALUE)
        );

        carder.add(joinPanel, "join");

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

        buttonStart.setText("Start");

        labelIp.setText("Your ip address is:");

        buttonReady.setText("Ready");

        buttonLeave.setText("Leave");

        labelServerName.setFont(labelServerName.getFont().deriveFont(labelServerName.getFont().getStyle() | java.awt.Font.BOLD));
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
                        .addComponent(labelIp, javax.swing.GroupLayout.PREFERRED_SIZE, 384, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(buttonLeave, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(buttonReady, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(buttonStart, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
            .addComponent(labelServerName, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        lobbyPanelLayout.setVerticalGroup(
            lobbyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, lobbyPanelLayout.createSequentialGroup()
                .addComponent(labelServerName, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 367, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addGroup(lobbyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(buttonStart)
                    .addComponent(labelIp)
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
            .addGap(0, 240, Short.MAX_VALUE)
        );
        controlRLayout.setVerticalGroup(
            controlRLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 240, Short.MAX_VALUE)
        );

        gamePanel.add(controlR);

        carder.add(gamePanel, "game");

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
    private javax.swing.JButton buttonAcceptName;
    private javax.swing.JButton buttonChangeName;
    private javax.swing.JButton buttonCreate;
    private javax.swing.JButton buttonJoin;
    private javax.swing.JButton buttonLeave;
    private javax.swing.JButton buttonReady;
    private javax.swing.JButton buttonStart;
    private javax.swing.JPanel carder;
    private javax.swing.JPanel controlL;
    private javax.swing.JPanel controlR;
    private javax.swing.JPanel createPanel;
    private javax.swing.JPanel gamePanel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JProgressBar jProgressBar1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JPanel joinPanel;
    private javax.swing.JLabel labelIp;
    private javax.swing.JLabel labelNameShow;
    private javax.swing.JLabel labelServerName;
    private javax.swing.JPanel left;
    private javax.swing.JPanel loadingPanel;
    private javax.swing.JPanel lobbyPanel;
    private javax.swing.JPanel menuPanel;
    private javax.swing.JPanel middle;
    private javax.swing.JPanel right;
    private javax.swing.JPanel self;
    private javax.swing.JTable standingsTable;
    private javax.swing.JPanel startPanel;
    private javax.swing.JTable tableRooster;
    private javax.swing.JTextField textMenuName;
    private javax.swing.JTextField textStartName;
    private javax.swing.JPanel title;
    // End of variables declaration//GEN-END:variables

}
