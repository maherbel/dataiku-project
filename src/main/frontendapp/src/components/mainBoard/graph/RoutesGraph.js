import React, { Component } from "react";
import { missionDataVar } from '../../../reactivities/reactiveVariables';
import { GraphView } from 'react-digraph';
import GraphUtil from '../../../utils/GraphUtil';
import RoutesService from '../../../services/RoutesService';
import ComponentsUtils from '../../../utils/ComponentsUtils';
import { Skeleton } from "@mui/material";

const GraphConfig = {
    NodeTypes: {
        planet: { // required to show planets
            typeText: "Planet",
            shapeId: "#planet", // relates to the type property of a node
            shape: (
                <symbol viewBox="0 0 50 50" id="planet" key="0">
                    <circle cx="25" cy="25" r="20"></circle>
                </symbol>
            )
        },
        pathPlanet: { // required to show planets
            typeText: "Path Planet",
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

export default class RoutesGrap extends Component {

    constructor(props) {
        super(props);

        this.state = {
            graph: {},
            selected: {}
        }
    }

    async componentDidMount() {
        try {
            ComponentsUtils.toggleLoadingOverlay(true);
            const missionData = await new RoutesService().getMissionData();
            missionDataVar({
                departure: missionData.departure,
                arrival: missionData.arrival,
            });
            console.log(missionData);
            this.setState({ graph: GraphUtil.computeGraphData(missionData) });
            ComponentsUtils.toggleLoadingOverlay(false);
        } catch (error) {
            console.error(error);
            ComponentsUtils.displayToastMessage(true, "error", "An error happened during mission data retrieval ! Please click on regenerate graph.", false);
            ComponentsUtils.toggleLoadingOverlay(false);
        }
    }

    /* Define custom graph editing methods here */

    render() {
        const { edges, nodes } = GraphUtil.computeMissionPathGraphData(this.props.missionPath, this.state.graph);
        const selected = this.state.selected;

        const NodeTypes = GraphConfig.NodeTypes;
        const NodeSubtypes = GraphConfig.NodeSubtypes;
        const EdgeTypes = GraphConfig.EdgeTypes;

        if (nodes) {
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
                        allowMultiselect={false} // true by default, set to false to disable multi select.
                        showGraphControls={false}
                        readOnly={true}
                    />
                </div>
            );
        } else {
            return <Skeleton variant="rectangular" height={500} width='100%'/>
        }
    }
}