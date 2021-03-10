// Copyright Ondrej Hrusovsky

package jetbrains.buildServer.commitPublisher.ugs;

import jetbrains.buildServer.commitPublisher.*;
import jetbrains.buildServer.commitPublisher.CommitStatusPublisher.Event;
import jetbrains.buildServer.serverSide.*;
import jetbrains.buildServer.util.StringUtil;
import jetbrains.buildServer.util.ssl.SSLTrustStoreProvider;
import jetbrains.buildServer.web.openapi.PluginDescriptor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * Settings for UGS commit status publisher.
 */
public class UgsPublisherSettings extends BasePublisherSettings implements CommitStatusPublisherSettings {
  private static final Set<Event> mySupportedEvents = new HashSet<Event>() {{
    add(Event.QUEUED);
    add(Event.STARTED);
    add(Event.FINISHED);
    add(Event.MARKED_AS_SUCCESSFUL);
    add(Event.INTERRUPTED);
    add(Event.FAILURE_DETECTED);
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
    result.put(UgsConstants.BADGE_LINK, "%env.teamcityUrl%/viewLog.html?buildId=<buildId>&tab=buildLog");
    result.put(UgsConstants.BADGE_NAME, "");
    result.put(UgsConstants.CHANGE, "<current>");
    result.put(UgsConstants.PROJECT, "%env.unreal.ugs.perforceProjectPath%");
    result.put(UgsConstants.SERVER_URL, "%env.unreal.ugs.serverUrl%");
    result.put(UgsConstants.POST_BADGE_STATUS_EXE, "%env.unreal.ugs.postBadgeStatusExe%");
    result.put(UgsConstants.INTERRUPTED_ACTION, UgsConstants.INTERRUPTED_ACTION_FAIL);
    result.put(UgsConstants.POST_RESULT_AS_SOON_AS_POSSIBLE, "true");
    result.put(UgsConstants.POST_WHEN_QUEUED, "false");
    return result;
  }

  @Nullable
  public PropertiesProcessor getParametersProcessor() {
    return new PropertiesProcessor() {
      private boolean checkNotEmpty(@NotNull final Map<String, String> properties,
                                    @NotNull final String key,
                                    @NotNull final String message,
                                    @NotNull final Collection<InvalidProperty> res) {
        if (isEmpty(properties, key)) {
          res.add(new InvalidProperty(key, message));
          return true;
        }
        return false;
      }

      private boolean isEmpty(@NotNull final Map<String, String> properties,
                              @NotNull final String key) {
        return StringUtil.isEmptyOrSpaces(properties.get(key));
      }

      @NotNull
      public Collection<InvalidProperty> process(@Nullable final Map<String, String> p) {
        final Collection<InvalidProperty> result = new ArrayList<InvalidProperty>();
        if (p == null) return result;

        final String serverUrl = p.get(UgsConstants.SERVER_URL);
        final String badgeName = p.get(UgsConstants.BADGE_NAME);
        final String project = p.get(UgsConstants.PROJECT);
        final String postBadgeStatusExe = p.get(UgsConstants.POST_BADGE_STATUS_EXE);
        final String Change = p.get(UgsConstants.CHANGE);

        checkNotEmpty(p, UgsConstants.SERVER_URL, "Server URL must be specified", result);
        checkNotEmpty(p, UgsConstants.BADGE_NAME, "Badge must be specified", result);
        checkNotEmpty(p, UgsConstants.PROJECT, "Project must be specified", result);
        checkNotEmpty(p, UgsConstants.POST_BADGE_STATUS_EXE, "PostBadgeStatus.exe Path must be specified", result);
        checkNotEmpty(p, UgsConstants.CHANGE, "Change must be specified", result);
        checkNotEmpty(p, UgsConstants.BADGE_LINK, "Badge Link must be specified", result);
        checkNotEmpty(p, UgsConstants.INTERRUPTED_ACTION, "Action must be specified", result);

        return result;
      }
    };
  }

  @Override
  protected Set<Event> getSupportedEvents(final SBuildType buildType, final Map<String, String> params) {
    return mySupportedEvents;
  }
}
