<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <link rel="stylesheet" type="text/css" href="tictactoe.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
    <script type="text/javascript" src="tictactoe.js"></script>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta/css/bootstrap.min.css"
          integrity="sha384-/Y6pD6FV/Vv2HJnA6t+vslU6fwYXjCFtcEpHbNJ0lyAFsXTsjBbfaDjzALeQsN6M" crossorigin="anonymous">
    <link rel="icon" type="image/png" href="icons8-Retro-Robot-64.png" sizes="64x64"/>
    <title>Tic Tac Toe</title>
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
                <li class="nav-item active">
                    <a class="nav-link" href="#" style="background-color: gray">Tris<span
                            class="sr-only">(current)</span></a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="/kelvin/connect4.jsp">Forza 4</a>
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

<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.11.0/umd/popper.min.js"
        integrity="sha384-b/U6ypiBEHpOf/4+1nzFpr53nxSS+GLCkfwBdFNTxtclqqenISfwAzpKaMNFNmj4"
        crossorigin="anonymous"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta/js/bootstrap.min.js"
        integrity="sha384-h0AbiXch4ZDo7tp9hKZ4TsHbi047NrKGLO3SEJAg45jXxnGIfYzk4Si90RDIqNm1"
        crossorigin="anonymous"></script>
</body>
</html>