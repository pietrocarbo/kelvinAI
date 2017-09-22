<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html>
<head>
    <link rel="icon" type="image/png" href="icons8-Retro-Robot-64.png" sizes="64x64"/>
    <link rel="stylesheet" type="text/css" href="briscola.css">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta/css/bootstrap.min.css"
          integrity="sha384-/Y6pD6FV/Vv2HJnA6t+vslU6fwYXjCFtcEpHbNJ0lyAFsXTsjBbfaDjzALeQsN6M" crossorigin="anonymous">
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Briscola</title>
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
                <li class="nav-item">
                    <a class="nav-link" href="/tictactoe.jsp">Tris</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="/connect4.jsp">Forza 4</a>
                </li>
                <li class="nav-item active">
                    <a class="nav-link " href="#" style="background-color: gray">Briscola<span
                            class="sr-only">(current)</span></a>
                </li>
            </ul>
        </div>
    </nav>
</div>
<br>

<div class="container-fluid">
    <div class="table-responsive" id="board-container">
        <table class="table-bordered" style="width: 40%;">
            <tr>
                <td><p id="kelvinTurn" style="display: none">È il turno dell'avversario (sta pensando...)</p></td>
                <td>
                    <button id="btnK0" type="button">
                        <img id="imgK0" src="cards/RetroCarteNapoletaneNormale.jpg" width="180" height="250"
                             style="border:2px solid #00C6FF" vspace="5" alt="missing image" align="middle">
                    </button>
                </td>
                <td>
                    <button id="btnK1" type="button">
                        <img id="imgK1" src="cards/RetroCarteNapoletaneNormale.jpg" width="180" height="250"
                             style="border:2px solid #00C6FF" vspace="5" alt="missing image" align="middle">
                    </button>
                </td>
                <td>
                    <button id="btnK2" type="button">
                        <img id="imgK2" src="cards/RetroCarteNapoletaneNormale.jpg" width="180" height="250"
                             style="border:2px solid #00C6FF" vspace="5" alt="missing image" align="middle">
                    </button>
                </td>
            </tr>

            <tr>
                <td>
                    <div style="position: relative;">
                        <a href="#" style="text-decoration: none; color: black; pointer-events: none; cursor: default;">
                            <img id="briscola" src="cards/noCard.png" width="90" height="125"/>
                            <div>Briscola</div>
                        </a>

                        <a href="#" style="text-decoration: none; color: black; pointer-events: none; cursor: default;">
                            <img id="mazzo" src="cards/RetroCarteNapoletane.jpg" width="150" height="125"/>
                            <div id="turnsLeft"></div>
                        </a>
                    </div>
                </td>

                <td id="winner"></td>
                <td id="tdT0">
                    1º di mano
                    <button type="button" style="width:180px; height: 250px">
                        <img id="imgT0" src="cards/noCard.png" width="160px" height="230px"
                             style="border:2px solid white"
                             title="prima carta giocata" alt="missing image">
                    </button>
                </td>
                <td id="tdT1">
                    2º di mano
                    <button type="button" style="width:180px; height: 250px">
                        <img id="imgT1" src="cards/noCard.png" width="160px" height="230px"
                             style="border:2px solid white"
                             title="seconda carta giocata" alt="missing image">
                    </button>
                </td>
            </tr>

            <tr>
                <td><p id="humanTurn" style="display: none">È il tuo turno</p></td>
                <td>
                    <button id="btnH0" type="button">
                        <img id="imgH0" src="cards/noCard.png" class="myCards" width="180" height="250"
                             title="gioca prima carta" style="border:2px solid #FC695F" vspace="5" alt="missing image"
                             align="middle">
                    </button>
                </td>
                <td>
                    <button id="btnH1" type="button">
                        <img id="imgH1" src="cards/noCard.png" class="myCards" width="180" height="250"
                             title="gioca seconda carta" style="border:2px solid #FC695F" vspace="5" alt="missing image"
                             align="middle">
                    </button>
                </td>
                <td>
                    <button id="btnH2" type="button">
                        <img id="imgH2" src="cards/noCard.png" class="myCards" width="180" height="250"
                             title="gioca terza carta" style="border:2px solid #FC695F" vspace="5" alt="missing image"
                             align="middle">
                    </button>
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
    <br>
    Scegli il grado di abilità di Kelvin<br>
    <select class="custom-select" name="depthSelected" id="depthSelected">
        <option value="1">Principiante</option>
        <option value="2">Moderato</option>
        <option value="3">Esperto</option>
    </select>
</div>

