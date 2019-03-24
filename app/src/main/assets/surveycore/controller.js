loadSurvey(json);

var list_names = [];
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
            JSInterface.sendData(JSON.stringify(removerTypeContent(result.data)),JSON.stringify(list_names));
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

function removerTypeContent(cnt_JSON)
{
    var obj = cnt_JSON;
    var contenido_preg ;
    Object.keys(obj).map(
        key_preg=>
        {
            try {
                contenido_preg = obj[key_preg][0];
                if(contenido_preg!=undefined)
                {
                    if(contenido_preg.type.includes("image"))
                    {
                        var d = new Date();
                        var arr = contenido_preg.name.split(".");
                        if(arr.length > 1){
                            contenido_preg.name = arr[0]+"_"+d.getTime()+"."+arr[1];
                            list_names.push(contenido_preg.name);
                        }
                        delete contenido_preg["type"];
                        delete contenido_preg["content"];
                    }
                }
            } catch (error) {
            }
        }
    );
    return obj;
}
