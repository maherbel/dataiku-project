import React from 'react';
import Grid2 from '@mui/material/Unstable_Grid2/Grid2';
import UploadZone from './upload/UploadZone';
import MissionResult from './missionResult/MissionResult';
import FlowStepper from './stepper/FlowStepper';
import RoutesGraph from './graph/RoutesGraph';
import { useReactiveVar } from '@apollo/client';
import { milleniumMissionResultVar } from '../../reactivities/reactiveVariables';

const MainBoard = () => {

  const { missionPath } = useReactiveVar(milleniumMissionResultVar);

  return (
    <Grid2 container spacing={3} sx={{ display: 'flex', flexDirection: 'row', margin: 2 }} >
      <Grid2 sx={{ width: '30%' }}>
        <FlowStepper />
      </Grid2>
      <Grid2 sx={{ display: 'flex', flexDirection: 'column', alignItems: 'left', width: '40%' }}>
        <Grid2>
          <UploadZone />
        </Grid2>
        <Grid2 sx={{ width: '100%', height: '100%' }}>
          <RoutesGraph missionPath={missionPath} />
        </Grid2>
      </Grid2>
      <Grid2 sx={{ justifyContent: 'center', width: '30%' }}>
        <MissionResult />
      </Grid2>
    </Grid2>
  )
}

export default MainBoard;