<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.11.0/umd/popper.min.js"
        integrity="sha384-b/U6ypiBEHpOf/4+1nzFpr53nxSS+GLCkfwBdFNTxtclqqenISfwAzpKaMNFNmj4"
        crossorigin="anonymous"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta/js/bootstrap.min.js"
        integrity="sha384-h0AbiXch4ZDo7tp9hKZ4TsHbi047NrKGLO3SEJAg45jXxnGIfYzk4Si90RDIqNm1"
        crossorigin="anonymous"></script>
<script>
    var deck, briscola, board = [], handKelvin = [], handHuman = [], collectedKelvin = [], collectedHuman = [],
        turnsLeft = 20, gameOver = false;
    var briscolaID = '#briscola', humanCardsID = ['#imgH0', '#imgH1', '#imgH2'],
        kelvinCardsID = ['#imgK0', '#imgK1', '#imgK2'], humanButtonIDs = ["#btnH0", "#btnH1", "#btnH2"];
    var returnedAiMoveCard, firstHandPlayer, firstHandPlayerCardIndex, secondHandPlayer, secondHandPlayerCardIndex;

    $(document).ready(function () {
        for (var i = 0; i < 3; i++) {
            $(humanButtonIDs[i]).click(humanClick(i));
        }
    });

    function humanClick(index) {
        return function (e) {
            var cardImage = $(humanCardsID[index]).attr('src').split("di");
            cardImage[0] = cardImage[0].substring(6);
            cardImage[1] = cardImage[1].substring(0, cardImage[1].indexOf(".jpg"));
            move('human', new Card(briscolaValue(briscolaNamesToNumericalValue(cardImage[0])), briscolaPoints(cardImage[0]), cardImage[0], cardImage[1]));
        }

    }

    function startGame(startingPlayer) {
        $('#starterSelect').html('<p><input type="button" class="btn btn-secondary" value="Nuova partita" onClick="window.location.reload()"><\p>');

        if (startingPlayer == 'ai') {
            humanButtonsDisabled(true);
            $('#kelvinTurn').css('display', 'block');
            setupGame();
            getAiMove();

        } else {
            $('#humanTurn').css('display', 'block');
            setupGame();

        }
        console.log(startingPlayer + " is starting a new game");
    }

    function setupGame() {
        deck = shuffle(Deck());
        handKelvin = deck.splice(0, 3);
        handHuman = deck.splice(0, 3);
        briscola = deck[deck.length - 1];

        updateCardDisplay(briscolaID, briscola);
        updateCardDisplay(humanCardsID, handHuman);
//        updateCardDisplay(kelvinCardsID, handKelvin);
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
        this.suits = ['DENARI', 'SPADE', 'COPPE', 'BASTONI'];
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
                console.log('ERROR in briscola name ' + name + ' in numerical conversion');
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

    function humanButtonsDisabled(disabled) {
        for (var i = 0; i < humanButtonIDs.length; i++) {
            $(humanButtonIDs[i]).prop('disabled', disabled);
        }
    }

    function chooseWinner(card0, card1) {
        var briscolaSuit = briscola.suit;

        if (card0.suit == briscolaSuit && card0.suit != card1.suit) return 0; // ha vinto il primo di mano
        if (card1.suit == briscolaSuit && card0.suit != card1.suit) return 1;  // ha vinto il secondo di mano

        if (card0.suit == card1.suit) return card0.value > card1.value ? 0 : 1;

        return 0;  // vince il primo di mano
    }

    function mapCardToHandArray(caller, cardChosen) {
        var handArray;

        if (caller == 'ai') handArray = handKelvin;
        else if (caller == 'human') handArray = handHuman;
        else {
            console.log("ERROR in mapping card of unknown player " + caller);
        }

        for (var i = 0; i < handArray.length; i++) {
            if (cardChosen.name == handArray[i].name && cardChosen.suit == handArray[i].suit) {
                return i;
            }
        }

        console.log("ERROR impossible to find mapping for card " + cardChosen.name + " di " + cardChosen.suit + " in player " + caller + " hand");
    }

    function mapCardToImage(caller, cardChosen) {
        var imageCardsID;

        if (caller == 'ai') imageCardsID = kelvinCardsID;
        else if (caller == 'human') imageCardsID = humanCardsID;
        else {
            console.log("ERROR in mapping card of unknown player " + caller);
        }

        for (var i = 0; i < imageCardsID.length; i++) {

            if ($(imageCardsID[i]).attr('src') != 'cards/noCard.png') {

                if (caller == 'human') {
                    var cardImage = $(imageCardsID[i]).attr('src').split("di");
                    cardImage[0] = cardImage[0].substring(6);
                    cardImage[1] = cardImage[1].substring(0, cardImage[1].indexOf(".jpg"));
                    if (cardImage[0] == cardChosen.name && cardImage[1] == cardChosen.suit) {
                        return imageCardsID[i];
                    }

                } else {
                    return imageCardsID[i];
                }

            }
        }

        console.log("ERROR impossible to find mapping for card " + cardChosen.name + " di " + cardChosen.suit + " in player " + caller + " hand");
    }

    function move(caller, card) {

        var cardToRemoveID = mapCardToImage(caller, card);
        var cardIndex = mapCardToHandArray(caller, card);

        var nextPlayerIndex = (caller == "ai" ? "human" : "ai");
        var boardPositionID = (board.length == 0 ? '#imgT0' : '#imgT1');

        console.log("move() called by " + caller + ": n. carte sul tavolo " + board.length + " indice carta giocata " + cardIndex + " cardToRemoveID " + cardToRemoveID + " boardPositionID " + boardPositionID);

        if (caller == 'ai') {
            $(boardPositionID).css({"border-width": "2px", "border-style": "solid", "border-color": "#00C6FF"});
            updateCardDisplay(boardPositionID, handKelvin[cardIndex]);
            board.push(handKelvin[cardIndex]);
        } else {
            $(boardPositionID).css({"border-width": "2px", "border-style": "solid", "border-color": "#FC695F"});
            updateCardDisplay(boardPositionID, handHuman[cardIndex]);
            board.push(handHuman[cardIndex]);
        }
        humanButtonsDisabled(true);
        updateCardDisplay(cardToRemoveID);
        $(cardToRemoveID).css("border-width", "0px");

        setTimeout(function () {

            if (board.length == 2) {
                $('#tdT' + (chooseWinner(board[0], board[1]) == 0 ? 0 : 1)).css('background-color', '#27A319');
            }

            setTimeout(function () {

                if (board.length == 2) {
                    // choosing winner
                    secondHandPlayer = caller;
                    secondHandPlayerCardIndex = cardIndex;

                    nextPlayerIndex = (chooseWinner(board[0], board[1]) == 0 ? firstHandPlayer : secondHandPlayer);
                    console.log("collecting phase: player " + nextPlayerIndex + " won this turn");

                    // collecting
                    updateCardDisplay(['#imgT0', '#imgT1']);
                    if (nextPlayerIndex == 'ai') {
                        collectedKelvin.push(board.pop());
                        collectedKelvin.push(board.pop());
                    } else if (nextPlayerIndex == 'human') {
                        collectedHuman.push(board.pop());
                        collectedHuman.push(board.pop());
                    } else {
                        console.log("ERROR wrong nextPlayerIndex found during collecting: " + nextPlayerIndex);
                    }

                    // dealing
                    turnsLeft--;
                    $('#turnsLeft').text('Mani rimanenti ' + turnsLeft);
                    console.log("entering dealing phase (turn " + turnsLeft + ")");

                    if (turnsLeft == 0) {
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

                    } else if (turnsLeft >= 3) {
                        // deal new cards in the respective handKelvin/handHuman positions removing them from the top the deck
                        var indexToDeal = -1;

                        if (nextPlayerIndex == 'ai') {

                            indexToDeal = (firstHandPlayer == 'ai' ? firstHandPlayerCardIndex : secondHandPlayerCardIndex);
                            handKelvin[indexToDeal] = deck.shift();

                            indexToDeal = (indexToDeal == firstHandPlayerCardIndex ? secondHandPlayerCardIndex : firstHandPlayerCardIndex);
                            handHuman[indexToDeal] = deck.shift();

                        } else if (nextPlayerIndex == 'human') {

                            indexToDeal = (firstHandPlayer == 'human' ? firstHandPlayerCardIndex : secondHandPlayerCardIndex);
                            handHuman[indexToDeal] = deck.shift();

                            indexToDeal = (indexToDeal == firstHandPlayerCardIndex ? secondHandPlayerCardIndex : firstHandPlayerCardIndex);
                            handKelvin[indexToDeal] = deck.shift();

                        } else {
                            console.log("ERROR wrong winner(i.e. nextPlayerIndex) found " + nextPlayerIndex);
                        }

                        if (turnsLeft == 3) { // "cancellare" graficamente il mazzo e la briscola
                            console.log("dealing at turn 4, erasing deck of length " + deck.length);
                            updateCardDisplay(['#mazzo']);
                            $(briscolaID).replaceWith('<p>' + briscola.suit + '</p>');
                        }

                        updateCardDisplay(humanCardsID, handHuman);
                        var arrayCardImageKelvin = [];
                        for (var i = 0; i < kelvinCardsID.length; i++) {
                            arrayCardImageKelvin.push('RetroCarteNapoletaneNormale.jpg');
                        }
                        updateCardDisplay(kelvinCardsID, arrayCardImageKelvin);
                        for (var i = 0; i < 3; i++) {
                            $(humanCardsID[i]).css({
                                "border-width": "2px",
                                "border-style": "solid",
                                "border-color": "#FC695F"
                            });
                            $(kelvinCardsID[i]).css({
                                "border-width": "2px",
                                "border-style": "solid",
                                "border-color": "#00C6FF"
                            });
                        }


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

                    $("#imgT0").css("border-width", "0px");
                    $("#imgT1").css("border-width", "0px");
                    $('#tdT0').css('background-color', '#fdf7da');
                    $('#tdT1').css('background-color', '#fdf7da');
                    firstHandPlayer = null;
                    firstHandPlayerCardIndex = null;


                } else {
                    $('#tdT0').css('background-color', '#fdf7da');
                    $('#tdT1').css('background-color', '#fdf7da');
                    firstHandPlayer = caller;
                    firstHandPlayerCardIndex = cardIndex;
                }

                if (gameOver) {
                    $('#kelvinTurn').css('display', 'none');
                    $('#humanTurn').css('display', 'none');
                    console.log("game over");
                } else if (nextPlayerIndex == 'human') {
                    $('#humanTurn').css('display', 'block');
                    $('#kelvinTurn').css('display', 'none');
                    humanButtonsDisabled(false);
                } else if (nextPlayerIndex == 'ai') {
                    $('#kelvinTurn').css('display', 'block');
                    $('#humanTurn').css('display', 'none');
                    getAiMove();
                } else {
                    console.log("ERROR wrong nextPlayerIndex " + nextPlayerIndex);
                }

            }, 1500); // let the user see the winning card of the board
        }, 500); // let the user see the card moved into the board
    }

    function updateCardDisplay(ids, cards) {
        console.log('updateCardDisplay entered: ids -> ' + ids + ' cards... ');
        console.log(cards);

        if (Array.isArray(ids)) {
            for (var i = 0; i < ids.length; i++) {
                if (cards === undefined) {
                    var cardPath = 'noCard.png';
                } else if (cards[i] instanceof Card) {
                    var cardPath = cards[i].name + 'di' + cards[i].suit + '.jpg';
                } else if (cards[i] = 'RetroCarteNapoletaneNormale.jpg') {
                    var cardPath = cards[i];
                }
                $(ids[i]).attr('src', 'cards/' + cardPath);

            }
        } else {
            if (cards === undefined) {
                var cardPath = 'noCard.png';
            } else if (cards instanceof Card) {
                var cardPath = cards.name + 'di' + cards.suit + '.jpg';
            } else if (cards = 'RetroCarteNapoletaneNormale.jpg') {
                var cardPath = cards;
            }
            $(ids).attr('src', 'cards/' + cardPath);
        }
    }

    async
    function getAiMove() {
        var depth = $('select[name=depthSelected]').val();
        console.log("entered getAiMove(): turnsLeft " + turnsLeft + " board length " + board.length + " deck length " + deck.length + ", depth " + depth);
        var randomDeals = -1, searchDepth = -1;
        if (depth == 1) {
            randomDeals = 50;
            searchDepth = 2;
        } else if (depth == 2) {
            randomDeals = 100;
            searchDepth = 5;
        } else if (depth == 3) {
            randomDeals = 250;
            searchDepth = 9;
        } else {
            console.log("ERROR impossible to recognize depth of search");
        }

        var unknownCards = deck.slice(0, deck.length - 1).concat(handHuman);
        $.ajax({
            url: 'aimBRI',
            type: 'GET',
            dataType: 'text',
            data: {
                depth: searchDepth,
                numberOfRandomDeals: randomDeals,
                nOppCards: handHuman.length,
                nMyCards: handKelvin.length,
                nBoardCards: board.length,
                nUnknownCards: unknownCards.length,
                turns: (20 - turnsLeft),
                myCards: handKelvin,
                nHumanCollected: collectedHuman.length,
                humanCollected: collectedHuman,
                briscola: briscola,
                board: board,
                unknownCards: unknownCards
            },
            success: function (data) {
                console.log("Kelvin chosen move " + data);
                var found = false;
                var chosenCardData = data.split("di");
                for (var i = 0; i < handKelvin.length; i++) {
                    if (chosenCardData[0] == handKelvin[i].name && chosenCardData[1] == handKelvin[i].suit) {
                        found = true;
                        returnedAiMoveCard = handKelvin[i];
                    }
                }
                if (!found) {
                    console.log("ERRORE il server ha ritornato una carta non esistente: " + data);
                }
                move('ai', returnedAiMoveCard);
            },
            error: function (jqxhr, textStatus, errorThrown) {
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