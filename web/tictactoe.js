var player, starter, row, column;
$.ajaxSetup({async: false});

function startGame(startingPlayer) {
    console.log(startingPlayer + " is starting a new game");

    $('#starterSelect').html("<p><input type=\"button\" class='btn btn-secondary' value=\"Nuova partita\" onClick=\"window.location.reload()\"><\p>");

    if (startingPlayer == 'ai') {
        starter = 0;
        player = "O";
        getAiMove(starter);

    } else {
        starter = 1;
        player = "X";
        setBoardButtonsDisabled(false);

    }
}

function getAiMove(starter) {
    $.ajax({
        url: 'aimTTT',
        type: 'GET',
        dataType: 'text',
        data: {gridValues: getBoardValues()},
        success: function (data) {
            console.log("AI chosen move " + data.charAt(0) + "," + data.charAt(1));
            move(data.charAt(0) + "" + data.charAt(1));
        },
        error: function (jqxhr, textStatus, errorThrown) {
            console.log(jqxhr);
            console.log(textStatus);
            console.log(errorThrown);
            alert("Errore del server. Ricaricare la pagina.");
        },
        complete: function (data, status) {
            console.log("AJAX aimTTT request completed, status " + status);
        }
    });
}

function getBoardValues() {
    var squaresId = ["00", "01", "02", "10", "11", "12", "20", "21", "22"];
    var result = [];
    for (var i = 0; i < squaresId.length; i++) {
        result.push(document.getElementById(squaresId[i]).textContent);
    }
    return result;
}

function setBoardButtonsDisabled(disabled) {
    var squaresId = ["00", "01", "02", "10", "11", "12", "20", "21", "22"];
    for (var i = 0; i < squaresId.length; i++) {
        document.getElementById(squaresId[i]).disabled = disabled;
    }
}

function move(index) {
    console.log("move called with index ");
    console.log(index);
    var row = index.charAt(0);
    var column = index.charAt(1);
    var buttonText = document.getElementById(row + "" + column).textContent;

    if (buttonText != "-") {
        alert("Posizione non valida");

    } else {
        var buttonPressed = document.getElementById(row + "" + column);
        buttonPressed.textContent = player;

        if (player == "O") {  // AI moved
            buttonPressed.style.backgroundColor = "#008CBA";
            setBoardButtonsDisabled(false);

        } else {              // human moved
            buttonPressed.style.backgroundColor = "#f44336";
            setBoardButtonsDisabled(true);
        }

        togglePlayer();
        var gridValues = getBoardValues();

        $.ajax({
            url: 'winTTT',
            type: 'GET',
            data: {gridValues: gridValues},
            dataType: 'text',
            success: function (data) {
                console.log('winTTT ' + data);

                if (data.charAt(0) == "W" || data.charAt(0) == "D") {

                    alert("Partita conclusa.\n\n" + data.substr(1));
                    setBoardButtonsDisabled(true);

                    setTimeout(function () {
                        $('#newGameInfo').css({"background-color": "#4CAF50", "font-size": "24px"});
                    }, 1000);

                } else if (player == "O") {        // AI have to continue the game
                    getAiMove(starter);
                }
            },
            error: function (jqxhr, textStatus, errorThrown) {
                console.log(jqxhr);
                console.log(textStatus);
                console.log(errorThrown);
                alert("Errore del server. Ricaricare la pagina.");
            },
            complete: function (data, status) {
                console.log("AJAX winTTT request completed, status " + status);
            }
        });

    }
}

function togglePlayer() {
    player = (player == "O" ? "X" : "O");
}