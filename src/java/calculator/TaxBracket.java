/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package calculator;

/**
 *
 * @author Jeff
 */
public class TaxBracket {
  private double minIncome;
  private double maxIncome;
  private double baseTax;
  private double taxPercentage;
  
  public TaxBracket()
  {
    minIncome = maxIncome = baseTax = taxPercentage = 0;
  }

  public TaxBracket(double minIncome, double maxIncome, double baseTax, double taxPercentage) {
    this.minIncome = minIncome;
    this.maxIncome = maxIncome;
    this.baseTax = baseTax;
    this.taxPercentage = taxPercentage;
  }
  
  public double computeTaxes(double taxableAmount) {
    return (taxableAmount-this.minIncome)*(this.taxPercentage/100) + this.baseTax;
  }
  
  public boolean containsAmount(double amount)
  {
    return (amount >= minIncome && (maxIncome < 0 || amount < maxIncome));  
  }
  
  public double getMinIncome() {
    return minIncome;
  }

  public void setMinIncome(double minIncome) {
    this.minIncome = minIncome;
  }

  public double getMaxIncome() {
    return maxIncome;
  }

  public void setMaxIncome(double maxIncome) {
    this.maxIncome = maxIncome;
  }

  public double getBaseTax() {
    return baseTax;
  }

  public void setBaseTax(double baseTax) {
    this.baseTax = baseTax;
  }

  public double getTaxPercentage() {
    return taxPercentage;
  }

  public void setTaxPercentage(double taxPercentage) {
    this.taxPercentage = taxPercentage;
  }
  
  
}
