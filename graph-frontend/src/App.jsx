import { useState } from "react";
import GraphView from "./components/GraphView";
import { runBFS, runDFS } from "./services/api";
import { useNodesState, useEdgesState } from "reactflow";

const convertToFlow = (graph) => {

  const n = graph.nodes;

  const adj = Array.from({ length: n }, () => []);

  graph.edges.forEach((e) => {
    adj[e.source].push(e.destination);

    if (!graph.directed) {
      adj[e.destination].push(e.source);
    }
  });

  // 🔥 BFS LEVEL POSITIONING
  const level = Array(n).fill(-1);

  const queue = [0];

  level[0] = 0;

  while (queue.length) {

    const node = queue.shift();

    for (let nei of adj[node]) {

      if (level[nei] === -1) {

        level[nei] = level[node] + 1;

        queue.push(nei);
      }
    }
  }

  // 🔥 GROUP BY LEVEL
  const levelMap = {};

  for (let i = 0; i < n; i++) {

    if (!levelMap[level[i]]) {
      levelMap[level[i]] = [];
    }

    levelMap[level[i]].push(i);
  }

  // 🔥 CREATE NODES
  const nodes = [];

  const levelGap = 150;

  const nodeGap = 120;

  Object.keys(levelMap).forEach((lvl) => {

    const arr = levelMap[lvl];

    const totalWidth = arr.length * nodeGap;

    arr.forEach((node, idx) => {

      nodes.push({
        id: String(node),

        data: {
          label: `Node ${node}`,
        },

        position: {
          x: idx * nodeGap - totalWidth / 2 + 300,
          y: lvl * levelGap + 50,
        },

        draggable: false,

        selectable: false,

        style: {
          background: "#1e293b",
          color: "#fff",
          borderRadius: "50%",
          border: "2px solid #334155",
          width: 60,
          height: 60,
          display: "flex",
          alignItems: "center",
          justifyContent: "center",
          fontWeight: "bold",
          cursor: "pointer",
          transition: "all 0.3s ease",
        },
      });
    });
  });

  // 🔥 CREATE EDGES
  const edges = graph.edges.map((e, i) => ({
    id: String(i),
    source: String(e.source),
    target: String(e.destination),
  }));

  return { nodes, edges };
};

