<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div class="deduction" id="deduction${deductionNumber}">
  <div class="formField">
    <label for="deduction${deductionNumber}Name">Deduction ${deductionNumber} Name</label>
    <input type="text" name="deduction${deductionNumber}Name" id="deduction${deductionNumber}Name"/>
  </div>
    <div class="formField">
    <label for="deduction${deductionNumber}Amount">Deduction ${deductionNumber} Amount</label>
    <input type="text" name="deduction${deductionNumber}Amount" id=deduction${deductionNumber}Amount" />
  </div>
  <div class="formField">
    <label for="deduction${deductionNumber}Type">Deduction ${deductionNumber} Type</label>
    <select id="deduction${deductionNumber}Type" name="deduction${deductionNumber}Type" onchange="updateForm()">
      <option value="percentGross" selected="selected">% of Gross Pay</option>
      <option value="percentNet">% of Net Pay</option>
      <option value="fixedAmount">Fixed Amount</option>
    </select>
  </div>
  <div class="formField" id="deduction${deductionNumber}ExemptArea">
    <label>Exempt: </label>
    <input type="checkbox" name="deduction${deductionNumber}ExemptFed" id="deduction${deductionNumber}ExemptFed"/>Federal
    <input type="checkbox" name="deduction${deductionNumber}ExemptFica" id="deduction${deductionNumber}ExemptFica"/>Fica
    <input type="checkbox" name="deduction${deductionNumber}ExemptState" id="deduction${deductionNumber}ExemptState"/>State
    <input type="checkbox" name="deduction${deductionNumber}ExemptLocal" id="deduction${deductionNumber}ExemptLocal"/>Local
  </div>
</div>
