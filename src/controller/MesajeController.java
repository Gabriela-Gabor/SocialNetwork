package controller;

import domain.Invitatie;
import domain.Message;
import domain.Utilizator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import service.UtilizatorService;

import java.time.LocalDateTime;

public class MesajeController {

    ObservableList<Message> mesaje= FXCollections.observableArrayList();;

    @FXML
    TableView<Message> mesajeTableView;
    @FXML
    TableColumn<Message, Utilizator> tableColumnFrom;
    @FXML
    TableColumn<Message,String> tableColumnMesaj;
    @FXML
    TableColumn<Message, LocalDateTime> tableColumnDate;
    @FXML
    TextField textFieldMesaj;


    private UtilizatorService service;
    Stage dialogStage;
    Utilizator utilizator;

    @FXML
    private void initialize(){
        tableColumnFrom.setCellValueFactory(new PropertyValueFactory<Message, Utilizator>("from"));
        tableColumnMesaj.setCellValueFactory(new PropertyValueFactory<Message, String>("message"));
        tableColumnDate.setCellValueFactory(new PropertyValueFactory<Message, LocalDateTime>("data"));

        mesajeTableView.setItems(mesaje);

    }

    public void setMesajeService(UtilizatorService service,Stage stage,Utilizator u){
        this.service=service;
        this.dialogStage=stage;
        this.utilizator=u;
        mesaje.setAll(service.mesajePrimite(u.getId()));
    }

    public void replyMessage(ActionEvent actionEvent) {
       Message m = mesajeTableView.getSelectionModel().getSelectedItem();
        if (m != null ) {
            String mesaj = textFieldMesaj.getText();
            service.addReplyMessage(m.getId(), utilizator.getId(), mesaj,LocalDateTime.now());
            textFieldMesaj.clear();
        }
    }
}
