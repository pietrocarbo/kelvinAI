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
                    <img id="imgK0" src="cards/RetroCarteNapoletaneNormale.jpg" width="180" height="250" style="border:2px solid #FC695F" vspace="5" alt="missing image" align="middle">
            </button> </td>
            <td> <button id="btnK1" type="button">
                    <img id="imgK1" src="cards/RetroCarteNapoletaneNormale.jpg" width="180" height="250" style="border:2px solid #FC695F" vspace="5" alt="missing image" align="middle">
            </button> </td>
            <td> <button id="btnK2" type="button">
                    <img id="imgK2" src="cards/RetroCarteNapoletaneNormale.jpg" width="180" height="250" style="border:2px solid #FC695F" vspace="5" alt="missing image" align="middle">
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
            <td id="tdT0"> <button type="button" style="background-color: white; border: 0px;"> 1º di mano
                <img id="imgT0" src="cards/noCard.png" width="180" height="250"  style="border:2px solid white" title="prima carta giocata" alt="missing image" >
            </button> </td>
            <td id="tdT1"> <button type="button" style="background-color: white; border: 0px;"> 2º di mano
                <img id="imgT1" src="cards/noCard.png" width="180" height="250" style="border:2px solid white" title="seconda carta giocata" alt="missing image" >
            </button> </td>
        </tr>

        <tr>
            <td><p id="humanTurn" style="display: none">È il tuo turno</p></td>
            <td> <button id="btnH0" type="button">
                    <img id="imgH0" src="cards/noCard.png" class="myCards" width="180" height="250" title="gioca prima carta" style="border:2px solid #00C6FF" vspace="5" alt="missing image" align="middle">
            </button> </td>
            <td> <button id="btnH1" type="button">
                    <img id="imgH1" src="cards/noCard.png" class="myCards" width="180" height="250" title="gioca seconda carta" style="border:2px solid #00C6FF" vspace="5" alt="missing image" align="middle">
            </button> </td>
            <td> <button id="btnH2" type="button">
                    <img id="imgH2" src="cards/noCard.png" class="myCards" width="180" height="250" title="gioca terza carta" style="border:2px solid #00C6FF" vspace="5" alt="missing image" align="middle">
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
    var deck, briscola, board = [], handKelvin = [], handHuman = [], collectedKelvin = [], collectedHuman = [], turnsLeft = 20, gameOver = false;
    var briscolaID = '#briscola', humanCardsID = ['#imgH0', '#imgH1', '#imgH2'], kelvinCardsID = ['#imgK0', '#imgK1', '#imgK2'], humanButtonIDs = ["#btnH0", "#btnH1", "#btnH2"];
    var returnedAiMoveCard, firstHandPlayer, firstHandPlayerCardIndex, secondHandPlayer, secondHandPlayerCardIndex;

    $.ajaxSetup({async: false});
    $(document).ready(function() {
        for (var i = 0; i < 3; i++) {
            $(humanButtonIDs[i]).click(humanClick(i));
        }
    });

    function humanClick(index) {
        return function (e) {
            var cardImage = $(humanCardsID[index]).attr('src').split("di");
            cardImage[0] = cardImage[0].substring(6);
            cardImage[1] = cardImage[1].substring(0, cardImage[1].indexOf(".jpg"));
            move('human',  new Card(briscolaValue(briscolaNamesToNumericalValue(cardImage[0])), briscolaPoints(cardImage[0]), cardImage[0], cardImage[1]));
        }

    }

    function startGame(startingPlayer) {
        $('#starterSelect').html('<p><input type="button" value="Nuova partita" onClick="window.location.reload()"><\p>');

        if (startingPlayer == 'ai') {
            humanButtonsDisabled(true);
            $('#kelvinTurn').css('display', 'block');
            setupGame();
            move('ai', getAiMove());

        } else {
            $('#humanTurn').css('display', 'block');
            setupGame();

        }
        console.log(startingPlayer + " is starting a new game");
    }

    function setupGame () {
        deck = shuffle(Deck());
        handKelvin = deck.splice(0,3);
        handHuman = deck.splice(0,3);
        briscola = deck[deck.length - 1];

        updateCardDisplay(briscolaID, briscola);
        updateCardDisplay(humanCardsID, handHuman);
        updateCardDisplay(kelvinCardsID, handKelvin);  // TO REMOVE
        $('#turnsLeft').text('Mani rimanenti ' + turnsLeft);
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

    function briscolaNamesToNumericalValue(name) {
        switch (name) {
            case "ASSO":
                return 0;
                break;
            case "DUE":
                return 1;
                break;
            case "TRE":
                return 2;
                break;
            case "QUATTRO":
                return 3;
                break;
            case "CINQUE":
                return 4;
                break;
            case "SEI":
                return 5;
                break;
            case "SETTE":
                return 6;
                break;
            case "FANTE":
                return 7;
                break;
            case "CAVALLO":
                return 8;
                break;
            case "RE":
                return 9;
                break;
            default:
                console.log('ERROR in briscola name ' + name+ ' in numerical conversion');
                break;
        }
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

    function humanButtonsDisabled (disabled) {
        for (var i = 0; i < humanButtonIDs.length; i++) {
            $(humanButtonIDs[i]).prop('disabled', disabled);
        }
    }

    function chooseWinner(card0, card1) {
        var briscolaSuit = briscola.suit;

        if(card0.suit == briscolaSuit && card0.suit != card1.suit)     return 0; // ha vinto il primo di mano
        if(card1.suit == briscolaSuit && card0.suit != card1.suit)     return 1;  // ha vinto il secondo di mano

        if(card0.suit == card1.suit)    return card0.value > card1.value ? 0 : 1;

        return 0;  // vince il primo di mano
    }

    function mapCardToHandArray(caller, cardChosen) {
        var handArray;

        if (caller == 'ai')             handArray = handKelvin;
        else if (caller == 'human')     handArray = handHuman;
        else    {   console.log("ERROR in mapping card of unknown player " + caller);  }

        for (var i = 0; i < handArray.length; i++) {
            if (cardChosen.name == handArray[i].name && cardChosen.suit == handArray[i].suit) {
                return i;
            }
        }

        console.log("ERROR impossible to find mapping for card " + cardChosen.name + " di " + cardChosen.suit + " in player " + caller + " hand");
    }

    function mapCardToImage(caller, cardChosen) {
        var imageCardsID;

        if (caller == 'ai')             imageCardsID = kelvinCardsID;
        else if (caller == 'human')     imageCardsID = humanCardsID;
        else    {   console.log("ERROR in mapping card of unknown player " + caller);  }

        for (var i = 0; i < imageCardsID.length; i++) {

            if ($(imageCardsID[i]).attr('src') != 'cards/noCard.png') {
                var cardImage = $(imageCardsID[i]).attr('src').split("di");
                cardImage[0] = cardImage[0].substring(6);
                cardImage[1] = cardImage[1].substring(0, cardImage[1].indexOf(".jpg"));
                if (cardImage[0] == cardChosen.name && cardImage[1] == cardChosen.suit) {
                    return imageCardsID[i];
                }
            }
        }

        console.log("ERROR impossible to find mapping for card " + cardChosen.name + " di " + cardChosen.suit + " in player " + caller + " hand");
    }

    function move(caller, card) {

        var cardToRemoveID =  mapCardToImage(caller, card);
        var cardIndex = mapCardToHandArray(caller, card);

        var nextPlayer = (caller == "ai" ? "human" : "ai");
        var boardPositionID = (board.length == 0 ? '#imgT0' : '#imgT1');

        console.log("move() called by " + caller + ": n. carte sul tavolo " + board.length + " indice carta giocata " + cardIndex + " cardToRemoveID " + cardToRemoveID + " boardPositionID " + boardPositionID);

        if (caller == 'ai') {
            updateCardDisplay(boardPositionID, handKelvin[cardIndex]);
            board.push(handKelvin[cardIndex]);
        } else {
            updateCardDisplay(boardPositionID, handHuman[cardIndex]);
            board.push(handHuman[cardIndex]);
        }
        humanButtonsDisabled(true);
        updateCardDisplay(cardToRemoveID);

//         setTimeout(function () {

            if (board.length == 2) {

                // choosing winner
                secondHandPlayer = caller;
                secondHandPlayerCardIndex = cardIndex;

                nextPlayer = (chooseWinner(board[0], board[1]) == 0 ? firstHandPlayer : secondHandPlayer);
                console.log("collecting phase: player " + nextPlayer + " won this turn");

                $('#tdT' + (nextPlayer == firstHandPlayer ? 0 : 1)).css('background-color', '#27A319');

                // setTimeout(function () {

                    // collecting
                    updateCardDisplay(['#imgT0', '#imgT1']);
                    if(nextPlayer == 'ai') {
                        collectedKelvin.push(board.pop());
                        collectedKelvin.push(board.pop());
                    } else if(nextPlayer == 'human') {
                        collectedHuman.push(board.pop());
                        collectedHuman.push(board.pop());
                    } else {
                        console.log("ERROR wrong nextPlayer found during collecting: " + nextPlayer);
                    }

                // dealing
                turnsLeft--;
                $('#turnsLeft').text('Mani rimanenti ' + turnsLeft);
                console.log("entering dealing phase (turn " + turnsLeft + ")");

                if(turnsLeft == 0) {
                    gameOver = true;
                    var pointKelvin = 0, pointHuman = 0;
                    for (var i = 0; i < collectedKelvin.length; i++) {
                            pointKelvin += collectedKelvin[i].points;
                        }
                    for (var i = 0; i < collectedHuman.length; i++) {
                            pointHuman += collectedHuman[i].points;
                        }

                    var winningMessage = pointKelvin > pointHuman ? "Kelvin ha vinto con " + pointKelvin + " punti" :
                                             pointKelvin < pointHuman ? "Hai ha vinto con " + pointHuman + " punti" :
                                                                        "Pareggio a " + pointHuman + " punti";
                        $('#winner').append('<p>' + winningMessage + '</p>');

                }  else if (turnsLeft >= 3) {
                    // deal new cards in the respective handKelvin/handHuman positions removing them from the top the deck
                    var indexToDeal = -1;

                    if (nextPlayer == 'ai') {

                        indexToDeal = (firstHandPlayer == 'ai' ? firstHandPlayerCardIndex : secondHandPlayerCardIndex);
                        handKelvin[indexToDeal] = deck.shift();

                        indexToDeal = (indexToDeal == firstHandPlayerCardIndex ? secondHandPlayerCardIndex : firstHandPlayerCardIndex);
                        handHuman[indexToDeal] = deck.shift();

                    } else if (nextPlayer == 'human') {

                        indexToDeal = (firstHandPlayer == 'human' ? firstHandPlayerCardIndex : secondHandPlayerCardIndex);
                        handHuman[indexToDeal] = deck.shift();

                        indexToDeal = (indexToDeal == firstHandPlayerCardIndex ? secondHandPlayerCardIndex : firstHandPlayerCardIndex);
                        handKelvin[indexToDeal] = deck.shift();

                    } else {
                        console.log("ERROR wrong winner(i.e. nextPlayer) found " + nextPlayer);
                    }

                    if (turnsLeft == 3) { // "cancellare" graficamente il mazzo e la briscola
                        console.log("dealing at turn 4, erasing deck of length " + deck.length);
                        updateCardDisplay(['#mazzo']);
                        $(briscolaID).replaceWith('<p>' + briscola.suit + '</p>');
                    }

                    updateCardDisplay(humanCardsID, handHuman);
                    updateCardDisplay(kelvinCardsID, handKelvin);  // TO REMOVE

                } else if (turnsLeft <= 2) {
                    // put 'noCard.png' in played cards and remove their IDs from hand arrays, splice handHuman and HandKelvin in respective index

                    var indexToRemove = firstHandPlayerCardIndex;
                    if (firstHandPlayer == 'ai') {
                        handKelvin.splice(indexToRemove, 1);
                    } else {
                        handHuman.splice(indexToRemove, 1);
                        $(humanButtonIDs[firstHandPlayerCardIndex]).prop('disabled', true);
                        humanButtonIDs.splice(firstHandPlayerCardIndex, 1);
                    }

                    indexToRemove = secondHandPlayerCardIndex;
                    if (secondHandPlayer == 'ai') {
                        handKelvin.splice(indexToRemove, 1);
                    } else {
                        handHuman.splice(indexToRemove, 1);
                        $(humanButtonIDs[secondHandPlayerCardIndex]).prop('disabled', true);
                        humanButtonIDs.splice(secondHandPlayerCardIndex, 1);
                    }

                }  // end collecting

                    $('#tdT' + (nextPlayer == firstHandPlayer ? 0 : 1)).css('background-color', '#fdf7da');
                    firstHandPlayer = null;
                    firstHandPlayerCardIndex = null;

//                }, 1500); // let the user see the winning card of the board

            } else {
                firstHandPlayer = caller;
                firstHandPlayerCardIndex = cardIndex;
            }

            if (gameOver) {
                $('#kelvinTurn').css('display', 'none');
                $('#humanTurn').css('display', 'none');
                console.log("game over");
            } else if (nextPlayer == 'human') {
                $('#humanTurn').css('display', 'block');
                $('#kelvinTurn').css('display', 'none');
                humanButtonsDisabled(false);
            } else if (nextPlayer == 'ai') {
                $('#kelvinTurn').css('display', 'block');
                $('#humanTurn').css('display', 'none');
                move('ai', getAiMove());
            } else {
                console.log("ERROR wrong nextPlayer " + nextPlayer);
            }

//         }, 1500); // let the user see the card moved into the board
    }

    function updateCardDisplay(ids, cards) {
        console.log('updateCardDisplay entered: ids -> ' + ids + ' cards... ');
        console.log(cards);

        if(Array.isArray(ids)) {
            for (var i = 0; i < ids.length; i++) {
                if (cards === undefined) {
                    var cardPath = 'noCard.png';
                } else if(cards[i] instanceof Card) {
                    var cardPath = cards[i].name + 'di' + cards[i].suit + '.jpg';
                } else if (cards[i] = 'RetroCarteNapoletaneNormale.jpg') {
                    var cardPath = cards[i];
                }
                $(ids[i]).attr('src', 'cards/' + cardPath);

            }
        } else {
            if (cards === undefined) {
                var cardPath = 'noCard.png';
            } else if(cards instanceof Card) {
                var cardPath = cards.name + 'di' + cards.suit + '.jpg';
            } else if (cards = 'RetroCarteNapoletaneNormale.jpg') {
                var cardPath = cards;
            }
            $(ids).attr('src', 'cards/' + cardPath);
        }
    }

    function getAiMove () {
        console.log("entered getAiMove(): turnsLeft " + turnsLeft + " board length " + board.length + " deck length " + deck.length);
        var unknownCards = deck.slice(0, deck.length-1).concat(handHuman);
        $.ajax({
            url : 'aimBRI',
            type: 'GET',
            dataType : 'text',
            data: {nOppCards: handHuman.length, nMyCards: handKelvin.length, nBoardCards: board.length, nUnknownCards: unknownCards.length,
                   turns: (20 - turnsLeft), myCards: handKelvin, briscola: briscola, board: board, unknownCards: unknownCards},
            success: function (data) {
                console.log("Kelvin chosen move " + data);
                var found = false;
                var chosenCardData = data.split("di");
                for (var i = 0; i < handKelvin.length; i++) {
                    if (chosenCardData[0] == handKelvin[i].name && chosenCardData[1] == handKelvin[i].suit) {
                        found = true;
//                        returnedAiMoveCardIndex = i;
                        returnedAiMoveCard = handKelvin[i];
                    }
                }
                if (!found) {
                    console.log("ERRORE il server ha ritornato una carta non esistente: " + data);
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
                console.log("AJAX aimBRI request completed");
            }
        });
        return returnedAiMoveCard;
    }

</script>

</body>
</html>