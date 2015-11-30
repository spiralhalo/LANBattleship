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
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import lbs.mvcn.controller.IViewController;
import lbs.mvcn.model.IModel;
import lbs.mvcn.controller.ButtonEnum;
import static lbs.mvcn.model.Constants.*;
import lbs.mvcn.model.PlayerId;
/**
 *
 * @author nanashi95
 */
public class MainView extends JFrame implements IView, ActionListener,
        MouseListener, KeyListener{
    
    private final ImageIcon[] icons = new ImageIcon[14];
    private final ImageIcon[] ghost_icons = new ImageIcon[6];
    
    private IViewController controller;
    private IModel model;
    private JButton gridButton[][];
    private CardLayout cards;
    
    private final Color NavyBlue = new Color(40,44,77);
    private final Color DarkRed = new Color(66,44,40);
    private final Color SkyBlue = new Color(80,151,230);
    
    private final String placingHint =
              "<html>Hint: Click on a tile to place"
            + "<br/>your ship. Press 'R' button to"
            + "<br/>rotate your ship before placing.";
    
    private final String attackHint =
              "<html>Hint: Click on a navy blue tile"
            + "<br/>to declare your attack.";
    
    private final String waitingHint =
              "<html>Waiting for other players...";
    
    private final String yourTurnHint =
              "<html>YOUR TURN<br/>"
            + "<br/>Hint: Click on a navy blue tile"
            + "<br/>to declare your attack.";
    
    private final String waitingForTurnHint =
              "<html>Waiting for your turn...";
    
    private final String deadHint =
              "<html>YOUR FLEET IS ANIHILATED<br/>"
            + "<br/>Waiting for game to end...";
    
    private String currentCard;
    
    /**
     * Creates new form Main
     */
    public MainView() {
        initComponents();
        setLocationRelativeTo(null);
        
        icons[0] = null;
        for(int i = 1; i<icons.length; i++)
            icons[i] = new javax.swing.ImageIcon(getClass()
                    .getResource("/lbs/gfx/"+i+".png"));
        
        for(int i = 1; i<ghost_icons.length+1; i++)
            ghost_icons[i-1] = new javax.swing.ImageIcon(getClass()
                    .getResource("/lbs/gfx/ghost"+i+".png"));
        
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
        tableRooster.setModel(new FixedLengthTableModel( model.getRoosterData(),
                new String[]{"IP Address", "Player Name", "Ready"}));
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
        buttonX2.addActionListener(this);
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
        } else if(e.getSource().equals(buttonX) || e.getSource()
                .equals(buttonX1) || e.getSource().equals(buttonX2) ){
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
                if(loadingBar.getValue()>load_milestone 
                        && load_halt_counter<load_increase){
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
        labelNameShow.setText("Welcome, "+playerName);
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
    public void updateStandingsTable(boolean passive) {
        model.createOrUpdateStandings(passive);
        ((FixedLengthTableModel)tableStandins.getModel()).fireTableDataChanged();
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

    private PlayerId[] pids;
    @Override
    public void showGameScreen(PlayerId[] pidArrangement) {
        pids = pidArrangement;
        
        model.createOrUpdateStandings(false);
        tableStandins.setModel(new FixedLengthTableModel( model.getStandingsData(), new String[]{"Player", "Fired", "Health"}));
        ((FixedLengthTableModel)tableStandins.getModel()).fireTableDataChanged();
        
        createGridButtons();
        showCard("game");
    }

    private boolean isPlacing = false;
    private boolean isVertical = false;
    private boolean placingFeasible = false;
        private final int[] shipLineUp = new int[]{5,4,4,3,3,3,2,2,2,2};
    private int placing;
    
    @Override
    public void modePlacing() {
        isDead = false;
        addKeyListener(this);
        this.setFocusable(true);
        this.requestFocus();
        isPlacing = true;
        labelHint.setText(placingHint);
        disableAiming();
        placing = 0;
        for (JButton tile : gridButton[pids.length-1])
            tile.setEnabled(true);
        //enable placing
    }
    
    private void endModePlacing() {
        controller.onFinishPlacingShip();
        removeKeyListener(this);
        isPlacing = false;
        for (JButton tile : gridButton[pids.length-1]){
            tile.removeKeyListener(this);
            tile.removeMouseListener(tile.getMouseListeners()[0]);
        }
    }
    
    private int lastPlacingIndex = 0;
    private void placingOnMouseEnter(int index){
        if(placing>=shipLineUp.length) return;
        lastPlacingIndex = index;
        int size = shipLineUp[placing];
        placingFeasible = true;
        drawGrid(pids.length-1);
        for(int i=0;i<size;i++)
            if(isVertical){
                if(index+i*10>=100 || model.getGrid(pids[pids.length-1], index/10+i, index%10) != NULL){
                    placingFeasible = false;
                    break;
                }
            } else {
                if(index%10+i>=10 || model.getGrid(pids[pids.length-1], index/10, index%10+i) != NULL){
                    placingFeasible = false;
                    break;
                }
            }
        if(placingFeasible){
            drawGhostGrid(index, size, isVertical);
        } 
    }
    
//    private void placingOnMouseExit(int index){
//        int size = shipLineUp[placing];
//    }
    
    private void placingOnClick(int index){
        if(placing>=shipLineUp.length) return;
        int size = shipLineUp[placing];
        if(placingFeasible){
            controller.onPlaceShip(index, size, isVertical);
            drawGrid(pids.length-1);
            placing++;
            if(placing >= shipLineUp.length)
                endModePlacing();
        }
    }
    
    //self is the last player
    private void drawGrid(int player) {
        if(model.getGrid(pids[player], 0, 0)==DISABLE_GRID)
            return;
        for(int i=0;i<100;i++){
            byte val = model.getGrid(model.get(pids[player].name,pids[player].id), i/10, i%10);
            if(player==pids.length-1 || val >= NULL_ATTACKED)
                gridButton[player][i].setIcon(icons[val]);
            else gridButton[player][i].setIcon(icons[NULL]);
        }
        
    }
    
    private void drawGhostGrid(int index, int size, boolean vertical) {
        
        for(int i=0;i<size;i++)
            if(vertical){
                if(i==0)
                    gridButton[pids.length-1][index+i*10].setIcon(ghost_icons[3]);
                else if(i==size-1)
                    gridButton[pids.length-1][index+i*10].setIcon(ghost_icons[5]);
                else gridButton[pids.length-1][index+i*10].setIcon(ghost_icons[1]);
            } else {
                if(i==0)
                    gridButton[pids.length-1][index+i].setIcon(ghost_icons[2]);
                else if(i==size-1)
                    gridButton[pids.length-1][index+i].setIcon(ghost_icons[4]);
                else gridButton[pids.length-1][index+i].setIcon(ghost_icons[0]);
            }
        
    }

    @Override
    public void modeBegin() {
        if(!isDead){
            labelHint.setText(attackHint);
            enableAiming();
        } else labelHint.setText(deadHint);
    }
    
    private boolean isDead = false;
    
    @Override
    public void modeDead() {
        isDead = true;
        disableAiming();
    }

    @Override
    public void modeWaiting() {
        if(!isDead)
            labelHint.setText(waitingHint);
        else labelHint.setText(deadHint);
        disableAiming();
    }

    @Override
    public void modeExecute() {
        labelHint.setText("");
        for(int i=0;i<pids.length;i++)
            drawGrid(i);
        controller.onFinishExecution();
    }

    @Override
    public void cleanGameScreen() {
        destroyGridButtons();
    }

    @Override
    public void showResultScreen() {
        model.createResult();
        tableResult.setModel(new FixedLengthTableModel(model.getResultData(), new String[]{"Place",""}));
        showCard("result");
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
                JButton temp = gridButton[i][j] = new JButton();
                
                temp.setBackground(new java.awt.Color(255, 255, 255));
                temp.setContentAreaFilled(false);
                temp.setText("");
                temp.setMargin(new java.awt.Insets(0, 0, 0, 0));
                temp.setMaximumSize(new java.awt.Dimension(24, 24));
                temp.setMinimumSize(new java.awt.Dimension(24, 24));
                temp.setPreferredSize(new java.awt.Dimension(24, 24));
                temp.setEnabled(false);
                temp.setFocusable(false);
                
                final int player_index = i;
                final int tile_index = j;
                if(i!=playerCount-1){
                    //enemies
                   temp.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
                   temp.addActionListener(new ActionListener() {

                       @Override
                       public void actionPerformed(ActionEvent e) {
                           if(model.getGrid(pids[player_index], tile_index/10, tile_index%10) >= NULL_ATTACKED)
                               return;
                           System.out.println("i am attacking lol");
                           controller.onGridButtonClick(pids[player_index], tile_index);
                           marx(player_index, tile_index);
                           disableAiming();
                       }
                       
                   });
                } else {
                    //self
                    temp.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(88, 88, 88)));
//                    temp.addKeyListener(this);
                    temp.addMouseListener(new MouseListener() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            placingOnClick(tile_index);
                        }
                        public void mousePressed(MouseEvent e) {}
                        public void mouseReleased(MouseEvent e) {}
                        @Override
                        public void mouseEntered(MouseEvent e) {
                            placingOnMouseEnter(tile_index);
                        }
                        @Override
                        public void mouseExited(MouseEvent e) {
//                            placingOnMouseExit(tile_index);
                        }
                    });
                }
                
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
        
        p1Name.setText("");
        p2Name.setText("");
        p3Name.setText("");
        switch(playerCount){
            case 2:
                p2Name.setText(pids[0].name);
                middle.setBackground(NavyBlue);
                self.setBackground(SkyBlue);
                break;
            case 3:
                p1Name.setText(pids[0].name);
                p3Name.setText(pids[1].name);
                left.setBackground(NavyBlue);
                right.setBackground(NavyBlue);
                self.setBackground(SkyBlue);
                break; 
            case 4:
                p1Name.setText(pids[0].name);
                p2Name.setText(pids[1].name);
                p3Name.setText(pids[2].name);
                left.setBackground(NavyBlue);
                middle.setBackground(NavyBlue);
                right.setBackground(NavyBlue);
                self.setBackground(SkyBlue);
                break;
        }
        
    }
    
    public void destroyGridButtons() {
        
        for(int i=0; i<gridButton.length; i++){
            for(int j=0; j<100; j++){
                JButton temp = gridButton[i][j];
                if(i<gridButton.length-1)
                    temp.removeActionListener(temp.getActionListeners()[0]);
                temp.getParent().remove(temp);
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
            if(model.getGrid(pids[i], 0, 0)!=DISABLE_GRID)
                for (JButton tile : gridButton[i])
                    tile.setEnabled(true);
    }

    public void disableAiming() {
        //disable all enemy grid buttons
        for (int i=0;i<gridButton.length-1; i++)
            for (JButton tile : gridButton[i])
                tile.setEnabled(false);
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
        jLabel10 = new javax.swing.JLabel();
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
        loadingPanel = new javax.swing.JPanel();
        loadingBar = new javax.swing.JProgressBar();
        labelLoading = new javax.swing.JLabel();
        resultPanel = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tableResult = new javax.swing.JTable();
        jLabel9 = new javax.swing.JLabel();
        buttonX2 = new javax.swing.JButton();
        gamePanel = new javax.swing.JPanel();
        gimpane = new javax.swing.JPanel();
        left = new javax.swing.JPanel();
        middle = new javax.swing.JPanel();
        right = new javax.swing.JPanel();
        controlL = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tableStandins = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        self = new javax.swing.JPanel();
        controlR = new javax.swing.JPanel();
        labelHint = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        p1Name = new javax.swing.JLabel();
        p2Name = new javax.swing.JLabel();
        p3Name = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(740, 600));
        setPreferredSize(new java.awt.Dimension(740, 600));
        setResizable(false);

        title.setBackground(new java.awt.Color(0, 51, 102));
        title.setFocusable(false);

        jLabel1.setFont(jLabel1.getFont().deriveFont(jLabel1.getFont().getSize()+13f));
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("LAN Battleship");
        jLabel1.setFocusable(false);

        javax.swing.GroupLayout titleLayout = new javax.swing.GroupLayout(title);
        title.setLayout(titleLayout);
        titleLayout.setHorizontalGroup(
            titleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(titleLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        titleLayout.setVerticalGroup(
            titleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(titleLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        carder.setMinimumSize(new java.awt.Dimension(796, 600));
        carder.setPreferredSize(new java.awt.Dimension(796, 600));
        carder.setLayout(new java.awt.CardLayout(6, 6));

        jLabel4.setFont(jLabel4.getFont().deriveFont((float)24));
        jLabel4.setText("Your name:");

        textStartName.setFont(textStartName.getFont().deriveFont((float)24));
        textStartName.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        textStartName.setText("Rookie");

        buttonAcceptName.setFont(buttonAcceptName.getFont().deriveFont((float)24));
        buttonAcceptName.setIcon(new javax.swing.ImageIcon(getClass().getResource("/lbs/gfx/5.png"))); // NOI18N
        buttonAcceptName.setText("OK");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap(84, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addGap(183, 183, 183))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addComponent(textStartName, javax.swing.GroupLayout.PREFERRED_SIZE, 337, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(70, 70, 70))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addComponent(buttonAcceptName, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(161, 161, 161))))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(textStartName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 36, Short.MAX_VALUE)
                .addComponent(buttonAcceptName)
                .addContainerGap())
        );

        javax.swing.GroupLayout startPanelLayout = new javax.swing.GroupLayout(startPanel);
        startPanel.setLayout(startPanelLayout);
        startPanelLayout.setHorizontalGroup(
            startPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(startPanelLayout.createSequentialGroup()
                .addGap(124, 124, 124)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(111, 111, 111))
        );
        startPanelLayout.setVerticalGroup(
            startPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(startPanelLayout.createSequentialGroup()
                .addGap(150, 150, 150)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(167, Short.MAX_VALUE))
        );

        carder.add(startPanel, "start");

        buttonJoin.setFont(buttonJoin.getFont().deriveFont((float)24));
        buttonJoin.setIcon(new javax.swing.ImageIcon(getClass().getResource("/lbs/gfx/1.png"))); // NOI18N
        buttonJoin.setText("Join Room");

        buttonCreate.setFont(buttonCreate.getFont().deriveFont((float)24));
        buttonCreate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/lbs/gfx/5.png"))); // NOI18N
        buttonCreate.setText("Create Room");

        buttonChangeName.setFont(buttonChangeName.getFont().deriveFont((float)16));
        buttonChangeName.setText("Change Name");

        textMenuName.setFont(textMenuName.getFont().deriveFont((float)16));
        textMenuName.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        textMenuName.setText("Rookie");

        labelNameShow.setFont(labelNameShow.getFont().deriveFont((float)20));
        labelNameShow.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelNameShow.setText("Your name is: Rookie");

        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel10.setText("LAN Battleship version: 1.0");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(buttonCreate, javax.swing.GroupLayout.PREFERRED_SIZE, 235, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(buttonJoin, javax.swing.GroupLayout.PREFERRED_SIZE, 231, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(labelNameShow, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(181, 181, 181)
                .addComponent(buttonChangeName)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 292, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(98, 98, 98))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(textMenuName, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(162, 162, 162))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(labelNameShow)
                .addGap(35, 35, 35)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(buttonCreate, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(buttonJoin, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 139, Short.MAX_VALUE)
                .addComponent(textMenuName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(buttonChangeName)
                .addGap(18, 18, 18)
                .addComponent(jLabel10))
        );

        javax.swing.GroupLayout menuPanelLayout = new javax.swing.GroupLayout(menuPanel);
        menuPanel.setLayout(menuPanelLayout);
        menuPanelLayout.setHorizontalGroup(
            menuPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, menuPanelLayout.createSequentialGroup()
                .addContainerGap(120, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(114, 114, 114))
        );
        menuPanelLayout.setVerticalGroup(
            menuPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, menuPanelLayout.createSequentialGroup()
                .addContainerGap(118, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        carder.add(menuPanel, "menu");

        jLabel6.setFont(jLabel6.getFont().deriveFont((float)24));
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("Room name:");

        textServerName.setFont(textServerName.getFont().deriveFont((float)24));
        textServerName.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        textServerName.setText("lol");

        buttonServerNameAccept.setFont(buttonServerNameAccept.getFont().deriveFont((float)20));
        buttonServerNameAccept.setIcon(new javax.swing.ImageIcon(getClass().getResource("/lbs/gfx/5.png"))); // NOI18N
        buttonServerNameAccept.setText("OK");

        jLabel7.setFont(jLabel7.getFont().deriveFont(jLabel7.getFont().getStyle() | java.awt.Font.BOLD, 24));
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel7.setText("Create Room");

        jLabel8.setFont(jLabel8.getFont().deriveFont((float)24));
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel8.setText("Max player (2-4):");

        spinnerMaxPlayer.setFont(spinnerMaxPlayer.getFont().deriveFont((float)24));
        spinnerMaxPlayer.setModel(new javax.swing.SpinnerNumberModel(2, 2, 4, 1));

        buttonX.setFont(buttonX.getFont().deriveFont((float)20));
        buttonX.setIcon(new javax.swing.ImageIcon(getClass().getResource("/lbs/gfx/10.png"))); // NOI18N
        buttonX.setText("Back");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(186, 186, 186)
                .addComponent(spinnerMaxPlayer, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(textServerName)
                    .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(105, 105, 105)
                        .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 252, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 99, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addComponent(buttonX, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(buttonServerNameAccept, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel7)
                .addGap(40, 40, 40)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(textServerName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(spinnerMaxPlayer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(40, 40, 40)
                        .addComponent(buttonServerNameAccept))
                    .addComponent(buttonX))
                .addContainerGap())
        );

        javax.swing.GroupLayout createPanelLayout = new javax.swing.GroupLayout(createPanel);
        createPanel.setLayout(createPanelLayout);
        createPanelLayout.setHorizontalGroup(
            createPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(createPanelLayout.createSequentialGroup()
                .addGap(121, 121, 121)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(129, Short.MAX_VALUE))
        );
        createPanelLayout.setVerticalGroup(
            createPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(createPanelLayout.createSequentialGroup()
                .addGap(65, 65, 65)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(106, Short.MAX_VALUE))
        );

        carder.add(createPanel, "create");

        jLabel3.setFont(jLabel3.getFont().deriveFont((float)24));
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("Enter room ip address:");

        textHostname.setFont(textHostname.getFont().deriveFont((float)24));
        textHostname.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        textHostname.setText("lolol");

        buttonHostnameAccept.setFont(buttonHostnameAccept.getFont().deriveFont((float)20));
        buttonHostnameAccept.setIcon(new javax.swing.ImageIcon(getClass().getResource("/lbs/gfx/5.png"))); // NOI18N
        buttonHostnameAccept.setText("OK");

        jLabel5.setFont(jLabel5.getFont().deriveFont(jLabel5.getFont().getStyle() | java.awt.Font.BOLD, 24));
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("Joining Room");

        buttonX1.setFont(buttonX1.getFont().deriveFont((float)20));
        buttonX1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/lbs/gfx/10.png"))); // NOI18N
        buttonX1.setText("Back");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(textHostname)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addGap(0, 148, Short.MAX_VALUE)
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 208, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(157, 157, 157))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addComponent(buttonX1, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(buttonHostnameAccept, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5)
                .addGap(26, 26, 26)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(textHostname, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 33, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(buttonHostnameAccept)
                    .addComponent(buttonX1))
                .addContainerGap())
        );

        javax.swing.GroupLayout joinPanelLayout = new javax.swing.GroupLayout(joinPanel);
        joinPanel.setLayout(joinPanelLayout);
        joinPanelLayout.setHorizontalGroup(
            joinPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, joinPanelLayout.createSequentialGroup()
                .addContainerGap(100, Short.MAX_VALUE)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(93, 93, 93))
        );
        joinPanelLayout.setVerticalGroup(
            joinPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(joinPanelLayout.createSequentialGroup()
                .addGap(126, 126, 126)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(143, Short.MAX_VALUE))
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
        buttonStart.setIcon(new javax.swing.ImageIcon(getClass().getResource("/lbs/gfx/4.png"))); // NOI18N
        buttonStart.setText("Start");

        labelIp.setFont(labelIp.getFont().deriveFont((float)20));
        labelIp.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        labelIp.setText("Your ip address is:");

        buttonReady.setFont(buttonReady.getFont().deriveFont((float)20));
        buttonReady.setIcon(new javax.swing.ImageIcon(getClass().getResource("/lbs/gfx/5.png"))); // NOI18N
        buttonReady.setText("Ready");

        buttonLeave.setFont(buttonLeave.getFont().deriveFont((float)20));
        buttonLeave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/lbs/gfx/10.png"))); // NOI18N
        buttonLeave.setText("Leave");

        JLabal.setFont(JLabal.getFont().deriveFont((float)16));
        JLabal.setText("Room");
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
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, lobbyPanelLayout.createSequentialGroup()
                        .addGroup(lobbyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(labelIp, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, lobbyPanelLayout.createSequentialGroup()
                                .addGap(338, 338, 338)
                                .addComponent(buttonLeave, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(buttonReady, javax.swing.GroupLayout.DEFAULT_SIZE, 121, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(buttonStart, javax.swing.GroupLayout.DEFAULT_SIZE, 110, Short.MAX_VALUE)))
                        .addGap(12, 12, 12)))
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 92, Short.MAX_VALUE)
                .addGroup(lobbyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(buttonStart)
                    .addComponent(buttonReady)
                    .addComponent(buttonLeave))
                .addContainerGap())
        );

        carder.add(lobbyPanel, "lobby");

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

        tableResult.setFont(tableResult.getFont().deriveFont((float)20));
        jScrollPane3.setViewportView(tableResult);

        jLabel9.setFont(jLabel9.getFont().deriveFont(jLabel9.getFont().getStyle() | java.awt.Font.BOLD, 24));
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel9.setText("RESULT");

        buttonX2.setFont(buttonX2.getFont().deriveFont((float)24));
        buttonX2.setText("Okay, cool");

        javax.swing.GroupLayout resultPanelLayout = new javax.swing.GroupLayout(resultPanel);
        resultPanel.setLayout(resultPanelLayout);
        resultPanelLayout.setHorizontalGroup(
            resultPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(resultPanelLayout.createSequentialGroup()
                .addGap(133, 133, 133)
                .addGroup(resultPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane3)
                    .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(141, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, resultPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(buttonX2, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(282, 282, 282))
        );
        resultPanelLayout.setVerticalGroup(
            resultPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(resultPanelLayout.createSequentialGroup()
                .addGap(7, 7, 7)
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 304, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 80, Short.MAX_VALUE)
                .addComponent(buttonX2)
                .addContainerGap())
        );

        carder.add(resultPanel, "result");

        gamePanel.setLayout(new java.awt.BorderLayout());

        gimpane.setFocusable(false);
        gimpane.setLayout(new java.awt.GridLayout(2, 3, 2, 2));

        left.setFocusable(false);
        left.setMinimumSize(new java.awt.Dimension(260, 260));
        left.setPreferredSize(new java.awt.Dimension(260, 260));
        left.setRequestFocusEnabled(false);
        left.setLayout(new java.awt.GridLayout(10, 10));
        gimpane.add(left);

        middle.setFocusable(false);
        middle.setMinimumSize(new java.awt.Dimension(260, 260));
        middle.setPreferredSize(new java.awt.Dimension(260, 260));
        middle.setRequestFocusEnabled(false);
        middle.setLayout(new java.awt.GridLayout(10, 10));
        gimpane.add(middle);

        right.setFocusable(false);
        right.setMinimumSize(new java.awt.Dimension(260, 260));
        right.setPreferredSize(new java.awt.Dimension(260, 260));
        right.setRequestFocusEnabled(false);
        right.setLayout(new java.awt.GridLayout(10, 10));
        gimpane.add(right);

        controlL.setFocusable(false);
        controlL.setRequestFocusEnabled(false);

        tableStandins.setFont(tableStandins.getFont().deriveFont((float)20));
        tableStandins.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        tableStandins.setFocusable(false);
        jScrollPane1.setViewportView(tableStandins);

        jLabel2.setFont(jLabel2.getFont().deriveFont(jLabel2.getFont().getStyle() | java.awt.Font.BOLD, 20));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("STANDINGS");
        jLabel2.setFocusable(false);

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

        gimpane.add(controlL);

        self.setFocusable(false);
        self.setMinimumSize(new java.awt.Dimension(260, 260));
        self.setPreferredSize(new java.awt.Dimension(260, 260));
        self.setRequestFocusEnabled(false);
        self.setLayout(new java.awt.GridLayout(10, 10));
        gimpane.add(self);

        controlR.setFocusable(false);
        controlR.setRequestFocusEnabled(false);

        labelHint.setFocusable(false);

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
                .addComponent(labelHint, javax.swing.GroupLayout.DEFAULT_SIZE, 198, Short.MAX_VALUE)
                .addContainerGap())
        );

        gimpane.add(controlR);

        gamePanel.add(gimpane, java.awt.BorderLayout.CENTER);

        jPanel6.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 40, 0, 40));
        jPanel6.setLayout(new java.awt.BorderLayout());

        p1Name.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        p1Name.setText("___");
        jPanel6.add(p1Name, java.awt.BorderLayout.WEST);

        p2Name.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        p2Name.setText("___");
        jPanel6.add(p2Name, java.awt.BorderLayout.CENTER);

        p3Name.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        p3Name.setText("___");
        jPanel6.add(p3Name, java.awt.BorderLayout.EAST);

        gamePanel.add(jPanel6, java.awt.BorderLayout.NORTH);

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
    private javax.swing.JButton buttonX2;
    private javax.swing.JPanel carder;
    private javax.swing.JPanel controlL;
    private javax.swing.JPanel controlR;
    private javax.swing.JPanel createPanel;
    private javax.swing.JPanel gamePanel;
    private javax.swing.JPanel gimpane;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
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
    private javax.swing.JLabel p1Name;
    private javax.swing.JLabel p2Name;
    private javax.swing.JLabel p3Name;
    private javax.swing.JPanel resultPanel;
    private javax.swing.JPanel right;
    private javax.swing.JPanel self;
    private javax.swing.JSpinner spinnerMaxPlayer;
    private javax.swing.JPanel startPanel;
    private javax.swing.JTable tableResult;
    private javax.swing.JTable tableRooster;
    private javax.swing.JTable tableStandins;
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
    public void mousePressed(MouseEvent e) { }

    @Override
    public void mouseReleased(MouseEvent e) { }

    @Override
    public void mouseEntered(MouseEvent e) { }

    @Override
    public void mouseExited(MouseEvent e) { }

    @Override
    public void keyTyped(KeyEvent e) { }

    @Override
    public void keyPressed(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {
        if(e.getKeyCode() == 'R'){
            isVertical = !isVertical;
            placingOnMouseEnter(lastPlacingIndex);
        }
    }
    
    private void marx(int player, int index){
        gridButton[player][index].setIcon(icons[NULL_ATTACKED]);
    }

    @Override
    public void displayTurn(boolean myTurn) {
        if(isDead) return;
        if(myTurn)
            labelHint.setText(yourTurnHint);
        else labelHint.setText(waitingForTurnHint);
    }

    @Override
    public void disableGrid(String enemyName, int enemyId, boolean isDead) {
        int index = -1;
        
        for(int i=0; i<pids.length; i++)
            if(pids[i].equals(enemyName, enemyId)){
                index = i;
                break;
            }
        
        if(index>-1){
            model.setGrid(pids[index], 0, 0, DISABLE_GRID);
            for (JButton tile : gridButton[index])
                tile.setEnabled(false);
            
            String text = "[ "+pids[index].name+" - "
                    +(isDead?"ANNIHILATED":"DISCONNECTED")+" ]";
        
            switch(model.getPlayerCount()){
                case 3:
                    switch(index){
                        case 0:
                            p1Name.setText(text);
                            left.setBackground(DarkRed);
                            break;
                        case 1:
                            p3Name.setText(text);
                            right.setBackground(DarkRed);
                            break;
                    }
                    break; 
                case 4:
                    switch(index){
                        case 0:
                            p1Name.setText(text);
                            left.setBackground(DarkRed);
                            break;
                        case 1:
                            p2Name.setText(text);
                            middle.setBackground(DarkRed);
                            break;
                        case 2:
                            p3Name.setText(text);
                            right.setBackground(DarkRed);
                            break;
                    }
                    break;
            }
        }
    }


}
