<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<!--
To change this license header, choose License Headers in Project Properties.
To change this template file, choose Tools | Templates
and open the template in the editor.
-->

<html>
  <head>
    <title>${titleString}</title>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="css/payroll.css">
    <link rel="stylesheet" href="http://code.jquery.com/ui/1.11.2/themes/smoothness/jquery-ui.css">
    <script type="text/javascript" src="http://code.jquery.com/jquery-2.1.3.min.js"></script>
    <script type="text/javascript" src="http://code.jquery.com/ui/1.11.2/jquery-ui.js"></script>
    <script type="text/javascript" src="js/calculator.js"></script>
  </head>
  <body>
    <div id="paycheckInputs">
      <h2>Paycheck Inputs</h2>
      <form>
        <div class="payrollSection">
          <h3>Date and State Information</h3>
          <div class="formField">
            <label for="checkDate">Check Date</label>
            <input type="text" id="checkDate" name="checkDate" />
          </div>
          <div class="formField">
            <label for="stateName">State for withholding</label>
            <select id="stateName" onchange="changeState()">
              <c:forEach var="state" items="${stateList}">
                <option value="${state}">${state}</option>
              </c:forEach>
            </select>
          </div>
        </div>
        <div id="generalInformation" class="payrollSection">
          <h3>General Information</h3>
          <div class="formField">
            <label for="grossPay">Gross Pay: </label>
            <input type="text" id="grossPay" name="grossPay" />
          </div>
          <div class="formField">
            <label for="grossPayType">Gross Pay Type: </label>
            <select id="grossPayType" name="grossPayType">
              <c:forEach var="payType" items="${grossPayTypeList}">
                <option value="${payType}">${payType}</option>
              </c:forEach>
            </select>
          </div>
          <div class="formField">
            <label for="grossPayYtd">Gross Salary YTD: </label>
            <input type="text" id="grossPayYtd" name="grossPayYtd" />
          </div>
          <div class="formField">
            <label for="payFrequency">Pay Frequency: </label>
            <select id="payFrequency" name="payFrequency">
              <c:forEach var="payFrequencyType" items="${payFrequencyList}">
                <option value="${payFrequencyType}">${payFrequencyType}</option>
              </c:forEach>
            </select>
          </div>
          <div class="formField">
            <label for="filingStatus">Filing Status: </label>
            <select id="filingStatus" name="filingStatus">
              <c:forEach var="filingStatusType" items="${filingStatusList}">
                <option value="${filingStatusType}">${filingStatusType}</option>
              </c:forEach>
            </select>
          </div>
          <div class="formField">
            <label for="federalAllowances">Number of Federal Allowances: </label>
            <input type="text" id="federalAllowances" name="federalAllowances" />
          </div>
          <div class="formField">
            <label for="addFedWithholding">Additional Federal Withholding: </label>
            <input type="text" id="addFedWithholding" name="addFedWithholding" />
          </div>
          <div class="formField">
            <label for="roundFederalWithholding">Round Federal Withholding: </label>
            <input type="radio" name="roundFedWithholding" />Yes
            <input type="radio" name="roundFedWithholding" checked />No
          </div>
          <div class="formField">
            <label for="fedExempt">Exempt: </label>
            <input type="checkbox" name="fedExempt" value="fedExempt"/>Federal
            <input type="checkbox" name="ficaExempt" value="ficaExempt"/>Fica 
            <input type="checkbox" name="medExempt" value="medExempt"/>Medicare 
          </div>
        </div>
        <div id="stateLocalInformation" class="payrollSection">
          <h3>State and Local Information</h3>
          <div id="stateForm">
            <jsp:include page="WEB-INF/jspf/arizona.jspf" />
          </div>
        </div>
        <div class="payrollSection">
          <h3>Voluntary Deductions</h3>
          <div class="deductionButtonField">
            <input class="deductionButton" type="button" value="Add Deduction" onclick="addDeduction()" />
            <input class="deductionButton" type="button" value="Remove Deduction" onclick="removeDeduction()" />
          </div>
          <input type="hidden" name="numDeductions" id="numDeductions" value="0"/>
          <div id="deductions">
          </div>
        </div>
        <div class="submitButtonArea">
          <input type="button" id="submit" value="Calculate" onclick="calculatePay()"/>
          <input type="button" id="reset" value="Reset" />
        </div>
      </form>
    </div>
    <div id="paycheckResults">
    </div> 
  </body>
</html>
