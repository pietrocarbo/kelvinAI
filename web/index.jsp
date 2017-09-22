<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta/css/bootstrap.min.css"
          integrity="sha384-/Y6pD6FV/Vv2HJnA6t+vslU6fwYXjCFtcEpHbNJ0lyAFsXTsjBbfaDjzALeQsN6M" crossorigin="anonymous">
    <link rel="icon" type="image/png" href="icons8-Retro-Robot-64.png" sizes="64x64"/>
    <title>Home @ Kelvin-AI</title>
</head>
<body>

<div class="jumbotron text-center">
    <h1>Agente Kelvin</h1>
    <p>Benvenuto, scegli il gioco in cui sfidare l'intelligenza artificiale</p>
</div>

<div class="container-fluid">
    <div class="row">
        <div class="col">
            <h3>Tris</h3>
            <p>Chiamato anche Tris. Sarai il giocatore 'X'.</p>
            <a href="/tictactoe.jsp" class="btn btn-success" role="button">Gioca</a>
        </div>
        <div class="col">
            <h3>Forza 4</h3>
            <p>Chiamato anche Connect 4. Sarai il giocatore 'X'.</p>
            <a href="/connect4.jsp" class="btn btn-success" role="button">Gioca</a>
        </div>
        <div class="col">
            <h3>Briscola</h3>
            <p>Il famoso gioco di carte. Usando il mazzo piacentino.</p>
            <a href="/briscola.jsp" class="btn btn-success" role="button">Gioca</a>
        </div>
    </div>
</div>

<script src="https://code.jquery.com/jquery-3.2.1.slim.min.js"
        integrity="sha384-KJ3o2DKtIkvYIK3UENzmM7KCkRr/rE9/Qpg6aAZGJwFDMVNA/GpGFF93hXpG5KkN"
        crossorigin="anonymous"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.11.0/umd/popper.min.js"
        integrity="sha384-b/U6ypiBEHpOf/4+1nzFpr53nxSS+GLCkfwBdFNTxtclqqenISfwAzpKaMNFNmj4"
        crossorigin="anonymous"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta/js/bootstrap.min.js"
        integrity="sha384-h0AbiXch4ZDo7tp9hKZ4TsHbi047NrKGLO3SEJAg45jXxnGIfYzk4Si90RDIqNm1"
        crossorigin="anonymous"></script>
</body>
</html>