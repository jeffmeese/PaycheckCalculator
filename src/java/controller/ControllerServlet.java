/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import calculator.Deduction;
import calculator.FederalCalculator;
import calculator.MichiganCalculator;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javafx.util.Pair;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Jeff
 */
@WebServlet(name = "ControllerServlet", 
        loadOnStartup=1, 
        urlPatterns = {"/PaycheckCalculator",
                       "/michigan"})
public class ControllerServlet extends HttpServlet {

    @Override
    public void init() throws ServletException {
        List<String> stateList = getStateList();
        getServletContext().setAttribute("stateList", stateList);
        
        List<String> payFrequencyList = getPayFrequencyList();
        getServletContext().setAttribute("payFrequencyList", payFrequencyList);
        
        List<String> filingStatusList = getFilingStatusList();
        getServletContext().setAttribute("filingStatusList", filingStatusList);
        
        List<String> grossPayList = getGrossPayTypeList();
        getServletContext().setAttribute("grossPayTypeList", grossPayList);
        
        getServletContext().setAttribute("titleString", "Paycheck Calculator");
    }
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
      
      if (request.getParameter("state") != null) {
        String stateChosen = request.getParameter("state");
        String urlPath = "WEB-INF/jspf/" + stateChosen.toLowerCase() + ".jspf";
        request.getRequestDispatcher(urlPath).include(request, response);
      }
      
      if (request.getParameter("deductionNumber") != null) {
        //int deductionNumber = parseInteger("deductionNumber", request);
        String urlPath = "WEB-INF/deduction.jsp";
        request.setAttribute("deductionNumber", request.getParameter("deductionNumber"));
        request.getRequestDispatcher(urlPath).forward(request, response);
      }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
      // Retrieve the request parameters
      String grossPayType = request.getParameter("grossPayType");
      String payFrequency = request.getParameter("payFrequency");
      String filingStatus = request.getParameter("filingStatus");
      int federalAllowances = parseInteger("federalAllowances", request);
      int additionalFederal = parseInteger("addFedWithholding", request);
      double grossPay = parseDouble("grossPay", request);
      double grossPayYtd = parseDouble("grossPayYtd", request);
      
      // Determine the paycheck gross
      double paycheckGross = grossPay;
      if (grossPayType.equals("Annually")) {
        paycheckGross = calculatePaycheckGross(grossPay, payFrequency);
      }
      
      // Create the federal calculator
      FederalCalculator federalCalculator = new FederalCalculator();
      federalCalculator.setFilingStatus(filingStatus);
      federalCalculator.setGrossIncome(paycheckGross);
      federalCalculator.setGrossIncomeYtd(grossPayYtd);
      federalCalculator.setNumExemptions(federalAllowances);
      
      // Create the state calculator
      MichiganCalculator stateCalculator = createStateCalculator(request);
      stateCalculator.setPaycheckGross(paycheckGross);
  
      // Create the deduction list
      List<Deduction> deductionList = createDeductionList(request);
      for (Deduction deduction : deductionList) {
        federalCalculator.addDeduction(deduction);
        stateCalculator.addDeduction(deduction);
      }
     
      // Determine tax amounts and net pay
      double federalTaxes = federalCalculator.getFederalTaxes();
      double ficaTaxes = federalCalculator.getFicaTaxes();
      double medicareTaxes = federalCalculator.getMedicareTaxes();
      double stateTaxes = stateCalculator.calculateTaxes();
      double netPay = paycheckGross - federalTaxes - ficaTaxes - 
                      medicareTaxes - stateTaxes - additionalFederal;

