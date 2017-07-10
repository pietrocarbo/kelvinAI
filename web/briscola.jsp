<%--<%@ page import="main.HW" %>--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <title>Briscola</title>
</head>
<body>
<div>
    <canvas id="myCanvas" width="800" height="600" style="border:1px solid #000000; background-color: darkgreen">
        This text is displayed if your browser does not support HTML5 Canvas.</canvas>
</div>

<script>

    function foo () {
        var c = document.getElementById("myCanvas");
        var ctx = c.getContext("2d");
        <%--ctx.fillText("<%= HW.getMessage() %>", 10, 50);--%>
    }

</script>

</body>
</html>