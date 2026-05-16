import axios from "axios";

const API = axios.create({
  baseURL: "https://dummyjson.com",
});

export const createGraph = (data) => API.post("/graph/create", data);
export const runBFS = (data) => API.post("/graph/bfs", data);
export const runDFS = (data) => API.post("/graph/dfs", data);