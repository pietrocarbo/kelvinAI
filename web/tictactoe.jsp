<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html>
<head>
    <link rel="icon" type="image/png" href="favicon-32x32.png" sizes="32x32" />
    <link rel="stylesheet" type="text/css" href="styleTTT.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <title>Tic Tac Toe</title>
</head>
<body>
<h1>Ciao sono Kelvin, prova a battermi a Tris!</h1>
<div>
    <table style="width: 50%; height: 50%;">
        <tr>
            <td> <button id="00" type="button" class="squareEmpty" onclick="move('00')" disabled>-</button> </td>
            <td> <button id="01" type="button" class="squareEmpty" onclick="move('01')" disabled>-</button> </td>
            <td> <button id="02" type="button" class="squareEmpty" onclick="move('02')" disabled>-</button> </td>
        </tr>
        <tr>
            <td> <button id="10" type="button" class="squareEmpty" onclick="move('10')" disabled>-</button> </td>
            <td> <button id="11" type="button" class="squareEmpty" onclick="move('11')" disabled>-</button> </td>
            <td> <button id="12" type="button" class="squareEmpty" onclick="move('12')" disabled>-</button> </td>
        </tr>
        <tr>
            <td> <button id="20" type="button" class="squareEmpty" onclick="move('20')" disabled>-</button> </td>
            <td> <button id="21" type="button" class="squareEmpty" onclick="move('21')" disabled>-</button> </td>
            <td> <button id="22" type="button" class="squareEmpty" onclick="move('22')" disabled>-</button> </td>
        </tr>
    </table>
</div>

<div id="starterSelect">
    <p>Scegli il primo giocatore</p>
    <button id="aiStart" type="button" onclick="startGame('ai')">Parte Kelvin</button>
    <button id="humanStart" type="button" onclick="startGame('human')">Parti tu</button>
</div>

<script>
    var player, starter, row, column;
    $.ajaxSetup({async: false});

    function startGame (startingPlayer) {
        console.log(startingPlayer + " is starting a new game");

        $('#starterSelect').html("<p id='newGameInfo' style='width: 50%;'>Per iniziare una nuova partita aggiorna la pagina</p>");

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

    function getAiMove (starter) {
        $.ajax({
            url : 'aimTTT',
            type: 'GET',
            data : {starter: starter, gridValues: getBoardValues()},
            dataType : 'text',
            success: function (data) {
                console.log("AI chosen move " + data.charAt(0) + "," + data.charAt(1));
                move(data.charAt(0) + "" + data.charAt(1));
            },
            error: function(jqxhr,textStatus,errorThrown)
            {
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

    function getBoardValues () {
        var squaresId = ["00", "01", "02", "10", "11", "12", "20", "21", "22"];
        var result = [];
        for (var i = 0; i < squaresId.length; i++) {
            result.push(document.getElementById(squaresId[i]).textContent);
        }
        return result;
    }

    function setBoardButtonsDisabled (disabled) {
        var squaresId = ["00", "01", "02", "10", "11", "12", "20", "21", "22"];
        for (var i = 0; i < squaresId.length; i++) {
            document.getElementById(squaresId[i]).disabled = disabled;
        }
    }

    function move (index) {
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
                url : 'winTTT',
                type: 'GET',
                data : {gridValues: gridValues},
                dataType : 'text',
                success: function (data) {
                    console.log('winTTT ' + data);

                    if(data.charAt(0) == "W" || data.charAt(0) == "D") {

                        alert("Partita conclusa.\n\n" + data.substr(1));
                        setBoardButtonsDisabled(true);

                        setTimeout(function () {
                            $('#newGameInfo').css({"background-color": "#4CAF50", "font-size": "24px"});
                        }, 1000);

                    } else if (player == "O") {        // AI have to continue the game
                        getAiMove(starter);
                    }
                },
                error: function(jqxhr,textStatus,errorThrown)
                {
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

    function togglePlayer () {
        player = (player == "O" ? "X" : "O");
    }
</script>

</body>
</html>