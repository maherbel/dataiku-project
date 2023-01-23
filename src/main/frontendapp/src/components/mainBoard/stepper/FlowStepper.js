import * as React from 'react';
import Box from '@mui/material/Box';
import Stepper from '@mui/material/Stepper';
import Step from '@mui/material/Step';
import StepLabel from '@mui/material/StepLabel';
import StepContent from '@mui/material/StepContent';
import Button from '@mui/material/Button';
import Paper from '@mui/material/Paper';
import Typography from '@mui/material/Typography';
import { useReactiveVar } from '@apollo/client';
import { stepperDetailsVar } from '../../../reactivities/reactiveVariables';

const steps = [
  {
    label: `Upload Empire's data to launch the mission.`,
    description: `Please use the drop zone to upload/drop a JSON file that contains the empire intercepted data.
    This file should contain the countdown value and the bounty hunters positions which should include the planet/day on which they will be going through.
    Once you upload the file, the mission success probability will be automatically computed.
    If the planets are not well positioned on the graph, you can regenerate it using the button below.`,
  },
  {
    label: `Instructions to use the Falcon mission's dashboard.`,
    description:
      `The mission result are now computed, if there is at least one possible route given the autonomy, countdown, etc.. you'll be provided with further details on the dashboard.
      In fact, you can check on the graph the optimal path as it will start from departure/green and go through planets/yellow (travel time is the edge's weight) until it arrives to the destination/blue.
      Also, the mission path steps will be displayed, with extra informations such as refueling, delaying a travel (from a planet to another) and risky positions (where bounty hunters are present).
      You can upload another file to compute another result or you can click on the button reset below to reset the dashboard.`,
  }
];

export default function VerticalLinearStepper() {
  const { currentFlowStep } = useReactiveVar(stepperDetailsVar);

  const handleReset = () => {
    window.location.reload();
  };

  return (
    <Box sx={{ maxWidth: '100%' }}>
      <Stepper activeStep={currentFlowStep} orientation="vertical">
        {steps.map((step) => (
          <Step key={step.label}>
            <StepLabel>
              {step.label}
            </StepLabel>
            <StepContent>
              <Typography>{step.description}</Typography>
              {currentFlowStep < steps.length - 1 && (
                <Paper square elevation={0} sx={{ p: 3, alignItems: 'center' }}>
                  <Button variant="contained" onClick={handleReset}>
                    Regenerate Graph
                  </Button>
                </Paper>
              )}
            </StepContent>
          </Step>
        ))}
      </Stepper>
      {currentFlowStep === steps.length - 1 && (
        <Paper square elevation={0} sx={{ p: 3 }}>
          <Button variant="contained" onClick={handleReset}>
            Reset Dashboard
          </Button>
        </Paper>
      )}
    </Box>
  );
}