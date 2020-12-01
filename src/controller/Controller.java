package controller;

import domain.Invitatie;
import domain.Utilizator;
import domain.validators.ValidationException;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import service.UtilizatorService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;


public class Controller {

    private UtilizatorService service;
    ObservableList<Utilizator> utilizatori = FXCollections.observableArrayList();

    ObservableList<Utilizator> prieteni = FXCollections.observableArrayList();


    @FXML
    TableColumn<Utilizator, Long> tableColumnId;
    @FXML
    TableColumn<Utilizator, String> tableColumnNume;
    @FXML
    TableColumn<Utilizator, String> tableColumnPrenume;
    @FXML
    TableColumn<Utilizator, Long> tableColumnIdP;
    @FXML
    TableColumn<Utilizator, String> tableColumnNumeP;
    @FXML
    TableColumn<Utilizator, String> tableColumnPrenumeP;
    @FXML
    TableView<Utilizator> tableViewUtilizatori;
    @FXML
    TableView<Utilizator> tableViewPrieteni;
    @FXML
    TextField textFieldNume;
    @FXML
    TextField textFieldNume2;


    @FXML
    public void initialize() {
        tableColumnId.setCellValueFactory(new PropertyValueFactory<Utilizator, Long>("Id"));
        tableColumnNume.setCellValueFactory(new PropertyValueFactory<Utilizator, String>("lastName"));
        tableColumnPrenume.setCellValueFactory(new PropertyValueFactory<Utilizator, String>("firstName"));

        tableColumnIdP.setCellValueFactory(new PropertyValueFactory<Utilizator, Long>("Id"));
        tableColumnNumeP.setCellValueFactory(new PropertyValueFactory<Utilizator, String>("lastName"));
        tableColumnPrenumeP.setCellValueFactory(new PropertyValueFactory<Utilizator, String>("firstName"));

        tableViewUtilizatori.setItems(utilizatori);
        tableViewPrieteni.setItems(prieteni);
        textFieldNume.textProperty().addListener(x -> handleFilter1());
        textFieldNume2.textProperty().addListener(x -> handleFilter2());
    }

    private List<Utilizator> getUtilizatoriList() {
        List<Utilizator> list = new ArrayList<>();
        // service.getAll().forEach(list::add);
        Iterable<Utilizator> i = service.getAll();
        i.forEach(list::add);
        return list;
    }

    private void handleFilter1() {
        Predicate<Utilizator> numePredicate = x -> x.getLastName().startsWith(textFieldNume.getText());

        utilizatori.setAll(getUtilizatoriList()
                .stream()
                .filter(numePredicate)
                .collect(Collectors.toList()));

    }

    ;

    private void handleFilter2() {
        Predicate<Utilizator> numePredicate = x -> x.getLastName().startsWith(textFieldNume2.getText());

        prieteni.setAll(getUtilizatoriList()
                .stream()
                .filter(numePredicate)
                .collect(Collectors.toList()));

    }

    ;

    public void handleFriends(ActionEvent actionEvent) {
        Utilizator selected = tableViewUtilizatori.getSelectionModel().getSelectedItem();
        if (selected != null) {
            prieteni.setAll(selected.getFriends());
        } else {
            MessageAlert.showErrorMessage(null, "Nu ai selectat nimic");
        }
    }

    public void handleDeleteFriend(ActionEvent actionEvent) {
        Utilizator u = tableViewUtilizatori.getSelectionModel().getSelectedItem();
        Utilizator selected = tableViewPrieteni.getSelectionModel().getSelectedItem();
        if (selected != null) {
            service.deletePrietenie(u.getId(), selected.getId());
            prieteni.setAll(u.getFriends());

        } else {
            MessageAlert.showErrorMessage(null, "Nu ai selectat nimic");
        }
    }

    public void handleUtilizatori(ActionEvent actionEvent) {

        Utilizator selected = tableViewUtilizatori.getSelectionModel().getSelectedItem();

        if (selected != null) {
            prieteni.setAll(getUtilizatoriList());
        } else {
            MessageAlert.showErrorMessage(null, "Nu ai selectat nimic");
        }
    }

    public void handleAddFriend(ActionEvent actionEvent) {
        Utilizator selected = tableViewUtilizatori.getSelectionModel().getSelectedItem();
        Utilizator u = tableViewPrieteni.getSelectionModel().getSelectedItem();
        if (selected != null & u != null) {
            try {
                service.addInvitatie(selected.getId(), u.getId());
                MessageAlert.showMessage(null, "Invitatia a fost trimisa!");
            } catch (ValidationException e) {
                MessageAlert.showWarningMessage(null, e.getMessage());
            }
        } else {
            MessageAlert.showErrorMessage(null, "Nu ai selectat cei doi utilizatori");
        }

    }

    public void handleInvitatii(ActionEvent actionEvent) {
        Utilizator selected = tableViewUtilizatori.getSelectionModel().getSelectedItem();
        if (selected != null) {
            showMessageInvitatiiDialog(selected);
        } else {
            MessageAlert.showErrorMessage(null, "Nu ai selectat un utilizator");

        }
    }

    public void showMessageInvitatiiDialog(Utilizator u) {
        try {
            FXMLLoader loaderI = new FXMLLoader();
            loaderI.setLocation(getClass().getResource("/sample/invitatii.fxml"));
            AnchorPane rootI = loaderI.load();
            Stage dialogStageI = new Stage();
            dialogStageI.setTitle("Friend request");
            Scene sceneI = new Scene(rootI, 400, 250);
            dialogStageI.setScene(sceneI);
            InvitatiiController invitatiiController = loaderI.getController();
            invitatiiController.setInvitatiiService(service, dialogStageI, u);
            dialogStageI.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void setService(UtilizatorService service) {
        this.service = service;
        utilizatori.setAll(getUtilizatoriList());

    }



}


