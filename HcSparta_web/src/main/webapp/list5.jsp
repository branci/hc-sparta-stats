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

<form action="${pageContext.request.contextPath}/players" method="get">
  <input type="submit" value="Players">
</form>
<form action="${pageContext.request.contextPath}/matches" method="get">
  <input type="submit" value="Opponents">
</form>

<h2>Statistics of players against <td><c:out value="${opponent}"/></td> </h2>

<form action="${pageContext.request.contextPath}/matches/players/year" method="post">

  <select name="positionItem">
    <option value="0">All players</option>
    <option value="1">Back players</option>
    <option value="2">Forward players</option>
  </select>
  <select name="gamesItem">
    <option value="2">All</option>
    <option value="1">Regular</option>
    <option value="0">Playoff</option>
  </select>
  <p>   </p>
  <select name="orderItem">
    <option value="NAME">Name</option>
    <option value="GOALS">Goals</option>
    <option value="ASSISTS">Assists</option>
    <option value="PENALTY_MINUTES">Penalty minutes</option>
    <option value="SHOTS">Shots</option>
    <option value="HITS">Hits</option>
  </select>
    <select name="ascItem">
    <option value="true">Ascending</option>
    <option value="false">Descending</option>
  </select>
  <input type="submit" value="Order by">
</form>
    
<table border="1">
    <thead>
    <tr>
        <th> </th>
        <th>Name</th>
        <th>Goals</th>
        <th>Assists</th>
        <th>Penalty minutes</th>
        <th>Shots</th>
        <th>Hits</th>
        <th>Shots effectivity</th>
    </tr>
    </thead>
    <c:forEach items="${players}" var="player">
        <tr>
            <td>   
                <form action="${pageContext.request.contextPath}/players/player" method="post">
                    <input type="hidden" name="idItem" value="${player.id}" />
                    <input type="submit" value="->">  
                </form>       
            </td>
            <td><c:out value="${player.name}"/></td>
            <td><c:out value="${player.goals}"/></td>
            <td><c:out value="${player.assist}"/></td>
            <td><c:out value="${player.penalty}"/></td>
            <td><c:out value="${player.shots}"/></td>
            <td><c:out value="${player.hits}"/></td>
            <td><c:out value="${player.shotEffectivity} %"/></td>
        </tr>
    </c:forEach>
</table>

</body>
</html>
