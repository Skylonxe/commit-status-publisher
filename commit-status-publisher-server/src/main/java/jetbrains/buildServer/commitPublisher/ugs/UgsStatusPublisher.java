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

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import jetbrains.buildServer.commitPublisher.*;
import jetbrains.buildServer.messages.Status;
import jetbrains.buildServer.serverSide.*;
import jetbrains.buildServer.serverSide.impl.LogUtil;
import jetbrains.buildServer.util.StringUtil;
import jetbrains.buildServer.vcs.VcsRoot;
import org.apache.http.entity.ContentType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.security.KeyStore;
import java.text.MessageFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

  private void runPostBadgeStatus(String status, String change)
  {
    final String changeStr = myParams.getOrDefault(UgsConstants.CHANGE, "").isEmpty() ? change : myParams.get(UgsConstants.CHANGE);
    final String cmd = MessageFormat.format("PostBadgeStatus.exe -Name={0} -Change={1} -Project={2} -RestUrl={3} -Status={4} -Url={5}",
      myParams.get(UgsConstants.BADGE_NAME), changeStr, myParams.get(UgsConstants.PROJECT), myParams.get(UgsConstants.SERVER_URL),
        status, UgsConstants.BADGE_LINK
        );

    LOG.debug(cmd);
    System.out.println(cmd);

    /*Runtime rt = Runtime.getRuntime();
    try {
      Process pr = rt.exec(cmd);
    } catch (IOException e) {
      e.printStackTrace();
    }*/
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
    runPostBadgeStatus("Starting", revision.getRevision());
    return false;
  }

  @Override
  public boolean buildFinished(@NotNull SBuild build, @NotNull BuildRevision revision) throws PublisherException {
    final String status = getStatus(false, build.getBuildStatus());
    runPostBadgeStatus(status, revision.getRevision());
    return true;
  }


  public boolean buildFailureDetected(@NotNull SBuild build, @NotNull BuildRevision revision) throws PublisherException {
    runPostBadgeStatus("Failure", revision.getRevision());
    return false;
  }

  public boolean buildMarkedAsSuccessful(@NotNull SBuild build, @NotNull BuildRevision revision, boolean buildInProgress) throws PublisherException {
    runPostBadgeStatus("Success", revision.getRevision());
    return false;
  }

  @Override
  public String getId() {
    return UgsConstants.ID;
  }
}
