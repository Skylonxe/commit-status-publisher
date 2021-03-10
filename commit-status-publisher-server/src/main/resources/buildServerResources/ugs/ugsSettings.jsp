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
      The badge name that will appear in UGS user interface. For example: Win
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

<tr class="advancedSetting">
  <th><label for="${keys.postResultAsSoonAsPossible}">Post as Soon as Possible:<l:star/></label></th>
  <td>
    <props:checkboxProperty name="${keys.postResultAsSoonAsPossible}"/>
    <span class="error" id="error_${keys.postResultAsSoonAsPossible}"></span>
    <span class="smallNote">
      If enabled, badge showing resulting status of the build can be in some cases posted even before finishing the build when we detected the build will fail/succeed.
    </span>
  </td>
</tr>

<tr class="advancedSetting">
  <th><label for="${keys.postWhenQueued}">Post when Queued:<l:star/></label></th>
  <td>
    <props:checkboxProperty name="${keys.postWhenQueued}"/>
    <span class="error" id="error_${keys.postWhenQueued}"></span>
    <span class="smallNote">
      Starting badge will be posted as soon as the build is added to the build queue. When build is removed from the queue, Starting badge will remain.
    </span>
  </td>
</tr>

<tr class="advancedSetting">
  <th><label for="${keys.interruptedAction}">When Interrupted:<l:star/></label></th>
  <td>
    <props:selectProperty name="${keys.interruptedAction}" className="mediumField">
        <c:set var="modeSelected" value="${propertiesBean.properties[keys.interruptedAction]}"/>
        <props:option value="${keys.interruptedActionNothing}" currValue="${modeSelected}">Do nothing</props:option>
        <props:option value="${keys.interruptedActionFail}" currValue="${modeSelected}">Fail</props:option>
        <props:option value="${keys.interruptedActionWarning}" currValue="${modeSelected}">Warn</props:option>
        <props:option value="${keys.interruptedActionSuccess}" currValue="${modeSelected}">Succeed</props:option>
    </props:selectProperty>
    <span class="error" id="error_${keys.interruptedAction}"></span>
    <span class="smallNote">
      What to post when build is canceled.
    </span>
  </td>
</tr>