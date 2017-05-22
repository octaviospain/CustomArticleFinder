package cuni.software;

import javafx.application.*;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.stage.*;

/**
 * @author Octavio Calleya
 */
public class ViewRunner extends Application {

    private Controller controller;

    public static void main(String[] args){
        launch();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view.fxml"));
        BorderPane root = loader.load();
        controller = loader.getController();
        controller.setHostServices(getHostServices());
        primaryStage.setScene(new Scene(root));
        primaryStage.setMinWidth(750);
        primaryStage.setMinHeight(520);
        primaryStage.setTitle("Custom article finder");
        primaryStage.show();
    }
}