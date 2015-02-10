/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

$(document).ready( function() {
  
  // Pre-populate the check date with the current date
  var dateString = $.datepicker.formatDate('mm/dd/yy', new Date());
  $('#checkDate').val(dateString);
  
  // Show the calendar widget
  $(function() {
   $('#checkDate').datepicker();
 });
});

function addDeduction() {
  var numDeductions = parseInt($('#numDeductions').val())+1;
  $('#numDeductions').val(numDeductions);
  
  $.get('PaycheckCalculator', {deductionNumber : numDeductions}, 
    function(repsonseData) {
      $('#deductions').append(repsonseData);
    });
}

function calculatePay() {
  var a = $("form").serialize();
  alert(a);
  
  var formData = $("form").serializeObject();
  $.post('PaycheckCalculator', formData,
    function(responseData) { 
      $('#paycheckResults').html(responseData);
  });
}

function changeState() {
  var stateChosen = $('#stateName option:selected').text();
  $.get('PaycheckCalculator', {state : stateChosen},
    function(repsonseData) {
      $('#stateForm').html(repsonseData);
    });
}

function removeDeduction() {
  var numDeductions = parseInt($('#numDeductions').val());
  if (numDeductions > 0) {
    var idString = '#deduction' + numDeductions.toString();
    $(idString).remove();
    $('#numDeductions').val(numDeductions-1);
  }
}

function updateForm() {
  var numDeductions = parseInt($('#numDeductions').val());
  for (var i = 0; i < numDeductions; i++) {
    var idString = '#deduction' + (i+1).toString();
    var deductionType = $(idString + ' option:selected').val();
    if (deductionType === 'percentNet') {
      $(idString + 'ExemptArea').hide();
    }
    else {
      $(idString + 'ExemptArea').show();
    }
  }
}

$.fn.serializeObject = function(){
  var o = {};
  var a = this.serializeArray();
  $.each(a, function() {
    if (o[this.name]) {
      if (!o[this.name].push) {
          o[this.name] = [o[this.name]];
      }
      o[this.name].push(this.value || '');
    } else {
      o[this.name] = this.value || '';
    }
  });
  return o;
};