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

import jetbrains.buildServer.BuildProblemData;
import jetbrains.buildServer.commitPublisher.*;
import jetbrains.buildServer.messages.Status;
import jetbrains.buildServer.serverSide.*;
import org.apache.commons.io.IOUtils;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.MessageFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static jetbrains.buildServer.commitPublisher.LoggerUtil.LOG;

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

  private void runPostBadgeStatus(SBuild build, String status, String change) throws PublisherException
  {
    final String changeStr = myParams.getOrDefault(UgsConstants.CHANGE, "<current>").equals("<current>") ? change : myParams.get(UgsConstants.CHANGE);
    final String urlStr = myParams.get(UgsConstants.BADGE_LINK).replace("<buildId>", String.valueOf(build.getBuildId()));

    final String cmd = MessageFormat.format("{0} -Name={1} -Change={2} -Project={3} -RestUrl={4} -Status={5} -Url={6}",
      myParams.get(UgsConstants.POST_BADGE_STATUS_EXE),
      myParams.get(UgsConstants.BADGE_NAME), changeStr, myParams.get(UgsConstants.PROJECT), myParams.get(UgsConstants.SERVER_URL), status, urlStr);

    LOG.debug(cmd);
    System.out.println(cmd);

    Runtime rt = Runtime.getRuntime();
    try {
      //build.getBuildLog().message("Posting " + status + " badge to the Unreal Game Sync metadata server", Status.NORMAL, MessageAttrs.serverMessage());

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

  private static String getStatus(boolean isStarting, Status status) {
    if (!isStarting) {
      if (status.isSuccessful()) return "Success";
      else if (status == Status.ERROR) return "Failure";
      else if (status == Status.FAILURE) return "Failure";
    }

    return "Starting";
  }

  public boolean buildStarted(@NotNull SBuild build, @NotNull BuildRevision revision) throws PublisherException {
    runPostBadgeStatus(build, "Starting", revision.getRepositoryVersion().getVersion());
    return false;
  }

  @Override
  public boolean buildFinished(@NotNull SBuild build, @NotNull BuildRevision revision) throws PublisherException {
    final String status = getStatus(false, build.getBuildStatus());
    runPostBadgeStatus(build, status, revision.getRevision());
    return true;
  }


  public boolean buildFailureDetected(@NotNull SBuild build, @NotNull BuildRevision revision) throws PublisherException {
    runPostBadgeStatus(build, "Failure", revision.getRevision());
    return false;
  }

  public boolean buildMarkedAsSuccessful(@NotNull SBuild build, @NotNull BuildRevision revision, boolean buildInProgress) throws PublisherException {
    runPostBadgeStatus(build, "Success", revision.getRevision());
    return false;
  }

  @Override
  public String getId() {
    return UgsConstants.ID;
  }
}
