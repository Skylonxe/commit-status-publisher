<%@ page import="jetbrains.buildServer.web.openapi.PlaceId" %>
<%@ include file="/include-internal.jsp" %>
<%@ taglib prefix="props" tagdir="/WEB-INF/tags/props" %>
<%@ taglib prefix="l" tagdir="/WEB-INF/tags/layout" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="util" uri="/WEB-INF/functions/util" %>

<%--
  ~ Copyright 2000-2021 JetBrains s.r.o.
  ~
  ~ Licensed under the Apache License, Version 2.0 (the "License");
  ~ you may not use this file except in compliance with the License.
  ~ You may obtain a copy of the License at
  ~
  ~ http://www.apache.org/licenses/LICENSE-2.0
  ~
  ~ Unless required by applicable law or agreed to in writing, software
  ~ distributed under the License is distributed on an "AS IS" BASIS,
  ~ WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  ~ See the License for the specific language governing permissions and
  ~ limitations under the License.
  --%>

<jsp:useBean id="keys" class="jetbrains.buildServer.commitPublisher.ugs.UgsConstants"/>

<tr>
  <th><label for="${keys.serverUrl}">Server URL:</label></th>
  <td>
    <props:textProperty name="${keys.serverUrl}" className="longField"/>
    <span class="error" id="error_${keys.serverUrl}"></span>
    <span class="smallNote">
      The base UGSAPI rest server URL where metadata service is deployed, e.g. http://ugsapi-server.net
    </span>
  </td>
</tr>


<tr>
  <th><label for="${keys.badgeName}">Badge:</label></th>
  <td>
    <props:textProperty name="${keys.badgeName}" className="longField"/>
    <span class="error" id="error_${keys.badgeName}"></span>
    <span class="smallNote">
      The badge name that will appear in UGS, e.g. Win64
    </span>
  </td>
</tr>

<tr>
  <th><label for="${keys.project}">Project:</label></th>
  <td>
    <props:textProperty name="${keys.project}" className="longField"/>
    <span class="error" id="error_${keys.project}"></span>
    <span class="smallNote">
      The project to show the badge for. Note that this is the P4 depot path to a folder, not the actual .uproject file. e.g. //UE4/Main/Samples/StarterContent
    </span>
  </td>
</tr>

<tr class="advancedSetting">
  <th><label for="${keys.change}">Change:</label></th>
  <td>
    <props:textProperty name="${keys.change}" className="longField"/>
    <span class="error" id="error_${keys.change}"></span>
    <span class="smallNote">
      The changelist being compiled. e.g. 123456. Leave empty to pass current VCS revision.
    </span>
  </td>
</tr>

<tr class="advancedSetting">
  <th><label for="${keys.badgeLink}">Badge Link:</label></th>
  <td>
    <props:textProperty name="${keys.badgeLink}" className="longField"/>
    <span class="error" id="error_${keys.badgeLink}"></span>
    <span class="smallNote">
      If a user clicks on a badge, this is the link that takes the user to the build log. e.g. http://link-to-build-log
    </span>
  </td>
</tr>

