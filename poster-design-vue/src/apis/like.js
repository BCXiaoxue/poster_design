import request from "@/utils/request";

export const getLikeByUserId = (workId) => {
  return request.get("/praise/workByUserId", {
    params: { workId },
  });
};

export const changeLikes = (data) => {
  return request.post("/praise/addLikeById", data);
};
