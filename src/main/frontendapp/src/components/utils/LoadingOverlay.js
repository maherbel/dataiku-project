import * as React from 'react';
import Backdrop from '@mui/material/Backdrop';
import CircularProgress from '@mui/material/CircularProgress';
import { useReactiveVar } from '@apollo/client';
import { loadingOverlayVar } from '../../reactivities/reactiveVariables';

const LoadingOverlay = () => {
    const { isLoading } = useReactiveVar(loadingOverlayVar);
    return (
        <div>
            <Backdrop
                sx={{ color: '#fff', zIndex: (theme) => theme.zIndex.drawer + 1 }}
                open={isLoading}
            >
                <CircularProgress color="inherit" />
            </Backdrop>
        </div>
    );
};

export default LoadingOverlay;