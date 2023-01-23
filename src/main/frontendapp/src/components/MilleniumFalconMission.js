import React, { useEffect } from 'react';
import { Box, Container } from "@mui/material";
import { useReactiveVar } from '@apollo/client';
import { milleniumMissionResultVar, selectedEmpireDataFileVar, stepperDetailsVar } from '../reactivities/reactiveVariables';
import Header from './utils/Header';
import Footer from './utils/Footer';
import ToastMessage from './utils/ToastMessage';
import LoadingOverlay from './utils/LoadingOverlay';
import ComponentsUtils from '../utils/ComponentsUtils';
import MainBoard from './mainBoard/MainBoard';
import RoutesService from '../services/RoutesService';

const MilleniumFalconMission = () => {
  const { jsonContent } = useReactiveVar(selectedEmpireDataFileVar);

  useEffect(() => {
    const fetchData = async () => {
      if (jsonContent) {
        ComponentsUtils.toggleLoadingOverlay(true);
        try {
          const missionResult = await new RoutesService().getMissionResultSuccess(jsonContent);
          if (missionResult) {
            milleniumMissionResultVar({
              missionSuccessProbability: missionResult.missionSuccessProbability,
              missionPath: missionResult.missionPath
            });
          }
          stepperDetailsVar({ currentFlowStep: 1 })
          ComponentsUtils.toggleLoadingOverlay(false);
        } catch (error) {
          console.error(error);
          ComponentsUtils.displayToastMessage(true, "error", "An error happened during the mission result compute ! Please click on regenerate graph or contact support.");
          ComponentsUtils.toggleLoadingOverlay(false);
        }
      }
    };
    fetchData();
  }, [jsonContent]);


  return (
    <Box>
      <Container maxWidth="false"
        sx={{
          display: 'flex',
          alignItems: 'left',
          flexDirection: 'column',
          m: 1,
          borderRadius: 1,
          disableEqualOverflow: true,
          font: 'sans-serif',
          maxHeight: '100vh',
          minHeight: '100vh',
          maxWidth: '100vv',
          minWidth: '100vv',
        }}
      >
        <Header />
        <MainBoard />
        <Footer />
      </Container>
      <LoadingOverlay />
      <ToastMessage />
    </Box>
  );
}

export default MilleniumFalconMission;