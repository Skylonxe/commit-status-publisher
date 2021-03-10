// Copyright Ondrej Hrusovsky

package jetbrains.buildServer.commitPublisher.ugs;

import jetbrains.buildServer.commitPublisher.*;
import jetbrains.buildServer.messages.Status;
import jetbrains.buildServer.serverSide.*;
import org.apache.commons.io.IOUtils;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Updates UGS commit statuses via PostBadgeStatus.exe
 */
class UgsStatusPublisher extends BaseCommitStatusPublisher {
  protected UgsStatusPublisher(@NotNull CommitStatusPublisherSettings settings,
                                      @NotNull SBuildType buildType,@NotNull String buildFeatureId,
                                      @NotNull Map<String, String> params,
                                      @NotNull CommitStatusPublisherProblems problems) {
    super(settings, buildType, buildFeatureId, params, problems);
  }

  private void runPostBadgeStatus(long buildId, String status, String change) throws PublisherException
  {
    final String changeStr = myParams.getOrDefault(UgsConstants.CHANGE, "<current>").equals("<current>") ? change : myParams.get(UgsConstants.CHANGE);
    final String urlStr = myParams.get(UgsConstants.BADGE_LINK).replace("<buildId>", String.valueOf(buildId));

    final String cmd = MessageFormat.format("{0} -Name={1} -Change={2} -Project={3} -RestUrl={4} -Status={5} -Url={6}",
      myParams.get(UgsConstants.POST_BADGE_STATUS_EXE),
      myParams.get(UgsConstants.BADGE_NAME), changeStr, myParams.get(UgsConstants.PROJECT), myParams.get(UgsConstants.SERVER_URL), status, urlStr);

    //System.out.println(cmd);

    Runtime rt = Runtime.getRuntime();
    try {
      Process pr = rt.exec(cmd);
      pr.waitFor(10, TimeUnit.SECONDS);
      if(pr.exitValue() != 0)
      {
        throw new PublisherException("PostBadgeStatus.exe returned " + pr.exitValue() + ":"
          + "\n" + IOUtils.toString(pr.getErrorStream(), "UTF-8")
          + "\n" + IOUtils.toString(pr.getInputStream(), "UTF-8"));
      }
    } catch (IOException | InterruptedException e) {
      throw new PublisherException("PostBadgeStatus.exe failed to start", e);
    }
  }

  private static String interruptedActionToStatus(String action)
  {
    if(action.equals(UgsConstants.INTERRUPTED_ACTION_NOTHING)) return "";
    if(action.equals(UgsConstants.INTERRUPTED_ACTION_FAIL)) return "Failure";
    if(action.equals(UgsConstants.INTERRUPTED_ACTION_WARNING)) return "Warning";
    if(action.equals(UgsConstants.INTERRUPTED_ACTION_SUCCESS)) return "Success";
    return "";
  }

  private static String getStatus(boolean isStarting, Status status) {
    if (!isStarting) {
      if (status.isSuccessful()) return "Success";
      else if (status == Status.ERROR) return "Failure";
      else if (status == Status.FAILURE) return "Failure";
    }

    return "Starting";
  }

  public boolean buildQueued(@NotNull SQueuedBuild build, @NotNull BuildRevision revision) throws PublisherException {
    if(myParams.getOrDefault(UgsConstants.POST_WHEN_QUEUED, "").equals("true"))
    {
      runPostBadgeStatus(build.getBuildPromotion().getId(), "Starting", revision.getRepositoryVersion().getVersion());
    }
    return true;
  }

  public boolean buildStarted(@NotNull SBuild build, @NotNull BuildRevision revision) throws PublisherException {
    runPostBadgeStatus(build.getBuildId(), "Starting", revision.getRepositoryVersion().getVersion());
    return true;
  }

  @Override
  public boolean buildFinished(@NotNull SBuild build, @NotNull BuildRevision revision) throws PublisherException {
    final String status = getStatus(false, build.getBuildStatus());
    runPostBadgeStatus(build.getBuildId(), status, revision.getRevision());
    return true;
  }

  public boolean buildInterrupted(@NotNull SBuild build, @NotNull BuildRevision revision) throws PublisherException {
    final String status = interruptedActionToStatus(myParams.getOrDefault(UgsConstants.INTERRUPTED_ACTION, ""));
    if(!status.isEmpty())
    {
      runPostBadgeStatus(build.getBuildPromotion().getId(), status, revision.getRepositoryVersion().getVersion());
    }
    return true;
  }

  public boolean buildFailureDetected(@NotNull SBuild build, @NotNull BuildRevision revision) throws PublisherException {
    if(myParams.getOrDefault(UgsConstants.POST_RESULT_AS_SOON_AS_POSSIBLE, "").equals("true")) {
      runPostBadgeStatus(build.getBuildId(), "Failure", revision.getRevision());
    }
    return true;
  }

  public boolean buildMarkedAsSuccessful(@NotNull SBuild build, @NotNull BuildRevision revision, boolean buildInProgress) throws PublisherException {
    if(myParams.getOrDefault(UgsConstants.POST_RESULT_AS_SOON_AS_POSSIBLE, "").equals("true")) {
      runPostBadgeStatus(build.getBuildId(), "Success", revision.getRevision());
    }
    return true;
  }

  @Override
  public String getId() {
    return UgsConstants.ID;
  }
}
