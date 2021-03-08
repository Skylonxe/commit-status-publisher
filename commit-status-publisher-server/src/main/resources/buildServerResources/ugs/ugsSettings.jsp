<%@ page import="jetbrains.buildServer.web.openapi.PlaceId" %>
<%@ include file="/include-internal.jsp" %>
<%@ taglib prefix="props" tagdir="/WEB-INF/tags/props" %>
<%@ taglib prefix="l" tagdir="/WEB-INF/tags/layout" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="util" uri="/WEB-INF/functions/util" %>

<jsp:useBean id="keys" class="jetbrains.buildServer.commitPublisher.ugs.UgsConstants"/>

<tr>
  <th><label for="${keys.serverUrl}">Server URL:<l:star/></label></th>
  <td>
    <props:textProperty name="${keys.serverUrl}" className="longField"/>
    <span class="error" id="error_${keys.serverUrl}"></span>
    <span class="smallNote">
      The base UGSAPI rest server URL where metadata service is deployed. For example: http://ugsapi-server.net
    </span>
  </td>
</tr>


<tr>
  <th><label for="${keys.badgeName}">Badge:<l:star/></label></th>
  <td>
    <props:textProperty name="${keys.badgeName}" className="longField"/>
    <span class="error" id="error_${keys.badgeName}"></span>
    <span class="smallNote">
      The badge name that will appear in UGS user interface. For example: Win64
    </span>
  </td>
</tr>

<tr>
  <th><label for="${keys.project}">Project:<l:star/></label></th>
  <td>
    <props:textProperty name="${keys.project}" className="longField"/>
    <span class="error" id="error_${keys.project}"></span>
    <span class="smallNote">
      The project to show the badge for. Note that this is the P4 depot path to a folder, not the actual .uproject file. For example: //UE4/Main/Samples/StarterContent
    </span>
  </td>
</tr>

<tr>
  <th><label for="${keys.postBadgeStatusExe}">PostBadgeStatus.exe Path:<l:star/></label></th>
  <td>
    <props:textProperty name="${keys.postBadgeStatusExe}" className="longField"/>
    <span class="error" id="error_${keys.postBadgeStatusExe}"></span>
    <span class="smallNote">
      Path to PostBadgeStatus.exe on the TeamCity server machine. For example: C:/SomeFolder/PostBadgeStatus.exe
    </span>
  </td>
</tr>

<tr class="advancedSetting">
  <th><label for="${keys.change}">Change:<l:star/></label></th>
  <td>
    <props:textProperty name="${keys.change}" className="longField"/>
    <span class="error" id="error_${keys.change}"></span>
    <span class="smallNote">
      The changelist being compiled. Use &#60current&#62 to pass current VCS revision. For example: 123456
    </span>
  </td>
</tr>

<tr class="advancedSetting">
  <th><label for="${keys.badgeLink}">Badge Link:<l:star/></label></th>
  <td>
    <props:textProperty name="${keys.badgeLink}" className="longField"/>
    <span class="error" id="error_${keys.badgeLink}"></span>
    <span class="smallNote">
      If a user clicks on a badge, this is the link that takes the user to the build log. Use &#60buildId&#62 to insert current build id. For example: http://link-to-build-log/&#60buildId&#62
    </span>
  </td>
</tr>
