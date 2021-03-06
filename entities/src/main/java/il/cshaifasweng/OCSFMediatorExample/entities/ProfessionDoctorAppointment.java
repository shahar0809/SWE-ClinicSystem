package il.cshaifasweng.OCSFMediatorExample.entities;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class ProfessionDoctorAppointment extends Appointment {
    public ProfessionDoctorAppointment() {
        super();
    }

    @Override
    public String getType() {
        return type.toString();
    }

    public AppointmentType getAppointmentType() {
        return type;
    }

    public void setType(AppointmentType type) {
        this.type = type;
    }

    public ProfessionDoctorAppointment(AppointmentType type, Patient patient, ProfessionDoctor professionDoctor, LocalDateTime treatmentDateTime, Clinic clinic) {
        super(patient, treatmentDateTime, professionDoctor, clinic);
        this.type = type;
    }

    public ProfessionDoctorAppointment(AppointmentType type, ProfessionDoctor professionDoctor, LocalDateTime treatmentDateTime, Clinic clinic) {
        super(treatmentDateTime, professionDoctor, clinic);
        this.type = type;
    }
}
