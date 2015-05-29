<%-- 
    Document   : list
    Created on : May 28, 2015, 11:43:32 PM
    Author     : Branislav Smik <xsmik @fi.muni>
--%>

<%@page contentType="text/html;charset=utf-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<body>  
<h1>HC Sparta Praha</h1>
<h2>Players</h2>
<table border="1">
    <thead>
    <tr>
        <th>Id</th>
        <th>Name</th>
        <th>Age</th>
        <th>Height</th>
        <th>Weight</th>
        <th>Num</th>
        <th>Pos</th>
        <th>Goals</th>
        <th>Assists</th>
        <th>Penalty minutes</th>
        <th>Shots</th>
        <th>Hits</th>
    </tr>
    </thead>
    <c:forEach items="${players}" var="player">
        <tr>
            <td><c:out value="${player.id}"/></td>
            <td><c:out value="${player.name}"/></td>
            <td><c:out value="${player.age}"/></td>
            <td><c:out value="${player.height}"/></td>
            <td><c:out value="${player.weight}"/></td>
            <td><c:out value="${player.playerNum}"/></td>
            <td><c:out value="${player.position}"/></td>
            <td><c:out value="${player.goals}"/></td>
            <td><c:out value="${player.assists}"/></td>
            <td><c:out value="${player.penalty}"/></td>
            <td><c:out value="${player.shots}"/></td>
            <td><c:out value="${player.hits}"/></td>
        </tr>
    </c:forEach>
</table>
<br><br><br>

</body>
</html>
