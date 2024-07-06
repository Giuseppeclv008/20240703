package it.polito.emergency;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

import it.polito.emergency.EmergencyApp.PatientStatus;

public class Professional {

    private final String id;
    private final String name;
    private final String surname;
    private final String specialization;
    private final String period;
    private final LocalDate start;
    private final LocalDate end;
    private  List<Patient> patients = new LinkedList<>();

    private final String workingHours;

    public Professional(String id, String name, String period, String specialization, String surname, String workingHours){
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.specialization = specialization;
        this.period = period;
        String[] tokens = period.split("\\s*to\\s*"); 

        this.start = LocalDate.parse(tokens[0]);
        this.end = LocalDate.parse(tokens[1]);
        this.workingHours = workingHours;
        
    }
    public String getId() {
        
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getSpecialization() {
        return specialization;
    }

    public String getPeriod() {
        return period;
    }

    public String getWorkingHours() {
        return workingHours;
    }

    public boolean isInService(String period){
        String[] tokens = period.split("\\s*to\\s*");
        LocalDate startRequired = LocalDate.parse(tokens[0]);
        LocalDate endRequired = LocalDate.parse(tokens[0]);

        if((start.isBefore(startRequired) || start.isEqual(startRequired)) && 
            (end.isAfter(endRequired)|| end.isEqual(endRequired))) {
                return true;
            }

        return false;
    }

    public boolean isInService(LocalDate date){
        if (date.isAfter(start) && date.isBefore(end)|| (date.isEqual(start) || date.isEqual(end))){
            return true;
        } 

        return false;

    }

    public void addPatient(Patient patient){
        patients.add(patient);
    }
    public List<Patient> getPatients() {
        return patients;
    }

    public int countDischarged(){
      
        return  patients.stream()
                        .filter(pat-> pat.getStatus().equals(PatientStatus.DISCHARGED))
                        .toList()
                        .size();
 
    }
}
