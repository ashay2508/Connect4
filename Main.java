package ashay.pro.connect4;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Main extends Application {

    private Controller controller;

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("game.fxml"));

        GridPane rootgridpane = loader.load();

        controller = loader.getController();
        controller.createPlayground();;

        MenuBar mb = createmenu();
        mb.prefWidthProperty().bind(primaryStage.widthProperty());

        Pane menupane = (Pane) rootgridpane.getChildren().get(0);
        menupane.getChildren().add(mb);

        Scene scene = new Scene(rootgridpane);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Connect Four");
        primaryStage.setResizable(false);
        primaryStage.show();
    }
    private MenuBar createmenu(){

        Menu filemenu = new Menu("File");
        MenuItem newgame = new MenuItem("New Game");

        newgame.setOnAction(event -> controller.resetGame());

        MenuItem resetgame = new MenuItem("Reset Game");

        resetgame.setOnAction(event -> controller.resetGame());

        SeparatorMenuItem smi = new SeparatorMenuItem();
        MenuItem quitgame = new MenuItem("Quit Game");
        quitgame.setOnAction(event -> quit());
        filemenu.getItems().addAll(newgame , resetgame, smi, quitgame);

        Menu helpmenu = new Menu("Help");
        MenuItem about1 = new MenuItem("About Connect 4");
        about1.setOnAction(event -> aboutgame());
        SeparatorMenuItem smu = new SeparatorMenuItem();
        MenuItem about2 = new MenuItem("About Me");
        about2.setOnAction(event -> aboutme());

        helpmenu.getItems().addAll(about1, smu, about2);
        MenuBar mb = new MenuBar();
        mb.getMenus().addAll(filemenu, helpmenu);
        return mb;
    }

    private void aboutme() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About The Creator");
        alert.setHeaderText("Ashay Srivastava");
        alert.setContentText("This game has been developed by Ashay Srivastava who presently is a " +
                "final year Computer Science and Engineering student at Sir MVIT!");
        alert.show();
    }

    private void aboutgame() {

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About Connect Four");
        alert.setHeaderText("How To Play?");
        alert.setContentText("Connect Four is a two-player connection game in which the " +
                "players first choose a color and then take turns dropping colored discs " +
                "from the top into a seven-column, six-row vertically suspended grid." +
                "The pieces fall straight down, occupying the next available space within the " +
                "column. The objective of the game is to be the first to form a horizontal, vertical," +
                " or diagonal line of four of one's own discs. Connect Four is a solved game." +
                " The first player can always win by playing the right moves.");
        alert.show();

    }

    private void quit() {
        Platform.exit();
        System.exit(0);
    }

    private void resetGame() {
        //TODO
    }


    public static void main(String[] args) {

        launch(args);
    }
}
