<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html>
<head>
    <link rel="icon" type="image/png" href="favicon-32x32.png" sizes="32x32" />
    <link rel="stylesheet" type="text/css" href="briscola.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Briscola</title>
</head>
<body>
<h1>Ciao sono Kelvin, prova a battermi a Briscola!</h1>

<div id="board-container">

    <table style="width: 50%; height: 50%;">

        <tr>
            <td><p id="kelvinTurn" style="display: none">È il turno dell'avversario (sta pensando...)</p></td>
            <td> <button id="btnK0" type="button">
                <img id="imgK0" src="cards/RetroCarteNapoletaneNormale.jpg" filled="true" width="180" height="250" title="gioca" style="border:2px solid #FC695F" vspace="5" alt="carta" align="middle">
            </button> </td>
            <td> <button id="btnK1" type="button">
                <img id="imgK1" src="cards/RetroCarteNapoletaneNormale.jpg" filled="true" width="180" height="250" title="gioca" style="border:2px solid #FC695F" vspace="5" alt="carta" align="middle">
            </button> </td>
            <td> <button id="btnK2" type="button">
                <img id="imgK2" src="cards/RetroCarteNapoletaneNormale.jpg" filled="true" width="180" height="250" title="gioca" style="border:2px solid #FC695F" vspace="5" alt="carta" align="middle">
            </button> </td>
        </tr>

        <tr>
            <td>
                <div style="position: relative;">
                    <a href="#" style="text-decoration: none; color: black; pointer-events: none; cursor: default;">
                            <img id="briscola" src="cards/noCard.png" width="90" height="125" />
                            <div>Briscola</div>
                    </a>

                    <a href="#" style="text-decoration: none; color: black; pointer-events: none; cursor: default;">
                            <img id="mazzo" src="cards/RetroCarteNapoletane.jpg" width="150" height="125" />
                            <div id="turnsLeft"></div>
                    </a>
                </div>
            </td>

            <td id="winner"></td>
            <td id="tdT0"> <button type="button" style="background-color: white; border: 0px;">
                <img id="imgT0" src="cards/noCard.png" width="180" height="250"  style="border:2px solid white" title="prima carta di mano" alt="missing" >
            </button> </td>
            <td id="tdT1"> <button type="button" style="background-color: white; border: 0px;">
                <img id="imgT1" src="cards/noCard.png" width="180" height="250" style="border:2px solid white" title="seconda carta di mano" alt="missing" >
            </button> </td>
        </tr>

        <tr>
            <td><p id="humanTurn" style="display: none">È il tuo turno</p></td>
            <td> <button id="btnH0" type="button" onclick="move('human', 0)">
                    <img id="imgH0" src="cards/noCard.png" class="myCards" filled="true" width="180" height="250" title="gioca" style="border:2px solid #00C6FF" vspace="5" alt="carta" align="middle">
            </button> </td>
            <td> <button id="btnH1" type="button" onclick="move('human', 1)">
                <img id="imgH1" src="cards/noCard.png" class="myCards" filled="true" width="180" height="250" title="gioca" style="border:2px solid #00C6FF" vspace="5" alt="carta" align="middle">
            </button> </td>
            <td> <button id="btnH2" type="button" onclick="move('human', 2)">
                <img id="imgH2" src="cards/noCard.png" class="myCards" filled="true" width="180" height="250" title="gioca" style="border:2px solid #00C6FF" vspace="5" alt="carta" align="middle">
            </button> </td>
        </tr>

    </table>

</div>

<div id="starterSelect">
    <p>Scegli il primo giocatore di mano</p>
    <button id="aiStart" type="button" onclick="startGame('ai')">Kelvin</button>
    <button id="humanStart" type="button" onclick="startGame('human')">Io</button>
</div>

<script>
    var nextPlayer, deck, briscola, board = [], handKelvin = [], handHuman = [], collectedKelvin = [], collectedHuman = [], turns = 20;
    var humanCardsID = ['imgH0', 'imgH1', 'imgH2'], kelvinCardsID = ['imgK0', 'imgK1', 'imgK2'], gameOver = false, firstHandPlayerCardIndex;