      // Reset the amount of deductions to reflect that actual
      // amount for the paycheck
      // Also compute any net pay subtractions
      double netSubtractions = 0.0;
      for (Deduction deduction : deductionList) {
        
        if (deduction.getType() == Deduction.Type.PercentGross) {
          double deductionAmount = deduction.getAmount() / 100 * paycheckGross;
          netSubtractions += deductionAmount;
          deduction.setAmount(deductionAmount);
        }
        else if (deduction.getType() == Deduction.Type.PercentNet) {
          double deductionAmount = deduction.getAmount() / 100 * netPay;
          deduction.setAmount(deductionAmount);
          netSubtractions += deductionAmount;
        }
        else if (deduction.getType() == Deduction.Type.FixedAmount) {
          netSubtractions += deduction.getAmount();
        }
      }
      netPay -= netSubtractions;
      
      // Set request parameters
      request.setAttribute("paycheckGross", paycheckGross);
      request.setAttribute("federalTaxes", federalTaxes);
      request.setAttribute("stateTaxes", stateTaxes);
      request.setAttribute("ficaTaxes", ficaTaxes);
      request.setAttribute("medicareTaxes", medicareTaxes);
      request.setAttribute("netPay", netPay);
      request.setAttribute("deductionList", deductionList);
      
      // Forward to the results page
      String urlPath = "WEB-INF/results.jsp";
      request.getRequestDispatcher(urlPath).forward(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

    private double calculatePaycheckGross(double grossPay, String payFrequency) {
      double paycheckGross = grossPay;
      switch (payFrequency) {
        case "Daily":
          paycheckGross /= 260;
          break;
        case "Weekly":
          paycheckGross /= 52;
          break;
        case "Biweekly":
          paycheckGross /= 26;
          break;
        case "SemiMontly":
          paycheckGross /= 24;
          break;
        case "Montly":
          paycheckGross /= 24;
          break;
        case "Quarterly":
          paycheckGross /= 24;
          break;
        case "SemiAnnually":
          paycheckGross /= 24;
          break;
      }
      return paycheckGross;
    }
    
    private MichiganCalculator createStateCalculator(HttpServletRequest request) {
      //String stateName = request.getParameter("stateName");
      
      MichiganCalculator michCalculator = new MichiganCalculator();
      int stateAllowances = parseInteger("stateAllowances", request);
      michCalculator.setStateAllowances(stateAllowances);
      
      return michCalculator;
    }
    
    private List<Deduction> createDeductionList(HttpServletRequest request) {
      int numDeductions = parseInteger("numDeductions", request);
      
      List<Deduction> deductionList = new ArrayList<>();
      for (int i = 0; i < numDeductions; i++) {
        Deduction deduction = new Deduction();
        String nameString = String.format("deduction%dName", i+1);
        String typeString = String.format("deduction%dType", i+1);
        String amountString = String.format("deduction%dAmount", i+1);
        String fedExemptString = String.format("deduction%dExemptFed", i+1);
        String ficaExemptString = String.format("deduction%dExemptFica", i+1);
        String stateExemptString = String.format("deduction%dExemptState", i+1);
        String localExemptString = String.format("deduction%dExemptLocal", i+1);
                
        String deductionName = request.getParameter(nameString);
        String deductionType = request.getParameter(typeString);
        double deductionAmount = parseDouble(amountString, request);
        boolean fedExempt = parseBoolean(fedExemptString, request);
        boolean ficaExempt = parseBoolean(ficaExemptString, request);
        boolean stateExempt = parseBoolean(stateExemptString, request);
        boolean localExempt = parseBoolean(localExemptString, request);
        
        deduction.setExemptFederal(fedExempt);
        deduction.setExemptFica(ficaExempt);
        deduction.setExemptState(stateExempt);
        deduction.setExemptLocal(localExempt);
        deduction.setName(deductionName);
        deduction.setAmount(deductionAmount);
        
        switch (deductionType) {
          case "percentGross":
            deduction.setType(Deduction.Type.PercentGross);
            break;
          case "percentNet":
            deduction.setType(Deduction.Type.PercentNet);
            break;
          case "fixedAmount":
            deduction.setType(Deduction.Type.FixedAmount);
            break;
        }
        
        deductionList.add(deduction);
      }
      return deductionList;
    }
    
    private boolean parseBoolean(String paramName, HttpServletRequest request) {
      String paramString = request.getParameter(paramName);
      
      boolean value = false;
      if (paramString != null && !paramString.isEmpty()) {
        value = (paramString.equals("on"));
      }
      
      return value;
    }
    
    private int parseInteger(String paramName, HttpServletRequest request) {
      String paramString = request.getParameter(paramName);
      
      int value = 0;
      if (paramString != null && !paramString.isEmpty()) {
        value = Integer.parseInt(paramString);
      }
      return value;
    }
    
    private double parseDouble(String paramName, HttpServletRequest request) {
      String paramString = request.getParameter(paramName);
      
      double value = 0.0;
      if (paramString != null && !paramString.isEmpty()) {
        value = Double.parseDouble(paramString);
      }
      return value;
    }
    
    private FederalCalculator getFederalCalculator(HttpServletRequest request) {
      FederalCalculator federalCalculator = new FederalCalculator();
      return federalCalculator;
    }
    
    private List<String> getFilingStatusList() {
        List<String> statusList = new ArrayList<>();
        statusList.add("Single");
        statusList.add("Married");
        statusList.add("Married Use Single Rate");
        statusList.add("Non Resident Alien");

        return statusList;
    }
    
    private List<String> getGrossPayTypeList() {
        List<String> payTypeList = new ArrayList<>();
        payTypeList.add("Annually");
        payTypeList.add("Per Pay Period");

        return payTypeList;
    }
    
    private List<String> getPayFrequencyList() {
        List<String> frequencyList = new ArrayList<>();
        frequencyList.add("Weekly");
        frequencyList.add("Biweekly");
        frequencyList.add("SemiMonthly");
        frequencyList.add("Monthly");
        frequencyList.add("Quarterly");
        frequencyList.add("SemiAnnually");
        frequencyList.add("Annually");
        frequencyList.add("Daily");
        
        return frequencyList;
    }
    
    private List<String> getStateList() {
        List<String> stateList = new ArrayList<>();
        //stateList.add("Alabama");
        //stateList.add("Alaska");
        stateList.add("Arizona");
//        stateList.add("Arkansas");
//        stateList.add("California");
//        stateList.add("Colorado");
//        stateList.add("Connecticut");
//        stateList.add("Delaware");
//        stateList.add("Florida");
//        stateList.add("Georgia");
//        stateList.add("Hawaii");
//        stateList.add("Idaho");
//        stateList.add("Illinois");
//        stateList.add("Indiana");
//        stateList.add("Iowa");
//        stateList.add("Kansas");
//        stateList.add("Kentucky");
//        stateList.add("Louisiana");
//        stateList.add("Maine");
//        stateList.add("Maryland");
//        stateList.add("Massachusetts");
        stateList.add("Michigan");
//        stateList.add("Minnesota");
//        stateList.add("Mississippi");
//        stateList.add("Missouri");
//        stateList.add("Montana");
//        stateList.add("Nebraska");
//        stateList.add("Nevada");
//        stateList.add("New Hampshire");
//        stateList.add("New Jersey");
//        stateList.add("New Mexico");
//        stateList.add("New York");
//        stateList.add("North Carolina");
//        stateList.add("North Dakota");
        stateList.add("Ohio");
//        stateList.add("Oklahoma");
//        stateList.add("Oregon");
//        stateList.add("Pennsylvania");
//        stateList.add("Rhode Island");
//        stateList.add("South Carolina");
//        stateList.add("South Dakota");
//        stateList.add("Tennessee");
//        stateList.add("Texas");
//        stateList.add("Utah");
//        stateList.add("Vermont");
//        stateList.add("Virginia");
//        stateList.add("Washington");
//        stateList.add("West Virginia");
//        stateList.add("Wisconsin");
//        stateList.add("Wyoming");
//        stateList.add("Washington DC");
//        stateList.add("Puerto Rico");
//        stateList.add("American Samoa");
//        stateList.add("Guam");
//        stateList.add("N. Mariana Islands");
//        stateList.add("US Virgin Islands");
        
        return stateList;
    }
}
