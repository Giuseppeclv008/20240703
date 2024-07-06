package it.polito.emergency;

import java.time.LocalDate;
import java.time.LocalDateTime;

import it.polito.emergency.EmergencyApp.*;

public class Patient {

    private final String fiscalCode;
    private final String name;
    private final String surname;
    private final LocalDate dateOfBirth;
    private final String reason;
    private  PatientStatus status;
    private final LocalDate acceptanceDate;

    public Patient(String fiscalCode, String dateOfBirth, String name, String reason, PatientStatus status, String surname, String acceptanceDate){
        this.fiscalCode = fiscalCode;
        this.name = name;
        this.surname = surname;
        this.dateOfBirth = LocalDate.parse(dateOfBirth);
        this.reason = reason;
        this.status = status;
        this.acceptanceDate = LocalDate.parse(acceptanceDate);
        
    }
    public String getFiscalCode() {
        return fiscalCode;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getDateOfBirth() {
        return dateOfBirth.toString();
    }

    public String getReason() {
        return reason;
    }

    public String getDateTimeAccepted() {
        return acceptanceDate.toString();
    }

    public PatientStatus getStatus() {
        return status;
    }
    public void setStatus(PatientStatus newStatus){
        this.status = newStatus;
    }

    public boolean isAcceptanceDate(String date){
        LocalDate aDateIp = LocalDate.parse(date);
       
        return aDateIp.isEqual(acceptanceDate) ;
    }
}
