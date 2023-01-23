import { makeVar } from "@apollo/client";

// Holds the result of an "Apply Lettering" result
export const selectedEmpireDataFileVar = makeVar({ 
    jsonContent: null,
    fileName: null,
});

// Holds the sate to display or not an alert as a toast
export const toastDetailsVar = makeVar({
    open: false,
    severity: null,
    level: null,
    autohide: null,
});

// Holds the state of the mission result
export const milleniumMissionResultVar = makeVar({
    missionSuccessProbability: null,
    missionPath: [],
});

// Holds the mission data used to compute the success probability
export const missionDataVar = makeVar({
    departure: null,
    arrival: null
});

// Holds the current state of the loading overlay component
export const loadingOverlayVar = makeVar({
    isLoading: false,
});

// Holds the current step in the stepper component
export const stepperDetailsVar = makeVar({
    currentFlowStep: 0,
})