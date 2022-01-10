package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.CovidVaccineAppointment;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class AppointmentController {

    @FXML
    private ListView<CovidVaccineAppointment> covidVaccineList;
    final ObservableList<CovidVaccineAppointment> covidVaccine = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        EventBus.getDefault().register(this);


        covidVaccineList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        ReserveFreeAppointmentRequest requestFreeAppointment = new ReserveFreeAppointmentRequest();
        App.getClient().sendRequest(requestFreeAppointment);
    }

    @FXML
    void onReserve(ActionEvent event) {
        CovidVaccine selectedCovidVaccine = covidVaccineList.getSelectionModel().getSelectedItem();
        if (selectedCovidVaccine == null)
            return;
        ReserveAppointmentRequest requestReserveAppointment = new ReserveAppointmentRequest(selectedCovidVaccine);
        App.getClient().sendRequest(requestReserveAppointment);
    }

    @Subscribe
    public void ReserveAppointment(ReserveAppointmentResponse response) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Covid Vaccine Appointment Saved!", ButtonType.OK);
        alert.setHeaderText("Success");
        alert.show();
    }

//    @Subscribe
//    public void updateListOfCovidVaccine(ReserveAppointmentResponse response) {
//        for (CovidVaccine vaccine : response.covidVaccines) {
//            covidVaccineList.getItems().add(vaccines.toString());
//        }
//    }
}