package il.cshaifasweng.OCSFMediatorExample.entities;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
public class FamilyDoctor extends ClinicMember {
    public FamilyDoctor(String username, String password, int employeeNum, String firstName, String lastName, String email, String role) {
        super(AppointmentType.FAMILY, username, password, employeeNum, firstName, lastName, email, role);
        super.appointmentDuration = 15;
    }

    public FamilyDoctor() {
        super();
        super.appointmentDuration = 15;
    }

    public void addReceptionHours(Clinic clinic, LocalDate day, LocalTime t1, LocalTime t2) {
        LocalDateTime curTime = LocalDateTime.of(day, t1);
        while (t2.isAfter(LocalTime.from(curTime))) {
            FamilyDoctorAppointment newApp = new FamilyDoctorAppointment(this, curTime, clinic);
            if (this.checkIfFree(newApp.treatmentDateTime))
                this.addAppointment(newApp);
            curTime = curTime.plusMinutes(this.appointmentDuration);
        }
    }
}
