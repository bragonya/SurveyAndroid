loadSurvey(json);
function loadSurvey(jsonObject){
    window.survey = new Survey.Model(jsonObject);

    survey
        .onComplete
        .add(function (result) {
            document
                .querySelector('#surveyResult')
                .innerHTML = "<button class = \"button1\" type=\"button\" onclick=\"otherSurvey()\">Llenar otra encuesta</button>                "+
                "<button class = \"button1\" type=\"button\" onclick=\"back()\">Salir</button>";
            document
                    .querySelector('#surveyElement')
                    .innerHTML = "<h1>Tu encuesta ha sido procesada con exito</h1>";
            JSInterface.sendData(JSON.stringify(result.data));
        });

    $("#surveyElement").Survey({model: survey});
    
}

function otherSurvey(){
    document
            .querySelector('#surveyResult')
            .innerHTML = "";
    loadSurvey(json)
}

function back(){
    JSInterface.back();
}