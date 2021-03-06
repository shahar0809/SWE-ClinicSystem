package il.cshaifasweng.OCSFMediatorExample.entities;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalTime;

@Entity
@Table(name = "Clinics")
public class Clinic implements Serializable {
    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    @Column(name = "id", nullable = false)
    protected int id;

    @Column(name = "name")
    protected String name;

    @Column(name = "openingHours")
    protected LocalTime openingHours;
    @Column(name = "closingHours")
    protected LocalTime closingHours;

    @Column(name = "CovidTestsStartHour")
    protected LocalTime CTstartHour;
    @Column(name = "CovidTestsEndHour")
    protected LocalTime CTendHour;

    @Column(name = "CovidVaccinesStartHour")
    protected LocalTime CVstartHour;
    @Column(name = "CovidVaccinesEndHour")
    protected LocalTime CVendHour;

    @Column(name = "FluVaccinesStartHour")
    protected LocalTime FVstartHour;
    @Column(name = "FluVaccinesEndHour")
    protected LocalTime FVendHour;

    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.DETACH})
    @JoinColumn(name = "clinicManager")
    ClinicManager clinicManager;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.DETACH})
    @JoinColumn(name = "hospitalManager")
    HospitalManager hospitalManager;

    public Clinic() {
    }

    public Clinic(String name, LocalTime openingHours, LocalTime closingHours) {
        this.name = name;
        this.openingHours = openingHours;
        this.closingHours = closingHours;

        // 00:00-00:00 as a default value...
        // if the service is available in the clinic, the manager should update the hours
        this.CTstartHour =  LocalTime.of(00, 00);
        this.CTendHour = LocalTime.of(00, 00);
        this.CVstartHour = LocalTime.of(00, 00);
        this.CVendHour = LocalTime.of(00, 00);
        this.FVstartHour = LocalTime.of(00, 00);
        this.FVendHour = LocalTime.of(00, 00);
    }

    public Clinic(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalTime getOpeningHours() {
        return openingHours;
    }

    public void setOpeningHours(LocalTime openingHours) {
        this.openingHours = openingHours;
    }

    public LocalTime getClosingHours() {
        return closingHours;
    }

    public void setClosingHours(LocalTime closingHours) {
        this.closingHours = closingHours;
    }

    public ClinicManager getClinicManager() {
        return clinicManager;
    }

    public void setClinicManager(ClinicManager clinicManager) {
        this.clinicManager = clinicManager;
    }

    public HospitalManager getHospitalManager() {
        return hospitalManager;
    }

    public void setHospitalManager(HospitalManager hospitalManager) {
        this.hospitalManager = hospitalManager;
    }

    public LocalTime getCovidTestStartHour() {
        return CTstartHour;
    }

    public void setCovidTestStartHour(LocalTime startHour) {
        this.CTstartHour = startHour;
    }

    public LocalTime getCovidTestEndHour() {
        return CTendHour;
    }

    public void setCovidTestEndHour(LocalTime endHour) {
        this.CTendHour = endHour;
    }

    public LocalTime getCovidVaccineStartHour() {
        return CVstartHour;
    }

    public void setCovidVaccineStartHour(LocalTime startHour) {
        this.CVstartHour = startHour;
    }

    public LocalTime getCovidVaccineEndHour() {
        return CVendHour;
    }

    public void setCovidVaccineEndHour(LocalTime endHour) {
        this.CVendHour = endHour;
    }

    public LocalTime getFluVaccineStartHour() {
        return FVstartHour;
    }

    public void setFluVaccineStartHour(LocalTime startHour) {
        this.FVstartHour = startHour;
    }

    public LocalTime getFluVaccineEndHour() {
        return FVendHour;
    }

    public void setFluVaccineEndHour(LocalTime endHour) {
        this.FVendHour = endHour;
    }
}
