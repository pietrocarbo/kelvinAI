<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html>
<head>
    <link rel="icon" type="image/png" href="icons8-Retro-Robot-64.png" sizes="64x64"/>
    <link rel="stylesheet" type="text/css" href="briscola.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
    <script type="text/javascript" src="briscola.js"></script>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta/css/bootstrap.min.css"
          integrity="sha384-/Y6pD6FV/Vv2HJnA6t+vslU6fwYXjCFtcEpHbNJ0lyAFsXTsjBbfaDjzALeQsN6M" crossorigin="anonymous">
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Briscola</title>
</head>
<body>

<div class="container-fluid">
    <nav class="navbar navbar-expand-lg navbar-light" style="background-color: lightgrey;">
        <a class="navbar-brand" href="/kelvin/index.jsp">Home</a>
        <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarSupportedContent"
                aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>

        <div class="collapse navbar-collapse" id="navbarSupportedContent">
            <ul class="navbar-nav mr-auto">
                <li class="nav-item">
                    <a class="nav-link" href="/kelvin/tictactoe.jsp">Tris</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="/kelvin/connect4.jsp">Forza 4</a>
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
                    1º di mano<br>
                    <button type="button" style="width:180px; height: 250px">
                        <img id="imgT0" src="cards/noCard.png" width="160px" height="230px"
                             style="border:2px solid white"
                             title="prima carta giocata" alt="missing image">
                    </button>
                </td>
                <td id="tdT1">
                    2º di mano<br>
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

<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.11.0/umd/popper.min.js"
        integrity="sha384-b/U6ypiBEHpOf/4+1nzFpr53nxSS+GLCkfwBdFNTxtclqqenISfwAzpKaMNFNmj4"
        crossorigin="anonymous"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta/js/bootstrap.min.js"
        integrity="sha384-h0AbiXch4ZDo7tp9hKZ4TsHbi047NrKGLO3SEJAg45jXxnGIfYzk4Si90RDIqNm1"
        crossorigin="anonymous"></script>
</body>
</html>