//    $.ajaxSetup({async: false});

    function startGame(startingPlayer) {
        $('#starterSelect').html('<p><input type="button" value="Nuova partita" onClick="window.location.reload()"><\p>');

        if (startingPlayer == 'ai') {
            nextPlayer = 0;
            $('#kelvinTurn').css('display', 'block');
            setupGame();
            getAiMove();

        } else {
            nextPlayer = 1;
            $('#humanTurn').css('display', 'block');
            setupGame();

        }
        console.log(startingPlayer + " is starting a new game (nextPlayer " + nextPlayer + ")");
    }

    function Card(value, points, name, suit) {
        this.value = value;
        this.points = points;
        this.name = name;
        this.suit = suit;
    }

    function Deck() {
        this.names = ['ASSO', 'DUE', 'TRE', 'QUATTRO', 'CINQUE', 'SEI', 'SETTE', 'FANTE', 'CAVALLO', 'RE'];
        this.suits = ['DENARI','SPADE','COPPE','BASTONI'];
        var cards = [];

        for (var s = 0; s < this.suits.length; s++) {
            for (var n = 0; n < this.names.length; n++) {
                cards.push(new Card(briscolaValue(n), briscolaPoints(this.names[n]), this.names[n], this.suits[s]));
            }
        }
        return cards;
    }

    function briscolaValue(numericalValue) {
        switch (numericalValue) {
            case 0:
                return 10;
                break;
            case 1:
                return 1;
                break;
            case 2:
                return 9;
                break;
            case 3:
                return 2;
                break;
            case 4:
                return 3;
                break;
            case 5:
                return 4;
                break;
            case 6:
                return 5;
                break;
            case 7:
                return 6;
                break;
            case 8:
                return 7;
                break;
            case 9:
                return 8;
                break;
            default:
                console.log('ERROR in numerical value ' + numericalValue + ' of a card');
                break;
        }
    }

    function briscolaPoints(cardName) {
        switch (cardName) {
            case 'ASSO':
                return 11;
                break;
            case 'TRE':
                return 10;
                break;
            case 'RE':
                return 4;
                break;
            case 'CAVALLO':
                return 3;
                break;
            case 'FANTE':
                return 2;
                break;
            case 'SETTE':
            case 'SEI':
            case 'CINQUE':
            case 'QUATTRO':
            case 'DUE':
                return 0;
                break;
            default:
                console.log('ERROR in card name ' + cardName + ' of a card');
                break;
        }
    }

    function shuffle(array) {
        var currentIndex = array.length, temporaryValue, randomIndex;
        while (currentIndex !== 0) {
            randomIndex = Math.floor(Math.random() * currentIndex);
            currentIndex -= 1;

            temporaryValue = array[currentIndex];
            array[currentIndex] = array[randomIndex];
            array[randomIndex] = temporaryValue;
        }
        return array;
    }

    function move(player, cardIndex) {

        var cardToRemoveID =  'img' + (player == 'human' ? 'H' : 'K') + cardIndex;
        var boardPositionID = board.length == 0 ? 'imgT0' : 'imgT1';

        if (player == 'kelvin') {
            updateCardDisplay(boardPositionID, handKelvin[cardIndex]);
            board.push(handKelvin[cardIndex]);
            updateCardDisplay(cardToRemoveID);
//            handKelvin.splice(cardIndex, 1);

        } else {
            updateCardDisplay(boardPositionID, handHuman[cardIndex]);
            board.push(handHuman[cardIndex]);
            updateCardDisplay(cardToRemoveID);
//            handHuman.splice(cardIndex, 1);
            humanButtonsDisabled(true);
        }
        $('#' + cardToRemoveID).attr('filled', 'false');

        setTimeout(function () {

            if (board.length == 2) {
                // collect, change color, and deal new cards if NOT game over (empty board, fill collected) else SHOW winner
                var secondHandPlayer = nextPlayer;
                nextPlayer = chooseWinner(board[0], board[1], nextPlayer);
                $('#tdT' + (secondHandPlayer == nextPlayer ? 1 : 0)).css('background-color', '#27A319');

                setTimeout(function () {

                    updateCardDisplay(['imgT0', 'imgT1']);
                    if(nextPlayer == 0) {
                        collectedKelvin.push(board.pop());
                        collectedKelvin.push(board.pop());
                    } else {
                        collectedHuman.push(board.pop());
                        collectedHuman.push(board.pop());
                    }


                    if(turns == 0) {
                        // calcolare vincitore e mostrarlo nello spazio vuoto
                        var pointKelvin = 0, pointHuman = 0;
                        for (var i = 0; i < collectedKelvin.length; i++) {
                            pointKelvin += briscolaPoints(collectedKelvin[i].name);
                        }
                        for (var i = 0; i < collectedHuman.length; i++) {
                            pointHuman += briscolaPoints(collectedHuman[i].name);
                        }

                        var winningMessage = pointKelvin > pointHuman ? "Kelvin ha vinto con " + pointKelvin + " punti" :
                            pointKelvin < pointHuman ? "Hai ha vinto con " + pointHuman + " punti" :
                                "Pareggio a " + pointHuman + " punti";
                        $('#winner').append("<p>" + winningMessage + "</p>");
                        gameOver = true;

                    }  else if (turns >= 4) {
                        // deal

                        for (var i = 0; i < 3; i++) {

                            // cardIndex per il secondo di mano
                            // first.. per il primo di mano

                            if ($('#' + (nextPlayer == 0 ? kelvinCardsID[i] : humanCardsID[i])).attr("filled") == "false") { // scorro carte del vincitore della mano
                                if (nextPlayer == 0) {
                                    handKelvin[(secondHandPlayer == nextPlayer ? cardIndex : firstHandPlayerCardIndex)] = deck.shift();
                                    updateCardDisplay(kelvinCardsID[i], 'RetroCarteNapoletaneNormale.jpg');
                                } else {
                                    handHuman[(secondHandPlayer == nextPlayer ? cardIndex : firstHandPlayerCardIndex)] = deck[0];
                                    updateCardDisplay(humanCardsID[i], deck.shift());
                                }
                                $('#' + (nextPlayer == 0 ? kelvinCardsID[i] : humanCardsID[i])).attr("filled", "true");
                                break;
                            }
                        }
                        for (var i = 0; i < 3; i++) {
                            if ($('#' + (nextPlayer == 0 ? humanCardsID[i] : kelvinCardsID[i])).attr('filled') == 'false') {
                                if (nextPlayer == 0) {
                                    handHuman[(secondHandPlayer == nextPlayer ? cardIndex : firstHandPlayerCardIndex)] = deck[0];
                                    updateCardDisplay(humanCardsID[i], deck.shift());
                                } else {
                                    handKelvin[(secondHandPlayer == nextPlayer ? cardIndex : firstHandPlayerCardIndex)] = deck.shift();
                                    updateCardDisplay(kelvinCardsID[i], 'RetroCarteNapoletaneNormale.jpg');
                                }
                                $('#' + (nextPlayer == 0 ? humanCardsID[i] : kelvinCardsID[i])).attr('filled', 'true');
                                break;
                            }
                        }

                        if (turns == 4) {
                            // scrivere seme di briscola e cancellare il mazzo
                            updateCardDisplay('#mazzo').attr('src', 'cards/noCards.png');
                            $('#briscola').replaceWith('<p>' + briscola.suit + '</p>');
                        }

                    }

                    turns--;
                    $('#turnsLeft').text('Mani rimanenti ' + turns);
                    $('#tdT' + (secondHandPlayer == nextPlayer ? 1 : 0)).css('background-color', '#fdf7da');

                }, 1500); // let the user see the winning card of the board

            } else {
                firstHandPlayerCardIndex = cardIndex;
                nextPlayer = 1 - nextPlayer;
            }

            if (nextPlayer == 1) {  // il prossimo e' l'utente
                $('#humanTurn').css('display', 'block');
                $('#kelvinTurn').css('display', 'none');
                humanButtonsDisabled(false);  // se è il suo turno, infine riattivo i bottoni all'utente
            } else {
                $('#kelvinTurn').css('display', 'block');
                $('#humanTurn').css('display', 'none');
                getAiMove();
            }

        }, 1500); // let the user see the card moved into the board
    }

    function chooseWinner(card0, card1, lastPlayer) {
        var briscolaSuit = briscola.suit;

        if(card0.suit == briscolaSuit && card0.suit != card1.suit)     return 1 - lastPlayer; // ha vinto il primo di mano
        if(card1.suit == briscolaSuit && card0.suit != card1.suit)     return lastPlayer;  // ha vinto il secondo di mano

        if(card0.suit == card1.suit)    return card0.value > card1.value ? 1 - lastPlayer : lastPlayer;

        return 1 - lastPlayer;  // vince il primo di mano
    }

    function humanButtonsDisabled (disabled) {
        var buttonIds = ["btnH0", "btnH1", "btnH2"];
        for (var i = 0; i < buttonIds.length; i++) {
            $('#' + buttonIds[i]).prop('disabled', disabled);
        }
    }

    function setupGame() {
        deck = shuffle(Deck());

        handKelvin = deck.splice(0,3);
        handHuman = deck.splice(0,3);
        briscola = deck[deck.length - 1];

        updateCardDisplay(humanCardsID.concat('briscola'), handHuman.concat(briscola));
        $('#turnsLeft').text('Mani rimanenti ' + turns);
    }

    function updateCardDisplay(ids, cards) {

        console.log('updateCardDisplay received: ids -> ' + ids + ' cards -> ' + cards);

        if(Array.isArray(ids)) {
            for (var i = 0; i < ids.length; i++) {
                if(cards != undefined && cards[i] instanceof Card) {
                    var cardPath = cards[i].name + 'di' + cards[i].suit + '.jpg';
                } else if (cards === undefined) {
                    var cardPath = 'noCard.png';
                } else if (cards[i] = 'RetroCarteNapoletaneNormale.jpg') {
                    var cardPath = 'RetroCarteNapoletaneNormale.jpg';
                }
                $('#' + ids[i]).attr('src', 'cards/' + cardPath);

            }
        } else {
            if (cards === undefined) {
                var cardPath = 'noCard.png';
            } else if(cards instanceof Card) {
                var cardPath = cards.name + 'di' + cards.suit + '.jpg';
            } else if (cards = 'RetroCarteNapoletaneNormale.jpg') {
                var cardPath = 'RetroCarteNapoletaneNormale.jpg';
            }
            $('#' + ids).attr('src', 'cards/' + cardPath);
        }
    }

    function getAiMove () {
        console.log("entered getAiMove(): turns " + turns + " board length " + board.length + " deck length " + deck.length);
        var unknownCards = deck.slice(0, deck.length-1).concat(handHuman);
        $.ajax({
            url : 'aimBRI',
            type: 'GET',
            data: {nOppCards: handHuman.length, nMyCards: handKelvin.length, nBoardCards: board.length, nUnknownCards: unknownCards.length,
                   turns: (20 - turns), myCards: handKelvin, briscola: briscola, board: board, unknownCards: unknownCards},
            dataType : 'text',
            success: function (data) {
                console.log("Kelvin chosen move " + data);
                var indexCardToPlay, found = false;
                var chosenCardData = data.split("di");
                for (var i = 0; i < handKelvin.length; i++) {
                    if (chosenCardData[0] == handKelvin[i].name && chosenCardData[1] == handKelvin[i].suit) {
                        found = true;
                        indexCardToPlay = i;
                    }
                }
                if (!found) {
                    console.log("ERRORE il server ha ritornato una carta non esistente: " + data);
                }
                move('kelvin', indexCardToPlay);
            },
            error: function(jqxhr,textStatus,errorThrown)
            {
                console.log(jqxhr);
                console.log(textStatus);
                console.log(errorThrown);
                alert("Errore del server. Ricaricare la pagina.");
            },
            complete: function (data, status) {
                console.log("AJAX aimBRI request completed");
            }
        });
    }

</script>

</body>
</html>