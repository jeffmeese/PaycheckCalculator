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
public class Deduction {
  private String name;
  private double amount;
  private boolean exemptFederal;
  private boolean exemptFica;
  private boolean exemptState;
  private boolean exemptLocal;
  private Type type;
  
  public enum Type 
  {
    PercentGross,
    PercentNet,
    FixedAmount
  }
  
  public Deduction()
  {
    exemptFederal = exemptFica = exemptState = exemptLocal = false;
    amount = 0.0;
    type = Type.PercentGross;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public double getAmount() {
    return amount;
  }
  
  public boolean isExemptFederal() {
    return exemptFederal;
  }
  
  public boolean isExemptFica() {
    return exemptFica;
  }

  public boolean isExemptState() {
    return exemptState;
  }
  
  public boolean isExemptLocal() {
    return exemptLocal;
  }

  public Type getType() {
    return type;
  }
  
  public void setAmount(double amount) {
    this.amount = amount;
  }
  
  public void setExemptFederal(boolean exemptFederal) {
    this.exemptFederal = exemptFederal;
  }
  
  public void setExemptFica(boolean exemptFica) {
    this.exemptFica = exemptFica;
  }
  
  public void setExemptState(boolean exemptState) {
    this.exemptState = exemptState;
  }

  public void setExemptLocal(boolean exemptLocal) {
    this.exemptLocal = exemptLocal;
  }
  
  public void setType(Type type) {
    this.type = type;
  }
  
  public void setType(String type) {
    
    // TODO: Need to make this more general
    if (type.equals("% of Gross Pay")) {
      setType(Type.PercentGross);
    }
    
    if (type.equals("% of Net Pay")) {
      setType(Type.PercentGross);
    }
    
    if (type.equals("Fixed Amount")) {
      setType(Type.PercentGross);
    }
  }
  
}
