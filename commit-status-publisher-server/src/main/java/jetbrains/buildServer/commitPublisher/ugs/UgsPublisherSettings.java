/*
 * Copyright 2000-2021 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package jetbrains.buildServer.commitPublisher.ugs;

import jetbrains.buildServer.commitPublisher.*;
import jetbrains.buildServer.commitPublisher.CommitStatusPublisher.Event;
import jetbrains.buildServer.serverSide.*;
import jetbrains.buildServer.serverSide.auth.SecurityContext;
import jetbrains.buildServer.serverSide.oauth.*;
import jetbrains.buildServer.serverSide.oauth.tfs.TfsAuthProvider;
import jetbrains.buildServer.users.SUser;
import jetbrains.buildServer.users.User;
import jetbrains.buildServer.util.StringUtil;
import jetbrains.buildServer.util.ssl.SSLTrustStoreProvider;
import jetbrains.buildServer.vcs.*;
import jetbrains.buildServer.web.openapi.PluginDescriptor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

import static jetbrains.buildServer.commitPublisher.LoggerUtil.LOG;

/**
 * Settings for UGS commit status publisher.
 */
public class UgsPublisherSettings extends BasePublisherSettings implements CommitStatusPublisherSettings {
  private static final Set<Event> mySupportedEvents = new HashSet<Event>() {{
    add(Event.STARTED);
    add(Event.FINISHED);
    add(Event.FAILURE_DETECTED);
    add(Event.MARKED_AS_SUCCESSFUL);
  }};

  public UgsPublisherSettings(@NotNull PluginDescriptor descriptor,
                              @NotNull WebLinks links,
                              @NotNull CommitStatusPublisherProblems problems,
                              @NotNull SSLTrustStoreProvider trustStoreProvider) {
    super(descriptor, links, problems, trustStoreProvider);
  }

  @NotNull
  public String getId() {
    return UgsConstants.ID;
  }

  @NotNull
  public String getName() {
    return "Unreal Game Sync";
  }

  @Nullable
  public String getEditSettingsUrl() {
    return "ugs/ugsSettings.jsp";
  }

  @Override
  public boolean isTestConnectionSupported() {
    return false;
  }

  @Nullable
  public CommitStatusPublisher createPublisher(@NotNull SBuildType buildType, @NotNull String buildFeatureId, @NotNull Map<String, String> params) {
    return new UgsStatusPublisher(this, buildType, buildFeatureId, params, myProblems);
  }

  @NotNull
  public String describeParameters(@NotNull Map<String, String> params) {
    return String.format("Post commit status to %s", getName());
  }

  @Nullable
  @Override
  public Map<String, String> getDefaultParameters() {
    final Map<String, String> result = new HashMap<String, String>();
    result.put(UgsConstants.BADGE_LINK, "%env.teamcityUrl%/viewLog.html?buildId=%teamcity.build.id%&tab=buildLog");
    result.put(UgsConstants.BADGE_NAME, "");
    result.put(UgsConstants.CHANGE, "%build.vcs.number%");
    result.put(UgsConstants.PROJECT, "");
    result.put(UgsConstants.SERVER_URL, "");
    return result;
  }

  @Nullable
  public PropertiesProcessor getParametersProcessor() {
    return new PropertiesProcessor() {
      @NotNull
      public Collection<InvalidProperty> process(@Nullable final Map<String, String> p) {
        final Collection<InvalidProperty> result = new ArrayList<InvalidProperty>();
        return result;
      }
    };
  }

  @Override
  protected Set<Event> getSupportedEvents(final SBuildType buildType, final Map<String, String> params) {
    return mySupportedEvents;
  }
}
