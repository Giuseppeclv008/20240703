package it.polito.emergency;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class EmergencyApp {

    private Map<String, Professional> professionals = new TreeMap<>();
    private Map<String, Integer> departments = new HashMap<>(); 
    private Map<String, Patient> patients = new HashMap<>();
    private Map<String, List<Patient>> patientsBysurname = new TreeMap<>();
    private Map<Integer, Report> reports = new TreeMap<>();
    private Map<String, List<Patient>> deptPat = new TreeMap<>();
    private int reportId = 0;
    private int dischargedPatients = 0;
    

    public enum PatientStatus {
        ADMITTED,
        DISCHARGED,
        HOSPITALIZED
    }
    
    /**
     * Add a professional working in the emergency room
     * 
     * @param id
     * @param name
     * @param surname
     * @param specialization
     * @param period
     * @param workingHours
     */
    public void addProfessional(String id, String name, String surname, String specialization, String period) {
    
        
        professionals.put(id, new Professional(id, name, period, specialization, surname, "24")) ;
    }

    /**
     * Retrieves a professional utilizing the ID.
     *
     * @param id The id of the professional.
     * @return A Professional.
     * @throws EmergencyException If no professional is found.
     */    
    public Professional getProfessionalById(String id) throws EmergencyException {
       if(!professionals.containsKey(id)) throw new EmergencyException("No such professional ");
        return professionals.get(id);
    }

    /**
     * Retrieves the list of professional IDs by their specialization.
     *
     * @param specialization The specialization to search for among the professionals.
     * @return A list of professional IDs who match the given specialization.
     * @throws EmergencyException If no professionals are found with the specified specialization.
     */    
    public List<String> getProfessionals(String specialization) throws EmergencyException {
        if(!professionals.values().stream()
                            .anyMatch(p-> p.getSpecialization()
                                            .equalsIgnoreCase(specialization))){
                                                throw new EmergencyException("No such spec");
 
                                            }
        return professionals.values().stream()
                                .filter(p-> p.getSpecialization().equalsIgnoreCase(specialization))
                                .map(Professional::getId)
                                .toList();
    }

    /**
     * Retrieves the list of professional IDs who are specialized and available during a given period.
     *
     * @param specialization The specialization to search for among the professionals.
     * @param period The period during which the professional should be available, formatted as "YYYY-MM-DD to YYYY-MM-DD".
     * @return A list of professional IDs who match the given specialization and are available during the period.
     * @throws EmergencyException If no professionals are found with the specified specialization and period.
     */    
    public List<String> getProfessionalsInService(String specialization, String period) throws EmergencyException {
        if(!professionals.values().stream()
                        .anyMatch(p-> p.getSpecialization()
                        .equalsIgnoreCase(specialization) && p.isInService(period))){
                            throw new EmergencyException("No such spec");

                        }



        return professionals.values().stream()
                                    .filter(p-> p.getSpecialization().equalsIgnoreCase(specialization) && p.isInService(period))
                                    .map(Professional::getId)
                                    .toList();
    }

    /**
     * Adds a new department to the emergency system if it does not already exist.
     *
     * @param name The name of the department.
     * @param maxPatients The maximum number of patients that the department can handle.
     * @throws EmergencyException If the department already exists.
     */
    public void addDepartment(String name, int maxPatients) {
        departments.put(name, maxPatients);
    }

    /**
     * Retrieves a list of all department names in the emergency system.
     *
     * @return A list containing the names of all registered departments.
     * @throws EmergencyException If no departments are found.
     */
    public List<String> getDepartments() throws EmergencyException {
        if(departments.isEmpty()) throw new EmergencyException("No departments");
        return departments.keySet().stream().toList();
    }

    /**
     * Reads professional data from a CSV file and stores it in the application.
     * Each line of the CSV should contain a professional's ID, name, surname, specialization, period of availability, and working hours.
     * The expected format of each line is: matricola, nome, cognome, specializzazione, period, orari_lavoro
     * 
     * @param reader The reader used to read the CSV file. Must not be null.
     * @return The number of professionals successfully read and stored from the file.
     * @throws IOException If there is an error reading from the file or if the reader is null.
     */
    public int readFromFileProfessionals(Reader reader) throws IOException {
       if(reader == null) throw new IOException();
        BufferedReader br = new BufferedReader(reader);
        boolean first = true;
        String line;
        int validProf = 0;

        

        while(( line = br.readLine()) != null){

            if (first) {
                first = false;
                continue;
            }

			line = line.trim();
			
			if(line.isEmpty()){
				continue;
			}   

			String[] parts = line.split("\\s*,\\s*") ; //con s* indichiamo che prima e dopo del punto e virgola possono esserci 0 o più caratteri di spazio 
			
			if(parts.length > 5 || parts.length < 5){
				continue;
			}; 

            if(parts.length > 0){
                ++validProf;
                professionals.put(parts[0], new Professional(parts[0], parts[1],  parts[4], parts[3], parts[2], "24"));
			}

		}



        return validProf;
    }

    /**
     * Reads department data from a CSV file and stores it in the application.
     * Each line of the CSV should contain a department's name and the maximum number of patients it can accommodate.
     * The expected format of each line is: nome_reparto, num_max
     * 
     * @param reader The reader used to read the CSV file. Must not be null.
     * @return The number of departments successfully read and stored from the file.
     * @throws IOException If there is an error reading from the file or if the reader is null.
     */    
    public int readFromFileDepartments(Reader reader) throws IOException {
        if(reader == null) throw new IOException();
        BufferedReader br = new BufferedReader(reader);
        boolean first = true;
        String line;
        int validDept = 0;

       
        
        while(( line = br.readLine()) != null){

            if (first) {
                first = false;
                continue;
            }

			line = line.trim();
			
			if(line.isEmpty()){
				continue;
			}   

			String[] parts = line.split("\\s*,\\s*") ; //con s* indichiamo che prima e dopo del punto e virgola possono esserci 0 o più caratteri di spazio 
			
			if(parts.length > 2 || parts.length < 2){
				continue;
			}; 

            if(parts.length > 0){
                ++validDept;
                departments.put(parts[0],Integer.parseInt(parts[1]));
			}

		}
        return validDept;
    }

    /**
     * Registers a new patient in the emergency system if they do not exist.
     * 
     * @param fiscalCode The fiscal code of the patient, used as a unique identifier.
     * @param name The first name of the patient.
     * @param surname The surname of the patient.
     * @param dateOfBirth The birth date of the patient.
     * @param reason The reason for the patient's visit.
     * @param dateTimeAccepted The date and time the patient was accepted into the emergency system.
     */
    public Patient addPatient(String fiscalCode, String name, String surname, String dateOfBirth, String reason, String dateTimeAccepted) {
        if(patients.containsKey(fiscalCode)) return patients.get(fiscalCode);
        
        Patient p = new Patient(fiscalCode, dateOfBirth, name, reason, PatientStatus.ADMITTED, surname, dateTimeAccepted);
        patients.put(fiscalCode, p );
        patientsBysurname.putIfAbsent(surname, new ArrayList<>());
        patientsBysurname.get(surname).add(p);
        return p;
    }

    /**
     * Retrieves a patient or patients based on a fiscal code or surname.
     *
     * @param identifier Either the fiscal code or the surname of the patient(s).
     * @return A single patient if a fiscal code is provided, or a list of patients if a surname is provided.
     *         Returns an empty collection if no match is found.
     */    
    public List<Patient> getPatient(String identifier) throws EmergencyException {
        if(!patients.containsKey(identifier) && !patientsBysurname.containsKey(identifier)) return new  ArrayList<>();
        List<Patient> pat = new ArrayList<>();

        if (patients.containsKey(identifier)){
            pat.add(patients.get(identifier));
            
        } 
           
        if(patientsBysurname.containsKey(identifier)){
            pat = patientsBysurname.values().stream()
                                            .flatMap(List::stream)
                                            .toList();
        }
       return pat;
    }

    /**
     * Retrieves the fiscal codes of patients accepted on a specific date, 
     * sorted by acceptance time in descending order.
     *
     * @param date The date of acceptance to filter the patients by, expected in the format "yyyy-MM-dd".
     * @return A list of patient fiscal codes who were accepted on the given date, sorted from the most recent.
     *         Returns an empty list if no patients were accepted on that date.
     */
    public List<String> getPatientsByDate(String date) {
        if(!patients.values().stream().anyMatch(p -> p.isAcceptanceDate(date))) return new ArrayList<>();

        return patients.values().stream()
                                .filter(p -> p.isAcceptanceDate(date))
                                .sorted(Comparator.comparing(Patient::getSurname)
                                                    .thenComparing(Patient::getName))
                                .map(Patient::getFiscalCode)
                                .toList();
    }

    /**
     * Assigns a patient to a professional based on the required specialization and checks availability during the request period.
     *
     * @param fiscalCode The fiscal code of the patient.
     * @param specialization The required specialization of the professional.
     * @return The ID of the assigned professional.
     * @throws EmergencyException If the patient does not exist, if no professionals with the required specialization are found, or if none are available during the period of the request.
     */
    public String assignPatientToProfessional(String fiscalCode, String specialization) throws EmergencyException {
        if(!patients.containsKey(fiscalCode)) throw new EmergencyException();
        Patient p = patients.get(fiscalCode);
        LocalDate acceptanceDate =LocalDate.parse(p.getDateTimeAccepted()) ;

        if(!professionals.values().stream().anyMatch( pr-> pr.isInService(acceptanceDate) ) 
        || professionals.values().stream()
        .filter(pr-> pr.getSpecialization().equalsIgnoreCase(specialization))
        .toList()
        .isEmpty() || (!professionals.values().stream().anyMatch( pr-> pr.isInService(acceptanceDate) ) && professionals.values().stream()
        .filter(pr-> pr.getSpecialization().equalsIgnoreCase(specialization))
        .toList()
        .isEmpty() ) ) throw new EmergencyException(" no such prof service");

        return professionals.values().stream()
                                    .filter(pr->pr.isInService(acceptanceDate) 
                                    && pr.getSpecialization().equals(specialization))
                                    .map(Professional::getId)
                                    .toList().get(0);
    }

    public Report saveReport(String professionalId, String fiscalCode, String date, String description) throws EmergencyException {
        if(!professionals.containsKey(professionalId) ) throw new EmergencyException();
        Report rep = new Report(date, description, fiscalCode, Integer.toString(++reportId), professionalId);
        reports.put(reportId, rep);
        return rep;
    }

    /**
     * Either discharges a patient or hospitalizes them depending on the availability of space in the requested department.
     * 
     * @param fiscalCode The fiscal code of the patient to be discharged or hospitalized.
     * @param departmentName The name of the department to which the patient might be admitted.
     * @throws EmergencyException If the patient does not exist or if the department does not exist.
     */
    public void dischargeOrHospitalize(String fiscalCode, String departmentName) throws EmergencyException {
        if(!patients.containsKey(fiscalCode) ) throw new EmergencyException("No such patient");
        if(!departments.containsKey(departmentName)) throw new EmergencyException("No such dept");

        if(departments.get(departmentName).equals(0)) { ++dischargedPatients; patients.get(fiscalCode).setStatus(PatientStatus.DISCHARGED); return;}

        patients.get(fiscalCode).setStatus(PatientStatus.HOSPITALIZED);
        deptPat.putIfAbsent(departmentName, new ArrayList<>());
        deptPat.get(departmentName).add(patients.get(fiscalCode));
        int num = departments.get(departmentName) -1;
        departments.put(departmentName, num) ;
      
    }

    /**
     * Checks if a patient is currently hospitalized in any department.
     *
     * @param fiscalCode The fiscal code of the patient to verify.
     * @return 0 if the patient is currently hospitalized, -1 if not hospitalized or discharged.
     * @throws EmergencyException If no patient is found with the given fiscal code.
     */
    public int verifyPatient(String fiscalCode) throws EmergencyException{
        //0 se il paziente è soltanto ammesso 
        if(!patients.containsKey(fiscalCode)){ throw new EmergencyException("No such patient");} 

        if(patients.get(fiscalCode).getStatus().equals(PatientStatus.DISCHARGED)
            || patients.get(fiscalCode).getStatus().equals(PatientStatus.ADMITTED)) 
            { return 0;}

        if(patients.get(fiscalCode).getStatus().equals(PatientStatus.HOSPITALIZED))
            return 1;
        
        return 0;
        
    }

    /**
     * Returns the number of patients currently being managed in the emergency room.
     *
     * @return The total number of patients in the system.
     */    
    public int getNumberOfPatients() {

        return patients.values().stream()
                                .filter(pat-> pat.getStatus().equals(PatientStatus.ADMITTED))
                                .toList()
                                .size();
    }

    /**
     * Returns the number of patients admitted on a specified date.
     *
     * @param dateString The date of interest provided as a String (format "yyyy-MM-dd").
     * @return The count of patients admitted on that date.
     */
    public int getNumberOfPatientsByDate(String date) {
       
        return patients.values().stream()
                                    .filter(pat-> pat.isAcceptanceDate(date))
                                    .toList()
                                    .size();
    }

    public int getNumberOfPatientsHospitalizedByDepartment(String departmentName) throws EmergencyException {
        if(!deptPat.containsKey(departmentName)) throw new EmergencyException("no such dept to pat");
        return deptPat.get(departmentName).size();
    }

    /**
     * Returns the number of patients who have been discharged from the emergency system.
     *
     * @return The count of discharged patients.
     */
    public int getNumberOfPatientsDischarged() {
        return dischargedPatients;
    }

    /**
     * Returns the number of discharged patients who were treated by professionals of a specific specialization.
     *
     * @param specialization The specialization of the professionals to filter by.
     * @return The count of discharged patients treated by professionals of the given specialization.
     */
    public int getNumberOfPatientsAssignedToProfessionalDischarged(String specialization) {
 
        
        return       professionals.values().stream()
                                        .filter(p->p.getSpecialization().equalsIgnoreCase(specialization))
                                        .mapToInt(Professional::countDischarged)
                                        .sum();
    }
}
