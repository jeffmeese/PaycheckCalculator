<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<h2>Paycheck Results</h2>
<table>
  <tr>
    <td class="label">Gross Pay</td>
    <td><fmt:formatNumber type="currency" value="${paycheckGross}"/></td>
  </tr>
  <c:forEach var="deduction" items="${deductionList}">
    <tr>
      <td class="label">${deduction.name}</td>
      <td><fmt:formatNumber type="currency" value="${deduction.amount}"/></td>
    </tr>
  </c:forEach>
  <tr>
    <td class="label">Federal Taxes</td>
    <td><fmt:formatNumber type="currency" value="${federalTaxes}"/></td>
  </tr>
  <tr>
    <td class="label">Social Security Taxes</td>
    <td><fmt:formatNumber type="currency" value="${ficaTaxes}"/></td>
  </tr>
  <tr>
    <td class="label">Medicare Taxes</td>
    <td><fmt:formatNumber type="currency" value="${medicareTaxes}"/></td>
  </tr>
  <tr>
    <td class="label">State Taxes</td>
    <td><fmt:formatNumber type="currency" value="${stateTaxes}"/></td>
  </tr>
  <tr>
    <td class="label">Net Pay</td>
    <td><fmt:formatNumber type="currency" value="${netPay}"/></td>
  </tr>
</table>
  