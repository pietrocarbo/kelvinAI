<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <link rel="stylesheet" type="text/css" href="tictactoe.css">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta/css/bootstrap.min.css"
          integrity="sha384-/Y6pD6FV/Vv2HJnA6t+vslU6fwYXjCFtcEpHbNJ0lyAFsXTsjBbfaDjzALeQsN6M" crossorigin="anonymous">
    <link rel="icon" type="image/png" href="icons8-Retro-Robot-64.png" sizes="64x64"/>
    <title>Tic Tac Toe</title>
</head>
<body>

<div class="container-fluid">
    <nav class="navbar navbar-expand-lg navbar-light" style="background-color: lightgrey;">
        <a class="navbar-brand" href="/">Home</a>
        <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarSupportedContent"
                aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>

        <div class="collapse navbar-collapse" id="navbarSupportedContent">
            <ul class="navbar-nav mr-auto">
                <li class="nav-item active">
                    <a class="nav-link" href="#" style="background-color: gray">Tris<span
                            class="sr-only">(current)</span></a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="/connect4.jsp">Forza 4</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link " href="/briscola.jsp">Briscola</a>
                </li>
            </ul>
        </div>
    </nav>
</div>
<br><br>
<div class="container-fluid">
    <div class="table-responsive">
        <table class="table-bordered" style="width: 50%; height: 50%;">
            <tr>
                <td>
                    <button id="00" type="button" class="squareEmpty" onclick="move('00')" disabled>-</button>
                </td>
                <td>
                    <button id="01" type="button" class="squareEmpty" onclick="move('01')" disabled>-</button>
                </td>
                <td>
                    <button id="02" type="button" class="squareEmpty" onclick="move('02')" disabled>-</button>
                </td>
            </tr>
            <tr>
                <td>
                    <button id="10" type="button" class="squareEmpty" onclick="move('10')" disabled>-</button>
                </td>
                <td>
                    <button id="11" type="button" class="squareEmpty" onclick="move('11')" disabled>-</button>
                </td>
                <td>
                    <button id="12" type="button" class="squareEmpty" onclick="move('12')" disabled>-</button>
                </td>
            </tr>
            <tr>
                <td>
                    <button id="20" type="button" class="squareEmpty" onclick="move('20')" disabled>-</button>
                </td>
                <td>
                    <button id="21" type="button" class="squareEmpty" onclick="move('21')" disabled>-</button>
                </td>
                <td>
                    <button id="22" type="button" class="squareEmpty" onclick="move('22')" disabled>-</button>
                </td>
            </tr>
        </table>
    </div>
</div>
<br>
<div class="container-fluid">
    <div id="starterSelect">
        <div class="alert alert-info">
            <strong>Per iniziare</strong> scegli il primo a giocare
        </div>

        <button type="button" class="btn btn-secondary" id="aiStart" onclick="startGame('ai')">Kelvin</button>
        <button type="button" class="btn btn-secondary" id="humanStart" onclick="startGame('human')">Tu</button>
    </div>
</div>

<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.11.0/umd/popper.min.js"
        integrity="sha384-b/U6ypiBEHpOf/4+1nzFpr53nxSS+GLCkfwBdFNTxtclqqenISfwAzpKaMNFNmj4"
        crossorigin="anonymous"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta/js/bootstrap.min.js"
        integrity="sha384-h0AbiXch4ZDo7tp9hKZ4TsHbi047NrKGLO3SEJAg45jXxnGIfYzk4Si90RDIqNm1"
        crossorigin="anonymous"></script>
<script>
    var player, starter, row, column;
    $.ajaxSetup({async: false});

    function startGame (startingPlayer) {
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

    function getAiMove (starter) {
        $.ajax({
            url : 'aimTTT',
            type: 'GET',
            dataType : 'text',
            data : {gridValues: getBoardValues()},
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