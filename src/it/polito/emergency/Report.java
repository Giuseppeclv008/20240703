package it.polito.emergency;

import java.time.LocalDate;

public class Report {

    private final String id;
    private final String professionalId;
    private final String fiscalCode;
    private final LocalDate date ;
    private final String description;

    public Report(String date, String description, String fiscalCode, String id, String professionalId){
        this.id = id;
        this.professionalId = professionalId;
        this.fiscalCode = fiscalCode;
        this.date = LocalDate.parse(date);

        this.description = description;
        
    }
     
    public String getId() {
        return id;
    }

    public String getProfessionalId() {
        return professionalId;
    }

    public String getFiscalCode() {
        return fiscalCode;
    }

    public String getDate() {
        return date.toString();
    }


    public String getDescription() {
        return description;
    }
}
