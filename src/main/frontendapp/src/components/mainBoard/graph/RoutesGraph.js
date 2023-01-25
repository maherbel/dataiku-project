import React, { Component } from "react";
import { GraphView } from 'react-digraph';
import { Skeleton } from "@mui/material";
import GraphUtil from '../../../utils/GraphUtil';
import { missionDataVar } from "../../../reactivities/reactiveVariables";
import ComponentsUtils from '../../../utils/ComponentsUtils';
import RoutesService from '../../../services/RoutesService';

const GraphConfig = {
    NodeTypes: {
        planet: { // required to show planets
            typeText: "Planet",
            shapeId: "#planet", // relates to the type property of a node
            shape: (
                <symbol viewBox="0 0 50 50" id="planet" key="0">
                    <circle cx="25" cy="25" r="20" style={{ fill: 'white' }}></circle>
                </symbol>
            )
        },
        pathPlanet: { // required to show planets
            typeText: "Route Planet",
            shapeId: "#pathPlanet", // relates to the type property of a node
            shape: (
                <symbol viewBox="0 0 50 50" id="pathPlanet" key="0">
                    <circle cx="25" cy="25" r="20" style={{ fill: 'yellow' }}></circle>
                </symbol>
            )
        },
        departure: { // required to show departure node
            typeText: "Departure",
            shapeId: "#departure", // relates to the type property of a node
            shape: (
                <symbol viewBox="0 0 50 50" id="departure" key="0">
                    <circle cx="25" cy="25" r="20" style={{ fill: 'lightGreen' }}></circle>
                </symbol>
            )
        },
        arrival: { // required to show arrival node
            typeText: "Arrival",
            shapeId: "#arrival", // relates to the type property of a node
            shape: (
                <symbol viewBox="0 0 50 50" id="arrival" key="0">
                    <circle cx="25" cy="25" r="20" style={{ fill: 'cyan' }}></circle>
                </symbol>
            )
        }
    },
    NodeSubtypes: {},
    EdgeTypes: {
        planetEdge: {  // required to show planets edges
            shapeId: "#planetEdge",
            shape: (
                <symbol viewBox="0 0 50 50" id="planetEdge" key="0">
                    <circle cx="25" cy="25" r="20" fill="currentColor"> </circle>
                </symbol>
            )
        },
        missionPathEdge: {  // required to show mission path edges
            shapeId: "#missionPathEdge",
            shape: (
                <symbol viewBox="0 0 50 50" id="missionPathEdge" key="0">
                    <circle cx="25" cy="25" r="20" fill="yellow"> </circle>
                </symbol>
            )
        }
    }
}

const NODE_KEY = "id"       // Allows D3 to correctly update DOM

export default class RoutesGraph extends Component {

    constructor(props) {
        super(props);
        this.initialGraphData = {};

        this.state = {
            selected: {},
            graphData: {}
        }
    }

    async fetchMissionDataAndComputeGraph() {
        try {
            ComponentsUtils.toggleLoadingOverlay(true);
            let missionData = this.initialGraphData;
            if (Object.keys(missionData).length === 0) {
                missionData = await new RoutesService().getMissionData();
                this.initialGraphData = missionData;
            }
            const missionGraph = GraphUtil.computeGraphData(missionData);
            missionDataVar({
                departure: missionData.departure,
                arrival: missionData.arrival
            });
            const graphData = await GraphUtil.computeMissionPathGraphData(this.props.missionPath, missionGraph);
            this.setState({ graphData: graphData });
            ComponentsUtils.toggleLoadingOverlay(false);
        } catch (error) {
            console.error(error);
            ComponentsUtils.displayToastMessage(true, "error", "An error happened during mission data retrieval ! Please click on regenerate graph or contact support.", false);
            ComponentsUtils.toggleLoadingOverlay(false);
        }
    }

    async componentDidMount() {
        await this.fetchMissionDataAndComputeGraph();
    }

    async componentDidUpdate(prevProps) {
        if (JSON.stringify(prevProps.missionPath) !== JSON.stringify(this.props.missionPath)) {
            await this.fetchMissionDataAndComputeGraph();
        }
    }

    render() {
        const { edges, nodes } = this.state.graphData;
        const selected = this.state.selected;

        const NodeTypes = GraphConfig.NodeTypes;
        const NodeSubtypes = GraphConfig.NodeSubtypes;
        const EdgeTypes = GraphConfig.EdgeTypes;

        if (nodes && nodes.length > 0) {
            return (
                <div id='graph' style={{ height: '500px', width: '100%' }}>
                    <GraphView ref='GraphView'
                        nodeKey={NODE_KEY}
                        nodes={nodes}
                        edges={edges}
                        selected={selected}
                        nodeTypes={NodeTypes}
                        nodeSubtypes={NodeSubtypes}
                        edgeTypes={EdgeTypes}
                        edgeArrowSize={1}
                        allowMultiselect={false}
                        showGraphControls={true}
                        readOnly={false}
                    />
                </div>
            );
        } else {
            return <Skeleton variant="rectangular" height={500} width='100%'/>
        }
    }
}