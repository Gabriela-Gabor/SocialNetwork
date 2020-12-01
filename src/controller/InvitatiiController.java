package controller;

import domain.Invitatie;
import domain.Utilizator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import service.UtilizatorService;

import java.time.LocalDateTime;

public class InvitatiiController {

    ObservableList<Invitatie> invitatii= FXCollections.observableArrayList();;

    @FXML
    TableView<Invitatie> invitatieTableView;
    @FXML
    TableColumn<Invitatie, Long> tableColumnId;
    @FXML
    TableColumn<Invitatie, Utilizator> tableColumnFromId;
    @FXML
    TableColumn<Invitatie, String> tableColumnFromFirstName;
    @FXML
    TableColumn<Invitatie, String> tableColumnFromLastName;
    @FXML
    TableColumn<Invitatie, String> tableColumnStatus;
    @FXML
    TableColumn<Invitatie, LocalDateTime> tableColumnDate;


    private UtilizatorService service;
    Stage dialogStage;
    Utilizator utilizator;

    @FXML
    private void initialize(){
        tableColumnId.setCellValueFactory(new PropertyValueFactory<Invitatie, Long>("Id"));
       // tableColumnFromId.setCellValueFactory(new PropertyValueFactory<Invitatie, Utilizator>("u1"));
       /* tableColumnFromFirstName.setCellValueFactory(new PropertyValueFactory<Invitatie, String>("getU1().getFirstName()"));
        tableColumnFromLastName.setCellValueFactory(new PropertyValueFactory<Invitatie, String>("getU1().getLastName()"));*/
        tableColumnStatus.setCellValueFactory(new PropertyValueFactory<Invitatie, String>("status"));
       // tableColumnDate.setCellValueFactory(new PropertyValueFactory<Invitatie, LocalDateTime>("date"));

        invitatieTableView.setItems(invitatii);

    }

    public void setInvitatiiService(UtilizatorService service,Stage stage,Utilizator u){
        this.service=service;
        this.dialogStage=stage;
        this.utilizator=u;
        invitatii.setAll(service.getInvitatiiPrimite(u.getId()));
    }


}
