package corona.games.client.view;

import javafx.application.Platform;
import javafx.beans.binding.MapExpression;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Observable;
import java.util.UUID;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.logging.Logger;

import corona.games.client.view.lobby.GameConfigurator;
import corona.games.communication.GameInfo;
import corona.games.communication.Message;
import corona.games.communication.Message.MessageType;
import corona.games.logger.Loggable;
public class ChatBox implements Loggable {

    private Stage stage;
    private TextArea transcript;
    private TextField messageInputBox;
    private Button sendButton;
    private Button createGame;
    private Button joinGame;
    private LinkedBlockingDeque<Message> chatMessages;
    private LinkedBlockingDeque<Message> outgoingMessages;
    private UUID clientID;
    private String username;
    private ChoiceBox<String> gameChoices;
    private ListView<GameInfo> openGames;
    private GameInfo selectedGame;
    private Logger logger;

    public ChatBox(LinkedBlockingDeque<Message> chatMessages, LinkedBlockingDeque<Message> outgoingMessages, TextArea transcript,
                   UUID clientID, String username) {
        this.chatMessages = chatMessages;
        this.outgoingMessages = outgoingMessages;
        this.transcript = transcript;
        this.clientID = clientID;
        this.username = username;
    }

    public void setID(UUID clientID) {
        this.clientID = clientID;
    }

    /**
     * Sets up the TextArea transcipt to display the chat messages
     */
    private void setUpTranscript() {
        transcript.setPrefRowCount(30);
        transcript.setPrefColumnCount(60);
        transcript.setWrapText(true);
        transcript.setEditable(false);
    }

    /**
     * creates EventHandler for sending message when hitting ENTER in message area
     */
    private void sendOnEnter() {
        messageInputBox.setOnKeyReleased(key -> {
            if(key.getCode() == KeyCode.ENTER) {
                String message = messageInputBox.getText();
                messageInputBox.clear();

                if(message == null || message.equals("")) {
                    // TODO show in GUI
                    System.out.println("Can't be empty message");
                } else {
                    System.out.println(clientID);
                    Message m = new Message(MessageType.CHAT_MSG, message, clientID, username);
                try {
                    chatMessages.put(m);
                    outgoingMessages.put(m);
                    // System.out.println("put a message");
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                }
            }
        });
    }

    /**
     * creates EventHandler for sending message when clicking send
     */
    private void sendOnClick() {
        sendButton.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                // TODO Auto-generated method stub
                String message = messageInputBox.getText();
                messageInputBox.clear();
                Message m = new Message(MessageType.CHAT_MSG, message, clientID, username);
                try {
                    System.out.println("here in the chat roon");
                    chatMessages.put(m);
                    outgoingMessages.put(m);
                    // System.out.println("put a message");
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }

        });
    }

    /**
     * Sets up everything for sending messages
     */
    private void setUpMessageSendingSection() {
        sendButton = new Button("send");
        messageInputBox = new TextField();
        messageInputBox.setPrefColumnCount(40);
        sendOnEnter();
        sendOnClick();
        messageInputBox.setEditable(true);
        messageInputBox.setDisable(false);
    }

    private void setUpGameOptions() {
        this.gameChoices = new ChoiceBox<>();
        this.gameChoices.getItems().addAll("Risk","Othello","Settlers","Scrabble");
        this.gameChoices.getSelectionModel().selectedItemProperty().addListener((v,oldV,newV) ->{
            System.out.println("Just picked " + newV);
        });
        createGame = new Button("Create Game");
        createGame.setOnAction(e -> startGame());
    }

    private void startGame() {
        String selectedGameName = this.gameChoices.getSelectionModel().getSelectedItem();
        String gameNameFromHost = new GameConfigurator().display();
        GameInfo newGame = new GameInfo(selectedGameName,gameNameFromHost,this.username,1,2,2);
        openGames.getItems().add(newGame);
        this.selectedGame = newGame;
        this.stage.close();
    }

    private void setUpOpenGames() {
        openGames = new ListView<>();
        joinGame = new Button("Join Game");
        joinGame.setOnAction(e -> joinGame());
        // GameInfo gm = new GameInfo("Othello", "firstgame", "Daniel", 3, 2, 4);
        // openGames.getItems().addAll(gm, gm, gm, gm, gm, gm, gm, gm, gm, gm, gm, gm, gm, 
        // gm, gm, gm, gm, gm, gm, gm, gm, gm, gm, gm);

    }

    private void joinGame() {
        this.selectedGame = openGames.getSelectionModel().getSelectedItem();
        this.stage.close();
    }

    private HBox getTextBoxSection() {
        HBox bottom = new HBox(8, new Label(""), messageInputBox, sendButton);
        HBox.setHgrow(messageInputBox, Priority.ALWAYS);
        // HBox.setMargin(quitButton, new Insets(0,0,0,50));
        bottom.setPadding(new Insets(8));
        bottom.setStyle("-fx-border-color: black; -fx-border-width:2px");

        return bottom;
    }

    private VBox getCreateGameSection() {
        VBox rightLayout = new VBox(15,new Label("Start new game"),gameChoices,createGame);
        rightLayout.setStyle("-fx-border-color: blue; -fx-border-width:1px");
        return rightLayout;
    }

    private VBox getOpenGameSection() {
        VBox centerLayout = new VBox(15, new Label("Open Games"), openGames,joinGame);  
        centerLayout.setAlignment(Pos.CENTER);
        return centerLayout;
    }

    private BorderPane setUpLayout() {

        HBox bottom = getTextBoxSection();
        VBox rightLayout = getCreateGameSection();
        VBox centerLayout = getOpenGameSection();
       
        BorderPane root = new BorderPane();
        root.setLeft(transcript);
        root.setRight(rightLayout);
        root.setCenter(centerLayout);
        root.setBottom(bottom);
        return root;
    }
    
    /**
     * Displays the Lobby 
     * @return the game the client has either selected or created
     */
    public GameInfo display() {
        
        stage = new Stage();
        stage.setOnCloseRequest(e -> {
            Platform.exit();
            System.exit(0);
        });

        setUpTranscript();

        setUpMessageSendingSection();

        setUpGameOptions();
        
        setUpOpenGames();

        BorderPane layout = setUpLayout();
        stage.setScene(new Scene(layout));
        stage.showAndWait();
        return selectedGame;
    }

    @Override
    public void setLogger(Logger logger) {
        this.logger = logger;
    }

    @Override
    public void log(String msg) {
        if(this.logger == null) return;
        else this.logger.info(msg);
    }
}