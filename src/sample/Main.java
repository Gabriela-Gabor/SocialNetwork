package sample;

import controller.Controller;
import domain.*;
import domain.validators.InvitatieValidator;
import domain.validators.MessageValidator;
import domain.validators.PrietenieValidator;
import domain.validators.UtilizatorValidator;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import repository.Repository;
import repository.file.InvitatieFile;
import repository.file.MessageFile;
import repository.file.PrietenieFile;
import repository.file.UtilizatorFile;
import service.UtilizatorService;

import java.util.stream.Collectors;


public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        String fileNameU = "data/users.csv";
        String fileNameF = "data/friends.csv";
        String fileNameM = "data/message.csv";
        String fileNameI = "data/invitatii.csv";

        Repository<Long, Utilizator> userFileRepository = new UtilizatorFile(fileNameU, new UtilizatorValidator());
        Repository<Tuple<Long, Long>, Prietenie> friendsFileRepository = new PrietenieFile(fileNameF, userFileRepository, new PrietenieValidator());
        Repository<Long, Message> messageFileRepository = new MessageFile(fileNameM, new MessageValidator(), userFileRepository);
        Repository<Long, Invitatie> invitatieFileRepository = new InvitatieFile(fileNameI, new InvitatieValidator(), userFileRepository, friendsFileRepository);

        UtilizatorService service = new UtilizatorService(userFileRepository, friendsFileRepository,messageFileRepository,invitatieFileRepository);

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("sample.fxml"));
        AnchorPane root = loader.load();


        Controller ctrl = loader.getController();
        ctrl.setService(service);

        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 790, 500));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
