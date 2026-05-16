import React from "react";
import ReactFlow, { MarkerType } from "reactflow";
import "reactflow/dist/style.css";

const GraphView = ({
  nodes,
  edges,
  onNodesChange,
  onEdgesChange,
}) => {
  return (
<div className="h-full w-full rounded-xl overflow-hidden shadow-lg border border-slate-700 bg-[radial-gradient(circle_at_center,#0f172a,#020617)]">
   
   
   <ReactFlow
        nodes={nodes}
        edges={edges}
        onNodesChange={onNodesChange}
        onEdgesChange={onEdgesChange}
        fitView
        nodesDraggable={false}
        nodesConnectable={false}
        defaultEdgeOptions={{
          type: "smoothstep",
          markerEnd: {
            type: MarkerType.ArrowClosed,
          },
        }}
      />
    
    </div>
  );
};

export default GraphView;