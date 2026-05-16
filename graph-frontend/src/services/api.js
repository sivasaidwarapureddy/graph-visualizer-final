import axios from "axios";

const API = axios.create({
  baseURL: "http://localhost:8080",
});

export const createGraph = (data) => API.post("/graph/create", data);
export const runBFS = (data) => API.post("/graph/bfs", data);
export const runDFS = (data) => API.post("/graph/dfs", data);