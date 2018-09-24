<html>
<head>
    <meta charset="UTF-8">
    <script type="text/javascript" src="/resources/static/js/jquery-3.3.1.min.js"></script>
    <link rel="stylesheet" href="/resources/static/css/bootstrap-4.0.0-dist/css/bootstrap.min.css" type="text/css" />

    <style type="text/css">
        container {
            width: 200px;
        }
    </style>    
</head>

<body>
    <div class="container">
        <form id="mainForm" class="needs-validation" method="post" novalidate>
            <div class="form-group">
                <label for="numberList">Give list numbers to sort</label>
                <input pattern= "^(-?[0-9]+,?)+$" type="text" id="numberListId" name="numberLIst" class="form-control">
                <div class="form-check form-check-inline">
                    <input class="form-check-input" type="radio" name="orderOption" id="ascOptionId" value="ASC" checked>
                    <label class="form-check-label" for="inlineRadio1">Ascending Order</label>
                  </div>
                  <div class="form-check form-check-inline">
                    <input class="form-check-input" type="radio" name="orderOption" id="descOptionId" value="DESC">
                    <label class="form-check-label" for="inlineRadio2">Descending Order</label>
                  </div>
                <div class="invalid-feedback">Give list numbers followed by comma X,X</div>  
                <div>     
                    <button id="confirmFormButtonId" type="button" class="btn btn-primary">Click</button>
                </div>			
            </div>        
        </form>
      
        <div class="form-group">
            <label for="resultSortedNumberList">Result</label>
            <input type="text" id="resultSortedNumberListId" class="form-control">
        </div>
    </div>

    <script>
        $("#confirmFormButtonId").click(function () {
            var numberListValues =  $("#numberListId").val();
            var data = { numbers:[], order:"" };
            var myRegexp = /(-?[0-9]+)(,?)/g;        
            match = myRegexp.exec(numberListValues);
            while (match != null) {                
                data.numbers.push(match[1]);
                match = myRegexp.exec(numberListValues);
            } 
            data.order =$( "input[type=radio][name=orderOption]:checked" ).val();
            validateForm(data);
        });

        function sendData(dataForm) {
            var localhostUrl = "http://localhost:8080/numbers/sort-command"; 
            $.ajax({
                type: 'post',
                url: localhostUrl,
                contentType: 'application/json',
                dataType: 'json',
                data: JSON.stringify(dataForm)
            }).done(function (data) {
            	console.log(data);
                $('#resultSortedNumberListId').val(data.numbers);
                $('#confirmFormButtonId').removeAttr('disabled');
            }).fail(function (data) {
            	$('#resultSortedNumberListId').val("");
                $('#confirmFormButtonId').removeAttr('disabled');                
            });         
        }

        function validateForm(dataForm) {
            var form = document.getElementById('mainForm');
            if (form.checkValidity() == false) {
                $('#mainForm').addClass('was-validated');
            	$('#resultSortedNumberListId').val("");
                return false;
            } else {
                if ($("#mainForm").hasClass('was-validated') == true) {
                    $("#mainForm").removeClass('was-validated');
                }
                $('#confirmFirstPart').attr('disabled', 'disabled');
                sendData(dataForm);
                return true;
            }
        }

    </script>
</body>
</html>