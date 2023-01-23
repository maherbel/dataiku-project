import * as React from 'react';
import Snackbar from '@mui/material/Snackbar';
import MuiAlert from '@mui/material/Alert';
import { useReactiveVar } from '@apollo/client';
import { toastDetailsVar } from '../../reactivities/reactiveVariables';

const Alert = React.forwardRef(function Alert(props, ref) {
    return <MuiAlert elevation={6} ref={ref} variant="filled" {...props} />;
});

const ToastMessage = () => {
    const { open, severity, message } = useReactiveVar(toastDetailsVar);

    const handleClose = (reason) => {
        if (reason === 'clickaway') {
            return;
        }

        toastDetailsVar({...toastDetailsVar(), open: false});
    };

    return (
        <Snackbar open={open} autoHideDuration={6000} onClose={handleClose}>
            <Alert onClose={handleClose} severity={severity}>
                {message}
            </Alert>
        </Snackbar>
    );
};

export default ToastMessage;