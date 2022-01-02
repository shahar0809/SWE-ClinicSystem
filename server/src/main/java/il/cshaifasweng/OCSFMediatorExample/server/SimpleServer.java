package il.cshaifasweng.OCSFMediatorExample.server;

import il.cshaifasweng.OCSFMediatorExample.entities.Clinic;
import il.cshaifasweng.OCSFMediatorExample.entities.User;
import il.cshaifasweng.OCSFMediatorExample.requests.*;
import il.cshaifasweng.OCSFMediatorExample.response.*;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.AbstractServer;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.ConnectionToClient;
import il.cshaifasweng.OCSFMediatorExample.utils.SecureUtils;
import il.cshaifasweng.OCSFMediatorExample.utils.Hours;

import javax.persistence.NoResultException;
import java.io.IOException;
import java.time.LocalTime;
import java.util.Objects;

public class SimpleServer extends AbstractServer {
    protected DatabaseAccess dataBase;

    public SimpleServer(int port) {
        super(port);

        dataBase = DatabaseAccess.getInstance();

        if (dataBase.getAll(Clinic.class).isEmpty()) {
            dataBase.insertEntity(new Clinic("clinic1",  LocalTime.of(10,43)  ,  LocalTime.of(17,43)));
            dataBase.insertEntity(new Clinic("clinic2",  LocalTime.of(9,17)  ,  LocalTime.of(16,05)));
            dataBase.insertEntity(new Clinic("clinic3",  LocalTime.of(11,36)  ,  LocalTime.of(19,34)));
        }
    }

    @Override
    protected void handleMessageFromClient(Object msg, ConnectionToClient client) {
        if (msg instanceof GetAllClinicsRequest) {
            try {
                client.sendToClient(getALLClinicRequest((GetAllClinicsRequest) msg));
            } catch (IOException e) {
                System.out.println("Error - getALLClinicRequest");
            }
            return;
        }

        if (msg instanceof GetClinicRequest) {
            try {
                client.sendToClient(getClinicRequest((GetClinicRequest) msg));
            } catch (IOException e) {
                System.out.println("Error - getClinicRequest");
            }
            return;
        }

        if (msg instanceof UpdateActiveHoursRequest) {
            try {
                client.sendToClient(updateActiveHoursRequest((UpdateActiveHoursRequest) msg));
            } catch (IOException e) {
                System.out.println("Error - updateActiveHoursRequest");
            }
            return;
        }

        if (msg instanceof LoginRequest) {
            try {
                client.sendToClient(handleLoginRequest((LoginRequest) msg));
            } catch (IOException e) {
                System.out.println("Error - LoginRequest");
            }
            return;
        }

        if (msg instanceof RegisterRequest) {
            try {
                client.sendToClient(handleRegisterRequest((RegisterRequest) msg));
            } catch (IOException e) {
                System.out.println("Error - RegisterRequest");
            }
            return;
        }

        if (msg instanceof UpdateCovidTestHoursRequest) {
            try {
                client.sendToClient(UpdateCovidTestHoursRequest((UpdateCovidTestHoursRequest) msg));
            } catch (IOException e) {
                System.out.println("Error - UpdateCovidTestHoursRequest");
            }
        }

        if (msg instanceof GetCovidTestHoursRequest) {
            try {
                client.sendToClient(getCovidTestHoursRequest((GetCovidTestHoursRequest) msg));
            } catch (IOException e) {
                System.out.println("Error - getClinicRequest");
            }
        }
    }

    protected Response updateActiveHoursRequest(UpdateActiveHoursRequest request) {
        dataBase.setOpeningHours(dataBase.getClinic(request.clinicName), request.activeHours.openingHours);
        dataBase.setClosingHours(dataBase.getClinic(request.clinicName), request.activeHours.closingHours);
        UpdateActiveHoursResponse response = new UpdateActiveHoursResponse();
        return response;
    }

    protected Response getClinicRequest(GetClinicRequest request) {
        GetClinicResponse response = new GetClinicResponse(dataBase.getClinic(request.clinicName));
        return response;
    }

    protected Response getALLClinicRequest(GetAllClinicsRequest request) {
        GetAllClinicsResponse allClinics = new GetAllClinicsResponse(dataBase.getAll(Clinic.class));
        return allClinics;
    }

    protected Response handleLoginRequest(LoginRequest request) {
        User user;
        try {
            user = dataBase.getUser(request.username);
        } catch (NoResultException e) {
            return new LoginResponse("User not found!");
        }
        String securePassword = SecureUtils.getSecurePassword(request.password, user.getSALT());
        if (!Objects.equals(user.getHashPassword(), securePassword))
            return new LoginResponse("Incorrect password!");
        return new LoginResponse(user);
    }

    protected Response handleRegisterRequest(RegisterRequest request) {
        try {
            dataBase.getUser(request.username);
            return new RegisterResponse("Username is already taken!");
        } catch (NoResultException ignored) {
        }
        return new RegisterResponse(dataBase.createPatient(request.username, request.password));
    }


    /*
    -> change the hours of the Corona tests, and if there is a patient who had already
        set a test appointment not between the new hours:
        1. check if there is an available appointment:
            yes -> set this appointment to the patient and send him message about the change
                    (the patient can cancel or set another appointment as he want)
            no -> send message that the appointment has been canceled and that there is no available
                    appointments at the moment.
     */
    protected Response UpdateCovidTestHoursRequest(UpdateCovidTestHoursRequest request) {
        Clinic clinic = dataBase.getClinic(request.clinicName);
        LocalTime oldStartH = dataBase.getCovidTestStartHour(clinic);
        LocalTime oldEndH = dataBase.getCovidTestEndHour(clinic);
        LocalTime newStartH = request.activeHours.openingHours, newEndH = request.activeHours.closingHours;
        List<Appointment> Canceled=new ArrayList<Appointment>();

        if(oldStartH.isBefore(newStartH)){
            //Canceled.addAll(CanceledAppointments(oldStartH, newStartH, getFreeAppointments(...)));
        }
        if(newEndH.isBefore(oldEndH)){
            //Canceled.addAll(CanceledAppointments(newEndH, oldEndH, getFreeAppointments(...)));
        }

        // Update test hours
        dataBase.setCovidTestStartHour(clinic, newStartH);
        dataBase.setCovidTestEndHour(clinic, oldEndH);

        // go through the list and ask to get appointment if one of them didn't succeeded to get one
        // send to the rest that there is no available
        for(Appointment test : Canceled ){
            //if(GetCovidTestAppointment(...)== "...")//there is no available Appointments
            //{**send message**}
        }

        UpdateCovidTestHoursResponse response = new UpdateCovidTestHoursResponse();
        return response;
    }

    protected Response getCovidTestHoursRequest(GetCovidTestHoursRequest request) {
        GetCovidTestHoursResponse response = new GetCovidTestHoursResponse(new Hours(dataBase.getCovidTestStartHour(dataBase.getClinic(request.clinicName))
                ,dataBase.getCovidTestEndHour(dataBase.getClinic(request.clinicName))));
        return response;
    }

    /*
    -> help function: which return a list of all the appointments that is between (from, to)
     */
    private List<Appointment> CanceledAppointments (LocalTime from, LocalTime to, List<Appointment> appointmentsList){
        List<Appointment> Canceled=new ArrayList<Appointment>();
        LocalTime testH;
        for(Appointment test : appointmentsList ){
            testH = test.getTreatmentDateTime().toLocalTime();
            if (testH.isBefore(to) && testH.isAfter(from)){
                Canceled.add(test);
                // CancelAppointmentRequest(...);
            }
        }
        return Canceled;
    }
}
