import * as React from 'react';
import Alert from '@mui/material/Alert';
import AlertTitle from '@mui/material/AlertTitle';
import { useReactiveVar } from '@apollo/client';
import { messageBannerDetailsVar } from '../../reactivities/reactiveVariables';
import { Typography } from '@mui/material';

// TODO TO BE REMOVED
const MessageBanner = () => {
    const { show, message, title, severity } = useReactiveVar(messageBannerDetailsVar);
    if (show) {
        return (
            <Alert variant="outlined" severity={severity} onClose={() => messageBannerDetailsVar({})}>
                <AlertTitle>{title}</AlertTitle>
                <Typography variant="h6">{message}</Typography>
            </Alert>
        );
    }
}

export default MessageBanner;