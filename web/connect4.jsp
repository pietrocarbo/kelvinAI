<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <link rel="stylesheet" type="text/css" href="connect4.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
    <script type="text/javascript" src="connect4.js"></script>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="icon" type="image/png" href="icons8-Retro-Robot-64.png" sizes="64x64"/>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta/css/bootstrap.min.css"
          integrity="sha384-/Y6pD6FV/Vv2HJnA6t+vslU6fwYXjCFtcEpHbNJ0lyAFsXTsjBbfaDjzALeQsN6M" crossorigin="anonymous">
    <title>Connect 4</title>
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
                <li class="nav-item active">
                    <a class="nav-link" href="#" style="background-color: gray">Forza 4<span
                            class="sr-only">(current)</span></a>
                </li>
                <li class="nav-item">
                    <a class="nav-link " href="/kelvin/briscola.jsp">Briscola</a>
                </li>
            </ul>
        </div>
    </nav>
</div>
<br><br>

<div class="container-fluid">
    <div class="table-responsive">
        <table class="table-bordered"
               style="width: 40%; background-color: white; border: 0px; padding: 1px; text-align: center;">
            <tr>
                <td>
                    <button id="0" style="height: 50px; width: 50px" class="down-arrow" onclick="move(0)" disabled>
                        &darr;
                    </button>
                </td>
                <td>
                    <button id="1" style="height: 50px; width: 50px" class="down-arrow" onclick="move(1)" disabled>
                        &darr;
                    </button>
                </td>
                <td>
                    <button id="2" style="height: 50px; width: 50px" class="down-arrow" onclick="move(2)" disabled>
                        &darr;
                    </button>
                </td>
                <td>
                    <button id="3" style="height: 50px; width: 50px" class="down-arrow" onclick="move(3)" disabled>
                        &darr;
                    </button>
                </td>
                <td>
                    <button id="4" style="height: 50px; width: 50px" class="down-arrow" onclick="move(4)" disabled>
                        &darr;
                    </button>
                </td>
                <td>
                    <button id="5" style="height: 50px; width: 50px" class="down-arrow" onclick="move(5)" disabled>
                        &darr;
                    </button>
                </td>
                <td>
                    <button id="6" style="height: 50px; width: 50px" class="down-arrow" onclick="move(6)" disabled>
                        &darr;
                    </button>
                </td>
            </tr>
        </table>
    </div>

    <div class="table-responsive" id="board-container"></div>
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
        <option value="2">Principiante</option>
        <option value="4">Moderato</option>
        <option value="7">Esperto</option>
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