import request from "@/utils/request";

export const getJudgeTeam = (id) =>
  request.get(`/judge/getCompetitionTeamAndWork?competitionId=${id}`);

export const sendJudgeInfo = (data) => {
  return request.post("/judge/uploadScoreAndComment", data);
};

export const getJudgeFinalInfo = (competitionId, teamId) => {
  return request.get("/judge/getCompetitionTeamJudge", {
    params: { competitionId, teamId },
  });
};
