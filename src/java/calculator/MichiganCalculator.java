/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package calculator;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Jeff
 */
public class MichiganCalculator {
  private int stateAllowances;
  private double paycheckGross;
  private final List<Deduction> voluntaryDeductions;
  
  public MichiganCalculator() {
    voluntaryDeductions = new ArrayList<>();
    stateAllowances = 0;
    paycheckGross = 0.0;
  }
  
  public void addDeduction(Deduction deduction) {
    voluntaryDeductions.add(deduction);
  }
  
  public double calculateTaxes() {
    double exemptionAmount = 153.80;
    double taxRate = 0.0425;
    
    double taxableIncome = this.paycheckGross;
    for (Deduction deduction : this.voluntaryDeductions) {
      if (deduction.isExemptState()) {
        
        if (deduction.getType() == Deduction.Type.PercentGross) {
          taxableIncome -= deduction.getAmount() / 100 * this.paycheckGross;
        }
        
        if (deduction.getType() == Deduction.Type.FixedAmount) {
          taxableIncome -= deduction.getAmount();
        }
      }
    }
    
    taxableIncome -= (exemptionAmount * this.stateAllowances);
    double taxes = taxableIncome * taxRate;
    return taxes;
  }

  public int getStateAllowances() {
    return stateAllowances;
  }

  public void setPaycheckGross(double paycheckGross) {
    this.paycheckGross = paycheckGross;
  }
  
  public void setStateAllowances(int stateAllowances) {
    this.stateAllowances = stateAllowances;
  }
  
}
