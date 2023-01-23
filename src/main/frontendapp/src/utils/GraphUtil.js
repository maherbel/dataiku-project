const GraphUtil = {
    computeGraphData: (missionData) => {
        let id = 1;
        const adjMap = missionData.adjacencyMap;
        const planets = Array.from(adjMap.keys()).map((nodeName) => {
            let type = 'planet';
            if (nodeName === missionData.departure) {
                type = 'departure';
            } else if (nodeName === missionData.arrival) {
                type = 'arrival';
            }
            return { id: id++, title: nodeName, x: 0, y: 0, type }
        });

        const centerX = 250;
        const centerY = 250;
        const radius = 400;
        const numPlanets = planets.length;
        const minDistance = 300;

        function distance(x1, y1, x2, y2) {
            return Math.sqrt((x1 - x2) ** 2 + (y1 - y2) ** 2);
        }

        for (let i = 0; i < numPlanets; i++) {
            let x, y;
            do {
                const angle = Math.random() * 2 * Math.PI;
                x = centerX + radius * Math.cos(angle);
                y = centerY + radius * Math.sin(angle);
                // eslint-disable-next-line
            } while (planets.some(p => distance(p.x, p.y, x, y) < minDistance));
            planets[i].x = x;
            planets[i].y = y;
        }


        const edges = Array.from(adjMap.entries()).map(([nodeName, neighborsMap]) => {
            return Array.from(neighborsMap.entries()).map(([neighbor, travelTime]) => {
                const sourceId = planets.find(planet => planet.title === nodeName).id;
                const targetId = planets.find(planet => planet.title === neighbor).id;
                return { source: sourceId, target: targetId, type: "planetEdge", handleText: `${travelTime}` };
            });
        }).flat();

        return {
            nodes: planets,
            edges: edges
        };
    },
    computeMissionPathGraphData(missionPath, graph) {
        if (!missionPath || missionPath.length === 0) return graph;
        const pathEdges = [];
        const nodeTitles = new Map(graph.nodes.map((node, id) => [node.title, id + 1]));
        for (let i = 0; i < missionPath.length - 1; i++) {
            pathEdges.push({
                source: nodeTitles.get(missionPath[i].planet),
                target: nodeTitles.get(missionPath[i + 1].planet),
                day: missionPath[i + 1].day,
                refuel: missionPath[i + 1].refuel,
                delay: missionPath[i + 1].delay
            });
        }
        const planetEdges = graph.edges;
        // Create a map of source/target from planetEdges and nodes
        const edgeByRoute = new Map(planetEdges.map(edge => [`${edge.source}-${edge.target}`, edge]));
        const planetById = new Map(graph.nodes.map(planet => [planet.id, planet]));
        // Iterate over pathObjects array
        pathEdges.forEach(pathEdge => {
            // check if the source/target of the current object is already in planetEdges
            this.flagMissionPathData(pathEdge.source, pathEdge.target, edgeByRoute, planetById);
            this.flagMissionPathData(pathEdge.target, pathEdge.source, edgeByRoute, planetById);
        });
        return {
            edges: Array.from(edgeByRoute.values()),
            nodes: Array.from(planetById.values())
        };
    },
    flagMissionPathData(pathEdgeSource, pathEdgeTarget, edgeByRoute, planetById) {
        const srcTarget = `${pathEdgeSource}-${pathEdgeTarget}`;
        if (edgeByRoute.has(srcTarget)) {
            edgeByRoute.set(srcTarget, { ...edgeByRoute.get(srcTarget), type: 'missionPathEdge' });
            if (this.isPlanetDepartureOrArrival(planetById.get(pathEdgeSource))) planetById.get(pathEdgeSource).type = 'pathPlanet';
            if (this.isPlanetDepartureOrArrival(planetById.get(pathEdgeTarget))) planetById.get(pathEdgeTarget).type = 'pathPlanet';
        }
    },
    isPlanetDepartureOrArrival(planet) {
        return ['departure', 'arrival'].indexOf(planet.type) === -1;
    },
    buildMissionPathSteps(missionPath, departure, arrival) {
        const steps = [];
        if (!missionPath || missionPath.length === 0) return steps;

        let stepNum = 0;
        missionPath.forEach(pathStep => {
            const { planet, day, refuel, delay, risky } = pathStep;
            let stepDescription = '';
            if (planet !== departure && planet !== arrival && !refuel) {
                stepDescription = 'Day [' + day + ']: Traveled to ' + planet + ' in ' + (day - missionPath[stepNum-1].day) + ' day(s)';
            } else if (planet === departure) {
                stepDescription = 'Day [' + day + ']: Departure from ' + planet;
            } else if (planet === arrival) {
                stepDescription = 'Day [' + day + ']: Arrival to destination ' + planet + ' in ' + (day - missionPath[stepNum-1].day) + ' day(s)';
            } else if (refuel) {
                stepDescription = 'Day [' + day + ']: Staying 1 extra day on ' + planet + ' for a refuel';
            }
            if (delay) {
                stepDescription = stepDescription + ' with a delay of ' + delay + ' day(s)';
            }
            if (risky) {
                stepDescription = stepDescription + ` => Risky position as the bounty hunters are in ` + planet + ` on day ` + day + ` !`;
            }
            stepDescription = stepDescription + '.';
            steps.push(stepDescription);
            stepNum++;
        });
        return steps;
    }
};

export default GraphUtil;
