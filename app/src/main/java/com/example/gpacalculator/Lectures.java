package com.example.gpacalculator;

public class Lectures {
    private String name;
    private double ects;
    private double midtermg;
    private double finalg;
    private double makeupg;
    private double average;
    private String letterGrade;
    private String status;

    public Lectures(String name, double ects, double midtermg, double finalg, double makeupg, double average, String letterGrade, String status) {
        this.name = name;
        this.ects = ects;
        this.midtermg = midtermg;
        this.finalg = finalg;
        this.makeupg = makeupg;
        this.average = average;
        this.letterGrade = letterGrade;
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getEcts() {
        return ects;
    }

    public void setEcts(double ects) {
        this.ects = ects;
    }

    public double getMidtermg() {
        return midtermg;
    }

    public void setMidtermg(double midtermg) {
        this.midtermg = midtermg;
    }

    public double getFinalg() {
        return finalg;
    }

    public void setFinalg(double finalg) {
        this.finalg = finalg;
    }

    public double getMakeupg() {
        return makeupg;
    }

    public void setMakeupg(double makeupg) {
        this.makeupg = makeupg;
    }

    public double getAverage() {
        return average;
    }

    public void setAverage(double average) {
        this.average = average;
    }

    public String getLetterGrade() {
        return letterGrade;
    }

    public void setLetterGrade(String letterGrade) {
        this.letterGrade = letterGrade;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
