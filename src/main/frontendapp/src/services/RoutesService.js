import axios from "axios";

export default class RoutesService {
    async getMissionResultSuccess(jsonContent) {
        try {
            const response = await axios.post('/missionResultSuccess', jsonContent);
            if (response && response.data) {
                return {
                    missionSuccessProbability: response.data.missionSuccessProbability,
                    missionPath: response.data.missionPath
                };
            }
        } catch (error) {
            console.log(error);
            throw error;
        }
    }

    async getMissionData() {
        try {
            const missionData = await axios.get('/missionData');
            if (missionData && missionData.data) {
                return this.buildMissionData(missionData.data);
            }
        } catch (error) {
            console.log(error);
            throw error;
        }
    }

    buildMissionData(missionData) {
        const outerMap = new Map();
        const regEx = /name='(.*)', day='null'/;

        for (const [outerKey, innerObj] of Object.entries(missionData.nodes)) {
            const innerMap = new Map();
            const outerKeyName = regEx.exec(outerKey)[1];

            for (const [innerKey, value] of Object.entries(innerObj)) {
                const innerKeyName = regEx.exec(innerKey)[1];
                innerMap.set(innerKeyName, value);
            }
            outerMap.set(outerKeyName, innerMap);
        }

        return {
            autonomy: missionData.autonomy,
            departure: missionData.departure,
            arrival: missionData.arrival,
            adjacencyMap: outerMap,
        }
    }
}