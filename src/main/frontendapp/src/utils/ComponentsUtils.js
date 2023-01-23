import { loadingOverlayVar, stepperDetailsVar, toastDetailsVar } from "../reactivities/reactiveVariables";

const ComponentsUtils = {
    toggleLoadingOverlay: (isLoading) => {
        loadingOverlayVar({ isLoading: isLoading });
    },
    displayToastMessage: (open, severity, message, autohide = true) => {
        toastDetailsVar({
            open: open,
            severity: severity,
            message: message,
            autohide: autohide
        });
    },
    goToStep: (step) => {
        stepperDetailsVar({ currentStep: step });
    }
};

export default ComponentsUtils;
