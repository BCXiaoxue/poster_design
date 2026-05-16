import request from "@/utils/request";

export const getInform = (title, pageNo, pageSize) => {
  return request.post("/annunciation/queryList", {
    title,
    pageNo,
    pageSize,
  });
};

export const getPrize = (title, pageNo, pageSize) => {
  return request.post("/news/queryList", {
    title,
    pageNo,
    pageSize,
  });
};
