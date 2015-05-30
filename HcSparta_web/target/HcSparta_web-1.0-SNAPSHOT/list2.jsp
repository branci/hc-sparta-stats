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
  
<h2>Matches</h2>

<form action="${pageContext.request.contextPath}/matches/season" method="post">
    <select name="seasonItem">
    <option value="2015">2014/15</option>
    <option value="2014">2013/14</option>
    <option value="2013">2012/13</option>
    <option value="2012">2011/12</option>
    <option value="2011">2010/11</option>
    <option value="2010">2009/10</option>
  </select>
  <select name="gamesItem">
    <option value="1">Regular</option>
    <option value="0">Playoff</option>
    <option value="2">All</option>
  </select>
  <input type="submit" value="Select">
</form>

<table border="1">
    <thead>
    <tr>
        <th>Opponent</th>
        <th>S-win</th>
        <th>S-los</th>
        <th>O-goa</th>
        <th>S-goa</th>
        <th>O-sho</th>
        <th>S-sho</th>
        <th>O-pen</th>
        <th>S-pen</th>
    </tr>
    </thead>
    <c:forEach items="${opponents}" var="opp">
        <tr>
            <td><c:out value="${opp.opponent}"/></td>
            <td><c:out value="${opp.win}"/></td>
            <td><c:out value="${opp.lose}"/></td>
            <td><c:out value="${opp.opponentGoals}"/></td>
            <td><c:out value="${opp.spartaGoals}"/></td>
            <td><c:out value="${opp.opponentShots}"/></td>
            <td><c:out value="${opp.spartaShots}"/></td>
            <td><c:out value="${opp.opponentPenalty}"/></td>
            <td><c:out value="${opp.spartaPenalty}"/></td>
        </tr>
    </c:forEach>
</table>
  
  

</body>
</html>
