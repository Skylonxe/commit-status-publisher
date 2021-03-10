// Copyright Ondrej Hrusovsky

package jetbrains.buildServer.commitPublisher.ugs;

public class UgsConstants {
  public static final String ID = "ugs";

  public static final String BADGE_NAME = "ugsBadgeName";
  public static final String CHANGE = "ugsChange";
  public static final String PROJECT = "ugsProject";
  public static final String SERVER_URL = "ugsServerUrl";
  public static final String BADGE_LINK = "ugsBadgeLink";
  public static final String POST_BADGE_STATUS_EXE = "ugsPostBadgeStatusExe";
  public static final String INTERRUPTED_ACTION = "ugsInterruptedAction";
  public static final String POST_RESULT_AS_SOON_AS_POSSIBLE = "ugsPostResultAsSoonAsPossible";
  public static final String POST_WHEN_QUEUED = "ugsPostWhenQueued";

  public static final String INTERRUPTED_ACTION_NOTHING = "ugsInterruptedActionNothing";
  public static final String INTERRUPTED_ACTION_FAIL = "ugsInterruptedActionFail";
  public static final String INTERRUPTED_ACTION_WARNING = "ugsInterruptedActionWarning";
  public static final String INTERRUPTED_ACTION_SUCCESS = "ugsInterruptedActionSuccess";

  public String getBadgeName() {  return BADGE_NAME; }

  public String getChange() {
    return CHANGE;
  }

  public String getProject() {
    return PROJECT;
  }

  public String getServerUrl() {
    return SERVER_URL;
  }

  public String getBadgeLink() { return BADGE_LINK; }

  public String getPostBadgeStatusExe() { return POST_BADGE_STATUS_EXE; }

  public String getInterruptedAction() { return INTERRUPTED_ACTION; }

  public String getPostResultAsSoonAsPossible() { return POST_RESULT_AS_SOON_AS_POSSIBLE; }

  public String getPostWhenQueued() { return POST_WHEN_QUEUED; }

  public String  getInterruptedActionNothing() { return INTERRUPTED_ACTION_NOTHING; }

  public String  getInterruptedActionFail() { return INTERRUPTED_ACTION_FAIL; }

  public String  getInterruptedActionWarning() { return INTERRUPTED_ACTION_WARNING; }

  public String  getInterruptedActionSuccess() { return INTERRUPTED_ACTION_SUCCESS; }
}
