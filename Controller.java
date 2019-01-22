package ashay.pro.connect4;

import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.util.Duration;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Controller implements Initializable {

    private static final int col = 7;
    private static final int row = 6;
    private static final int circledia = 80;
    private static final String disccolor1 = "#24303E";
    private static final String disccolor2 = "#4CAA88";

    private static String p1 = "Player one";
    private static  String p2 = "Player two";

    private boolean isplayeroneturn = true;

    private Disc[][] inseteddiscarray = new Disc[row][col];

    @FXML
    public GridPane rootgridpane;

    @FXML
    public Pane inserteddiscpane;

    @FXML
    public Label playernamelabel;

    @FXML
    public TextField p1textfield, p2textfield;

    @FXML
    public Button setnamebtn;

    private boolean isallowedtoinsert = true;

    public void createPlayground(){


        Shape rectwithholes = createGameStructuralGrid();
        rootgridpane.add(rectwithholes,0,1);

        List<Rectangle> rectangleList = createclick();
        for (Rectangle rectangle:rectangleList) {
            rootgridpane.add(rectangle, 0, 1);
        }

        setnamebtn.setOnAction(event-> {
            p1 = p1textfield.getText();
            p2 = p2textfield.getText();
            System.out.println(p1);
            System.out.println(p2);
            playernamelabel.setText(p1);

        });

    }

    private Shape createGameStructuralGrid(){
        Shape rectwithholes = new Rectangle((col+1)*circledia,(row+1)*circledia);
        for(int r = 0; r < row; r++){
            for(int c = 0; c < col; c++){
                Circle circle = new Circle();
                circle.setRadius(circledia/2);
                circle.setCenterX(circledia/2);
                circle.setCenterY(circledia/2);
                circle.setSmooth(true);

                circle.setTranslateX(c * (circledia+5) + circledia/4);
                circle.setTranslateY(r * (circledia+5) + circledia/4);
                rectwithholes = Shape.subtract(rectwithholes, circle);
            }
        }

        rectwithholes.setFill(Color.WHITE);
        return rectwithholes;

    }

    private List<Rectangle> createclick() {


        List<Rectangle> rectangleList = new ArrayList<>();

        for(int c=0 ; c<col; c++) {
            Rectangle rect = new Rectangle(circledia, (row + 1) * circledia);
            rect.setFill(Color.TRANSPARENT);
            rect.setTranslateX(c * (circledia + 5) + circledia / 4);
            rect.setOnMouseEntered(event -> rect.setFill(Color.valueOf("#eeeeee26")));
            rect.setOnMouseExited(event -> rect.setFill(Color.TRANSPARENT));
            final int colo = c;
            rect.setOnMouseClicked(event -> {
                if (isallowedtoinsert) {
                    isallowedtoinsert = false;
                    insertDisc(new Disc(isplayeroneturn), colo);

                }
            });
            rectangleList.add(rect);
        }

        return rectangleList;
    }

    private void insertDisc(Disc disc, int colom) {


        int rows = row-1;
        while(rows>=0){
            if(getDiscIfPresent(rows,colom)==null) break;
            rows--;
        }
        if(rows<0)
            return;
        inseteddiscarray[rows][colom] = disc;
        inserteddiscpane.getChildren().add(disc);

        disc.setTranslateX(colom * (circledia+5) + circledia/4 );

        int currow = rows;
        TranslateTransition tt = new TranslateTransition(Duration.seconds(0.5),disc);
        tt.setToY(rows * (circledia+5) + circledia/4 );
        tt.setOnFinished(event -> {
            isallowedtoinsert = true;
            if(gameEnded(currow,colom)){
                gameOver();
                return;
            }
            isplayeroneturn = !isplayeroneturn;
            playernamelabel.setText(isplayeroneturn? p1:p2);
        });
        tt.play();
    }

    private boolean gameEnded(int ro,int co){

        List<Point2D> vp = IntStream.rangeClosed(ro-3,ro+3)
                .mapToObj(r -> new Point2D(r,co))
                .collect(Collectors.toList());

        List<Point2D> hp = IntStream.rangeClosed(co-3,co+3)
                .mapToObj(c -> new Point2D(ro,c))
                .collect(Collectors.toList());

        Point2D startp1 = new Point2D(ro-3,co+3);

        List<Point2D> dp1 = IntStream.rangeClosed(0,6)
                .mapToObj(i -> startp1.add(i,-i))
                .collect(Collectors.toList());


        Point2D startp2 = new Point2D(ro-3,co-3);
        List<Point2D> dp2 = IntStream.rangeClosed(0,6)
                .mapToObj(i -> startp2.add(i,i))
                .collect(Collectors.toList());

        boolean isended = checkCombinations(vp) || checkCombinations(hp) ||
                checkCombinations(dp1) || checkCombinations(dp2);

        return isended;
    }

    private boolean checkCombinations(List<Point2D> points) {
        int chain =0;
        for (Point2D point:points) {


            int rowindexforarray = (int) point.getX();
            int columnindexforarray = (int) point.getY();

            Disc disc = getDiscIfPresent(rowindexforarray,columnindexforarray);
            if(disc != null && disc.isplayeronemove == isplayeroneturn){

                chain++;
                if(chain==4) return true;
            }
            else chain = 0;

        }
        return false;
    }

    private Disc getDiscIfPresent(int ro,int co)
    {
        if(ro>=row || ro < 0 || co >= col || co < 0)
            return null;
        return inseteddiscarray[ro][co];
    }

    private void gameOver(){

        String winner =isplayeroneturn ? p1 :p2;
        System.out.println("Winner is: "+winner);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Connect Four");
        alert.setContentText("Want to play again?");

        ButtonType yesbtn = new ButtonType("Yes");
        ButtonType nobtn = new ButtonType("EXIT");

        alert.getButtonTypes().setAll(yesbtn,nobtn);
        Platform.runLater(() ->{
            Optional<ButtonType> btnclicked = alert.showAndWait();

            if(btnclicked.isPresent() && btnclicked.get() == yesbtn)
            {
                resetGame();
            }
            else
            {
                Platform.exit();
                System.exit(0);
            }
        } );

    }

    public void resetGame() {

        inserteddiscpane.getChildren().clear();
        for(int ro = 0; ro < inseteddiscarray.length; ro++)
        {
            for(int co = 0; co < inseteddiscarray[ro].length; co++)
            {
                inseteddiscarray[ro][co] = null;

            }
        }
        isplayeroneturn = true;
        p1textfield.clear();
        p2textfield.clear();
        playernamelabel.setText("Player one");
        createPlayground();
    }

    private static class Disc extends Circle{
        private final boolean isplayeronemove;
        public Disc(boolean isplayeronemove){
            this.isplayeronemove = isplayeronemove;
            setRadius(circledia/2);
            setFill(isplayeronemove? Color.valueOf(disccolor1): Color.valueOf(disccolor2));
            setCenterX(circledia/2);
            setCenterY(circledia/2);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