function App() {

  const [graph, setGraph] = useState(null);

  const [isRunning, setIsRunning] = useState(false);

  const [nodes, setNodes, onNodesChange] = useNodesState([]);

  const [edges, setEdges, onEdgesChange] = useEdgesState([]);

  const [structure, setStructure] = useState([]);

  const [mode, setMode] = useState("");

  const [currentNode, setCurrentNode] = useState(null);

  // ✅ CREATE GRAPH
  const handleCreate = async () => {

    const res = await fetch(
      "https://graph-visualizer-production.up.railway.app/graph/create",
      {
        method: "POST",

        headers: {
          "Content-Type": "application/json",
        },

        body: JSON.stringify({
          nodes: 10,

          directed: true,

          edges: [
            [0,1],
            [0,2],
            [1,3],
            [1,4],
            [2,5],
            [2,6],
            [3,7],
            [4,8],
            [5,9]
          ],
        }),
      }
    );

    const data = await res.json();

    const flowData = convertToFlow(data);

    setGraph(data);

    setNodes(flowData.nodes);

    setEdges(flowData.edges);

    setStructure([]);
  };

  // ✅ BFS
  const handleBFS = async () => {

    if (!graph) return;

    setMode("BFS");

    setStructure([]);

    const res = await runBFS({
      graphId: graph.id,
      startNode: 0,
    });

    animate(res.data.steps);
  };

  // ✅ DFS
  const handleDFS = async () => {

    if (!graph) return;

    setMode("DFS");

    setStructure([]);

    const res = await runDFS({
      graphId: graph.id,
      startNode: 0,
    });

    animate(res.data.steps);
  };

  // ✅ ANIMATION
  const animate = async (steps) => {

    setIsRunning(true);

    let visited = new Set();

    for (let i = 0; i < steps.length; i++) {

      const current = steps[i].current;

      visited.add(current);

      setCurrentNode(current);

      // 🔵 BFS QUEUE
      if (mode === "BFS") {
        setStructure(steps[i].queue || []);
      }

      // 🟣 DFS STACK
      else {
        setStructure(steps[i].stack || []);
      }

      // 🔥 NODE UPDATE
      setNodes((nds) =>
        nds.map((n) => {

          const isCurrent = n.id === String(current);

          const isVisited = visited.has(Number(n.id));

          return {
            ...n,

            style: {
              ...n.style,

              background: isVisited
                ? "#f97316"
                : "#1e293b",

              border: isCurrent
                ? "3px solid #fb923c"
                : "2px solid #334155",

              boxShadow: isCurrent
                ? "0 0 25px rgba(249,115,22,0.9)"
                : isVisited
                ? "0 0 10px rgba(249,115,22,0.4)"
                : "none",
            },
          };
        })
      );

      // 🔥 EDGE UPDATE
      setEdges((eds) =>
        eds.map((e) => ({
          ...e,

          animated:
            i > 0 &&
            e.source === String(steps[i - 1].current) &&
            e.target === String(current),

          style: {
            stroke:
              i > 0 &&
              e.source === String(steps[i - 1].current) &&
              e.target === String(current)
                ? "#f97316"
                : "#64748b",

            strokeWidth:
              i > 0 &&
              e.source === String(steps[i - 1].current) &&
              e.target === String(current)
                ? 3
                : 1.5,
          },
        }))
      );

      await new Promise((r) => setTimeout(r, 1000));
    }

    setIsRunning(false);
  };

  return (
    <div className="h-screen flex flex-col bg-slate-900 text-white overflow-hidden">

      <h1 className="text-3xl font-bold mb-4 text-center">
        Graph Visualizer 🚀
      </h1>

      {/* BUTTONS */}
      <div className="flex gap-4 justify-center mb-4">

        <button
          onClick={handleCreate}
          className="btn"
        >
          Create Graph
        </button>

        <button
          onClick={handleBFS}
          disabled={!graph || isRunning}
          className="btn"
        >
          Run BFS
        </button>

        <button
          onClick={handleDFS}
          disabled={!graph || isRunning}
          className="btn"
        >
          Run DFS
        </button>
      </div>

      {/* MAIN */}
      <div className="flex flex-1 overflow-hidden px-6 gap-6">

        {/* GRAPH */}
        <div className="flex-1 flex justify-center items-center">

          <div className="w-[90%] h-full">

            <GraphView
              nodes={nodes}
              edges={edges}
              onNodesChange={onNodesChange}
              onEdgesChange={onEdgesChange}
            />

          </div>
        </div>

        {/* STACK / QUEUE */}
        {mode && (

          <div className="w-[140px] flex flex-col items-center">

            <h2 className="mb-3 font-semibold text-lg">

              {mode === "BFS"
                ? "QUEUE"
                : "STACK"}

            </h2>

            <div
              className={`border-2 border-green-400 w-full flex ${
                mode === "BFS"
                  ? "flex-col"
                  : "flex-col-reverse"
              } items-center bg-slate-800 rounded-lg max-h-[70vh] overflow-y-auto`}
            >

              {/* FRONT */}
              {mode === "BFS" && (
                <div className="text-xs text-green-400 py-1">
                  FRONT
                </div>
              )}

              {/* DATA */}
              {structure.map((item, idx) => (

                <div
                  key={idx}

                  className={`w-full text-center py-2 ${
                    item === currentNode
                      ? "bg-orange-400 text-white"
                      : "bg-green-200 text-black"
                  }`}
                >
                  {item}
                </div>
              ))}

              {/* BACK */}
              {mode === "BFS" && (
                <div className="text-xs text-green-400 py-1">
                  BACK
                </div>
              )}

            </div>
          </div>
        )}
      </div>
    </div>
  );
}

export default App;
