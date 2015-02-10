/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package calculator;

import java.util.*;

/**
 *
 * @author Jeff
 */
public class FederalCalculator 
{
  private boolean needsUpdate;
  private double grossIncome;
  private double grossIncomeYtd;
  private int numExemptions;
  private double federalTaxes;
  private double ficaTaxes;
  private double medicareTaxes;
  private double ficaTaxRate;
  private double medicareTaxRate;
  private Date payDate;
  private FilingStatus filingStatus;
  private final List<Deduction> voluntaryDeductions;

  public enum FilingStatus {
    Single, 
    Married,
    NonResidentAlien
  };
  
  public FederalCalculator() {
    numExemptions = 0;
    grossIncome = grossIncomeYtd = 0.0;
    federalTaxes = ficaTaxes = medicareTaxes = 0;
    filingStatus = FilingStatus.Single;
    voluntaryDeductions = new ArrayList<>();
    ficaTaxRate = 0.062;
    medicareTaxRate = 0.0145;
  }

  public void addDeduction(Deduction deduction) {
    voluntaryDeductions.add(deduction);
  }
  
  public double getGrossIncome() {
    return grossIncome;
  }

  public double getGrossIncomeYtd() {
    return grossIncomeYtd;
  }
  
  public double getFederalTaxes() {
    if (needsUpdate)
      recalculate();

    return federalTaxes;
  }

  public double getFicaTaxes() {
    if (needsUpdate)
      recalculate();

    return ficaTaxes;
  }

  public double getMedicareTaxes() {
    if (needsUpdate)
      recalculate();

    return medicareTaxes;
  }

  public int getNumExemptions() {
    return numExemptions;
  }

  public Date getPayDate() {
    return payDate;
  }

  public FilingStatus getFilingStatus() {
    return filingStatus;
  }

  public String getFilingStatusString() {
    if (filingStatus == FilingStatus.Single)
      return "Single";

    return "Married";
  }

  public void setFilingStatus(FilingStatus filingStatus) {
    this.filingStatus = filingStatus;
  }

  public void setFilingStatus(String filingStatus) {
    if (filingStatus.equals("Single")) {
      setFilingStatus(FilingStatus.Single);
    }
    if (filingStatus.equals("Married")) {
      setFilingStatus(FilingStatus.Married);
    }
    if (filingStatus.equals("Non Resident Alien")) {
      setFilingStatus(FilingStatus.NonResidentAlien);
    }
  }
  
  public void setGrossIncome(double grossIncome) {
    this.grossIncome = grossIncome;
    needsUpdate = true;
  }

  public void setGrossIncomeYtd(double grossIncomeYtd) {
    this.grossIncomeYtd = grossIncomeYtd;
    needsUpdate = true;
  }
  
  public void setNumExemptions(int numExemptions) {
    this.numExemptions = numExemptions;
  }

  public void setPayDate(Date payDate) {
    this.payDate = payDate;
  }

  private void recalculate()
  {
    double exemptionAmount = 153.8;
    List<TaxBracket> taxBrackets = new ArrayList<>();
    
    taxBrackets.add(new TaxBracket(331.0, 1040, 0, 10));
    taxBrackets.add(new TaxBracket(1040.0, 3212, 70.9, 15));
    taxBrackets.add(new TaxBracket(3212.0, 6146, 396.7, 25));
    taxBrackets.add(new TaxBracket(6146.0, 9194, 1130.2, 28));
    taxBrackets.add(new TaxBracket(9194.0, 16158, 1983.64, 33));
    taxBrackets.add(new TaxBracket(16158.0, 18210, 4281.76, 35));
    taxBrackets.add(new TaxBracket(18210.0, -1, 4999.96, 39.6));
    
    double paycheckGross = this.grossIncome;
    double ficaTaxableIncome = paycheckGross;
    double fedTaxableIncome = paycheckGross;
    for (Deduction deduction : this.voluntaryDeductions) {
      Deduction.Type type = deduction.getType();
      
      if (deduction.isExemptFederal() && (type == Deduction.Type.PercentGross))
        fedTaxableIncome -= (deduction.getAmount() / 100 * paycheckGross);
      
      if (deduction.isExemptFederal() && (type == Deduction.Type.FixedAmount))
        fedTaxableIncome -= deduction.getAmount();
      
      if (deduction.isExemptFica() && (type == Deduction.Type.PercentGross))
        ficaTaxableIncome -= deduction.getAmount() / 100 * paycheckGross;
      
      if (deduction.isExemptFica() && (type == Deduction.Type.FixedAmount))
        ficaTaxableIncome -= deduction.getAmount();
    }
    
    this.ficaTaxes = ficaTaxableIncome*this.ficaTaxRate;
    this.medicareTaxes = ficaTaxableIncome*this.medicareTaxRate;
    fedTaxableIncome -= (exemptionAmount * this.numExemptions);
    for (TaxBracket taxBracket : taxBrackets) {
      if (taxBracket.containsAmount(fedTaxableIncome)) {
        this.federalTaxes = taxBracket.computeTaxes(fedTaxableIncome);
        break;
      }
    }
    this.needsUpdate = false;
  } 
}
