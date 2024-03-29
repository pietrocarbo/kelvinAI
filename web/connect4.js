var player, row, column;
var boardIndexes = [
    ["50", "51", "52", "53", "54", "55", "56"],
    ["40", "41", "42", "43", "44", "45", "46"],
    ["30", "31", "32", "33", "34", "35", "36"],
    ["20", "21", "22", "23", "24", "25", "26"],
    ["10", "11", "12", "13", "14", "15", "16"],
    ["00", "01", "02", "03", "04", "05", "06"]
];

$.ajaxSetup({async: false});

$(document).ready(function () {

    var boardContainer = $("#board-container");
    var table = $("<table class='table-bordered' style='width: 40%;'/>");

    $.each(boardIndexes, function (rowIndex, r) {

        var row = $("<tr/>");

        $.each(r, function (colIndex, c) {

            row.append("<td> <svg height='50' width='50'> <circle id='" + boardIndexes[rowIndex][colIndex] + "' cx='25' cy='25' r='20' stroke='#fdf7da' stroke-width='2' fill='#fdf7da' /> </svg>");

        });

        table.append(row);
    });

    return boardContainer.append(table);
});

function startGame(startingPlayer) {
    console.log(startingPlayer + " is starting a new game");

    $('#starterSelect').html("<p><input type=\"button\" class='btn btn-secondary' value=\"Nuova partita\" onClick=\"window.location.reload()\"><\p>");

    if (startingPlayer == 'ai') {
        player = "O";
        getAiMove();

    } else {
        player = "X";
        setBoardButtonsDisabled(false);

    }
}

function getAiMove() {
    var depth = $('select[name=depthSelected]').val();
    $.ajax({
        url: 'aimC4',
        type: 'GET',
        data: {depth: depth, gridValues: getBoardValues()},
        dataType: 'text',
        success: function (data) {
            console.log("AI chosen move " + data.charAt(0) + "," + data.charAt(1));
            move(Number(data.charAt(1)));
        },
        error: function (jqxhr, textStatus, errorThrown) {
            console.log(jqxhr);
            console.log(textStatus);
            console.log(errorThrown);
            alert("Errore del server. Ricaricare la pagina.");
        },
        complete: function (data, status) {
            console.log("AJAX aimTTT request completed, depth " + depth + ", status " + status);
        }
    });
}

function getBoardValues() {
    var result = [];
    for (var i = 5; i >= 0; i--) {
        for (var j = 0; j < 7; j++) {
            var color = $('#' + boardIndexes[i][j]).attr('fill');

            if (color == '#fdf7da') {
                result.push('-');
            } else if (color == '#008CBA') {
                result.push('O');
            } else if (color == '#f44336') {
                result.push('X');
            } else {
                console.log('(i,j) ' + i + ',' + j + '\t (board index ' + boardIndexes[i][j] + ')\t unknown color ' + color + ' in the board');
            }
        }
    }
    console.log(result);
    return result;
}

function setBoardButtonsDisabled(disabled) {
    var buttonIds = ["0", "1", "2", "3", "4", "5", "6"];
    for (var i = 0; i < buttonIds.length; i++) {
        $('#' + buttonIds[i]).prop('disabled', disabled);
    }
}

function move(column) {
    console.log("move called on column " + column);
    var row = undefined;
    for (var i = 5; i >= 0; i--) {
        var colorMen = $('#' + boardIndexes[i][column]).attr('fill');
        console.log("(i,j) " + i + "," + column + "\t\t boardIndexes " + boardIndexes[i][column] + "\t\t colorMen " + colorMen + " (empty men #fdf7da)");
        if (colorMen == '#fdf7da') {
            row = i;
            break;
        }
    }
    console.log("row selected " + row);

    var circleMen = $('#' + boardIndexes[row][column]);

    if (row == undefined) {
        alert("Posizione non valida");

    } else {

        if (player == "O") {  // AI moved
            circleMen.attr('fill', "#008CBA");
            setBoardButtonsDisabled(false);

        } else {              // human moved
            circleMen.attr('fill', "#f44336");
            setBoardButtonsDisabled(true);
        }

        togglePlayer();
        var gridValues = getBoardValues();

        $.ajax({
            url: 'winC4',
            type: 'GET',
            data: {gridValues: gridValues},
            dataType: 'text',
            success: function (data) {
                if (data.charAt(0) == "W" || data.charAt(0) == "D") {

                    alert("Partita conclusa.\n\n" + data.substr(1));
                    setBoardButtonsDisabled(true);

                    setTimeout(function () {
                        $('#newGameInfo').css({"background-color": "#4CAF50", "font-size": "24px"});
                    }, 1000);

                } else if (player == "O") {        // AI have to continue the game
                    getAiMove();
                }
            },
            error: function (jqxhr, textStatus, errorThrown) {
                console.log(jqxhr);
                console.log(textStatus);
                console.log(errorThrown);
                alert("Errore del server. Ricaricare la pagina.");
            },
            complete: function (data, status) {
                console.log("AJAX winC4 request completed, status " + status);
            }
        });

    }
}

function togglePlayer() {
    player = (player == "O" ? "X" : "O");
}