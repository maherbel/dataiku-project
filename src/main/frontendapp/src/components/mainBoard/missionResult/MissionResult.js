import React, { useEffect, useState } from 'react';
import { List, ListItem, Box, Typography, Paper, Stack } from "@mui/material";
import { useReactiveVar } from '@apollo/client';
import { milleniumMissionResultVar, missionDataVar, selectedEmpireDataFileVar } from '../../../reactivities/reactiveVariables';
import GraphUtil from '../../../utils/GraphUtil';

import './MissionResult.css';

const MissionResult = () => {
    const [progress, setProgress] = useState(0);
    const { missionPath, missionSuccessProbability } = useReactiveVar(milleniumMissionResultVar);
    const { departure, arrival } = missionDataVar();
    const { jsonContent } = selectedEmpireDataFileVar();
    const steps = GraphUtil.buildMissionPathSteps(missionPath, departure, arrival);
    let messageColor;
    if (missionSuccessProbability !== null) {
        if (missionSuccessProbability === 0) {
            messageColor = 'red';
        } else if (0 < missionSuccessProbability && missionSuccessProbability < 85) {
            messageColor = 'orange';
        } else {
            messageColor = 'green';
        }
    }

    useEffect(() => {
        let intervalId = null;
        setProgress(0);
        if (missionSuccessProbability !== null && missionSuccessProbability >= 0) {
            intervalId = setInterval(() => {
                setProgress(prevProgress =>
                    prevProgress >= missionSuccessProbability ? missionSuccessProbability : prevProgress + 1
                );
            }, 20);
        }
        return () => clearInterval(intervalId);
    }, [missionSuccessProbability]);

    if (missionSuccessProbability !== null && missionSuccessProbability >= 0) {
        return (
            <Box sx={{ display: 'flex', flexDirection: 'column', alignItems: 'center', justifyContent: 'center' }}>
                <Typography variant="h6">
                    The possibility of successfully navigating from <strong>{departure}</strong> to <strong>{arrival}</strong> in <strong>maximum {jsonContent.countdown} days</strong> is:
                </Typography>
                {missionSuccessProbability > 0 &&
                    <div className="speedometer">
                        <div className="speedometer-progress" style={{ width: `${progress}%`, backgroundColor: messageColor }}>
                            <div className="speedometer-label">{progress}%</div>
                        </div>
                    </div>
                }
                {missionSuccessProbability === 0 &&
                    <Stack sx={{ alignItems: 'center' }}>
                        <Typography variant='h3' sx={{ color: 'red', fontWeight: 'bold' }}>
                            0%
                        </Typography>
                        <Typography variant='h3' sx={{ color: 'red', fontWeight: 'bold' }}>
                            (Not Possible)
                        </Typography>
                    </Stack>
                }
                {steps &&
                    <Paper elevation={2}>
                        <List sx={{ width: '100%', maxWidth: '100%' }}>
                            {steps.map((step, index) => (
                                <ListItem key={index}>
                                    <Typography sx={{ fontSize: 20 }}>
                                        {step}
                                    </Typography>
                                </ListItem>
                            ))}
                        </List>
                    </Paper>
                }
            </Box>
        );
    }
};

export default MissionResult;
