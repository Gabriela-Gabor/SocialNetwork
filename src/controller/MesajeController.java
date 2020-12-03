package controller;

import domain.Invitatie;
import domain.Message;
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

public class MesajeController {

    ObservableList<Message> mesaje= FXCollections.observableArrayList();;

    @FXML
    TableView<Message> mesajeTableView;
    @FXML
    TableColumn<Message, Long> tableColumnId;
    @FXML
    TableColumn<Message, Long> tableColumnFromId;
    @FXML
    TableColumn<Message, String> tableColumnFromFirstName;
    @FXML
    TableColumn<Message, Long> tableColumnToId;
    @FXML
    TableColumn<Message, String> tableColumnToFirstName;
    @FXML
    TableColumn<Message,String> tableColumnMesaj;
    @FXML
    TableColumn<Message, LocalDateTime> tableColumnDate;


    private UtilizatorService service;
    Stage dialogStage;
    Utilizator utilizator;

    @FXML
    private void initialize(){
        tableColumnId.setCellValueFactory(new PropertyValueFactory<Message, Long>("Id"));

        mesajeTableView.setItems(mesaje);

    }

    public void setMesajeService(UtilizatorService service,Stage stage,Utilizator u){
        this.service=service;
        this.dialogStage=stage;
        this.utilizator=u;
        mesaje.setAll(service.mesajePrimite(u.getId()));
    }

}
