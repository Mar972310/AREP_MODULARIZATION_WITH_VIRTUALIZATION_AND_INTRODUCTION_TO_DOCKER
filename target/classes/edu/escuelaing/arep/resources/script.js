function loadGetMsg() {
    let nameVar = document.getElementById("textInputGet").value;
    let url = "/app/greeting?name=" + nameVar;

    fetch(url, { method: 'GET' })
        .then(response => response.json())  
        .then(data => {
            
            document.getElementById("getResult").innerHTML = data.response;
        });
}

function loadPiMsg() {
    let decimalsVar = document.getElementById("decimalsPi").value;
    let url = "/app/pi?decimals=" + decimalsVar;
    fetch(url, { method: 'GET' })
        .then(response => response.json())  
        .then(data => {
            document.getElementById("resultNumberPi").innerHTML = data.response;
        });
}

function loadSumMsg() {
    let numbersVar = document.getElementById("sumNumbers").value;
    let url = "/app/sum?number=" + numbersVar;
    fetch(url, { method: 'GET' })
        .then(response => response.json())  
        .then(data => {
            document.getElementById("resultSumNumbers").innerHTML = data.response;
        });
}

function loadMulMsg() {
    let numbersVar = document.getElementById("mulNumbers").value;
    let url = "/app/mul?number=" + numbersVar;
    fetch(url, { method: 'GET' })
        .then(response => response.json())  
        .then(data => {
            document.getElementById("resultMulNumbers").innerHTML = data.response;
        });
}

function loadDivMsg() {
    let numbersVar = document.getElementById("divNumbers").value;
    let url = "/app/div?number=" + numbersVar;
    fetch(url, { method: 'GET' })
        .then(response => response.json())  
        .then(data => {
            document.getElementById("resultDivNumbers").innerHTML = data.response;
        });
}

function loadResMsg() {
    let numbersVar = document.getElementById("restNumbers").value;
    let url = "/app/rest?number=" + numbersVar;
    fetch(url, { method: 'GET' })
        .then(response => response.json())  
        .then(data => {
            document.getElementById("resultRestNumbers").innerHTML = data.response;
        });
}